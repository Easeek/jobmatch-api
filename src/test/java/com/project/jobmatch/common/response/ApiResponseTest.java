package com.project.jobmatch.common.response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class ApiResponseTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void successResponseMatchesApiSpecification() throws JsonProcessingException {
        JsonNode json = objectMapper.readTree(
                objectMapper.writeValueAsString(ApiResponse.success(Map.of("conditionId", 100)))
        );

        assertThat(json.get("success").asBoolean()).isTrue();
        assertThat(json.get("data").get("conditionId").asInt()).isEqualTo(100);
        assertThat(json.get("error").isNull()).isTrue();
    }

    @Test
    void errorResponseMatchesApiSpecification() throws JsonProcessingException {
        JsonNode json = objectMapper.readTree(
                objectMapper.writeValueAsString(ApiResponse.error("JOB_NOT_FOUND", "해당 직업을 찾을 수 없습니다."))
        );

        assertThat(json.get("success").asBoolean()).isFalse();
        assertThat(json.get("data").isNull()).isTrue();
        assertThat(json.get("error").get("code").asText()).isEqualTo("JOB_NOT_FOUND");
        assertThat(json.get("error").get("message").asText()).isEqualTo("해당 직업을 찾을 수 없습니다.");
    }
}
