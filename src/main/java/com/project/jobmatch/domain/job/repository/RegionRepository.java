package com.project.jobmatch.domain.job.repository;

import com.project.jobmatch.domain.job.entity.Region;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegionRepository extends JpaRepository<Region, Long> {
}
