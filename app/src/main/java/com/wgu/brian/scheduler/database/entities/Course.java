package com.wgu.brian.scheduler.database.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Course {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "term_id", index = true)
    @ForeignKey(entity = Term.class, parentColumns = "id", childColumns = "term_id")
    private int term_id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "start_date")
    private String start_date;

    @ColumnInfo(name = "end_date")
    private String end_date;

    @ColumnInfo(name = "status")
    private String status;

    public Course(int term_id, String name, String start_date, String end_date, String status) {
        this.term_id = term_id;
        this.name = name;
        this.start_date = start_date;
        this.end_date = end_date;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getTerm_id() {
        return term_id;
    }

    public void setTerm_id(int term_id) {
        this.term_id = term_id;
    }
}
