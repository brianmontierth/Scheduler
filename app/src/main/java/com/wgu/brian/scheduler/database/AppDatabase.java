package com.wgu.brian.scheduler.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.wgu.brian.scheduler.database.entities.Assessment;
import com.wgu.brian.scheduler.database.entities.AssessmentNote;
import com.wgu.brian.scheduler.database.entities.Course;
import com.wgu.brian.scheduler.database.entities.CourseNote;
import com.wgu.brian.scheduler.database.entities.Mentor;
import com.wgu.brian.scheduler.database.entities.Term;

@Database(entities = {Term.class, Course.class, CourseNote.class, Mentor.class, Assessment.class, AssessmentNote.class}, version = 3)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;
    public static AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "app-database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

    public abstract TermDao termDao();

    public abstract CourseDao courseDao();

    public abstract CourseNoteDao courseNoteDao();

    public abstract MentorDao mentorDao();

    public abstract AssessmentDao assessmentDao();

    public abstract AssessmentNoteDao assessmentNoteDao();

    public static void destroyInstance() {
        instance = null;
    }
}
