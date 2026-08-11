package com.project.jobmatch.domain.user.dto;

import com.project.jobmatch.domain.user.entity.CareerLevel;
import com.project.jobmatch.domain.user.entity.EducationLevel;
import com.project.jobmatch.domain.user.entity.WorkType;
import jakarta.validation.constraints.*;

import java.util.List;

public record ConditionCreateRequest(
        @NotBlank @Size(max = 64)
        @Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[1-5][0-9a-fA-F]{3}-[89aAbB][0-9a-fA-F]{3}-[0-9a-fA-F]{12}$")
        String sessionKey,
        @NotNull CareerLevel careerLevel,
        Long regionId,
        WorkType workType,
        @NotNull EducationLevel educationLevel,
        boolean trainingDesired,
        @NotNull List<@NotNull Long> interestFieldIds
) {}
