package com.project.jobmatch.domain.saved.repository;

import com.project.jobmatch.domain.saved.entity.SavedJob;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SavedJobRepository extends JpaRepository<SavedJob, Long> {
    boolean existsBySessionKeyAndJobJobId(String sessionKey, Long jobId);

    @EntityGraph(attributePaths = "job")
    List<SavedJob> findAllBySessionKeyOrderBySavedJobIdAsc(String sessionKey);
}
