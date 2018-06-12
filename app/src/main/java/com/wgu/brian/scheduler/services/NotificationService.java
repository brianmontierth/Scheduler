package com.wgu.brian.scheduler.services;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.wgu.brian.scheduler.AssessmentAdapter;
import com.wgu.brian.scheduler.AssessmentDetail;
import com.wgu.brian.scheduler.CourseAdapter;
import com.wgu.brian.scheduler.CourseDetail;
import com.wgu.brian.scheduler.R;
import com.wgu.brian.scheduler.database.AppDatabase;
import com.wgu.brian.scheduler.database.entities.Assessment;
import com.wgu.brian.scheduler.database.entities.Course;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 */
public class NotificationService extends IntentService {

    private static final String TAG = "NotificationService";
    private AppDatabase db;

    public NotificationService() {
        super("NotificationService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        db = AppDatabase.getInstance(this);

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);

        if (settings.getBoolean(getString(R.string.notifications_course_start), false)) {
            handleCourseStartNotifications();
        }
        if (settings.getBoolean(getString(R.string.notifications_course_end), false)) {
            handleCourseEndNotifications();
        }
        if (settings.getBoolean(getString(R.string.notifications_assessment_due), false)) {
            handleAssessmentDueNotifications();
        }

    }

    private void handleCourseStartNotifications() {
        List<Course> coursesStarting = db.courseDao().getAllCourses();
        if (!(coursesStarting == null) && !coursesStarting.isEmpty()) {
            for (Course course :
                    coursesStarting) {
                if (!LocalDate.parse(course.getStart_date(), DateTimeFormatter.ofPattern("MM/dd/yyyy", Locale.getDefault())).equals(LocalDate.now())) {
                    continue;
                }

                Intent intent = new Intent(this, CourseDetail.class);
                intent.putExtra(CourseAdapter.POSITION, course.getId());
                intent.putExtra(CourseAdapter.PARENT_ID, course.getTerm_id());
                NotificationBuilder.notify(this, "WGU Course Starts Today", course.getName(), intent);
            }
        }
    }

    private void handleCourseEndNotifications() {
        List<Course> coursesEnding = db.courseDao().getAllCourses();
        if (!(coursesEnding == null) && !coursesEnding.isEmpty()) {
            for (Course course :
                    coursesEnding) {
                if (!LocalDate.parse(course.getEnd_date(), DateTimeFormatter.ofPattern("MM/dd/yyyy", Locale.getDefault())).equals(LocalDate.now())) {
                    continue;
                }

                Intent intent = new Intent(this, CourseDetail.class);
                intent.putExtra(CourseAdapter.POSITION, course.getId());
                intent.putExtra(CourseAdapter.PARENT_ID, course.getTerm_id());
                NotificationBuilder.notify(this, "WGU Course Ends Today", course.getName(), intent);
            }
        }
    }

    private void handleAssessmentDueNotifications() {
        List<Assessment> assessments = db.assessmentDao().getAllAssessments();
        if (!(assessments == null) && !assessments.isEmpty()) {
            for (Assessment assessment : assessments) {
                if (!LocalDate.parse(assessment.getDue_date(), DateTimeFormatter.ofPattern("MM/dd/yyyy", Locale.getDefault())).equals(LocalDate.now())) {
                    continue;
                }

                Intent intent = new Intent(this, AssessmentDetail.class);
                intent.putExtra(AssessmentAdapter.POSITION, assessment.getId());
                intent.putExtra(AssessmentAdapter.PARENT_ID, assessment.getCourse_id());
                NotificationBuilder.notify(this, "WGU Assessment Due Today", assessment.getName(), intent);
            }
        }
    }
}
