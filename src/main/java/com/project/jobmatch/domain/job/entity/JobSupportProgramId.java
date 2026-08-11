package com.project.jobmatch.domain.job.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public record JobSupportProgramId(
        @Column(name = "job_id") Long jobId,
        @Column(name = "program_id") Long programId
) implements Serializable {
    public JobSupportProgramId() { this(null, null); }
}
