package com.project.jobmatch.domain.saved.service;

import com.project.jobmatch.common.exception.CustomException;
import com.project.jobmatch.domain.job.entity.Job;
import com.project.jobmatch.domain.job.repository.JobRepository;
import com.project.jobmatch.domain.saved.dto.SavedJobRequest;
import com.project.jobmatch.domain.saved.repository.SavedJobRepository;
import com.project.jobmatch.domain.saved.repository.SavedTrainingRepository;
import com.project.jobmatch.domain.training.repository.TrainingCourseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SavedItemServiceTest {
    @Mock SavedJobRepository savedJobRepository;
    @Mock SavedTrainingRepository savedTrainingRepository;
    @Mock JobRepository jobRepository;
    @Mock TrainingCourseRepository trainingRepository;

    private SavedItemService service;

    @BeforeEach
    void setUp() {
        service = new SavedItemService(savedJobRepository, savedTrainingRepository,
                jobRepository, trainingRepository);
    }

    @Test
    void duplicateSavedJobThrowsConflictWithAlreadySavedCode() {
        String sessionKey = "550e8400-e29b-41d4-a716-446655440000";
        when(jobRepository.findById(1L)).thenReturn(Optional.of(mock(Job.class)));
        when(savedJobRepository.existsBySessionKeyAndJobJobId(sessionKey, 1L)).thenReturn(true);

        assertThatThrownBy(() -> service.saveJob(new SavedJobRequest(sessionKey, 1L, null)))
                .isInstanceOfSatisfying(CustomException.class, exception -> {
                    assertThat(exception.getStatus()).isEqualTo(HttpStatus.CONFLICT);
                    assertThat(exception.getCode()).isEqualTo("ALREADY_SAVED");
                });
    }
}
