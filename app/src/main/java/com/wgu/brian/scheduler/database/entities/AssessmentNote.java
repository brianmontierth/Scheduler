package com.wgu.brian.scheduler.database.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class AssessmentNote {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "assessment_id", index = true)
    @ForeignKey(entity = Assessment.class, parentColumns = "id", childColumns = "assessment_id")
    private int assessment_id;

    @ColumnInfo(name = "note")
    private String note;

    public AssessmentNote(int assessment_id, String note) {
        this.assessment_id = assessment_id;
        this.note = note;
    }

    public AssessmentNote() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAssessment_id() {
        return assessment_id;
    }

    public void setAssessment_id(int assessment_id) {
        this.assessment_id = assessment_id;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
