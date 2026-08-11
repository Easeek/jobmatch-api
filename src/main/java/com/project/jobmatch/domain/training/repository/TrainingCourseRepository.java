package com.project.jobmatch.domain.training.repository;

import com.project.jobmatch.domain.training.entity.TrainingCourse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainingCourseRepository extends JpaRepository<TrainingCourse, Long> {
}
