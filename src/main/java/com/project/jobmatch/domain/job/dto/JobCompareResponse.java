package com.project.jobmatch.domain.job.dto;

import com.project.jobmatch.domain.user.entity.EducationLevel;

import java.util.List;

public record JobCompareResponse(List<JobComparison> jobs) {
    public record JobComparison(Long jobId, String jobName, EducationLevel requiredEducation,
            String avgSalaryText, List<String> fields, long trainingCount,
            long supportCount, long postingCount) {}
}
