package com.project.jobmatch.domain.admin.service;

import com.project.jobmatch.common.exception.CustomException;
import com.project.jobmatch.domain.admin.dto.AdminDataRequest.*;
import com.project.jobmatch.domain.admin.dto.AdminDataResponse.Created;
import com.project.jobmatch.domain.job.entity.*;
import com.project.jobmatch.domain.job.repository.*;
import com.project.jobmatch.domain.support.entity.SupportProgram;
import com.project.jobmatch.domain.support.repository.SupportProgramRepository;
import com.project.jobmatch.domain.training.entity.TrainingCourse;
import com.project.jobmatch.domain.training.repository.TrainingCourseRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class AdminDataService {
    private final JobRepository jobRepository;
    private final TrainingCourseRepository trainingRepository;
    private final SupportProgramRepository supportRepository;
    private final JobPostingRepository postingRepository;
    private final RegionRepository regionRepository;
    private final InterestFieldRepository interestFieldRepository;
    private final JobInterestFieldRepository jobFieldRepository;
    private final JobWorkTypeRepository jobWorkTypeRepository;
    private final JobTrainingCourseRepository jobTrainingRepository;
    private final JobSupportProgramRepository jobSupportRepository;

    public AdminDataService(JobRepository jobRepository, TrainingCourseRepository trainingRepository,
            SupportProgramRepository supportRepository, JobPostingRepository postingRepository,
            RegionRepository regionRepository, InterestFieldRepository interestFieldRepository,
            JobInterestFieldRepository jobFieldRepository, JobWorkTypeRepository jobWorkTypeRepository,
            JobTrainingCourseRepository jobTrainingRepository,
            JobSupportProgramRepository jobSupportRepository) {
        this.jobRepository = jobRepository;
        this.trainingRepository = trainingRepository;
        this.supportRepository = supportRepository;
        this.postingRepository = postingRepository;
        this.regionRepository = regionRepository;
        this.interestFieldRepository = interestFieldRepository;
        this.jobFieldRepository = jobFieldRepository;
        this.jobWorkTypeRepository = jobWorkTypeRepository;
        this.jobTrainingRepository = jobTrainingRepository;
        this.jobSupportRepository = jobSupportRepository;
    }

    public Created createJob(JobRequest request) {
        Job job = saveJob(new Job(request.jobCode(), request.jobName(), request.requiredEducation(),
                request.avgSalaryText(), request.description(), request.source()));
        replaceJobMappings(job, request.interestFieldIds(), request.workTypes());
        return new Created(job.getJobId());
    }

    public void updateJob(Long jobId, JobRequest request) {
        Job job = findJob(jobId);
        job.update(request.jobCode(), request.jobName(), request.requiredEducation(),
                request.avgSalaryText(), request.description(), request.source());
        saveJob(job);
        replaceJobMappings(job, request.interestFieldIds(), request.workTypes());
    }

    public void deleteJob(Long jobId) {
        Job job = findJob(jobId);
        jobFieldRepository.deleteAllByJobJobId(jobId);
        jobWorkTypeRepository.deleteAllByJobJobId(jobId);
        jobTrainingRepository.deleteAllByJobJobId(jobId);
        jobSupportRepository.deleteAllByJobJobId(jobId);
        postingRepository.deleteAllByJobJobId(jobId);
        deleteOrConflict(() -> {
            jobRepository.delete(job);
            jobRepository.flush();
        });
    }

    public Created createTraining(TrainingRequest request) {
        TrainingCourse course = new TrainingCourse(request.courseName(), request.institution(),
                findRegion(request.regionId()), request.costType(), request.startDate(), request.endDate(),
                request.description(), request.externalUrl(), request.source());
        return new Created(trainingRepository.save(course).getCourseId());
    }

    public void updateTraining(Long courseId, TrainingRequest request) {
        TrainingCourse course = findTraining(courseId);
        course.update(request.courseName(), request.institution(), findRegion(request.regionId()),
                request.costType(), request.startDate(), request.endDate(), request.description(),
                request.externalUrl(), request.source());
    }

    public void deleteTraining(Long courseId) {
        TrainingCourse course = findTraining(courseId);
        jobTrainingRepository.deleteAllByTrainingCourseCourseId(courseId);
        deleteOrConflict(() -> {
            trainingRepository.delete(course);
            trainingRepository.flush();
        });
    }

    public Created createSupportProgram(SupportProgramRequest request) {
        SupportProgram program = new SupportProgram(request.programName(), request.organization(),
                request.targetAudience(), request.supportContent(), findRegion(request.regionId()),
                request.applyStartDate(), request.applyEndDate(), request.externalUrl(), request.source());
        return new Created(supportRepository.save(program).getProgramId());
    }

    public void updateSupportProgram(Long programId, SupportProgramRequest request) {
        SupportProgram program = findSupport(programId);
        program.update(request.programName(), request.organization(), request.targetAudience(),
                request.supportContent(), findRegion(request.regionId()), request.applyStartDate(),
                request.applyEndDate(), request.externalUrl(), request.source());
    }

    public void deleteSupportProgram(Long programId) {
        SupportProgram program = findSupport(programId);
        jobSupportRepository.deleteAllBySupportProgramProgramId(programId);
        deleteOrConflict(() -> {
            supportRepository.delete(program);
            supportRepository.flush();
        });
    }

    public Created createPosting(PostingRequest request) {
        JobPosting posting = new JobPosting(findOptionalJob(request.jobId()), request.title(),
                request.companyName(), findRegion(request.regionId()), request.workType(),
                request.requiredEducation(), request.careerLevel(), request.salaryText(),
                request.applyDeadline(), request.externalUrl(), request.source());
        return new Created(postingRepository.save(posting).getPostingId());
    }

    public void updatePosting(Long postingId, PostingRequest request) {
        JobPosting posting = findPosting(postingId);
        posting.update(findOptionalJob(request.jobId()), request.title(), request.companyName(),
                findRegion(request.regionId()), request.workType(), request.requiredEducation(),
                request.careerLevel(), request.salaryText(), request.applyDeadline(),
                request.externalUrl(), request.source());
    }

    public void deletePosting(Long postingId) {
        postingRepository.delete(findPosting(postingId));
    }

    public void linkTraining(Long jobId, Long courseId) {
        Job job = findJob(jobId);
        TrainingCourse course = findTraining(courseId);
        if (jobTrainingRepository.existsByJobJobIdAndTrainingCourseCourseId(jobId, courseId)) {
            throw alreadyMapped();
        }
        jobTrainingRepository.save(new JobTrainingCourse(job, course));
    }

    public void linkSupportProgram(Long jobId, Long programId) {
        Job job = findJob(jobId);
        SupportProgram program = findSupport(programId);
        if (jobSupportRepository.existsByJobJobIdAndSupportProgramProgramId(jobId, programId)) {
            throw alreadyMapped();
        }
        jobSupportRepository.save(new JobSupportProgram(job, program));
    }

    private void replaceJobMappings(Job job, List<Long> requestedFieldIds,
                                    List<com.project.jobmatch.domain.user.entity.WorkType> workTypes) {
        Set<Long> fieldIds = new LinkedHashSet<>(requestedFieldIds);
        List<InterestField> fields = interestFieldRepository.findAllById(fieldIds);
        if (fields.size() != fieldIds.size()) {
            throw notFound("INTEREST_FIELD_NOT_FOUND", "해당 관심분야를 찾을 수 없습니다.");
        }
        jobFieldRepository.deleteAllByJobJobId(job.getJobId());
        jobWorkTypeRepository.deleteAllByJobJobId(job.getJobId());
        jobFieldRepository.saveAll(fields.stream().map(field -> new JobInterestField(job, field)).toList());
        new LinkedHashSet<>(workTypes).forEach(workType ->
                jobWorkTypeRepository.save(new JobWorkType(job, workType)));
    }

    private Job saveJob(Job job) {
        try {
            return jobRepository.saveAndFlush(job);
        } catch (DataIntegrityViolationException exception) {
            throw new CustomException(HttpStatus.CONFLICT, "JOB_CODE_ALREADY_EXISTS",
                    "이미 사용 중인 직업 코드입니다.");
        }
    }

    private Region findRegion(Long regionId) {
        return regionId == null ? null : regionRepository.findById(regionId)
                .orElseThrow(() -> notFound("REGION_NOT_FOUND", "해당 지역을 찾을 수 없습니다."));
    }

    private Job findOptionalJob(Long jobId) { return jobId == null ? null : findJob(jobId); }
    private Job findJob(Long id) { return jobRepository.findById(id)
            .orElseThrow(() -> notFound("JOB_NOT_FOUND", "해당 직업을 찾을 수 없습니다.")); }
    private TrainingCourse findTraining(Long id) { return trainingRepository.findById(id)
            .orElseThrow(() -> notFound("TRAINING_NOT_FOUND", "해당 훈련 과정을 찾을 수 없습니다.")); }
    private SupportProgram findSupport(Long id) { return supportRepository.findById(id)
            .orElseThrow(() -> notFound("SUPPORT_PROGRAM_NOT_FOUND", "해당 지원제도를 찾을 수 없습니다.")); }
    private JobPosting findPosting(Long id) { return postingRepository.findById(id)
            .orElseThrow(() -> notFound("POSTING_NOT_FOUND", "해당 채용공고를 찾을 수 없습니다.")); }

    private void deleteOrConflict(Runnable delete) {
        try { delete.run(); }
        catch (DataIntegrityViolationException exception) {
            throw new CustomException(HttpStatus.CONFLICT, "RESOURCE_IN_USE", "사용 중인 데이터는 삭제할 수 없습니다.");
        }
    }

    private CustomException alreadyMapped() {
        return new CustomException(HttpStatus.CONFLICT, "ALREADY_MAPPED", "이미 연결된 항목입니다.");
    }
    private CustomException notFound(String code, String message) {
        return new CustomException(HttpStatus.NOT_FOUND, code, message);
    }
}
