package com.wgu.brian.scheduler.events;

import com.wgu.brian.scheduler.database.entities.Assessment;

public class AssessmentEvent {
    private Assessment assessment;

    public AssessmentEvent(Assessment assessment) {
        this.assessment = assessment;
    }

    public Assessment getAssessment() {
        return assessment;
    }

    public void setAssessment(Assessment assessment) {
        this.assessment = assessment;
    }
}
