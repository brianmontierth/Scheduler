package com.wgu.brian.scheduler.database.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class CourseNote {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "course_id", index = true)
    @ForeignKey(entity = Course.class, parentColumns = "id", childColumns = "course_id")
    private int course_id;

    @ColumnInfo(name = "note")
    private String note;

    public CourseNote(int course_id, String note) {
        this.course_id = course_id;
        this.note = note;
    }

    public CourseNote() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCourse_id() {
        return course_id;
    }

    public void setCourse_id(int course_id) {
        this.course_id = course_id;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
