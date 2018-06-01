package com.wgu.brian.scheduler.events;

import com.wgu.brian.scheduler.database.entities.CourseNote;

public class CourseNoteEvent {
    private CourseNote courseNote;

    public CourseNoteEvent(CourseNote courseNote) {
        this.courseNote = courseNote;
    }

    public CourseNote getCourseNote() {
        return courseNote;
    }

    public void setCourseNote(CourseNote courseNote) {
        this.courseNote = courseNote;
    }
}
