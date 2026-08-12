package com.project.jobmatch.domain.job.entity;

import com.project.jobmatch.domain.user.entity.EducationLevel;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "job")
@EntityListeners(AuditingEntityListener.class)
public class Job {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "job_id")
    private Long jobId;
    @Column(name = "job_code", length = 30, nullable = false, unique = true)
    private String jobCode;
    @Column(name = "job_name", length = 50, nullable = false)
    private String jobName;
    @Enumerated(EnumType.STRING)
    @Column(name = "required_education", length = 20)
    private EducationLevel requiredEducation;
    @Column(name = "avg_salary_text", length = 100)
    private String avgSalaryText;
    @Column(columnDefinition = "TEXT")
    private String description;
    @Column(length = 30)
    private String source;
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    protected Job() {}

    public Job(String jobCode, String jobName, EducationLevel requiredEducation, String avgSalaryText,
               String description, String source) {
        this.jobCode = jobCode;
        this.jobName = jobName;
        this.requiredEducation = requiredEducation;
        this.avgSalaryText = avgSalaryText;
        this.description = description;
        this.source = source;
    }

    public void update(String jobCode, String jobName, EducationLevel requiredEducation,
                       String avgSalaryText, String description, String source) {
        this.jobCode = jobCode;
        this.jobName = jobName;
        this.requiredEducation = requiredEducation;
        this.avgSalaryText = avgSalaryText;
        this.description = description;
        this.source = source;
    }

    public Long getJobId() { return jobId; }
    public String getJobCode() { return jobCode; }
    public String getJobName() { return jobName; }
    public EducationLevel getRequiredEducation() { return requiredEducation; }
    public String getAvgSalaryText() { return avgSalaryText; }
    public String getDescription() { return description; }
    public String getSource() { return source; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
