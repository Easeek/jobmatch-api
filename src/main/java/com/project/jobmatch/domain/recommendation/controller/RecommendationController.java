package com.project.jobmatch.domain.recommendation.controller;

import com.project.jobmatch.common.response.ApiResponse;
import com.project.jobmatch.domain.recommendation.dto.RecommendationItemDetailResponse;
import com.project.jobmatch.domain.recommendation.dto.RecommendationResponse;
import com.project.jobmatch.domain.recommendation.service.RecommendationService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class RecommendationController {
    private final RecommendationService recommendationService;

    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    @Operation(summary = "추천 실행")
    @PostMapping("/conditions/{conditionId}/recommendations")
    public ApiResponse<RecommendationResponse> recommend(@PathVariable Long conditionId) {
        return ApiResponse.success(recommendationService.recommend(conditionId));
    }

    @Operation(summary = "추천 결과 재조회")
    @GetMapping("/recommendations/{resultId}")
    public ApiResponse<RecommendationResponse> getResult(@PathVariable Long resultId) {
        return ApiResponse.success(recommendationService.getResult(resultId));
    }

    @Operation(summary = "추천 직업 상세 (연관 정보 포함)")
    @GetMapping("/recommendations/{resultId}/items/{itemId}")
    public ApiResponse<RecommendationItemDetailResponse> getItem(@PathVariable Long resultId,
                                                                 @PathVariable Long itemId) {
        return ApiResponse.success(recommendationService.getItem(resultId, itemId));
    }
}
