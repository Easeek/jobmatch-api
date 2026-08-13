package com.project.jobmatch.domain.saved.controller;

import com.project.jobmatch.common.response.ApiResponse;
import com.project.jobmatch.domain.saved.dto.SavedJobRequest;
import com.project.jobmatch.domain.saved.dto.SavedJobResponse;
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
@RequestMapping("/api/v1/saved-jobs")
@Tag(name = "관심 직업 저장", description = "관심 직업 저장, 목록 조회 및 삭제 API")
public class SavedJobController {
    private static final String UUID_PATTERN =
            "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[1-5][0-9a-fA-F]{3}-[89aAbB][0-9a-fA-F]{3}-[0-9a-fA-F]{12}$";
    private final SavedItemService savedItemService;

    public SavedJobController(SavedItemService savedItemService) {
        this.savedItemService = savedItemService;
    }

    @Operation(summary = "관심 직업 저장")
    @PostMapping
    public ApiResponse<SavedJobResponse.Created> save(@Valid @RequestBody SavedJobRequest request) {
        return ApiResponse.success(savedItemService.saveJob(request));
    }

    @Operation(summary = "관심 직업 목록 조회")
    @GetMapping
    public ApiResponse<List<SavedJobResponse.Summary>> getAll(
            @RequestParam @Pattern(regexp = UUID_PATTERN) String sessionKey) {
        return ApiResponse.success(savedItemService.getSavedJobs(sessionKey));
    }

    @Operation(summary = "관심 직업 삭제")
    @DeleteMapping("/{savedJobId}")
    public ApiResponse<Void> delete(@PathVariable Long savedJobId) {
        savedItemService.deleteSavedJob(savedJobId);
        return ApiResponse.success(null);
    }
}
