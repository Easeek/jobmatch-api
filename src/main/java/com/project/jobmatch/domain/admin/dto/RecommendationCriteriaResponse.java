package com.project.jobmatch.domain.admin.dto;

import com.project.jobmatch.domain.recommendation.entity.CriteriaKey;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record RecommendationCriteriaResponse(Long criteriaId, CriteriaKey criteriaKey,
        BigDecimal weight, boolean isActive, String description, LocalDateTime updatedAt) {}
