package com.project.jobmatch.domain.job.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public record JobInterestFieldId(
        @Column(name = "job_id") Long jobId,
        @Column(name = "field_id") Long fieldId
) implements Serializable {
    public JobInterestFieldId() { this(null, null); }
}
