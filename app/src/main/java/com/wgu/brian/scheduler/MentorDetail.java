package com.wgu.brian.scheduler;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.wgu.brian.scheduler.database.AppDatabase;
import com.wgu.brian.scheduler.database.entities.Mentor;
import com.wgu.brian.scheduler.events.MentorEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MentorDetail extends AppCompatActivity {

    public static final String TAG = "MentorDetail";

    private TextView mentorName;
    private TextView mentorPhone;
    private TextView mentorEmail;

    private AppDatabase db;
    public static int id;
    private Mentor selectedMentor = new Mentor();

    private boolean newMentor = true;
    private boolean formEnabled = true;

    private Menu menu;

    private Executor executor = Executors.newCachedThreadPool();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mentor_detail);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        try {
            id = getIntent().getExtras().getInt(MentorAdapter.POSITION, -1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        mentorName = findViewById(R.id.mentorDetailName);
        mentorPhone = findViewById(R.id.mentorDetailPhone);
        mentorEmail = findViewById(R.id.mentorDetailEmail);

        db = AppDatabase.getInstance(this);

        if (id != -1) {
            newMentor = false;
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    Mentor mentor = db.mentorDao().findById(MentorDetail.id);
                    EventBus.getDefault().post(new MentorEvent(mentor));
                }
            });
        }

        EventBus.getDefault().register(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the mentor_menu; this adds items to the action bar if it is present.
        this.menu = menu;
        getMenuInflater().inflate(R.menu.mentor_menu, menu);
        enableForm(newMentor);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()) {
            case R.id.mentor_menu_edit:
                enableForm(true);
                break;
            case R.id.mentor_menu_save:
                save();
                break;
            case R.id.mentor_menu_delete:
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

                db.mentorDao().delete(selectedMentor);
            }
        });
        Toast.makeText(getApplicationContext(), selectedMentor.getName() + " deleted.",Toast.LENGTH_LONG).show();
        setResult(RESULT_OK);
        finish();
    }

    private void enableForm(boolean enabled) {
        formEnabled = enabled;
        menu.getItem(0).setVisible(!enabled);
        menu.getItem(1).setVisible(enabled);
        mentorName.setEnabled(enabled);
        mentorPhone.setEnabled(enabled);
        mentorEmail.setEnabled(enabled);
    }

    private void bindActivity(MentorEvent event) {
        selectedMentor = event.getMentor();
        mentorName.setText(selectedMentor.getName());
        mentorPhone.setText(selectedMentor.getPhone());
        mentorEmail.setText(selectedMentor.getEmail());
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

        // TODO: 5/21/2018 Mentor Detail Validation

        selectedMentor.setName(mentorName.getText().toString());
        selectedMentor.setPhone(mentorPhone.getText().toString());
        selectedMentor.setEmail(mentorEmail.getText().toString());
        selectedMentor.setCourse_id(CourseDetail.id);
        executor.execute(new Runnable() {
            @Override
            public void run() {

                db.mentorDao().insert(selectedMentor);
            }
        });
        Toast.makeText(getApplicationContext(), selectedMentor.getName() + " saved.",Toast.LENGTH_LONG).show();
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
    public void MentorEventHandler(MentorEvent event) {
        Log.d(TAG, "MentorEventHandler: Mentor Event triggered!");
        bindActivity(event);
    }
}
