package com.project.jobmatch.domain.recommendation.service;

import com.project.jobmatch.common.exception.CustomException;
import com.project.jobmatch.domain.job.entity.Job;
import com.project.jobmatch.domain.job.entity.JobPosting;
import com.project.jobmatch.domain.job.entity.Region;
import com.project.jobmatch.domain.job.repository.*;
import com.project.jobmatch.domain.recommendation.entity.CriteriaKey;
import com.project.jobmatch.domain.recommendation.entity.RecommendationCriteria;
import com.project.jobmatch.domain.recommendation.repository.*;
import com.project.jobmatch.domain.user.entity.*;
import com.project.jobmatch.domain.user.repository.UserConditionInterestFieldRepository;
import com.project.jobmatch.domain.user.repository.UserConditionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RecommendationServiceTest {
    @Mock RecommendationCriteriaRepository criteriaRepository;
    @Mock RecommendationResultRepository resultRepository;
    @Mock RecommendationItemRepository itemRepository;
    @Mock UserConditionRepository conditionRepository;
    @Mock UserConditionInterestFieldRepository conditionFieldRepository;
    @Mock JobRepository jobRepository;
    @Mock JobInterestFieldRepository jobFieldRepository;
    @Mock JobWorkTypeRepository jobWorkTypeRepository;
    @Mock JobPostingRepository postingRepository;
    @Mock JobTrainingCourseRepository trainingLinkRepository;
    @Mock JobSupportProgramRepository supportLinkRepository;

    private RecommendationService service;

    @BeforeEach
    void setUp() {
        service = new RecommendationService(criteriaRepository, resultRepository, itemRepository,
                conditionRepository, conditionFieldRepository, jobRepository, jobFieldRepository,
                jobWorkTypeRepository, postingRepository, trainingLinkRepository, supportLinkRepository);
    }

    @Test
    void scoreAddsEveryMatchingActiveCriterionWeight() {
        Region region = new Region("SEOUL", "서울특별시");
        ReflectionTestUtils.setField(region, "regionId", 1L);
        UserCondition condition = new UserCondition("session", CareerLevel.NEW, region,
                WorkType.FULL_TIME, EducationLevel.BACHELOR, true);
        Job job = new Job("DEV", "개발자", EducationLevel.ASSOCIATE, null, null, null);
        JobPosting posting = org.mockito.Mockito.mock(JobPosting.class);
        when(posting.getCareerLevel()).thenReturn(CareerLevel.NEW);
        when(posting.getRegion()).thenReturn(region);
        Map<CriteriaKey, BigDecimal> weights = Map.of(
                CriteriaKey.INTEREST_FIELD_MATCH, new BigDecimal("10.00"),
                CriteriaKey.WORK_TYPE_MATCH, new BigDecimal("20.00"),
                CriteriaKey.EDUCATION_MATCH, new BigDecimal("30.00"),
                CriteriaKey.CAREER_MATCH, new BigDecimal("15.00"),
                CriteriaKey.REGION_MATCH, new BigDecimal("25.00"));

        RecommendationService.ScoredJob scored = service.score(job, condition, Set.of(1L), Set.of(1L),
                Set.of(WorkType.FULL_TIME), List.of(posting), weights);

        assertThat(scored.score()).isEqualByComparingTo("100.00");
        assertThat(scored.reason()).contains("관심분야", "근무형태", "학력", "경력", "지역");
    }

    @Test
    void getResultThrowsNotFoundForUnknownResult() {
        when(resultRepository.findByResultId(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getResult(999L))
                .isInstanceOfSatisfying(CustomException.class,
                        exception -> assertThat(exception.getCode()).isEqualTo("RECOMMENDATION_NOT_FOUND"));
    }

    @Test
    void eachRecommendationExecutionReloadsActiveCriteria() {
        UserCondition condition = new UserCondition("session", CareerLevel.NEW, null,
                null, EducationLevel.BACHELOR, false);
        when(conditionRepository.findByConditionId(1L)).thenReturn(Optional.of(condition));
        when(jobRepository.findAll()).thenReturn(List.of());
        when(conditionFieldRepository.findAllByConditionConditionIdOrderByInterestFieldFieldId(1L))
                .thenReturn(List.of());
        when(criteriaRepository.findAllByActiveTrue()).thenReturn(List.of(), List.of(
                new RecommendationCriteria(CriteriaKey.EDUCATION_MATCH, new BigDecimal("55.00"), true, null)));
        when(resultRepository.save(org.mockito.ArgumentMatchers.any()))
                .thenAnswer(invocation -> invocation.getArgument(0));
        when(itemRepository.saveAll(org.mockito.ArgumentMatchers.anyList())).thenReturn(List.of());

        service.recommend(1L);
        service.recommend(1L);

        verify(criteriaRepository, org.mockito.Mockito.times(2)).findAllByActiveTrue();
    }
}
