package com.wgu.brian.scheduler.database.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Term {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "start_date")
    private String start_date;

    @ColumnInfo(name = "end_date")
    private String end_date;

    public Term(String name, String start_date, String end_date) {
        this.name = name;
        this.start_date = start_date;
        this.end_date = end_date;
    }

    public Term() {

    }


    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getStart_date() {
        return start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public void setId(int id) {
        this.id = id;
    }

}
