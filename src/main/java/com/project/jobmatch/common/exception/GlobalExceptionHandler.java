package com.project.jobmatch.common.exception;

import com.project.jobmatch.common.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ApiResponse<Void>> handleCustomException(CustomException exception) {
        ApiResponse<Void> response = ApiResponse.error(exception.getCode(), exception.getMessage());
        return ResponseEntity.status(exception.getStatus()).body(response);
    }
}
