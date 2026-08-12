package com.project.jobmatch.domain.job.controller;

import com.project.jobmatch.common.response.ApiResponse;
import com.project.jobmatch.domain.job.dto.PostingResponse;
import com.project.jobmatch.domain.job.service.PostingService;
import com.project.jobmatch.domain.user.entity.WorkType;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/postings")
public class PostingController {
    private final PostingService postingService;

    public PostingController(PostingService postingService) { this.postingService = postingService; }

    @Operation(summary = "일자리 목록")
    @GetMapping
    public ApiResponse<List<PostingResponse.Summary>> getPostings(
            @RequestParam(required = false) Long regionId,
            @RequestParam(required = false) Long jobId,
            @RequestParam(required = false) WorkType workType,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String sort) {
        return ApiResponse.success(postingService.getPostings(regionId, jobId, workType, keyword, sort));
    }

    @Operation(summary = "일자리 상세")
    @GetMapping("/{postingId}")
    public ApiResponse<PostingResponse.Detail> getPosting(@PathVariable Long postingId) {
        return ApiResponse.success(postingService.getPosting(postingId));
    }
}
