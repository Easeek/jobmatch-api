package com.project.jobmatch.domain.job.dto;

import com.project.jobmatch.domain.user.entity.CareerLevel;
import com.project.jobmatch.domain.user.entity.EducationLevel;
import com.project.jobmatch.domain.user.entity.WorkType;

import java.time.LocalDate;

public final class PostingResponse {
    private PostingResponse() {}

    public record Summary(Long postingId, String title, String companyName, String region,
            WorkType workType, CareerLevel careerLevel, LocalDate applyDeadline) {}

    public record Detail(Long postingId, Job job, String title, String companyName, Region region,
            WorkType workType, EducationLevel requiredEducation, CareerLevel careerLevel,
            String salaryText, LocalDate applyDeadline, String externalUrl, String source) {}

    public record Job(Long jobId, String jobName) {}
    public record Region(Long regionId, String regionName) {}
}
