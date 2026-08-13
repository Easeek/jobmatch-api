package com.project.jobmatch.domain.admin.controller;

import com.project.jobmatch.common.response.ApiResponse;
import com.project.jobmatch.domain.admin.dto.AdminLoginRequest;
import com.project.jobmatch.domain.admin.dto.AdminLoginResponse;
import com.project.jobmatch.domain.admin.service.AdminAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin")
@Tag(name = "관리자 - 인증", description = "관리자 로그인 및 인증 API")
public class AdminAuthController {
    private final AdminAuthService adminAuthService;

    public AdminAuthController(AdminAuthService adminAuthService) {
        this.adminAuthService = adminAuthService;
    }

    @Operation(summary = "관리자 로그인")
    @PostMapping("/login")
    public ApiResponse<AdminLoginResponse> login(@Valid @RequestBody AdminLoginRequest request) {
        return ApiResponse.success(adminAuthService.login(request));
    }
}
