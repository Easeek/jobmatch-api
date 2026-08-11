package com.project.jobmatch.domain.job.repository;

import com.project.jobmatch.domain.job.entity.JobSupportProgram;
import com.project.jobmatch.domain.job.entity.JobSupportProgramId;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobSupportProgramRepository extends JpaRepository<JobSupportProgram, JobSupportProgramId> {
    @EntityGraph(attributePaths = "supportProgram")
    List<JobSupportProgram> findAllByJobJobId(Long jobId);
}
