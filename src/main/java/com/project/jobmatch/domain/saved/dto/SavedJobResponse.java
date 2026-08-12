package com.project.jobmatch.domain.saved.dto;

import java.time.LocalDateTime;

public final class SavedJobResponse {
    private SavedJobResponse() {}

    public record Created(Long savedJobId, LocalDateTime createdAt) {}
    public record Summary(Long savedJobId, Job job, String memo, LocalDateTime createdAt) {}
    public record Job(Long jobId, String jobName) {}
}
