package com.project.jobmatch.domain.saved.service;

import com.project.jobmatch.common.exception.CustomException;
import com.project.jobmatch.domain.job.entity.Job;
import com.project.jobmatch.domain.job.repository.JobRepository;
import com.project.jobmatch.domain.saved.dto.*;
import com.project.jobmatch.domain.saved.entity.SavedJob;
import com.project.jobmatch.domain.saved.entity.SavedTraining;
import com.project.jobmatch.domain.saved.repository.SavedJobRepository;
import com.project.jobmatch.domain.saved.repository.SavedTrainingRepository;
import com.project.jobmatch.domain.training.entity.TrainingCourse;
import com.project.jobmatch.domain.training.repository.TrainingCourseRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class SavedItemService {
    private final SavedJobRepository savedJobRepository;
    private final SavedTrainingRepository savedTrainingRepository;
    private final JobRepository jobRepository;
    private final TrainingCourseRepository trainingRepository;

    public SavedItemService(SavedJobRepository savedJobRepository,
                            SavedTrainingRepository savedTrainingRepository,
                            JobRepository jobRepository, TrainingCourseRepository trainingRepository) {
        this.savedJobRepository = savedJobRepository;
        this.savedTrainingRepository = savedTrainingRepository;
        this.jobRepository = jobRepository;
        this.trainingRepository = trainingRepository;
    }

    @Transactional
    public SavedJobResponse.Created saveJob(SavedJobRequest request) {
        Job job = jobRepository.findById(request.jobId())
                .orElseThrow(() -> notFound("JOB_NOT_FOUND", "해당 직업을 찾을 수 없습니다."));
        if (savedJobRepository.existsBySessionKeyAndJobJobId(request.sessionKey(), request.jobId())) {
            throw alreadySaved();
        }
        try {
            SavedJob saved = savedJobRepository.saveAndFlush(new SavedJob(request.sessionKey(), job, request.memo()));
            return new SavedJobResponse.Created(saved.getSavedJobId(), saved.getCreatedAt());
        } catch (DataIntegrityViolationException exception) {
            throw alreadySaved();
        }
    }

    public List<SavedJobResponse.Summary> getSavedJobs(String sessionKey) {
        return savedJobRepository.findAllBySessionKeyOrderBySavedJobIdAsc(sessionKey).stream()
                .map(saved -> new SavedJobResponse.Summary(saved.getSavedJobId(),
                        new SavedJobResponse.Job(saved.getJob().getJobId(), saved.getJob().getJobName()),
                        saved.getMemo(), saved.getCreatedAt()))
                .toList();
    }

    @Transactional
    public void deleteSavedJob(Long savedJobId) {
        SavedJob saved = savedJobRepository.findById(savedJobId)
                .orElseThrow(() -> notFound("SAVED_JOB_NOT_FOUND", "저장된 직업을 찾을 수 없습니다."));
        savedJobRepository.delete(saved);
    }

    @Transactional
    public SavedTrainingResponse.Created saveTraining(SavedTrainingRequest request) {
        TrainingCourse course = trainingRepository.findById(request.courseId())
                .orElseThrow(() -> notFound("TRAINING_NOT_FOUND", "해당 훈련 과정을 찾을 수 없습니다."));
        if (savedTrainingRepository.existsBySessionKeyAndCourseCourseId(
                request.sessionKey(), request.courseId())) {
            throw alreadySaved();
        }
        try {
            SavedTraining saved = savedTrainingRepository.saveAndFlush(
                    new SavedTraining(request.sessionKey(), course));
            return new SavedTrainingResponse.Created(saved.getSavedTrainingId(), saved.getCreatedAt());
        } catch (DataIntegrityViolationException exception) {
            throw alreadySaved();
        }
    }

    public List<SavedTrainingResponse.Summary> getSavedTrainings(String sessionKey) {
        return savedTrainingRepository.findAllBySessionKeyOrderBySavedTrainingIdAsc(sessionKey).stream()
                .map(saved -> new SavedTrainingResponse.Summary(saved.getSavedTrainingId(),
                        new SavedTrainingResponse.Training(saved.getCourse().getCourseId(),
                                saved.getCourse().getCourseName(), saved.getCourse().getInstitution()),
                        saved.getCreatedAt()))
                .toList();
    }

    @Transactional
    public void deleteSavedTraining(Long savedTrainingId) {
        SavedTraining saved = savedTrainingRepository.findById(savedTrainingId)
                .orElseThrow(() -> notFound("SAVED_TRAINING_NOT_FOUND", "저장된 훈련 과정을 찾을 수 없습니다."));
        savedTrainingRepository.delete(saved);
    }

    private CustomException alreadySaved() {
        return new CustomException(HttpStatus.CONFLICT, "ALREADY_SAVED", "이미 저장된 항목입니다.");
    }

    private CustomException notFound(String code, String message) {
        return new CustomException(HttpStatus.NOT_FOUND, code, message);
    }
}
