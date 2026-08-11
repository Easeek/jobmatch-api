package com.project.jobmatch.domain.job.entity;

import com.project.jobmatch.domain.user.entity.WorkType;
import jakarta.persistence.*;

@Entity
@Table(name = "job_work_type")
public class JobWorkType {
    @EmbeddedId
    private JobWorkTypeId id;
    @MapsId("jobId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id", nullable = false)
    private Job job;

    protected JobWorkType() {}

    public JobWorkType(Job job, WorkType workType) {
        this.id = new JobWorkTypeId(job.getJobId(), workType);
        this.job = job;
    }

    public Job getJob() { return job; }
    public WorkType getWorkType() { return id.workType(); }
}
