package com.project.jobmatch.domain.job.repository;

import com.project.jobmatch.domain.job.entity.JobPosting;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface JobPostingRepository extends JpaRepository<JobPosting, Long> {
    @EntityGraph(attributePaths = {"job", "region"})
    List<JobPosting> findAllByJobJobIdIn(Collection<Long> jobIds);
    void deleteAllByJobJobId(Long jobId);
}
