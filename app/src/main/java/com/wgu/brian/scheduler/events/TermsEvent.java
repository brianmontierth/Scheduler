package com.wgu.brian.scheduler.events;

import com.wgu.brian.scheduler.database.entities.Term;

import java.util.List;

public class TermsEvent {

    private List<Term> termList;

    public TermsEvent(List<Term> termList) {
        this.termList = termList;
    }

    public List<Term> getTermList() {
        return termList;
    }

    public void setTermList(List<Term> termList) {
        this.termList = termList;
    }
}

