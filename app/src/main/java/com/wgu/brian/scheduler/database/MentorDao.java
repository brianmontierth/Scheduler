package com.wgu.brian.scheduler.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.wgu.brian.scheduler.database.entities.Mentor;

import java.util.List;

@Dao
public interface MentorDao {

    @Query("SELECT * FROM Mentor WHERE course_id = :course_id ORDER BY name")
    List<Mentor> findAllByCourseId(int course_id);

    @Query("SELECT * FROM Mentor WHERE id = :id")
    Mentor findById(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertAll(Mentor... mentors);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertAll(List<Mentor> mentors);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Mentor mentor);

    @Delete
    void delete(Mentor mentor);
}
