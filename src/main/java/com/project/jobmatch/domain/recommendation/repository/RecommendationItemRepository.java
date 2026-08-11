package com.project.jobmatch.domain.recommendation.repository;

import com.project.jobmatch.domain.recommendation.entity.RecommendationItem;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RecommendationItemRepository extends JpaRepository<RecommendationItem, Long> {
    @EntityGraph(attributePaths = "job")
    List<RecommendationItem> findAllByResultResultIdOrderByRankOrderAsc(Long resultId);

    @EntityGraph(attributePaths = {"job", "result"})
    Optional<RecommendationItem> findByItemIdAndResultResultId(Long itemId, Long resultId);
}
