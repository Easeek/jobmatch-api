package com.project.jobmatch.domain.user.service;

import com.project.jobmatch.common.exception.CustomException;
import com.project.jobmatch.domain.job.entity.InterestField;
import com.project.jobmatch.domain.job.entity.Region;
import com.project.jobmatch.domain.job.repository.InterestFieldRepository;
import com.project.jobmatch.domain.job.repository.RegionRepository;
import com.project.jobmatch.domain.user.dto.*;
import com.project.jobmatch.domain.user.entity.*;
import com.project.jobmatch.domain.user.repository.UserConditionInterestFieldRepository;
import com.project.jobmatch.domain.user.repository.UserConditionRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional(readOnly = true)
public class ConditionService {
    private final UserConditionRepository conditionRepository;
    private final UserConditionInterestFieldRepository linkRepository;
    private final RegionRepository regionRepository;
    private final InterestFieldRepository fieldRepository;

    public ConditionService(UserConditionRepository conditionRepository,
                            UserConditionInterestFieldRepository linkRepository,
                            RegionRepository regionRepository,
                            InterestFieldRepository fieldRepository) {
        this.conditionRepository = conditionRepository;
        this.linkRepository = linkRepository;
        this.regionRepository = regionRepository;
        this.fieldRepository = fieldRepository;
    }

    @Transactional
    public ConditionCreateResponse create(ConditionCreateRequest request) {
        Region region = request.regionId() == null ? null : regionRepository.findById(request.regionId())
                .orElseThrow(() -> notFound("REGION_NOT_FOUND", "해당 지역을 찾을 수 없습니다."));
        Set<Long> requestedIds = new LinkedHashSet<>(request.interestFieldIds());
        List<InterestField> fields = fieldRepository.findAllById(requestedIds);
        if (fields.size() != requestedIds.size()) {
            throw notFound("INTEREST_FIELD_NOT_FOUND", "해당 관심분야를 찾을 수 없습니다.");
        }

        UserCondition condition = conditionRepository.save(new UserCondition(request.sessionKey(),
                request.careerLevel(), region, request.workType(), request.educationLevel(),
                request.trainingDesired()));
        linkRepository.saveAll(fields.stream()
                .map(field -> new UserConditionInterestField(condition, field))
                .toList());
        return new ConditionCreateResponse(condition.getConditionId(), condition.getCreatedAt());
    }

    public ConditionResponse get(Long conditionId) {
        UserCondition condition = conditionRepository.findByConditionId(conditionId)
                .orElseThrow(() -> notFound("CONDITION_NOT_FOUND", "해당 사용자 조건을 찾을 수 없습니다."));
        List<ConditionResponse.InterestFieldSummary> fields = linkRepository
                .findAllByConditionConditionIdOrderByInterestFieldFieldId(conditionId).stream()
                .map(link -> new ConditionResponse.InterestFieldSummary(
                        link.getInterestField().getFieldId(), link.getInterestField().getFieldName()))
                .toList();
        ConditionResponse.RegionSummary region = condition.getRegion() == null ? null
                : new ConditionResponse.RegionSummary(condition.getRegion().getRegionId(),
                        condition.getRegion().getRegionName());
        return new ConditionResponse(condition.getConditionId(), condition.getSessionKey(),
                condition.getCareerLevel(), region, condition.getWorkType(), condition.getEducationLevel(),
                condition.isTrainingDesired(), fields);
    }

    public ConditionMetaResponse getMeta() {
        var regions = regionRepository.findAllByOrderByRegionIdAsc().stream()
                .map(region -> new ConditionMetaResponse.RegionOption(region.getRegionId(),
                        region.getRegionCode(), region.getRegionName())).toList();
        var fields = fieldRepository.findAllByOrderByFieldIdAsc().stream()
                .map(field -> new ConditionMetaResponse.InterestFieldOption(field.getFieldId(),
                        field.getFieldCode(), field.getFieldName())).toList();
        return new ConditionMetaResponse(regions, fields, Arrays.asList(CareerLevel.values()),
                Arrays.asList(WorkType.values()), List.of(EducationLevel.HIGH_SCHOOL,
                EducationLevel.ASSOCIATE, EducationLevel.BACHELOR, EducationLevel.MASTER,
                EducationLevel.DOCTORATE));
    }

    private CustomException notFound(String code, String message) {
        return new CustomException(HttpStatus.NOT_FOUND, code, message);
    }
}
