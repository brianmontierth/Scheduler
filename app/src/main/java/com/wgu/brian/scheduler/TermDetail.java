package com.wgu.brian.scheduler;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.wgu.brian.scheduler.database.AppDatabase;
import com.wgu.brian.scheduler.database.entities.Course;
import com.wgu.brian.scheduler.database.entities.Term;
import com.wgu.brian.scheduler.events.CoursesEvent;
import com.wgu.brian.scheduler.events.TermEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class TermDetail extends AppCompatActivity {

    private static final String TAG = "TERM_DETAIL_ACTIVITY_TAG";
    private List<Course> courses;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private FloatingActionButton fab;
    private AppDatabase db;
    private Term selectedTerm = new Term();
    public static int position;

    private TextView termName;
    private TextView termStart;
    private TextView termEnd;

    private Executor executor = Executors.newCachedThreadPool();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_detail);

        try {
            position = getIntent().getExtras().getInt(TermAdapter.POSITION, -1);

        } catch (Exception e) {
            System.out.println("Get Position failed!");
            e.printStackTrace();
        }

        termName = findViewById(R.id.termDetailName);
        termStart = findViewById(R.id.termDetailStart);
        termEnd = findViewById(R.id.termDetailEnd);

        db = AppDatabase.getInstance(this);


        executor.execute(new Runnable() {
            @Override
            public void run() {
                if (position != -1) {
                    Term term = db.termDao().findById(TermDetail.position + 1);
                    EventBus.getDefault().post(new TermEvent(term));
                    courses = db.courseDao().findAllByTermId(term.getId());
                    EventBus.getDefault().post(new CoursesEvent(courses));
                }
            }
        });

        fab = findViewById(R.id.term_detail_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TermDetail.this, CourseDetail.class));
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void CoursesEventHandler(CoursesEvent event) {
        Log.d(TAG, "CoursesEventHandler: Event triggered!");

        courses = event.getCourses();
        recyclerView = findViewById(R.id.course_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CourseAdapter(courses);
        recyclerView.setAdapter(adapter);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void TermEventHandler(TermEvent event){
        Log.d(TAG, "TermEventHandler: Caught event");
        bindActivity(event);

        Toast.makeText(getApplicationContext(),"Need to populate term detail page. " + position, Toast.LENGTH_LONG).show();
        // TODO: 5/21/2018 populate term detail page.
    }

    private void bindActivity(TermEvent event) {
        selectedTerm = event.getTerm();
        termName.setText(selectedTerm.getName());
        termStart.setText(selectedTerm.getStart_date());
        termEnd.setText(selectedTerm.getEnd_date());
    }

    @Override
    public void onBackPressed() {

        // TODO: 5/21/2018 Term Detail Validation

        executor.execute(new Runnable() {
            @Override
            public void run() {

                selectedTerm.setName(termName.getText().toString());
                selectedTerm.setStart_date(termStart.getText().toString());
                selectedTerm.setEnd_date(termEnd.getText().toString());

                db.termDao().insert(selectedTerm);
            }
        });

        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        AppDatabase.destroyInstance();
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
