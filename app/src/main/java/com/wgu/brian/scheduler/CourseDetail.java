package com.wgu.brian.scheduler;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.wgu.brian.scheduler.database.AppDatabase;
import com.wgu.brian.scheduler.database.entities.Assessment;
import com.wgu.brian.scheduler.database.entities.Course;
import com.wgu.brian.scheduler.database.entities.CourseNote;
import com.wgu.brian.scheduler.database.entities.Mentor;
import com.wgu.brian.scheduler.events.AssessmentsEvent;
import com.wgu.brian.scheduler.events.CourseEvent;
import com.wgu.brian.scheduler.events.CourseNotesEvent;
import com.wgu.brian.scheduler.events.MentorsEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class CourseDetail extends AppCompatActivity {

    List<Assessment> assessments;
    List<Mentor> mentors;
    List<CourseNote> notes;

    RecyclerView assessmentRV;
    RecyclerView mentorRV;
    RecyclerView noteRV;

    private TextView courseName;
    private TextView courseStart;
    private TextView courseEnd;

    private AppDatabase db;
    public static int id;
    private Course selectedCourse = new Course();

    private boolean newCourse = true;
    private boolean formEnabled = true;

    private Menu menu;

    private Executor executor = Executors.newCachedThreadPool();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        try {
            id = getIntent().getExtras().getInt(CourseAdapter.POSITION, -1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assessments = new ArrayList<>();
        mentors = new ArrayList<>();
        notes = new ArrayList<>();

        courseName = findViewById(R.id.courseDetailName);
        courseStart = findViewById(R.id.courseDetailStart);
        courseEnd = findViewById(R.id.courseDetailEnd);

        db = AppDatabase.getInstance(this);

        if (id != -1) {
            newCourse = false;
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    Course course = db.courseDao().findById(TermDetail.id);
                    EventBus.getDefault().post(new CourseEvent(course));
                    List<Assessment> tempAssessments = db.assessmentDao().findAllByCourseId(course.getId());
                    List<Mentor> tempMentors = db.mentorDao().findAllByCourseId(course.getId());
                    List<CourseNote> tempNotes = db.courseNoteDao().findAllByCourseId(course.getId());
                    EventBus.getDefault().post(new AssessmentsEvent(tempAssessments));
                    EventBus.getDefault().post(new MentorsEvent(tempMentors));
                    EventBus.getDefault().post(new CourseNotesEvent(tempNotes));
                }
            });
        }
        else {
            bindAssessmentRecycler();
            bindMentorRecycler();
            bindNoteRecycler();
        }

        EventBus.getDefault().register(this);
    }

    private void bindAssessmentRecycler() {
        assessmentRV = findViewById(R.id.assessment_recycler_view);
        assessmentRV.setLayoutManager(new LinearLayoutManager(this));
        RecyclerView.Adapter adapter = new AssessmentAdapter(assessments);
        assessmentRV.setAdapter(adapter);
    }

    private void bindNoteRecycler() {

    }

    private void bindMentorRecycler() {

    }



}
