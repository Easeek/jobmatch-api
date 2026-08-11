package com.project.jobmatch.domain.job.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "job_interest_field")
public class JobInterestField {
    @EmbeddedId
    private JobInterestFieldId id;
    @MapsId("jobId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id", nullable = false)
    private Job job;
    @MapsId("fieldId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "field_id", nullable = false)
    private InterestField interestField;

    protected JobInterestField() {}

    public JobInterestField(Job job, InterestField interestField) {
        this.id = new JobInterestFieldId(job.getJobId(), interestField.getFieldId());
        this.job = job;
        this.interestField = interestField;
    }

    public Job getJob() { return job; }
    public InterestField getInterestField() { return interestField; }
}
