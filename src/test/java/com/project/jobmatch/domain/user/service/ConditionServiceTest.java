package com.project.jobmatch.domain.user.service;

import com.project.jobmatch.common.exception.CustomException;
import com.project.jobmatch.domain.job.repository.InterestFieldRepository;
import com.project.jobmatch.domain.job.repository.RegionRepository;
import com.project.jobmatch.domain.user.dto.ConditionMetaResponse;
import com.project.jobmatch.domain.user.entity.CareerLevel;
import com.project.jobmatch.domain.user.entity.EducationLevel;
import com.project.jobmatch.domain.user.entity.WorkType;
import com.project.jobmatch.domain.user.repository.UserConditionInterestFieldRepository;
import com.project.jobmatch.domain.user.repository.UserConditionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ConditionServiceTest {
    @Mock UserConditionRepository conditionRepository;
    @Mock UserConditionInterestFieldRepository linkRepository;
    @Mock RegionRepository regionRepository;
    @Mock InterestFieldRepository fieldRepository;

    private ConditionService service;

    @BeforeEach
    void setUp() {
        service = new ConditionService(conditionRepository, linkRepository, regionRepository, fieldRepository);
    }

    @Test
    void getMetaReturnsEnumsDefinedByApiSpecification() {
        when(regionRepository.findAllByOrderByRegionIdAsc()).thenReturn(List.of());
        when(fieldRepository.findAllByOrderByFieldIdAsc()).thenReturn(List.of());

        ConditionMetaResponse response = service.getMeta();

        assertThat(response.careerLevels()).containsExactly(CareerLevel.values());
        assertThat(response.workTypes()).containsExactly(WorkType.values());
        assertThat(response.educationLevels()).containsExactly(
                EducationLevel.HIGH_SCHOOL, EducationLevel.ASSOCIATE, EducationLevel.BACHELOR,
                EducationLevel.MASTER, EducationLevel.DOCTORATE);
    }

    @Test
    void getThrowsNotFoundWhenConditionDoesNotExist() {
        when(conditionRepository.findByConditionId(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.get(999L))
                .isInstanceOfSatisfying(CustomException.class, exception -> {
                    assertThat(exception.getStatus()).isEqualTo(HttpStatus.NOT_FOUND);
                    assertThat(exception.getCode()).isEqualTo("CONDITION_NOT_FOUND");
                });
    }
}
