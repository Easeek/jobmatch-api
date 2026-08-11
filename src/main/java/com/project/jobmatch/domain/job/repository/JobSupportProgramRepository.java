package com.project.jobmatch.domain.job.repository;

import com.project.jobmatch.domain.job.entity.JobSupportProgram;
import com.project.jobmatch.domain.job.entity.JobSupportProgramId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobSupportProgramRepository extends JpaRepository<JobSupportProgram, JobSupportProgramId> {
}
