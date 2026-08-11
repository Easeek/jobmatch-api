package com.project.jobmatch.domain.job.repository;

import com.project.jobmatch.domain.job.entity.InterestField;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InterestFieldRepository extends JpaRepository<InterestField, Long> {
    List<InterestField> findAllByOrderByFieldIdAsc();
}
