package com.wgu.brian.scheduler.events;

import com.wgu.brian.scheduler.database.entities.AssessmentNote;

public class AssessmentNoteEvent {
    private AssessmentNote assessmentNote;

    public AssessmentNoteEvent(AssessmentNote assessmentNote) {
        this.assessmentNote = assessmentNote;
    }

    public AssessmentNote getAssessmentNote() {
        return assessmentNote;
    }

    public void setAssessmentNote(AssessmentNote assessmentNote) {
        this.assessmentNote = assessmentNote;
    }
}
