package com.project.jobmatch.domain.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public record UserConditionInterestFieldId(
        @Column(name = "condition_id") Long conditionId,
        @Column(name = "field_id") Long fieldId
) implements Serializable {
    public UserConditionInterestFieldId() {
        this(null, null);
    }
}
