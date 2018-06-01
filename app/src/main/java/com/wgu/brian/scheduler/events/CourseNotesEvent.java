package com.wgu.brian.scheduler.events;

import com.wgu.brian.scheduler.database.entities.CourseNote;

import java.util.List;

public class CourseNotesEvent {
    private List<CourseNote> courseNotes;

    public CourseNotesEvent(List<CourseNote> courseNotes) {
        this.courseNotes = courseNotes;
    }

    public List<CourseNote> getCourseNotes() {
        return courseNotes;
    }

    public void setCourseNotes(List<CourseNote> courseNotes) {
        this.courseNotes = courseNotes;
    }
}
