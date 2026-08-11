package com.project.jobmatch.domain.user.controller;

import com.project.jobmatch.common.response.ApiResponse;
import com.project.jobmatch.domain.user.dto.*;
import com.project.jobmatch.domain.user.service.ConditionService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class ConditionController {
    private final ConditionService conditionService;

    public ConditionController(ConditionService conditionService) {
        this.conditionService = conditionService;
    }

    @Operation(summary = "사용자 조건 저장")
    @PostMapping("/conditions")
    public ApiResponse<ConditionCreateResponse> create(@Valid @RequestBody ConditionCreateRequest request) {
        return ApiResponse.success(conditionService.create(request));
    }

    @Operation(summary = "저장된 조건 조회")
    @GetMapping("/conditions/{conditionId}")
    public ApiResponse<ConditionResponse> get(@PathVariable Long conditionId) {
        return ApiResponse.success(conditionService.get(conditionId));
    }

    @Operation(summary = "조건 입력용 코드값 목록 (드롭다운 채우기)")
    @GetMapping("/meta/conditions")
    public ApiResponse<ConditionMetaResponse> getMeta() {
        return ApiResponse.success(conditionService.getMeta());
    }
}
