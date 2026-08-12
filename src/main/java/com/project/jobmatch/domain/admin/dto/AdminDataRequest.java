package com.project.jobmatch.domain.admin.dto;

import com.project.jobmatch.domain.training.entity.CostType;
import com.project.jobmatch.domain.user.entity.CareerLevel;
import com.project.jobmatch.domain.user.entity.EducationLevel;
import com.project.jobmatch.domain.user.entity.WorkType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;

public final class AdminDataRequest {
    private AdminDataRequest() {}

    public record JobRequest(
            @NotBlank @Size(max = 30) String jobCode,
            @NotBlank @Size(max = 50) String jobName,
            EducationLevel requiredEducation,
            @Size(max = 100) String avgSalaryText,
            String description,
            @Size(max = 30) String source,
            @NotNull List<@NotNull Long> interestFieldIds,
            @NotNull List<@NotNull WorkType> workTypes) {}

    public record TrainingRequest(
            @NotBlank @Size(max = 200) String courseName,
            @Size(max = 100) String institution,
            Long regionId,
            CostType costType,
            LocalDate startDate,
            LocalDate endDate,
            String description,
            @Size(max = 500) String externalUrl,
            @Size(max = 30) String source) {}

    public record SupportProgramRequest(
            @NotBlank @Size(max = 200) String programName,
            @Size(max = 100) String organization,
            @Size(max = 200) String targetAudience,
            String supportContent,
            Long regionId,
            LocalDate applyStartDate,
            LocalDate applyEndDate,
            @Size(max = 500) String externalUrl,
            @Size(max = 30) String source) {}

    public record PostingRequest(
            Long jobId,
            @NotBlank @Size(max = 200) String title,
            @Size(max = 100) String companyName,
            Long regionId,
            WorkType workType,
            EducationLevel requiredEducation,
            CareerLevel careerLevel,
            @Size(max = 100) String salaryText,
            LocalDate applyDeadline,
            @Size(max = 500) String externalUrl,
            @Size(max = 30) String source) {}

    public record TrainingLinkRequest(@NotNull Long courseId) {}
    public record SupportProgramLinkRequest(@NotNull Long programId) {}
}
