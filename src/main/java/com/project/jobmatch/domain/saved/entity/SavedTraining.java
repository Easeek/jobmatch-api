package com.project.jobmatch.domain.saved.entity;

import com.project.jobmatch.domain.training.entity.TrainingCourse;
import com.project.jobmatch.domain.user.entity.User;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "saved_training", uniqueConstraints =
        @UniqueConstraint(name = "uk_saved_training_session_course", columnNames = {"session_key", "course_id"}))
@EntityListeners(AuditingEntityListener.class)
public class SavedTraining {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "saved_training_id")
    private Long savedTrainingId;
    @Column(name = "session_key", length = 64, nullable = false)
    private String sessionKey;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "course_id", nullable = false)
    private TrainingCourse course;
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    protected SavedTraining() {}

    public SavedTraining(String sessionKey, TrainingCourse course) {
        this.sessionKey = sessionKey;
        this.course = course;
    }

    public Long getSavedTrainingId() { return savedTrainingId; }
    public String getSessionKey() { return sessionKey; }
    public TrainingCourse getCourse() { return course; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
