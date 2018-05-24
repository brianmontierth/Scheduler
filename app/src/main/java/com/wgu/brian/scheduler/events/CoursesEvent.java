package com.wgu.brian.scheduler.events;

import com.wgu.brian.scheduler.database.entities.Course;

import java.util.List;

public class CoursesEvent {

    private List<Course> courseList;

    public CoursesEvent(List<Course> courses) {
        this.courseList = courses;
    }

    public List<Course> getCourseList() {
        return courseList;
    }

    public void setCourseList(List<Course> courseList) {
        this.courseList = courseList;
    }
}
