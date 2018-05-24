package com.wgu.brian.scheduler;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.wgu.brian.scheduler.database.AppDatabase;
import com.wgu.brian.scheduler.database.entities.Term;
import com.wgu.brian.scheduler.events.TermsEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class TermListActivity extends AppCompatActivity {

    private static final String TAG = "TERM_LIST_ACTIVITY_TAG";
    List<Term> terms;
    FloatingActionButton fab;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    private AppDatabase db;

    private Executor executor = Executors.newSingleThreadExecutor();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        db = AppDatabase.getInstance(this);
        EventBus.getDefault().register(this);


        executor.execute(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "TermsEventHandler: trying to execute this thread.");
                terms = db.termDao().getAllTerms();
                EventBus.getDefault().post(new TermsEvent(terms));
                }
        });


         fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TermListActivity.this, TermDetail.class);
                intent.putExtra(TermAdapter.POSITION, -1);
                startActivity(intent);
            }
        });


    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void TermsEventHandler(TermsEvent event) {

        Log.d(TAG, "TermsEventHandler: Event triggered!");

        terms = event.getTermList();
        recyclerView = findViewById(R.id.term_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TermAdapter(terms);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        AppDatabase.destroyInstance();
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
