package com.project.jobmatch.domain.saved.entity;

import com.project.jobmatch.domain.job.entity.Job;
import com.project.jobmatch.domain.user.entity.User;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "saved_job", uniqueConstraints =
        @UniqueConstraint(name = "uk_saved_job_session_job", columnNames = {"session_key", "job_id"}))
@EntityListeners(AuditingEntityListener.class)
public class SavedJob {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "saved_job_id")
    private Long savedJobId;
    @Column(name = "session_key", length = 64, nullable = false)
    private String sessionKey;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "job_id", nullable = false)
    private Job job;
    @Column(length = 255)
    private String memo;
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    protected SavedJob() {}

    public SavedJob(String sessionKey, Job job, String memo) {
        this.sessionKey = sessionKey;
        this.job = job;
        this.memo = memo;
    }

    public Long getSavedJobId() { return savedJobId; }
    public String getSessionKey() { return sessionKey; }
    public Job getJob() { return job; }
    public String getMemo() { return memo; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
