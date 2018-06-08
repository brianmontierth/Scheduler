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
import com.wgu.brian.scheduler.database.entities.AssessmentNote;
import com.wgu.brian.scheduler.events.AssessmentNoteEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AssessmentNoteDetail extends AppCompatActivity {

    public static final String TAG = "AssessmentNoteDetail";
    public static int id;
    private TextView note;
    private AppDatabase db;
    private AssessmentNote selectedNote = new AssessmentNote();

    private boolean newNote = true;
    private boolean formEnabled = true;

    private Menu menu;

    private Executor executor = Executors.newCachedThreadPool();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_note_detail);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        try {
            id = getIntent().getExtras().getInt(AssessmentNoteAdapter.POSITION, -1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        note = findViewById(R.id.assessmentNoteDetailNote);

        db = AppDatabase.getInstance(this);

        if (id != -1) {
            newNote = false;
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    AssessmentNote assessmentNote = db.assessmentNoteDao().findById(AssessmentNoteDetail.id);
                    EventBus.getDefault().post(new AssessmentNoteEvent(assessmentNote));
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
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Scheduler Assessment Note");
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

                db.assessmentNoteDao().delete(selectedNote);
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

    private void bindActivity(AssessmentNoteEvent event) {
        selectedNote = event.getAssessmentNote();
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
        selectedNote.setAssessment_id(getIntent().getExtras().getInt(AssessmentNoteAdapter.PARENT_ID));
        executor.execute(new Runnable() {
            @Override
            public void run() {

                db.assessmentNoteDao().insert(selectedNote);
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
    public void AssessmentNoteEventHandler(AssessmentNoteEvent event) {
        Log.d(TAG, "AssessmentNoteEventHandler: Note Event triggered!");
        bindActivity(event);
    }
}
