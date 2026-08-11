package com.project.jobmatch.domain.user.entity;

import com.project.jobmatch.domain.job.entity.InterestField;
import jakarta.persistence.*;

@Entity
@Table(name = "user_condition_interest_field")
public class UserConditionInterestField {
    @EmbeddedId
    private UserConditionInterestFieldId id;

    @MapsId("conditionId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "condition_id", nullable = false)
    private UserCondition condition;

    @MapsId("fieldId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "field_id", nullable = false)
    private InterestField interestField;

    protected UserConditionInterestField() {}

    public UserConditionInterestField(UserCondition condition, InterestField interestField) {
        this.id = new UserConditionInterestFieldId(condition.getConditionId(), interestField.getFieldId());
        this.condition = condition;
        this.interestField = interestField;
    }

    public InterestField getInterestField() { return interestField; }
}
