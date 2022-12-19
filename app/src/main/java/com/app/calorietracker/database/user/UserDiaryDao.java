package com.app.calorietracker.database.user;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface UserDiaryDao {
    
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    Completable insertEntry(DiaryEntry entry);
    
    @Update
    Completable updateEntry(DiaryEntry entry);
    
    @Delete
    Completable deleteEntry(DiaryEntry entry);
    
    @Query("SELECT * FROM DIARY_ENTRIES WHERE _date = :date")
    Single<DiaryEntry> getEntry(String date);
}
