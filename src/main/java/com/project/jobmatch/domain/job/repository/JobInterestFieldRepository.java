package com.project.jobmatch.domain.job.repository;

import com.project.jobmatch.domain.job.entity.JobInterestField;
import com.project.jobmatch.domain.job.entity.JobInterestFieldId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface JobInterestFieldRepository extends JpaRepository<JobInterestField, JobInterestFieldId> {
    List<JobInterestField> findAllByJobJobIdIn(Collection<Long> jobIds);
}
