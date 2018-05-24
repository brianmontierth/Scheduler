package com.wgu.brian.scheduler.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.wgu.brian.scheduler.database.entities.AssessmentNote;

import java.util.List;

@Dao
public interface AssessmentNoteDao {

    @Query("SELECT * FROM AssessmentNote WHERE assessment_id = :assessment_id")
    List<AssessmentNote> findAllByAssessmentId(int assessment_id);

    @Query("SELECT * FROM AssessmentNote WHERE id = :id")
    AssessmentNote findById(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertAll(AssessmentNote... assessmentNotes);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertAll(List<AssessmentNote> assessmentNotes);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(AssessmentNote assessmentNote);

    @Delete
    void delete(AssessmentNote assessmentNote);

}
