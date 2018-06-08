package com.wgu.brian.scheduler.services;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.wgu.brian.scheduler.AssessmentAdapter;
import com.wgu.brian.scheduler.AssessmentDetail;
import com.wgu.brian.scheduler.CourseAdapter;
import com.wgu.brian.scheduler.CourseDetail;
import com.wgu.brian.scheduler.R;
import com.wgu.brian.scheduler.database.AppDatabase;
import com.wgu.brian.scheduler.database.entities.Assessment;
import com.wgu.brian.scheduler.database.entities.Course;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
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
        Log.i(TAG, "handleCourseStartNotifications: Course Notify triggered: " + coursesStarting.isEmpty());
        if (!(coursesStarting == null) && !coursesStarting.isEmpty()) {
            Log.i(TAG, "handleCourseStartNotifications: Size: " + coursesStarting.size());
            for (Course course :
                    coursesStarting) {
                //TODO figure out date problems
                if (!LocalDate.parse(course.getStart_date(), DateTimeFormatter.ofPattern("MM/dd/yyyy", Locale.getDefault())).equals(LocalDate.now(ZoneId.systemDefault()))) {
                    Log.i(TAG, "handleCourseStartNotifications: " + LocalDate.parse(course.getStart_date(), DateTimeFormatter.ofPattern("MM/dd/yyyy", Locale.getDefault())).equals(LocalDate.now(ZoneId.systemDefault())));
                    Log.i(TAG, "handleCourseStartNotifications: " + LocalDate.parse(course.getStart_date(), DateTimeFormatter.ofPattern("MM/dd/yyyy", Locale.getDefault())));
                    Log.i(TAG, "handleCourseStartNotifications: " + ZonedDateTime.now(ZoneId.systemDefault()).toLocalDate());
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
        List<Course> coursesEnding = db.courseDao().getEndingCourses();
        if (!(coursesEnding == null) && !coursesEnding.isEmpty()) {
            for (Course course :
                    coursesEnding) {
                Intent intent = new Intent(this, CourseDetail.class);
                intent.putExtra(CourseAdapter.POSITION, course.getId());
                intent.putExtra(CourseAdapter.PARENT_ID, course.getTerm_id());
                NotificationBuilder.notify(this, "WGU Course Ends Today", course.getName(), intent);
            }
        }
    }

    private void handleAssessmentDueNotifications() {
        List<Assessment> assessments = db.assessmentDao().getDueAssessments();
        if (!(assessments == null) && !assessments.isEmpty()) {
            for (Assessment assessment :
                    assessments) {
                Intent intent = new Intent(this, AssessmentDetail.class);
                intent.putExtra(AssessmentAdapter.POSITION, assessment.getId());
                intent.putExtra(AssessmentAdapter.PARENT_ID, assessment.getCourse_id());
                NotificationBuilder.notify(this, "WGU Assessment Due Today", assessment.getName(), intent);
            }
        }
    }
}
