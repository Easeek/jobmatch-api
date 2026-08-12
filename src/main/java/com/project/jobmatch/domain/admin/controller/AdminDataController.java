package com.project.jobmatch.domain.admin.controller;

import com.project.jobmatch.common.response.ApiResponse;
import com.project.jobmatch.domain.admin.dto.AdminDataRequest.*;
import com.project.jobmatch.domain.admin.dto.AdminDataResponse.Created;
import com.project.jobmatch.domain.admin.service.AdminDataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin")
@SecurityRequirement(name = "bearerAuth")
public class AdminDataController {
    private final AdminDataService service;

    public AdminDataController(AdminDataService service) { this.service = service; }

    @Operation(summary = "직업 등록")
    @PostMapping("/jobs")
    public ApiResponse<Created> createJob(@Valid @RequestBody JobRequest request) {
        return ApiResponse.success(service.createJob(request));
    }

    @Operation(summary = "직업 수정")
    @PutMapping("/jobs/{jobId}")
    public ApiResponse<Void> updateJob(@PathVariable Long jobId, @Valid @RequestBody JobRequest request) {
        service.updateJob(jobId, request); return ApiResponse.success(null);
    }

    @Operation(summary = "직업 삭제")
    @DeleteMapping("/jobs/{jobId}")
    public ApiResponse<Void> deleteJob(@PathVariable Long jobId) {
        service.deleteJob(jobId); return ApiResponse.success(null);
    }

    @Operation(summary = "훈련 과정 등록")
    @PostMapping("/trainings")
    public ApiResponse<Created> createTraining(@Valid @RequestBody TrainingRequest request) {
        return ApiResponse.success(service.createTraining(request));
    }

    @Operation(summary = "훈련 과정 수정")
    @PutMapping("/trainings/{courseId}")
    public ApiResponse<Void> updateTraining(@PathVariable Long courseId,
                                            @Valid @RequestBody TrainingRequest request) {
        service.updateTraining(courseId, request); return ApiResponse.success(null);
    }

    @Operation(summary = "훈련 과정 삭제")
    @DeleteMapping("/trainings/{courseId}")
    public ApiResponse<Void> deleteTraining(@PathVariable Long courseId) {
        service.deleteTraining(courseId); return ApiResponse.success(null);
    }

    @Operation(summary = "지원제도 등록")
    @PostMapping("/support-programs")
    public ApiResponse<Created> createSupport(@Valid @RequestBody SupportProgramRequest request) {
        return ApiResponse.success(service.createSupportProgram(request));
    }

    @Operation(summary = "지원제도 수정")
    @PutMapping("/support-programs/{programId}")
    public ApiResponse<Void> updateSupport(@PathVariable Long programId,
                                           @Valid @RequestBody SupportProgramRequest request) {
        service.updateSupportProgram(programId, request); return ApiResponse.success(null);
    }

    @Operation(summary = "지원제도 삭제")
    @DeleteMapping("/support-programs/{programId}")
    public ApiResponse<Void> deleteSupport(@PathVariable Long programId) {
        service.deleteSupportProgram(programId); return ApiResponse.success(null);
    }

    @Operation(summary = "일자리 등록")
    @PostMapping("/postings")
    public ApiResponse<Created> createPosting(@Valid @RequestBody PostingRequest request) {
        return ApiResponse.success(service.createPosting(request));
    }

    @Operation(summary = "일자리 수정")
    @PutMapping("/postings/{postingId}")
    public ApiResponse<Void> updatePosting(@PathVariable Long postingId,
                                           @Valid @RequestBody PostingRequest request) {
        service.updatePosting(postingId, request); return ApiResponse.success(null);
    }

    @Operation(summary = "일자리 삭제")
    @DeleteMapping("/postings/{postingId}")
    public ApiResponse<Void> deletePosting(@PathVariable Long postingId) {
        service.deletePosting(postingId); return ApiResponse.success(null);
    }

    @Operation(summary = "직업과 훈련 과정 연결 등록")
    @PostMapping("/jobs/{jobId}/trainings")
    public ApiResponse<Void> linkTraining(@PathVariable Long jobId,
                                          @Valid @RequestBody TrainingLinkRequest request) {
        service.linkTraining(jobId, request.courseId()); return ApiResponse.success(null);
    }

    @Operation(summary = "직업과 지원제도 연결 등록")
    @PostMapping("/jobs/{jobId}/support-programs")
    public ApiResponse<Void> linkSupport(@PathVariable Long jobId,
                                         @Valid @RequestBody SupportProgramLinkRequest request) {
        service.linkSupportProgram(jobId, request.programId()); return ApiResponse.success(null);
    }
}
