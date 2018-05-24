package com.wgu.brian.scheduler;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class CourseDetail extends AppCompatActivity {

    TextView courseName;
    TextView courseStart;
    TextView courseEnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        courseName = findViewById(R.id.courseDetailName);
        courseStart = findViewById(R.id.courseDetailStart);
        courseEnd = findViewById(R.id.courseDetailEnd);





    }

}
