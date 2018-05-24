package com.wgu.brian.scheduler.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.wgu.brian.scheduler.database.entities.Assessment;

import java.util.List;

@Dao
public interface AssessmentDao {

    @Query("SELECT * FROM Assessment WHERE course_id = :course_id ORDER BY type")
    List<Assessment> findAllByCourseId(int course_id);

    @Query("SELECT * FROM Assessment WHERE id = :id")
    Assessment findById(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertAll(Assessment... assessments);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertAll(List<Assessment> assessments);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Assessment assessment);

    @Delete
    void delete(Assessment assessment);
}
