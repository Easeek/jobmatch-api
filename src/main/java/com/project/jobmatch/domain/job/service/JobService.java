package com.project.jobmatch.domain.job.service;

import com.project.jobmatch.common.exception.CustomException;
import com.project.jobmatch.domain.job.dto.JobResponse;
import com.project.jobmatch.domain.job.entity.*;
import com.project.jobmatch.domain.job.repository.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class JobService {
    private final JobRepository jobRepository;
    private final JobInterestFieldRepository fieldRepository;
    private final JobWorkTypeRepository workTypeRepository;
    private final JobTrainingCourseRepository trainingRepository;
    private final JobSupportProgramRepository supportRepository;

    public JobService(JobRepository jobRepository, JobInterestFieldRepository fieldRepository,
                      JobWorkTypeRepository workTypeRepository,
                      JobTrainingCourseRepository trainingRepository,
                      JobSupportProgramRepository supportRepository) {
        this.jobRepository = jobRepository;
        this.fieldRepository = fieldRepository;
        this.workTypeRepository = workTypeRepository;
        this.trainingRepository = trainingRepository;
        this.supportRepository = supportRepository;
    }

    public List<JobResponse.Summary> getJobs(Long fieldId, String keyword) {
        List<Job> jobs = jobRepository.findAll();
        Set<Long> ids = jobs.stream().map(Job::getJobId).collect(Collectors.toSet());
        Map<Long, List<JobInterestField>> fields = fieldRepository.findAllByJobJobIdIn(ids).stream()
                .collect(Collectors.groupingBy(link -> link.getJob().getJobId()));
        String normalized = normalize(keyword);
        return jobs.stream()
                .filter(job -> fieldId == null || fields.getOrDefault(job.getJobId(), List.of()).stream()
                        .anyMatch(link -> Objects.equals(link.getInterestField().getFieldId(), fieldId)))
                .filter(job -> normalized == null || job.getJobName().toLowerCase(Locale.ROOT).contains(normalized))
                .sorted(Comparator.comparing(Job::getJobId))
                .map(job -> new JobResponse.Summary(job.getJobId(), job.getJobName(),
                        fields.getOrDefault(job.getJobId(), List.of()).stream()
                                .map(link -> link.getInterestField().getFieldName()).sorted().toList()))
                .toList();
    }

    public JobResponse.Detail getJob(Long jobId) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> notFound("JOB_NOT_FOUND", "해당 직업을 찾을 수 없습니다."));
        List<JobResponse.Field> fields = fieldRepository.findAllByJobJobIdIn(List.of(jobId)).stream()
                .map(JobInterestField::getInterestField)
                .sorted(Comparator.comparing(InterestField::getFieldId))
                .map(field -> new JobResponse.Field(field.getFieldId(), field.getFieldCode(), field.getFieldName()))
                .toList();
        var workTypes = workTypeRepository.findAllByJobJobIdIn(List.of(jobId)).stream()
                .map(JobWorkType::getWorkType).sorted().toList();
        var trainings = trainingRepository.findAllByJobJobId(jobId).stream()
                .map(JobTrainingCourse::getTrainingCourse)
                .map(course -> new JobResponse.RelatedTraining(course.getCourseId(), course.getCourseName()))
                .sorted(Comparator.comparing(JobResponse.RelatedTraining::courseId)).toList();
        var supports = supportRepository.findAllByJobJobId(jobId).stream()
                .map(JobSupportProgram::getSupportProgram)
                .map(program -> new JobResponse.RelatedSupportProgram(program.getProgramId(), program.getProgramName()))
                .sorted(Comparator.comparing(JobResponse.RelatedSupportProgram::programId)).toList();
        return new JobResponse.Detail(job.getJobId(), job.getJobCode(), job.getJobName(),
                job.getRequiredEducation(), job.getAvgSalaryText(), job.getDescription(), job.getSource(),
                fields, workTypes, trainings, supports);
    }

    private String normalize(String keyword) {
        return keyword == null || keyword.isBlank() ? null : keyword.trim().toLowerCase(Locale.ROOT);
    }

    private CustomException notFound(String code, String message) {
        return new CustomException(HttpStatus.NOT_FOUND, code, message);
    }
}
