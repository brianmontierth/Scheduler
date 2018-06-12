package com.wgu.brian.scheduler;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

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
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class CourseDetail extends AppCompatActivity {

    private static final String TAG = "CourseDetail";
    List<Assessment> assessments;
    List<Mentor> mentors;
    List<CourseNote> notes;

    RecyclerView assessmentRV;
    RecyclerView mentorRV;
    RecyclerView noteRV;

    private TextView courseName;
    private TextView courseStart;
    private TextView courseEnd;
    private TextView courseStatus;

    private AppDatabase db;
    public static int id;
    private Course selectedCourse = new Course();

    private boolean newCourse = true;
    private boolean formEnabled = true;

    private Menu menu;

    private Executor executor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

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
        courseStatus = findViewById(R.id.courseDetailStatus);

        db = AppDatabase.getInstance(this);

        EventBus.getDefault().register(this);

        if (id != -1) {
            newCourse = false;
            executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    Course course = db.courseDao().findById(CourseDetail.id);
                    List<Assessment> tempAssessments = db.assessmentDao().findAllByCourseId(course.getId());
                    List<Mentor> tempMentors = db.mentorDao().findAllByCourseId(course.getId());
                    List<CourseNote> tempNotes = db.courseNoteDao().findAllByCourseId(course.getId());
                    EventBus.getDefault().post(new AssessmentsEvent(tempAssessments));
                    EventBus.getDefault().post(new MentorsEvent(tempMentors));
                    EventBus.getDefault().post(new CourseNotesEvent(tempNotes));

                    EventBus.getDefault().post(new CourseEvent(course));
                }
            });
        }
        else {
            bindAssessmentRecycler();
            bindMentorRecycler();
            bindNoteRecycler();
        }

        Thread.yield();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the course_menu; this adds items to the action bar if it is present.
        this.menu = menu;
        getMenuInflater().inflate(R.menu.course_menu, menu);
        enableForm(newCourse);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.course_menu_edit:
                enableForm(true);
                break;
            case R.id.course_menu_save:
                save();
                break;
            case R.id.course_menu_delete:
                delete();
                break;
            case R.id.course_menu_add_assessment:
                Intent intentAssessment = new Intent(CourseDetail.this, AssessmentDetail.class);
                intentAssessment.putExtra(AssessmentAdapter.POSITION, -1);
                intentAssessment.putExtra(AssessmentAdapter.PARENT_ID, selectedCourse.getId());
                startActivity(intentAssessment);
                break;
            case R.id.course_menu_add_mentor:
                Intent intentMentor = new Intent(CourseDetail.this, MentorDetail.class);
                intentMentor.putExtra(MentorAdapter.POSITION, -1);
                intentMentor.putExtra(MentorAdapter.PARENT_ID, selectedCourse.getId());
                startActivity(intentMentor);
                break;
            case R.id.course_menu_add_note:
                Intent intentNote = new Intent(CourseDetail.this, CourseNoteDetail.class);
                intentNote.putExtra(CourseNoteAdapter.POSITION, -1);
                intentNote.putExtra(CourseNoteAdapter.PARENT_ID, selectedCourse.getId());
                startActivity(intentNote);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void delete() {
        executor.execute(new Runnable() {
            @Override
            public void run() {

                for (Assessment assessment : assessments) {
                    db.assessmentDao().delete(assessment);
                }

                db.courseDao().delete(selectedCourse);
            }
        });
        Toast.makeText(getApplicationContext(), selectedCourse.getName() + " deleted.", Toast.LENGTH_LONG).show();
        setResult(RESULT_OK, new Intent(CourseDetail.this, TermDetail.class));
        finish();
    }

    @Override
    public void onBackPressed() {

        if (selectedCourse.getName() == null && selectedCourse.getStart_date() == null) {
            super.onBackPressed();
            return;
        }

        if (formEnabled) {
            save();
            return;
        }
        super.onBackPressed();
    }

    private void save() {

        if (!isValidDate(courseStart.getText().toString()) || !isValidDate(courseEnd.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Dates must be in format: MM/dd/yyyy", Toast.LENGTH_LONG).show();
            return;
        }

        selectedCourse.setName(courseName.getText().toString());
        selectedCourse.setStart_date(courseStart.getText().toString());
        selectedCourse.setEnd_date(courseEnd.getText().toString());
        selectedCourse.setStatus(courseStatus.getText().toString());

        selectedCourse.setTerm_id(getIntent().getExtras().getInt(CourseAdapter.PARENT_ID, selectedCourse.getTerm_id()));
        executor.execute(new Runnable() {
            @Override
            public void run() {

                db.courseDao().insert(selectedCourse);
            }
        });
        Toast.makeText(getApplicationContext(), selectedCourse.getName() + " saved.", Toast.LENGTH_LONG).show();
        enableForm(false);
    }

    private boolean isValidDate(String date) {
        try {
            LocalDate.parse(date, DateTimeFormatter.ofPattern("MM/dd/yyyy"));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private void enableForm(boolean enabled) {
        formEnabled = enabled;
        menu.getItem(0).setVisible(!enabled);
        menu.getItem(1).setVisible(enabled);
        courseName.setEnabled(enabled);
        courseStart.setEnabled(enabled);
        courseEnd.setEnabled(enabled);
        courseStatus.setEnabled(enabled);
        if (!formEnabled) {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    private void bindActivity(CourseEvent event) {
        courseName.setText(selectedCourse.getName());
        courseStart.setText(selectedCourse.getStart_date());
        courseEnd.setText(selectedCourse.getEnd_date());
        courseStatus.setText(selectedCourse.getStatus());
    }

    private void bindAssessmentRecycler() {
        assessmentRV = findViewById(R.id.assessment_recycler_view);
        assessmentRV.setLayoutManager(new LinearLayoutManager(this));
        RecyclerView.Adapter adapter = new AssessmentAdapter(assessments);
        assessmentRV.setAdapter(adapter);
    }

    private void bindNoteRecycler() {
        noteRV = findViewById(R.id.course_note_recycler_view);
        noteRV.setLayoutManager(new LinearLayoutManager(this));
        RecyclerView.Adapter adapter = new CourseNoteAdapter(notes);
        noteRV.setAdapter(adapter);
    }

    private void bindMentorRecycler() {
        mentorRV =  findViewById(R.id.mentor_recycler_view);
        mentorRV.setLayoutManager(new LinearLayoutManager(this));
        RecyclerView.Adapter adapter = new MentorAdapter(mentors);
        mentorRV.setAdapter(adapter);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void CourseEventHandler(CourseEvent event) {
        Log.d(TAG, "CourseEventHandler: Course Event triggered!");
        selectedCourse = event.getCourse();
        bindActivity(event);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void AssessmentsEventHandler(AssessmentsEvent event) {
        Log.d(TAG, "AssessmentsEventHandler: Assessments Event triggered!");
        assessments = event.getAssessments();
        bindAssessmentRecycler();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void MentorsEventHandler(MentorsEvent event) {
        Log.d(TAG, "MentorsEventHandler: Mentors Event triggered!");
        mentors = event.getMentors();
        bindMentorRecycler();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void CourseNotesEventHandler(CourseNotesEvent event) {
        Log.d(TAG, "CourseNotesEventHandler: Notes Event triggered!");
        notes = event.getCourseNotes();
        bindNoteRecycler();
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    protected void onRestart() {
        recreate();
        super.onRestart();
    }
}
