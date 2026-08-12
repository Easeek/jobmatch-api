package com.project.jobmatch.domain.job.entity;

import com.project.jobmatch.domain.user.entity.CareerLevel;
import com.project.jobmatch.domain.user.entity.EducationLevel;
import com.project.jobmatch.domain.user.entity.WorkType;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "job_posting")
@EntityListeners(AuditingEntityListener.class)
public class JobPosting {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "posting_id")
    private Long postingId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id")
    private Job job;
    @Column(length = 200, nullable = false)
    private String title;
    @Column(name = "company_name", length = 100)
    private String companyName;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id")
    private Region region;
    @Enumerated(EnumType.STRING)
    @Column(name = "work_type", length = 20)
    private WorkType workType;
    @Enumerated(EnumType.STRING)
    @Column(name = "required_education", length = 20)
    private EducationLevel requiredEducation;
    @Enumerated(EnumType.STRING)
    @Column(name = "career_level", length = 20)
    private CareerLevel careerLevel;
    @Column(name = "salary_text", length = 100)
    private String salaryText;
    @Column(name = "apply_deadline")
    private LocalDate applyDeadline;
    @Column(name = "external_url", length = 500)
    private String externalUrl;
    @Column(length = 30)
    private String source;
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    protected JobPosting() {}

    public Long getPostingId() { return postingId; }
    public Job getJob() { return job; }
    public String getTitle() { return title; }
    public String getCompanyName() { return companyName; }
    public Region getRegion() { return region; }
    public WorkType getWorkType() { return workType; }
    public EducationLevel getRequiredEducation() { return requiredEducation; }
    public CareerLevel getCareerLevel() { return careerLevel; }
    public String getSalaryText() { return salaryText; }
    public LocalDate getApplyDeadline() { return applyDeadline; }
    public String getExternalUrl() { return externalUrl; }
    public String getSource() { return source; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
