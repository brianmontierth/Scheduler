package com.wgu.brian.scheduler.database.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Assessment {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "course_id", index = true)
    @ForeignKey(entity = Course.class, parentColumns = "id", childColumns = "course_id")
    private int course_id;

    @ColumnInfo(name = "type")
    private String type;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "due_date")
    private String due_date;

    public Assessment(int course_id, String type, String name, String due_date) {
        this.course_id = course_id;
        this.type = type;
        this.name = name;
        this.due_date = due_date;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDue_date() {
        return due_date;
    }

    public void setDue_date(String due_date) {
        this.due_date = due_date;
    }
}
