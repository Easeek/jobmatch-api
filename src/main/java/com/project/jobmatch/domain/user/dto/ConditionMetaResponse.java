package com.project.jobmatch.domain.user.dto;

import com.project.jobmatch.domain.user.entity.CareerLevel;
import com.project.jobmatch.domain.user.entity.EducationLevel;
import com.project.jobmatch.domain.user.entity.WorkType;

import java.util.List;

public record ConditionMetaResponse(List<RegionOption> regions, List<InterestFieldOption> interestFields,
        List<CareerLevel> careerLevels, List<WorkType> workTypes, List<EducationLevel> educationLevels) {
    public record RegionOption(Long regionId, String regionCode, String regionName) {}
    public record InterestFieldOption(Long fieldId, String fieldCode, String fieldName) {}
}
