package com.project.jobmatch.domain.support.dto;

import java.time.LocalDate;

public final class SupportProgramResponse {
    private SupportProgramResponse() {}

    public record Summary(Long programId, String programName, String organization,
            String targetAudience, String region, LocalDate applyEndDate) {}

    public record Detail(Long programId, String programName, String organization,
            String targetAudience, String supportContent, Region region,
            LocalDate applyStartDate, LocalDate applyEndDate, String externalUrl, String source) {}

    public record Region(Long regionId, String regionName) {}
}
