package com.wgu.brian.scheduler.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.wgu.brian.scheduler.database.entities.CourseNote;

import java.util.List;

@Dao
public interface CourseNoteDao {

    @Query("SELECT * FROM CourseNote WHERE course_id = :course_id")
    List<CourseNote> findAllByCourseId(int course_id);

    @Query("SELECT * FROM CourseNote WHERE id = :id")
    CourseNote findById(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertAll(CourseNote... courseNotes);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertAll(List<CourseNote> courseNotes);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(CourseNote courseNote);

    @Delete
    void delete(CourseNote courseNote);
}
