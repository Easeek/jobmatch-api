package com.project.jobmatch.domain.user.entity;

import com.project.jobmatch.domain.job.entity.Region;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_condition")
@EntityListeners(AuditingEntityListener.class)
public class UserCondition {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "condition_id")
    private Long conditionId;
    @Column(name = "session_key", length = 64, nullable = false)
    private String sessionKey;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    @Enumerated(EnumType.STRING)
    @Column(name = "career_level", length = 20, nullable = false)
    private CareerLevel careerLevel;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id")
    private Region region;
    @Enumerated(EnumType.STRING)
    @Column(name = "work_type", length = 20)
    private WorkType workType;
    @Enumerated(EnumType.STRING)
    @Column(name = "education_level", length = 20, nullable = false)
    private EducationLevel educationLevel;
    @Column(name = "training_desired", nullable = false)
    private boolean trainingDesired;
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    protected UserCondition() {}

    public UserCondition(String sessionKey, CareerLevel careerLevel, Region region, WorkType workType,
                         EducationLevel educationLevel, boolean trainingDesired) {
        this.sessionKey = sessionKey;
        this.careerLevel = careerLevel;
        this.region = region;
        this.workType = workType;
        this.educationLevel = educationLevel;
        this.trainingDesired = trainingDesired;
    }

    public Long getConditionId() { return conditionId; }
    public String getSessionKey() { return sessionKey; }
    public CareerLevel getCareerLevel() { return careerLevel; }
    public Region getRegion() { return region; }
    public WorkType getWorkType() { return workType; }
    public EducationLevel getEducationLevel() { return educationLevel; }
    public boolean isTrainingDesired() { return trainingDesired; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
