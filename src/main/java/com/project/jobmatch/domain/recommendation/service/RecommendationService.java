package com.project.jobmatch.domain.recommendation.service;

import com.project.jobmatch.common.exception.CustomException;
import com.project.jobmatch.domain.job.entity.*;
import com.project.jobmatch.domain.job.repository.*;
import com.project.jobmatch.domain.recommendation.dto.RecommendationItemDetailResponse;
import com.project.jobmatch.domain.recommendation.dto.RecommendationResponse;
import com.project.jobmatch.domain.recommendation.entity.*;
import com.project.jobmatch.domain.recommendation.repository.*;
import com.project.jobmatch.domain.support.entity.SupportProgram;
import com.project.jobmatch.domain.training.entity.TrainingCourse;
import com.project.jobmatch.domain.user.entity.EducationLevel;
import com.project.jobmatch.domain.user.entity.UserCondition;
import com.project.jobmatch.domain.user.repository.UserConditionInterestFieldRepository;
import com.project.jobmatch.domain.user.repository.UserConditionRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class RecommendationService {
    private static final int RECOMMENDATION_LIMIT = 5;

    private final RecommendationCriteriaRepository criteriaRepository;
    private final RecommendationResultRepository resultRepository;
    private final RecommendationItemRepository itemRepository;
    private final UserConditionRepository conditionRepository;
    private final UserConditionInterestFieldRepository conditionFieldRepository;
    private final JobRepository jobRepository;
    private final JobInterestFieldRepository jobFieldRepository;
    private final JobWorkTypeRepository jobWorkTypeRepository;
    private final JobPostingRepository postingRepository;
    private final JobTrainingCourseRepository trainingLinkRepository;
    private final JobSupportProgramRepository supportLinkRepository;

    public RecommendationService(RecommendationCriteriaRepository criteriaRepository,
            RecommendationResultRepository resultRepository, RecommendationItemRepository itemRepository,
            UserConditionRepository conditionRepository,
            UserConditionInterestFieldRepository conditionFieldRepository, JobRepository jobRepository,
            JobInterestFieldRepository jobFieldRepository, JobWorkTypeRepository jobWorkTypeRepository,
            JobPostingRepository postingRepository, JobTrainingCourseRepository trainingLinkRepository,
            JobSupportProgramRepository supportLinkRepository) {
        this.criteriaRepository = criteriaRepository;
        this.resultRepository = resultRepository;
        this.itemRepository = itemRepository;
        this.conditionRepository = conditionRepository;
        this.conditionFieldRepository = conditionFieldRepository;
        this.jobRepository = jobRepository;
        this.jobFieldRepository = jobFieldRepository;
        this.jobWorkTypeRepository = jobWorkTypeRepository;
        this.postingRepository = postingRepository;
        this.trainingLinkRepository = trainingLinkRepository;
        this.supportLinkRepository = supportLinkRepository;
    }

    @Transactional
    public RecommendationResponse recommend(Long conditionId) {
        UserCondition condition = conditionRepository.findByConditionId(conditionId)
                .orElseThrow(() -> notFound("CONDITION_NOT_FOUND", "해당 사용자 조건을 찾을 수 없습니다."));
        List<Job> jobs = jobRepository.findAll();
        Set<Long> jobIds = jobs.stream().map(Job::getJobId).collect(Collectors.toSet());
        Set<Long> conditionFieldIds = conditionFieldRepository
                .findAllByConditionConditionIdOrderByInterestFieldFieldId(conditionId).stream()
                .map(link -> link.getInterestField().getFieldId()).collect(Collectors.toSet());

        Map<Long, Set<Long>> fieldsByJob = jobFieldRepository.findAllByJobJobIdIn(jobIds).stream()
                .collect(Collectors.groupingBy(link -> link.getJob().getJobId(),
                        Collectors.mapping(link -> link.getInterestField().getFieldId(), Collectors.toSet())));
        Map<Long, Set<com.project.jobmatch.domain.user.entity.WorkType>> workTypesByJob =
                jobWorkTypeRepository.findAllByJobJobIdIn(jobIds).stream()
                        .collect(Collectors.groupingBy(link -> link.getJob().getJobId(),
                                Collectors.mapping(JobWorkType::getWorkType, Collectors.toSet())));
        Map<Long, List<JobPosting>> postingsByJob = postingRepository.findAllByJobJobIdIn(jobIds).stream()
                .collect(Collectors.groupingBy(posting -> posting.getJob().getJobId()));
        Map<CriteriaKey, BigDecimal> weights = criteriaRepository.findAllByActiveTrue().stream()
                .collect(Collectors.toMap(RecommendationCriteria::getCriteriaKey,
                        RecommendationCriteria::getWeight));

        List<ScoredJob> scoredJobs = jobs.stream()
                .map(job -> score(job, condition, conditionFieldIds,
                        fieldsByJob.getOrDefault(job.getJobId(), Set.of()),
                        workTypesByJob.getOrDefault(job.getJobId(), Set.of()),
                        postingsByJob.getOrDefault(job.getJobId(), List.of()), weights))
                .sorted(Comparator.comparing(ScoredJob::score).reversed()
                        .thenComparing(scored -> scored.job().getJobId()))
                .limit(RECOMMENDATION_LIMIT)
                .toList();

        RecommendationResult result = resultRepository.save(new RecommendationResult(condition, scoredJobs.size()));
        List<RecommendationItem> items = new ArrayList<>();
        for (int index = 0; index < scoredJobs.size(); index++) {
            ScoredJob scored = scoredJobs.get(index);
            items.add(new RecommendationItem(result, scored.job(), scored.score(), scored.reason(), index + 1));
        }
        items = itemRepository.saveAll(items);
        return toResponse(result, items);
    }

    public RecommendationResponse getResult(Long resultId) {
        RecommendationResult result = findResult(resultId);
        return toResponse(result, itemRepository.findAllByResultResultIdOrderByRankOrderAsc(resultId));
    }

    public RecommendationItemDetailResponse getItem(Long resultId, Long itemId) {
        findResult(resultId);
        RecommendationItem item = itemRepository.findByItemIdAndResultResultId(itemId, resultId)
                .orElseThrow(() -> notFound("RECOMMENDATION_ITEM_NOT_FOUND", "해당 추천 항목을 찾을 수 없습니다."));
        Job job = item.getJob();
        List<RecommendationItemDetailResponse.TrainingSummary> trainings = trainingLinkRepository
                .findAllByJobJobId(job.getJobId()).stream().map(JobTrainingCourse::getTrainingCourse)
                .sorted(Comparator.comparing(TrainingCourse::getCourseId))
                .map(course -> new RecommendationItemDetailResponse.TrainingSummary(course.getCourseId(),
                        course.getCourseName(), course.getInstitution(), course.getCostType())).toList();
        List<RecommendationItemDetailResponse.SupportProgramSummary> programs = supportLinkRepository
                .findAllByJobJobId(job.getJobId()).stream().map(JobSupportProgram::getSupportProgram)
                .sorted(Comparator.comparing(SupportProgram::getProgramId))
                .map(program -> new RecommendationItemDetailResponse.SupportProgramSummary(program.getProgramId(),
                        program.getProgramName(), program.getOrganization())).toList();
        return new RecommendationItemDetailResponse(item.getItemId(),
                new RecommendationItemDetailResponse.JobDetail(job.getJobId(), job.getJobName(),
                        job.getDescription(), job.getRequiredEducation(), job.getAvgSalaryText()),
                item.getMatchScore(), item.getReason(), trainings, programs);
    }

    ScoredJob score(Job job, UserCondition condition, Set<Long> conditionFieldIds,
            Set<Long> jobFieldIds, Set<com.project.jobmatch.domain.user.entity.WorkType> jobWorkTypes,
            List<JobPosting> postings, Map<CriteriaKey, BigDecimal> weights) {
        BigDecimal score = BigDecimal.ZERO;
        List<String> reasons = new ArrayList<>();
        if (weights.containsKey(CriteriaKey.INTEREST_FIELD_MATCH)
                && !Collections.disjoint(conditionFieldIds, jobFieldIds)) {
            score = score.add(weight(weights, CriteriaKey.INTEREST_FIELD_MATCH));
            reasons.add("관심분야가 일치합니다");
        }
        if (weights.containsKey(CriteriaKey.WORK_TYPE_MATCH) && condition.getWorkType() != null
                && jobWorkTypes.contains(condition.getWorkType())) {
            score = score.add(weight(weights, CriteriaKey.WORK_TYPE_MATCH));
            reasons.add("희망 근무형태가 일치합니다");
        }
        if (weights.containsKey(CriteriaKey.EDUCATION_MATCH)
                && educationMatches(condition.getEducationLevel(), job.getRequiredEducation())) {
            score = score.add(weight(weights, CriteriaKey.EDUCATION_MATCH));
            reasons.add("학력 조건을 충족합니다");
        }
        if (weights.containsKey(CriteriaKey.CAREER_MATCH)
                && postings.stream().anyMatch(posting -> posting.getCareerLevel() == condition.getCareerLevel())) {
            score = score.add(weight(weights, CriteriaKey.CAREER_MATCH));
            reasons.add("경력 수준에 적합한 채용공고가 있습니다");
        }
        if (weights.containsKey(CriteriaKey.REGION_MATCH) && condition.getRegion() != null
                && postings.stream().anyMatch(posting ->
                posting.getRegion() != null && Objects.equals(posting.getRegion().getRegionId(),
                        condition.getRegion().getRegionId()))) {
            score = score.add(weight(weights, CriteriaKey.REGION_MATCH));
            reasons.add("선택 지역의 채용공고가 있습니다");
        }
        String reason = reasons.isEmpty() ? "일치하는 추천 기준이 없습니다." : String.join(", ", reasons) + ".";
        return new ScoredJob(job, score.setScale(2, RoundingMode.HALF_UP), reason);
    }

    private boolean educationMatches(EducationLevel actual, EducationLevel required) {
        if (required == null || required == EducationLevel.ANY || actual == EducationLevel.ANY) return true;
        return educationRank(actual) >= educationRank(required);
    }

    private int educationRank(EducationLevel level) {
        return switch (level) {
            case HIGH_SCHOOL -> 1;
            case ASSOCIATE -> 2;
            case BACHELOR -> 3;
            case MASTER -> 4;
            case DOCTORATE -> 5;
            case ANY -> 0;
        };
    }

    private BigDecimal weight(Map<CriteriaKey, BigDecimal> weights, CriteriaKey key) {
        return weights.getOrDefault(key, BigDecimal.ZERO);
    }

    private RecommendationResult findResult(Long resultId) {
        return resultRepository.findByResultId(resultId)
                .orElseThrow(() -> notFound("RECOMMENDATION_NOT_FOUND", "해당 추천 결과를 찾을 수 없습니다."));
    }

    private RecommendationResponse toResponse(RecommendationResult result, List<RecommendationItem> items) {
        List<RecommendationResponse.ItemSummary> summaries = items.stream().map(item -> {
            Job job = item.getJob();
            return new RecommendationResponse.ItemSummary(item.getItemId(), item.getRankOrder(),
                    new RecommendationResponse.JobSummary(job.getJobId(), job.getJobName()),
                    item.getMatchScore(), item.getReason(),
                    trainingLinkRepository.findAllByJobJobId(job.getJobId()).size(),
                    supportLinkRepository.findAllByJobJobId(job.getJobId()).size());
        }).toList();
        return new RecommendationResponse(result.getResultId(), result.getCondition().getConditionId(),
                result.getRecommendedCount(), summaries);
    }

    private CustomException notFound(String code, String message) {
        return new CustomException(HttpStatus.NOT_FOUND, code, message);
    }

    record ScoredJob(Job job, BigDecimal score, String reason) {}
}
