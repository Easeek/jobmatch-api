package com.project.jobmatch.domain.saved.dto;

import java.time.LocalDateTime;

public final class SavedTrainingResponse {
    private SavedTrainingResponse() {}

    public record Created(Long savedTrainingId, LocalDateTime createdAt) {}
    public record Summary(Long savedTrainingId, Training training, LocalDateTime createdAt) {}
    public record Training(Long courseId, String courseName, String institution) {}
}
