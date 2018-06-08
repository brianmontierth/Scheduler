package com.wgu.brian.scheduler;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.wgu.brian.scheduler.database.AppDatabase;
import com.wgu.brian.scheduler.database.entities.CourseNote;
import com.wgu.brian.scheduler.events.CourseNoteEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class CourseNoteDetail extends AppCompatActivity {

    public static final String TAG = "CourseNoteDetail";
    public static int id;
    private TextView note;
    private AppDatabase db;
    private CourseNote selectedNote = new CourseNote();

    private boolean newNote = true;
    private boolean formEnabled = true;

    private Menu menu;

    private Executor executor = Executors.newCachedThreadPool();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_note_detail);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        try {
            id = getIntent().getExtras().getInt(CourseNoteAdapter.POSITION, -1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        note = findViewById(R.id.courseNoteDetailNote);

        db = AppDatabase.getInstance(this);

        if (id != -1) {
            newNote = false;
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    CourseNote courseNote = db.courseNoteDao().findById(CourseNoteDetail.id);
                    EventBus.getDefault().post(new CourseNoteEvent(courseNote));
                }
            });
        }

        EventBus.getDefault().register(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the note_menu; this adds items to the action bar if it is present.
        this.menu = menu;
        getMenuInflater().inflate(R.menu.note_menu, menu);
        enableForm(newNote);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.note_menu_edit:
                enableForm(true);
                break;
            case R.id.note_menu_save:
                save();
                break;
            case R.id.note_menu_delete:
                delete();
                break;
            case R.id.note_menu_share:
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Scheduler Course Note");
                shareIntent.putExtra(Intent.EXTRA_TEXT, note.getText().toString());
                startActivity(Intent.createChooser(shareIntent, "Share using..."));
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

                db.courseNoteDao().delete(selectedNote);
            }
        });
        Toast.makeText(getApplicationContext(), "Note deleted.", Toast.LENGTH_LONG).show();
        setResult(RESULT_OK);
        finish();
    }

    private void enableForm(boolean enabled) {
        formEnabled = enabled;
        menu.getItem(0).setVisible(!enabled);
        menu.getItem(1).setVisible(enabled);
        note.setEnabled(enabled);
    }

    private void bindActivity(CourseNoteEvent event) {
        selectedNote = event.getCourseNote();
        note.setText(selectedNote.getNote());
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

        selectedNote.setNote(note.getText().toString());
        selectedNote.setCourse_id(getIntent().getExtras().getInt(CourseNoteAdapter.PARENT_ID));
        executor.execute(new Runnable() {
            @Override
            public void run() {

                db.courseNoteDao().insert(selectedNote);
            }
        });
        Toast.makeText(getApplicationContext(), "Note saved.", Toast.LENGTH_LONG).show();
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
    public void CourseNoteEventHandler(CourseNoteEvent event) {
        Log.d(TAG, "CourseNoteEventHandler: Note Event triggered!");
        bindActivity(event);
    }
}
