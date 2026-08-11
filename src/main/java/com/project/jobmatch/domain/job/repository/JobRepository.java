package com.project.jobmatch.domain.job.repository;

import com.project.jobmatch.domain.job.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobRepository extends JpaRepository<Job, Long> {
}
