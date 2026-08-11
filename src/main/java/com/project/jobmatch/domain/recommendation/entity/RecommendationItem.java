package com.project.jobmatch.domain.recommendation.entity;

import com.project.jobmatch.domain.job.entity.Job;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "recommendation_item")
public class RecommendationItem {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long itemId;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "result_id", nullable = false)
    private RecommendationResult result;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "job_id", nullable = false)
    private Job job;
    @Column(name = "match_score", nullable = false, precision = 5, scale = 2)
    private BigDecimal matchScore;
    @Column(columnDefinition = "TEXT")
    private String reason;
    @Column(name = "rank_order", nullable = false)
    private int rankOrder;

    protected RecommendationItem() {}

    public RecommendationItem(RecommendationResult result, Job job, BigDecimal matchScore,
                              String reason, int rankOrder) {
        this.result = result;
        this.job = job;
        this.matchScore = matchScore;
        this.reason = reason;
        this.rankOrder = rankOrder;
    }

    public Long getItemId() { return itemId; }
    public RecommendationResult getResult() { return result; }
    public Job getJob() { return job; }
    public BigDecimal getMatchScore() { return matchScore; }
    public String getReason() { return reason; }
    public int getRankOrder() { return rankOrder; }
}
