package com.wgu.brian.scheduler.events;

import com.wgu.brian.scheduler.database.entities.Term;

import java.util.List;

public class TermsEvent {

    private List<Term> terms;

    public TermsEvent(List<Term> terms) {
        this.terms = terms;
    }

    public List<Term> getTerms() {
        return terms;
    }

    public void setTerms(List<Term> terms) {
        this.terms = terms;
    }
}

