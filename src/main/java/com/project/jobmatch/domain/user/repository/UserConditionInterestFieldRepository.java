package com.project.jobmatch.domain.user.repository;

import com.project.jobmatch.domain.user.entity.UserConditionInterestField;
import com.project.jobmatch.domain.user.entity.UserConditionInterestFieldId;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserConditionInterestFieldRepository
        extends JpaRepository<UserConditionInterestField, UserConditionInterestFieldId> {
    @EntityGraph(attributePaths = "interestField")
    List<UserConditionInterestField> findAllByConditionConditionIdOrderByInterestFieldFieldId(Long conditionId);
}
