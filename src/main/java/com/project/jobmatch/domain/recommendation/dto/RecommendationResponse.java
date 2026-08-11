package com.project.jobmatch.domain.recommendation.dto;

import java.math.BigDecimal;
import java.util.List;

public record RecommendationResponse(Long resultId, Long conditionId, int recommendedCount,
                                     List<ItemSummary> items) {
    public record ItemSummary(Long itemId, int rankOrder, JobSummary job, BigDecimal matchScore,
                              String reason, long relatedTrainingCount, long relatedSupportCount) {}
    public record JobSummary(Long jobId, String jobName) {}
}
