package com.project.jobmatch.domain.job.repository;

import com.project.jobmatch.domain.job.entity.JobWorkType;
import com.project.jobmatch.domain.job.entity.JobWorkTypeId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface JobWorkTypeRepository extends JpaRepository<JobWorkType, JobWorkTypeId> {
    List<JobWorkType> findAllByJobJobIdIn(Collection<Long> jobIds);
    void deleteAllByJobJobId(Long jobId);
}
