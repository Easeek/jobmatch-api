package com.project.jobmatch.domain.admin.controller;

import com.project.jobmatch.common.response.ApiResponse;
import com.project.jobmatch.domain.admin.dto.RecommendationCriteriaResponse;
import com.project.jobmatch.domain.admin.dto.RecommendationCriteriaUpdateRequest;
import com.project.jobmatch.domain.admin.service.AdminRecommendationCriteriaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/recommendation-criteria")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "관리자 - 추천 기준 관리", description = "직업 추천 기준의 가중치 및 활성화 상태 관리 API")
public class AdminRecommendationCriteriaController {
    private final AdminRecommendationCriteriaService service;

    public AdminRecommendationCriteriaController(AdminRecommendationCriteriaService service) {
        this.service = service;
    }

    @Operation(summary = "추천 기준(가중치) 목록")
    @GetMapping
    public ApiResponse<List<RecommendationCriteriaResponse>> getAll() {
        return ApiResponse.success(service.getAll());
    }

    @Operation(summary = "추천 기준 가중치/활성화 수정")
    @PutMapping("/{criteriaId}")
    public ApiResponse<RecommendationCriteriaResponse> update(@PathVariable Long criteriaId,
            @Valid @RequestBody RecommendationCriteriaUpdateRequest request) {
        return ApiResponse.success(service.update(criteriaId, request));
    }
}
