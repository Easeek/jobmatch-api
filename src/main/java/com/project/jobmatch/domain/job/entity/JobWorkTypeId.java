package com.project.jobmatch.domain.job.entity;

import com.project.jobmatch.domain.user.entity.WorkType;
import jakarta.persistence.*;

import java.io.Serializable;

@Embeddable
public record JobWorkTypeId(
        @Column(name = "job_id") Long jobId,
        @Enumerated(EnumType.STRING) @Column(name = "work_type", length = 20) WorkType workType
) implements Serializable {
    public JobWorkTypeId() { this(null, null); }
}
