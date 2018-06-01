package com.wgu.brian.scheduler.events;

import com.wgu.brian.scheduler.database.entities.Course;

import java.util.List;

public class CoursesEvent {

    private List<Course> courses;

    public CoursesEvent(List<Course> courses) {
        this.courses = courses;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }
}
