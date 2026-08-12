package com.project.jobmatch.domain.training.controller;

import com.project.jobmatch.common.response.ApiResponse;
import com.project.jobmatch.domain.training.dto.TrainingResponse;
import com.project.jobmatch.domain.training.entity.CostType;
import com.project.jobmatch.domain.training.service.TrainingService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/trainings")
public class TrainingController {
    private final TrainingService trainingService;

    public TrainingController(TrainingService trainingService) { this.trainingService = trainingService; }

    @Operation(summary = "훈련 과정 목록")
    @GetMapping
    public ApiResponse<List<TrainingResponse.Summary>> getTrainings(
            @RequestParam(required = false) Long regionId,
            @RequestParam(required = false) CostType costType,
            @RequestParam(required = false) Long jobId,
            @RequestParam(required = false) String keyword) {
        return ApiResponse.success(trainingService.getTrainings(regionId, costType, jobId, keyword));
    }

    @Operation(summary = "훈련 과정 상세 (설명·URL 포함)")
    @GetMapping("/{courseId}")
    public ApiResponse<TrainingResponse.Detail> getTraining(@PathVariable Long courseId) {
        return ApiResponse.success(trainingService.getTraining(courseId));
    }
}
