package com.project.jobmatch.domain.job.entity;

import com.project.jobmatch.domain.training.entity.TrainingCourse;
import jakarta.persistence.*;

@Entity
@Table(name = "job_training_course")
public class JobTrainingCourse {
    @EmbeddedId
    private JobTrainingCourseId id;
    @MapsId("jobId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id", nullable = false)
    private Job job;
    @MapsId("courseId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private TrainingCourse trainingCourse;

    protected JobTrainingCourse() {}

    public JobTrainingCourse(Job job, TrainingCourse trainingCourse) {
        this.id = new JobTrainingCourseId(job.getJobId(), trainingCourse.getCourseId());
        this.job = job;
        this.trainingCourse = trainingCourse;
    }

    public Job getJob() { return job; }
    public TrainingCourse getTrainingCourse() { return trainingCourse; }
}
