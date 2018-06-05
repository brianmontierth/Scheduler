package com.wgu.brian.scheduler;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class TermDetail extends AppCompatActivity {

    private static final String TAG = "TermDetail";
    private List<Course> courses;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private FloatingActionButton fab;
    private AppDatabase db;
    private Term selectedTerm = new Term();
    public static int id;

    private TextView termName;
    private TextView termStart;
    private TextView termEnd;

    private boolean newTerm = true;
    private boolean formEnabled = true;

    private Menu menu;

    private Executor executor = Executors.newCachedThreadPool();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_detail);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        try {
            id = getIntent().getExtras().getInt(TermAdapter.POSITION, -1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        courses = new ArrayList<>();

        termName = findViewById(R.id.termDetailName);
        termStart = findViewById(R.id.termDetailStart);
        termEnd = findViewById(R.id.termDetailEnd);

        db = AppDatabase.getInstance(this);

        if (id != -1) {
            newTerm = false;
            executor.execute(new Runnable() {
                @Override
                public void run() {
                        Term term = db.termDao().findById(TermDetail.id);
                        EventBus.getDefault().post(new TermEvent(term));
                        List<Course> tempCourses = db.courseDao().findAllByTermId(term.getId());
                        EventBus.getDefault().post(new CoursesEvent(tempCourses));
                }
            });
        }
        else {
            bindCourseRecycler();
        }

        fab = findViewById(R.id.term_detail_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id = selectedTerm.getId();
                Intent intent = new Intent(TermDetail.this, CourseDetail.class);
                intent.putExtra(CourseAdapter.POSITION, -1);
                startActivity(intent);
            }
        });
        EventBus.getDefault().register(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the term_menu; this adds items to the action bar if it is present.
        this.menu = menu;
        getMenuInflater().inflate(R.menu.term_menu, menu);
        enableForm(newTerm);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()) {
            case R.id.term_menu_edit:
                enableForm(true);
                break;
            case R.id.term_menu_save:
                save();
                break;
            case R.id.term_menu_delete:
                delete();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void delete() {
        // TODO: 6/1/2018 delete confirmation

        executor.execute(new Runnable() {
            @Override
            public void run() {

                db.termDao().delete(selectedTerm);
            }
        });
        Toast.makeText(getApplicationContext(), selectedTerm.getName() + " deleted.",Toast.LENGTH_LONG).show();
        setResult(RESULT_OK);
        finish();
    }

    private void enableForm(boolean enabled) {
        formEnabled = enabled;
        menu.getItem(0).setVisible(!enabled);
        menu.getItem(1).setVisible(enabled);
        termName.setEnabled(enabled);
        termStart.setEnabled(enabled);
        termEnd.setEnabled(enabled);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void CoursesEventHandler(CoursesEvent event) {
        Log.d(TAG, "CoursesEventHandler: Course Event triggered!");
        courses = event.getCourses();
        bindCourseRecycler();
    }

    private void bindCourseRecycler() {
        recyclerView = findViewById(R.id.course_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CourseAdapter(courses);
        recyclerView.setAdapter(adapter);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void TermEventHandler(TermEvent event){
        Log.d(TAG, "TermEventHandler: Caught Term event");
        bindActivity(event);
    }

    private void bindActivity(TermEvent event) {
        selectedTerm = event.getTerm();
        termName.setText(selectedTerm.getName());
        termStart.setText(selectedTerm.getStart_date());
        termEnd.setText(selectedTerm.getEnd_date());
    }

    @Override
    public void onBackPressed() {

        if (formEnabled) {
            save();
            return;
        }
        super.onBackPressed();
    }

    private void save() {

        // TODO: 5/21/2018 Term Detail Validation

        selectedTerm.setName(termName.getText().toString());
        selectedTerm.setStart_date(termStart.getText().toString());
        selectedTerm.setEnd_date(termEnd.getText().toString());
        executor.execute(new Runnable() {
            @Override
            public void run() {

                db.termDao().insert(selectedTerm);
            }
        });
        Toast.makeText(getApplicationContext(), selectedTerm.getName() + " saved.",Toast.LENGTH_LONG).show();
        enableForm(false);
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
