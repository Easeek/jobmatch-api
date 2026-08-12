package com.project.jobmatch.domain.training.dto;

import com.project.jobmatch.domain.training.entity.CostType;

import java.time.LocalDate;

public final class TrainingResponse {
    private TrainingResponse() {}

    public record Summary(Long courseId, String courseName, String institution, String region,
            CostType costType, LocalDate startDate, LocalDate endDate) {}

    public record Detail(Long courseId, String courseName, String institution, Region region,
            CostType costType, LocalDate startDate, LocalDate endDate, String description,
            String externalUrl, String source) {}

    public record Region(Long regionId, String regionName) {}
}
