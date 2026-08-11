package com.project.jobmatch.common.exception;

import com.project.jobmatch.common.response.ApiResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void customExceptionUsesSpecifiedHttpStatusAndErrorCode() {
        CustomException exception = new CustomException(
                HttpStatus.NOT_FOUND,
                "JOB_NOT_FOUND",
                "해당 직업을 찾을 수 없습니다."
        );

        ResponseEntity<ApiResponse<Void>> response = handler.handleCustomException(exception);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().success()).isFalse();
        assertThat(response.getBody().data()).isNull();
        assertThat(response.getBody().error().code()).isEqualTo("JOB_NOT_FOUND");
        assertThat(response.getBody().error().message()).isEqualTo("해당 직업을 찾을 수 없습니다.");
    }
}
