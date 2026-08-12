package com.project.jobmatch.domain.saved.repository;

import com.project.jobmatch.domain.saved.entity.SavedTraining;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SavedTrainingRepository extends JpaRepository<SavedTraining, Long> {
    boolean existsBySessionKeyAndCourseCourseId(String sessionKey, Long courseId);

    @EntityGraph(attributePaths = "course")
    List<SavedTraining> findAllBySessionKeyOrderBySavedTrainingIdAsc(String sessionKey);
}
