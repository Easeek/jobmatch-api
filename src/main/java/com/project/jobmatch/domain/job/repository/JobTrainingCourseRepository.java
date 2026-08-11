package com.project.jobmatch.domain.job.repository;

import com.project.jobmatch.domain.job.entity.JobTrainingCourse;
import com.project.jobmatch.domain.job.entity.JobTrainingCourseId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobTrainingCourseRepository extends JpaRepository<JobTrainingCourse, JobTrainingCourseId> {
}
