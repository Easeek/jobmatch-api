package com.project.jobmatch.domain.recommendation.entity;

import jakarta.persistence.*;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "recommendation_criteria")
@EntityListeners(AuditingEntityListener.class)
public class RecommendationCriteria {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "criteria_id")
    private Long criteriaId;
    @Enumerated(EnumType.STRING)
    @Column(name = "criteria_key", length = 30, nullable = false, unique = true)
    private CriteriaKey criteriaKey;
    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal weight;
    @Column(name = "is_active", nullable = false)
    private boolean active = true;
    @Column(length = 255)
    private String description;
    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    protected RecommendationCriteria() {}

    public RecommendationCriteria(CriteriaKey criteriaKey, BigDecimal weight, boolean active, String description) {
        this.criteriaKey = criteriaKey;
        this.weight = weight;
        this.active = active;
        this.description = description;
    }

    public void update(BigDecimal weight, boolean active) {
        this.weight = weight;
        this.active = active;
    }

    public Long getCriteriaId() { return criteriaId; }
    public CriteriaKey getCriteriaKey() { return criteriaKey; }
    public BigDecimal getWeight() { return weight; }
    public boolean isActive() { return active; }
    public String getDescription() { return description; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
