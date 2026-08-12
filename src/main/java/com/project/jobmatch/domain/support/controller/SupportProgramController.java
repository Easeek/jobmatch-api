package com.project.jobmatch.domain.support.controller;

import com.project.jobmatch.common.response.ApiResponse;
import com.project.jobmatch.domain.support.dto.SupportProgramResponse;
import com.project.jobmatch.domain.support.service.SupportProgramService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/support-programs")
public class SupportProgramController {
    private final SupportProgramService programService;

    public SupportProgramController(SupportProgramService programService) { this.programService = programService; }

    @Operation(summary = "지원제도 목록")
    @GetMapping
    public ApiResponse<List<SupportProgramResponse.Summary>> getPrograms(
            @RequestParam(required = false) Long regionId,
            @RequestParam(required = false) Long jobId,
            @RequestParam(required = false) String keyword) {
        return ApiResponse.success(programService.getPrograms(regionId, jobId, keyword));
    }

    @Operation(summary = "지원제도 상세")
    @GetMapping("/{programId}")
    public ApiResponse<SupportProgramResponse.Detail> getProgram(@PathVariable Long programId) {
        return ApiResponse.success(programService.getProgram(programId));
    }
}
