package com.project.jobmatch.domain.job.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public record JobTrainingCourseId(
        @Column(name = "job_id") Long jobId,
        @Column(name = "course_id") Long courseId
) implements Serializable {
    public JobTrainingCourseId() { this(null, null); }
}
