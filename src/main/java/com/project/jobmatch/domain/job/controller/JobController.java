package com.project.jobmatch.domain.job.controller;

import com.project.jobmatch.common.response.ApiResponse;
import com.project.jobmatch.domain.job.dto.JobResponse;
import com.project.jobmatch.domain.job.service.JobService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/jobs")
public class JobController {
    private final JobService jobService;

    public JobController(JobService jobService) { this.jobService = jobService; }

    @Operation(summary = "직업 목록")
    @GetMapping
    public ApiResponse<List<JobResponse.Summary>> getJobs(
            @RequestParam(required = false) Long fieldId,
            @RequestParam(required = false) String keyword) {
        return ApiResponse.success(jobService.getJobs(fieldId, keyword));
    }

    @Operation(summary = "직업 상세")
    @GetMapping("/{jobId}")
    public ApiResponse<JobResponse.Detail> getJob(@PathVariable Long jobId) {
        return ApiResponse.success(jobService.getJob(jobId));
    }
}
