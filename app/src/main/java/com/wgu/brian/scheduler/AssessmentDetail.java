package com.wgu.brian.scheduler;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.wgu.brian.scheduler.database.AppDatabase;
import com.wgu.brian.scheduler.database.entities.Assessment;
import com.wgu.brian.scheduler.database.entities.AssessmentNote;
import com.wgu.brian.scheduler.events.AssessmentEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AssessmentDetail extends AppCompatActivity {

    public static final String TAG = "MentorDetail";

    List<AssessmentNote> notes;
    RecyclerView noteRV;

    private TextView assessmentName;
    private Spinner assessmentType;
    private TextView assessmentDue;

    private AppDatabase db;
    public static int id;
    private Assessment selectedAssessment = new Assessment();

    private boolean newAssessment = true;
    private boolean formEnabled = true;

    private Menu menu;

    private Executor executor = Executors.newCachedThreadPool();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_detail);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        try {
            id = getIntent().getExtras().getInt(AssessmentAdapter.POSITION, -1);
        } catch (Exception e) {
            e.printStackTrace();
        }


        assessmentName = findViewById(R.id.assessmentDetailName);
        assessmentType = findViewById(R.id.assessmentDetailType);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, Assessment.TYPES);
        assessmentType.setAdapter(adapter);
        assessmentType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedAssessment.setType(parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        assessmentDue = findViewById(R.id.assessmentDetailDue);

        db = AppDatabase.getInstance(this);

        if (id != -1) {
            newAssessment = false;
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    Assessment assessment = db.assessmentDao().findById(AssessmentDetail.id);
                    EventBus.getDefault().post(new AssessmentEvent(assessment));
                }
            });
        }
        else {
            bindNoteRecycler();
        }

        EventBus.getDefault().register(this);
    }

    private void bindNoteRecycler() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the assessment_menu; this adds items to the action bar if it is present.
        this.menu = menu;
        getMenuInflater().inflate(R.menu.assessment_menu, menu);
        enableForm(newAssessment);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()) {
            case R.id.assessment_menu_edit:
                enableForm(true);
                break;
            case R.id.assessment_menu_save:
                save();
                break;
            case R.id.assessment_menu_delete:
                delete();
                break;
            case R.id.assessment_menu_add_note:
                // TODO Add assessment note here
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

                db.assessmentDao().delete(selectedAssessment);
            }
        });
        Toast.makeText(getApplicationContext(), selectedAssessment.getName() + " deleted.",Toast.LENGTH_LONG).show();
        setResult(RESULT_OK);
        finish();
    }

    private void enableForm(boolean enabled) {
        formEnabled = enabled;
        menu.getItem(0).setVisible(!enabled);
        menu.getItem(1).setVisible(enabled);
        assessmentName.setEnabled(enabled);
        assessmentType.setEnabled(enabled);
        assessmentDue.setEnabled(enabled);
    }

    private void bindActivity(AssessmentEvent event) {
        selectedAssessment = event.getAssessment();
        assessmentName.setText(selectedAssessment.getName());
        assessmentType.setSelection(getIndex(assessmentType, selectedAssessment.getType()));
        assessmentDue.setText(selectedAssessment.getDue_date());
    }

    private int getIndex(Spinner spinner, String myString){
        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                return i;
            }
        }
        return 0;
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

        // TODO: 5/21/2018 Assessment Detail Validation

        selectedAssessment.setName(assessmentName.getText().toString());
        selectedAssessment.setDue_date(assessmentDue.getText().toString());
        selectedAssessment.setCourse_id(CourseDetail.id);
        executor.execute(new Runnable() {
            @Override
            public void run() {

                db.assessmentDao().insert(selectedAssessment);
            }
        });
        Toast.makeText(getApplicationContext(), selectedAssessment.getName() + " saved.",Toast.LENGTH_LONG).show();
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void AssessmentEventHandler(AssessmentEvent event) {
        Log.d(TAG, "AssessmentEventHandler: Assessment Event triggered!");
        bindActivity(event);
    }
}
