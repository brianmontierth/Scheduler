package com.wgu.brian.scheduler.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.wgu.brian.scheduler.database.entities.Term;

import java.util.List;
@Dao
public interface TermDao {
    @Query("SELECT * FROM Term ORDER BY id")
    List<Term> getAllTerms();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertAll(Term... terms);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertAll(List<Term> terms);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Term term);

    @Query("SELECT * FROM Term WHERE id = :id")
    Term findById(int id);

    @Delete
    void delete(Term term);



}
