package com.wgu.brian.scheduler.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBOpenHelper extends SQLiteOpenHelper {


    private static final String database_name = "schedule.db";
    private static final int database_version = 1;

    public static final String TABLE_TERM = "term";
    public static final String TERM_ID = "_ID";
    public static final String TERM_NAME = "name";
    public static final String TERM_START = "start_date";
    public static final String TERM_END = "end_date";
    public static final String[] TERM_ALL_COLUMNS =
            {TERM_ID,TERM_NAME,TERM_START,TERM_END};

    public static final String TABLE_COURSE = "course";
    public static final String COURSE_ID = "_ID";
    public static final String COURSE_TERM_ID = "term_id";
    public static final String COURSE_NAME = "name";
    public static final String COURSE_START = "start_date";
    public static final String COURSE_END = "end_date";
    public static final String COURSE_STATUS = "status";
    public static final String[] COURSE_ALL_COLUMNS =
            {COURSE_ID,COURSE_TERM_ID,COURSE_NAME,COURSE_START,COURSE_END,COURSE_STATUS};

    public static final String TABLE_MENTOR = "mentor";
    public static final String MENTOR_ID = "_ID";
    public static final String MENTOR_COURSE_ID = "course_id";
    public static final String MENTOR_NAME = "name";
    public static final String MENTOR_PHONE = "phone";
    public static final String MENTOR_EMAIL = "email";
    public static final String[] MENTOR_ALL_COLUMNS =
            {MENTOR_ID,MENTOR_COURSE_ID,MENTOR_NAME,MENTOR_PHONE,MENTOR_EMAIL};

    public static final String TABLE_ASSESSMENT = "assessment";
    public static final String ASSESSMENT_ID = "_ID";
    public static final String ASSESSMENT_COURSE_ID = "course_id";
    public static final String ASSESSMENT_TYPE = "type";
    public static final String ASSESSMENT_NAME = "name";
    public static final String ASSESSMENT_DUE = "due_date";
    public static final String[] ASSESSMENT_ALL_COLUMNS =
            {ASSESSMENT_ID,ASSESSMENT_COURSE_ID,ASSESSMENT_TYPE,ASSESSMENT_NAME,ASSESSMENT_DUE};

    public static final String TABLE_COURSE_NOTE = "course_note";
    public static final String COURSE_NOTE_ID = "_ID";
    public static final String COURSE_NOTE_COURSE_ID = "course_id";
    public static final String COURSE_NOTE_NOTE = "note";
    public static final String[] COURSE_NOTE_ALL_COLUMNS =
            {COURSE_NOTE_ID,COURSE_NOTE_COURSE_ID,COURSE_NOTE_NOTE};

    public static final String TABLE_ASSESSMENT_NOTE = "assessment_note";
    public static final String ASSESSMENT_NOTE_ID = "_ID";
    public static final String ASSESSMENT_NOTE_ASSESSMENT_ID = "assessment_id";
    public static final String ASSESSMENT_NOTE_NOTE = "note";
    public static final String[] ASSESSMENT_NOTE_ALL_COLUMNS =
            {ASSESSMENT_NOTE_ID,ASSESSMENT_NOTE_ASSESSMENT_ID,ASSESSMENT_NOTE_NOTE};



    public DBOpenHelper(Context context) {
        super(context, database_name, null, database_version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String term = "CREATE TABLE term (\n" +
                "  _ID integer PRIMARY KEY AUTOINCREMENT,\n" +
                "  name text NOT NULL,\n" +
                "  start_date text NOT NULL,\n" +
                "  end_date text NOT NULL);";

        String course = "CREATE TABLE course (\n" +
                "  _ID integer PRIMARY KEY AUTOINCREMENT,\n" +
                "  term_id integer NOT NULL ,\n" +
                "  name text NOT NULL,\n" +
                "  start_date text NOT NULL,\n" +
                "  end_date text NOT NULL,\n" +
                "  status text NOT NULL,\n" +
                "  FOREIGN KEY(term_id) REFERENCES term(_ID)" +
                ");";

        String mentor = "CREATE TABLE mentor (\n" +
                "  _ID integer PRIMARY KEY AUTOINCREMENT,\n" +
                "  course_id integer NOT NULL,\n" +
                "  name text NOT NULL,\n" +
                "  phone text NOT NULL,\n" +
                "  email text NOT NULL,\n" +
                "  FOREIGN KEY(course_id) REFERENCES course(_ID)" +
                ");";

        String assessment = "CREATE TABLE assessment (\n" +
                "  _ID integer PRIMARY KEY AUTOINCREMENT,\n" +
                "  course_id integer NOT NULL ,\n" +
                "  type text NOT NULL,\n" +
                "  name text NOT NULL,\n" +
                "  due_date text NOT NULL,\n" +
                "  FOREIGN KEY(course_id) REFERENCES course(_ID)" +
                ");";

        String course_note = "CREATE TABLE course_note (\n" +
                "  _ID integer PRIMARY KEY AUTOINCREMENT,\n" +
                "  course_id integer NOT NULL ,\n" +
                "  note text NOT NULL,\n" +
                "  FOREIGN KEY(course_id) REFERENCES course(_ID)" +
                ");";

        String assessment_note = "CREATE TABLE assessment_note (\n" +
                "  _ID integer PRIMARY KEY AUTOINCREMENT,\n" +
                "  assessment_id integer NOT NULL ,\n" +
                "  note text NOT NULL,\n" +
                " FOREIGN KEY(assessment_id) REFERENCES course(_ID)" +
                ");";

        db.execSQL(term);
        db.execSQL(course);
        db.execSQL(mentor);
        db.execSQL(assessment);
        db.execSQL(course_note);
        db.execSQL(assessment_note);
}

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        String assessment_note = "DROP TABLE IF EXISTS assessment_note;";

        String course_note = "DROP TABLE IF EXISTS course_note;";

        String assessment = "DROP TABLE IF EXISTS assessment;";

        String mentor = "DROP TABLE IF EXISTS mentor;";

        String course = "DROP TABLE IF EXISTS course;";

        String term = "DROP TABLE IF EXISTS term;";

        db.execSQL(assessment_note);
        db.execSQL(course_note);
        db.execSQL(assessment);
        db.execSQL(mentor);
        db.execSQL(course);
        db.execSQL(term);
        onCreate(db);
    }

}
