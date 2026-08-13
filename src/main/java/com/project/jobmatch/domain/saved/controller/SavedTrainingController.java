package com.project.jobmatch.domain.saved.controller;

import com.project.jobmatch.common.response.ApiResponse;
import com.project.jobmatch.domain.saved.dto.SavedTrainingRequest;
import com.project.jobmatch.domain.saved.dto.SavedTrainingResponse;
import com.project.jobmatch.domain.saved.service.SavedItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/v1/saved-trainings")
@Tag(name = "관심 훈련 저장", description = "관심 훈련 과정 저장, 목록 조회 및 삭제 API")
public class SavedTrainingController {
    private static final String UUID_PATTERN =
            "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[1-5][0-9a-fA-F]{3}-[89aAbB][0-9a-fA-F]{3}-[0-9a-fA-F]{12}$";
    private final SavedItemService savedItemService;

    public SavedTrainingController(SavedItemService savedItemService) {
        this.savedItemService = savedItemService;
    }

    @Operation(summary = "관심 훈련 저장")
    @PostMapping
    public ApiResponse<SavedTrainingResponse.Created> save(
            @Valid @RequestBody SavedTrainingRequest request) {
        return ApiResponse.success(savedItemService.saveTraining(request));
    }

    @Operation(summary = "관심 훈련 목록 조회")
    @GetMapping
    public ApiResponse<List<SavedTrainingResponse.Summary>> getAll(
            @RequestParam @Pattern(regexp = UUID_PATTERN) String sessionKey) {
        return ApiResponse.success(savedItemService.getSavedTrainings(sessionKey));
    }

    @Operation(summary = "관심 훈련 삭제")
    @DeleteMapping("/{savedTrainingId}")
    public ApiResponse<Void> delete(@PathVariable Long savedTrainingId) {
        savedItemService.deleteSavedTraining(savedTrainingId);
        return ApiResponse.success(null);
    }
}
