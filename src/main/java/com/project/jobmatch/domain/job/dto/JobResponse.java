package com.project.jobmatch.domain.job.dto;

import com.project.jobmatch.domain.user.entity.EducationLevel;
import com.project.jobmatch.domain.user.entity.WorkType;

import java.util.List;

public final class JobResponse {
    private JobResponse() {}

    public record Summary(Long jobId, String jobName, List<String> fields) {}

    public record Detail(Long jobId, String jobCode, String jobName,
            EducationLevel requiredEducation, String avgSalaryText, String description, String source,
            List<Field> fields, List<WorkType> workTypes,
            List<RelatedTraining> relatedTrainings, List<RelatedSupportProgram> relatedSupportPrograms) {}

    public record Field(Long fieldId, String fieldCode, String fieldName) {}
    public record RelatedTraining(Long courseId, String courseName) {}
    public record RelatedSupportProgram(Long programId, String programName) {}
}
