package com.wgu.brian.scheduler.events;

import com.wgu.brian.scheduler.database.entities.Course;

public class CourseEvent {
    private Course course;

    public CourseEvent(Course course) {
        this.course = course;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
}
