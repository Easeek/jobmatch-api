package com.project.jobmatch.domain.recommendation.entity;

import com.project.jobmatch.domain.user.entity.UserCondition;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "recommendation_result")
@EntityListeners(AuditingEntityListener.class)
public class RecommendationResult {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "result_id")
    private Long resultId;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "condition_id", nullable = false)
    private UserCondition condition;
    @Column(name = "recommended_count", nullable = false)
    private int recommendedCount;
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    protected RecommendationResult() {}

    public RecommendationResult(UserCondition condition, int recommendedCount) {
        this.condition = condition;
        this.recommendedCount = recommendedCount;
    }

    public Long getResultId() { return resultId; }
    public UserCondition getCondition() { return condition; }
    public int getRecommendedCount() { return recommendedCount; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
