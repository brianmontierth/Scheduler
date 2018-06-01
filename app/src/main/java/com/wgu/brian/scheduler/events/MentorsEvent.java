package com.wgu.brian.scheduler.events;

import com.wgu.brian.scheduler.database.entities.Mentor;

import java.util.List;

public class MentorsEvent {
    private List<Mentor> mentors;

    public MentorsEvent(List<Mentor> mentors) {
        this.mentors = mentors;
    }

    public List<Mentor> getMentors() {
        return mentors;
    }

    public void setMentors(List<Mentor> mentors) {
        this.mentors = mentors;
    }
}
