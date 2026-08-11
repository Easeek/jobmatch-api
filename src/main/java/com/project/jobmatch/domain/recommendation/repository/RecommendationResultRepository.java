package com.project.jobmatch.domain.recommendation.repository;

import com.project.jobmatch.domain.recommendation.entity.RecommendationResult;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RecommendationResultRepository extends JpaRepository<RecommendationResult, Long> {
    @EntityGraph(attributePaths = "condition")
    Optional<RecommendationResult> findByResultId(Long resultId);
}
