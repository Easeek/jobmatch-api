package com.project.jobmatch.domain.training.entity;

import com.project.jobmatch.domain.job.entity.Region;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "training_course")
@EntityListeners(AuditingEntityListener.class)
public class TrainingCourse {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_id")
    private Long courseId;
    @Column(name = "course_name", length = 200, nullable = false)
    private String courseName;
    @Column(length = 100)
    private String institution;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id")
    private Region region;
    @Enumerated(EnumType.STRING)
    @Column(name = "cost_type", length = 20)
    private CostType costType;
    @Column(name = "start_date")
    private LocalDate startDate;
    @Column(name = "end_date")
    private LocalDate endDate;
    @Column(columnDefinition = "TEXT")
    private String description;
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

    protected TrainingCourse() {}

    public TrainingCourse(String courseName, String institution, Region region, CostType costType,
                          LocalDate startDate, LocalDate endDate, String description,
                          String externalUrl, String source) {
        this.courseName = courseName;
        this.institution = institution;
        this.region = region;
        this.costType = costType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.description = description;
        this.externalUrl = externalUrl;
        this.source = source;
    }

    public Long getCourseId() { return courseId; }
    public String getCourseName() { return courseName; }
    public String getInstitution() { return institution; }
    public Region getRegion() { return region; }
    public CostType getCostType() { return costType; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }
    public String getDescription() { return description; }
    public String getExternalUrl() { return externalUrl; }
    public String getSource() { return source; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
