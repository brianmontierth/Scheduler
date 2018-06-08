package com.wgu.brian.scheduler.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.wgu.brian.scheduler.database.entities.Course;

import java.util.List;

@Dao
public interface CourseDao {
    @Query("SELECT * FROM Course ORDER BY name")
    List<Course> getAllCourses();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertAll(Course... course);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertAll(List<Course> course);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Course course);

    @Query("SELECT * FROM Course WHERE id = :id")
    Course findById(int id);

    @Query("SELECT * FROM Course WHERE term_id = :term_id ORDER BY name")
    List<Course> findAllByTermId(int term_id);

    @Delete
    void delete(Course course);
}
