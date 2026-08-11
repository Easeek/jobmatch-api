package com.project.jobmatch.domain.recommendation.repository;

import com.project.jobmatch.domain.recommendation.entity.RecommendationCriteria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecommendationCriteriaRepository extends JpaRepository<RecommendationCriteria, Long> {
    List<RecommendationCriteria> findAllByActiveTrue();
}
