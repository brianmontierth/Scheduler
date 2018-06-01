package com.wgu.brian.scheduler.events;

import com.wgu.brian.scheduler.database.entities.AssessmentNote;

import java.util.List;

public class AssessmentNotesEvent {
    private List<AssessmentNote> assessmentNotes;

    public AssessmentNotesEvent(List<AssessmentNote> assessmentNotes) {
        this.assessmentNotes = assessmentNotes;
    }

    public List<AssessmentNote> getAssessmentNotes() {
        return assessmentNotes;
    }

    public void setAssessmentNotes(List<AssessmentNote> assessmentNotes) {
        this.assessmentNotes = assessmentNotes;
    }
}
