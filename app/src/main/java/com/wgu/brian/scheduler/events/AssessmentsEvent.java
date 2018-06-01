package com.wgu.brian.scheduler.events;

import com.wgu.brian.scheduler.database.entities.Assessment;

import java.util.List;

public class AssessmentsEvent {
    private List<Assessment> assessments;

    public AssessmentsEvent(List<Assessment> assessments) {
        this.assessments = assessments;
    }

    public List<Assessment> getAssessments() {
        return assessments;
    }

    public void setAssessments(List<Assessment> assessments) {
        this.assessments = assessments;
    }
}
