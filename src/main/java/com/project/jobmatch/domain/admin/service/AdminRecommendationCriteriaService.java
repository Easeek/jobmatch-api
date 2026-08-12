package com.project.jobmatch.domain.admin.service;

import com.project.jobmatch.common.exception.CustomException;
import com.project.jobmatch.domain.admin.dto.RecommendationCriteriaResponse;
import com.project.jobmatch.domain.admin.dto.RecommendationCriteriaUpdateRequest;
import com.project.jobmatch.domain.recommendation.entity.RecommendationCriteria;
import com.project.jobmatch.domain.recommendation.repository.RecommendationCriteriaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class AdminRecommendationCriteriaService {
    private final RecommendationCriteriaRepository criteriaRepository;

    public AdminRecommendationCriteriaService(RecommendationCriteriaRepository criteriaRepository) {
        this.criteriaRepository = criteriaRepository;
    }

    public List<RecommendationCriteriaResponse> getAll() {
        return criteriaRepository.findAll().stream()
                .sorted((left, right) -> left.getCriteriaId().compareTo(right.getCriteriaId()))
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public RecommendationCriteriaResponse update(Long criteriaId,
                                                 RecommendationCriteriaUpdateRequest request) {
        RecommendationCriteria criteria = criteriaRepository.findById(criteriaId)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND,
                        "RECOMMENDATION_CRITERIA_NOT_FOUND", "해당 추천 기준을 찾을 수 없습니다."));
        criteria.update(request.weight(), request.isActive());
        RecommendationCriteria saved = criteriaRepository.saveAndFlush(criteria);
        return toResponse(saved);
    }

    private RecommendationCriteriaResponse toResponse(RecommendationCriteria criteria) {
        return new RecommendationCriteriaResponse(criteria.getCriteriaId(), criteria.getCriteriaKey(),
                criteria.getWeight(), criteria.isActive(), criteria.getDescription(), criteria.getUpdatedAt());
    }
}
