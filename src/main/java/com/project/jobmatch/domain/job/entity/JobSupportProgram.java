package com.project.jobmatch.domain.job.entity;

import com.project.jobmatch.domain.support.entity.SupportProgram;
import jakarta.persistence.*;

@Entity
@Table(name = "job_support_program")
public class JobSupportProgram {
    @EmbeddedId
    private JobSupportProgramId id;
    @MapsId("jobId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id", nullable = false)
    private Job job;
    @MapsId("programId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "program_id", nullable = false)
    private SupportProgram supportProgram;

    protected JobSupportProgram() {}

    public JobSupportProgram(Job job, SupportProgram supportProgram) {
        this.id = new JobSupportProgramId(job.getJobId(), supportProgram.getProgramId());
        this.job = job;
        this.supportProgram = supportProgram;
    }

    public Job getJob() { return job; }
    public SupportProgram getSupportProgram() { return supportProgram; }
}
