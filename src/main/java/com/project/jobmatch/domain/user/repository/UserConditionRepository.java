package com.project.jobmatch.domain.user.repository;

import com.project.jobmatch.domain.user.entity.UserCondition;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserConditionRepository extends JpaRepository<UserCondition, Long> {
    @EntityGraph(attributePaths = "region")
    Optional<UserCondition> findByConditionId(Long conditionId);
}
