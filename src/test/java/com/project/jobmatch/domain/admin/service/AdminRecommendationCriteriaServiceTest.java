package com.project.jobmatch.domain.admin.service;

import com.project.jobmatch.domain.admin.dto.RecommendationCriteriaUpdateRequest;
import com.project.jobmatch.domain.recommendation.entity.CriteriaKey;
import com.project.jobmatch.domain.recommendation.entity.RecommendationCriteria;
import com.project.jobmatch.domain.recommendation.repository.RecommendationCriteriaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdminRecommendationCriteriaServiceTest {
    @Mock RecommendationCriteriaRepository repository;

    @Test
    void updateChangesWeightAndActiveState() {
        RecommendationCriteria criteria = new RecommendationCriteria(
                CriteriaKey.INTEREST_FIELD_MATCH, new BigDecimal("30.00"), true, "interest");
        when(repository.findById(1L)).thenReturn(Optional.of(criteria));
        when(repository.saveAndFlush(criteria)).thenReturn(criteria);
        AdminRecommendationCriteriaService service = new AdminRecommendationCriteriaService(repository);

        var response = service.update(1L,
                new RecommendationCriteriaUpdateRequest(new BigDecimal("45.00"), false));

        assertThat(response.weight()).isEqualByComparingTo("45.00");
        assertThat(response.isActive()).isFalse();
    }
}
