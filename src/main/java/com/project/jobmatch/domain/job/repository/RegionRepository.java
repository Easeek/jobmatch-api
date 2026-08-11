package com.project.jobmatch.domain.job.repository;

import com.project.jobmatch.domain.job.entity.Region;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RegionRepository extends JpaRepository<Region, Long> {
    List<Region> findAllByOrderByRegionIdAsc();
}
