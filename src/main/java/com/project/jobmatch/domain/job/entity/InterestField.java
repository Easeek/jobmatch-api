package com.project.jobmatch.domain.job.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "interest_field")
public class InterestField {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "field_id")
    private Long fieldId;

    @Column(name = "field_code", length = 30, nullable = false, unique = true)
    private String fieldCode;

    @Column(name = "field_name", length = 50, nullable = false)
    private String fieldName;

    protected InterestField() {
    }

    public InterestField(String fieldCode, String fieldName) {
        this.fieldCode = fieldCode;
        this.fieldName = fieldName;
    }

    public Long getFieldId() {
        return fieldId;
    }

    public String getFieldCode() {
        return fieldCode;
    }

    public String getFieldName() {
        return fieldName;
    }
}
