package com.project.jobmatch.domain.recommendation.dto;

import com.project.jobmatch.domain.training.entity.CostType;
import com.project.jobmatch.domain.user.entity.EducationLevel;

import java.math.BigDecimal;
import java.util.List;

public record RecommendationItemDetailResponse(
        Long itemId,
        JobDetail job,
        BigDecimal matchScore,
        String reason,
        List<TrainingSummary> relatedTrainings,
        List<SupportProgramSummary> relatedSupportPrograms
) {
    public record JobDetail(Long jobId, String jobName, String description,
                            EducationLevel requiredEducation, String avgSalaryText) {}
    public record TrainingSummary(Long courseId, String courseName, String institution, CostType costType) {}
    public record SupportProgramSummary(Long programId, String programName, String organization) {}
}
