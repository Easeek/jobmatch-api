package com.project.jobmatch.domain.training.service;

import com.project.jobmatch.common.exception.CustomException;
import com.project.jobmatch.domain.job.entity.JobTrainingCourse;
import com.project.jobmatch.domain.job.repository.JobTrainingCourseRepository;
import com.project.jobmatch.domain.training.dto.TrainingResponse;
import com.project.jobmatch.domain.training.entity.CostType;
import com.project.jobmatch.domain.training.entity.TrainingCourse;
import com.project.jobmatch.domain.training.repository.TrainingCourseRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class TrainingService {
    private final TrainingCourseRepository trainingRepository;
    private final JobTrainingCourseRepository jobTrainingRepository;

    public TrainingService(TrainingCourseRepository trainingRepository,
                           JobTrainingCourseRepository jobTrainingRepository) {
        this.trainingRepository = trainingRepository;
        this.jobTrainingRepository = jobTrainingRepository;
    }

    public List<TrainingResponse.Summary> getTrainings(Long regionId, CostType costType,
                                                       Long jobId, String keyword) {
        Set<Long> jobCourseIds = jobId == null ? Set.of() : jobTrainingRepository.findAllByJobJobId(jobId).stream()
                .map(JobTrainingCourse::getTrainingCourse).map(TrainingCourse::getCourseId)
                .collect(Collectors.toSet());
        String normalized = normalize(keyword);
        return trainingRepository.findAll().stream()
                .filter(course -> regionId == null || course.getRegion() != null
                        && Objects.equals(course.getRegion().getRegionId(), regionId))
                .filter(course -> costType == null || course.getCostType() == costType)
                .filter(course -> jobId == null || jobCourseIds.contains(course.getCourseId()))
                .filter(course -> normalized == null
                        || course.getCourseName().toLowerCase(Locale.ROOT).contains(normalized)
                        || course.getInstitution() != null
                        && course.getInstitution().toLowerCase(Locale.ROOT).contains(normalized))
                .sorted(Comparator.comparing(TrainingCourse::getCourseId))
                .map(course -> new TrainingResponse.Summary(course.getCourseId(), course.getCourseName(),
                        course.getInstitution(), regionName(course), course.getCostType(),
                        course.getStartDate(), course.getEndDate()))
                .toList();
    }

    public TrainingResponse.Detail getTraining(Long courseId) {
        TrainingCourse course = trainingRepository.findById(courseId)
                .orElseThrow(() -> notFound("TRAINING_NOT_FOUND", "해당 훈련 과정을 찾을 수 없습니다."));
        TrainingResponse.Region region = course.getRegion() == null ? null
                : new TrainingResponse.Region(course.getRegion().getRegionId(), course.getRegion().getRegionName());
        return new TrainingResponse.Detail(course.getCourseId(), course.getCourseName(), course.getInstitution(),
                region, course.getCostType(), course.getStartDate(), course.getEndDate(),
                course.getDescription(), course.getExternalUrl(), course.getSource());
    }

    private String regionName(TrainingCourse course) {
        return course.getRegion() == null ? null : course.getRegion().getRegionName();
    }

    private String normalize(String keyword) {
        return keyword == null || keyword.isBlank() ? null : keyword.trim().toLowerCase(Locale.ROOT);
    }

    private CustomException notFound(String code, String message) {
        return new CustomException(HttpStatus.NOT_FOUND, code, message);
    }
}
