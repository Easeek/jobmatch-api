package com.project.jobmatch.domain.job.repository;

import com.project.jobmatch.domain.job.entity.JobTrainingCourse;
import com.project.jobmatch.domain.job.entity.JobTrainingCourseId;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobTrainingCourseRepository extends JpaRepository<JobTrainingCourse, JobTrainingCourseId> {
    @EntityGraph(attributePaths = "trainingCourse")
    List<JobTrainingCourse> findAllByJobJobId(Long jobId);
}
