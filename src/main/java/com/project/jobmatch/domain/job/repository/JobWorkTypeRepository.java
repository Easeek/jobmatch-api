package com.project.jobmatch.domain.job.repository;

import com.project.jobmatch.domain.job.entity.JobWorkType;
import com.project.jobmatch.domain.job.entity.JobWorkTypeId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobWorkTypeRepository extends JpaRepository<JobWorkType, JobWorkTypeId> {
}
