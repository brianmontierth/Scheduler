package com.wgu.brian.scheduler.events;

import com.wgu.brian.scheduler.database.entities.Term;

public class TermEvent {
    private Term term;

    public TermEvent(Term term) {
        this.term = term;
    }

    public Term getTerm() {
        return term;
    }

    public void setTerm(Term term) {
        this.term = term;
    }
}
