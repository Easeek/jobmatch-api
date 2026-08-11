package com.project.jobmatch.domain.user.dto;

import com.project.jobmatch.domain.user.entity.CareerLevel;
import com.project.jobmatch.domain.user.entity.EducationLevel;
import com.project.jobmatch.domain.user.entity.WorkType;

import java.util.List;

public record ConditionResponse(Long conditionId, String sessionKey, CareerLevel careerLevel,
        RegionSummary region, WorkType workType, EducationLevel educationLevel,
        boolean trainingDesired, List<InterestFieldSummary> interestFields) {
    public record RegionSummary(Long regionId, String regionName) {}
    public record InterestFieldSummary(Long fieldId, String fieldName) {}
}
