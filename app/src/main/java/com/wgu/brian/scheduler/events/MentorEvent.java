package com.wgu.brian.scheduler.events;

        import com.wgu.brian.scheduler.database.entities.Mentor;

public class MentorEvent {
    private Mentor mentor;

    public MentorEvent(Mentor mentor) {
        this.mentor = mentor;
    }

    public Mentor getMentor() {
        return mentor;
    }

    public void setMentor(Mentor mentor) {
        this.mentor = mentor;
    }
}
