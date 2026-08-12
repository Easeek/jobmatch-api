package com.project.jobmatch.domain.support.entity;

import com.project.jobmatch.domain.job.entity.Region;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "support_program")
@EntityListeners(AuditingEntityListener.class)
public class SupportProgram {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "program_id")
    private Long programId;
    @Column(name = "program_name", length = 200, nullable = false)
    private String programName;
    @Column(length = 100)
    private String organization;
    @Column(name = "target_audience", length = 200)
    private String targetAudience;
    @Column(name = "support_content", columnDefinition = "TEXT")
    private String supportContent;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id")
    private Region region;
    @Column(name = "apply_start_date")
    private LocalDate applyStartDate;
    @Column(name = "apply_end_date")
    private LocalDate applyEndDate;
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

    protected SupportProgram() {}

    public SupportProgram(String programName, String organization, String targetAudience,
                          String supportContent, Region region, LocalDate applyStartDate,
                          LocalDate applyEndDate, String externalUrl, String source) {
        this.programName = programName;
        this.organization = organization;
        this.targetAudience = targetAudience;
        this.supportContent = supportContent;
        this.region = region;
        this.applyStartDate = applyStartDate;
        this.applyEndDate = applyEndDate;
        this.externalUrl = externalUrl;
        this.source = source;
    }

    public void update(String programName, String organization, String targetAudience,
                       String supportContent, Region region, LocalDate applyStartDate,
                       LocalDate applyEndDate, String externalUrl, String source) {
        this.programName = programName;
        this.organization = organization;
        this.targetAudience = targetAudience;
        this.supportContent = supportContent;
        this.region = region;
        this.applyStartDate = applyStartDate;
        this.applyEndDate = applyEndDate;
        this.externalUrl = externalUrl;
        this.source = source;
    }

    public Long getProgramId() { return programId; }
    public String getProgramName() { return programName; }
    public String getOrganization() { return organization; }
    public String getTargetAudience() { return targetAudience; }
    public String getSupportContent() { return supportContent; }
    public Region getRegion() { return region; }
    public LocalDate getApplyStartDate() { return applyStartDate; }
    public LocalDate getApplyEndDate() { return applyEndDate; }
    public String getExternalUrl() { return externalUrl; }
    public String getSource() { return source; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
