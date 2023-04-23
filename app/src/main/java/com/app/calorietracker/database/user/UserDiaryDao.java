package com.app.calorietracker.database.user;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.google.common.util.concurrent.ListenableFuture;

import java.time.LocalDate;
import java.util.List;

@Dao
public interface UserDiaryDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    ListenableFuture<Long> insert(UserDiaryEntity diaryEntity);
    
    @Update
    ListenableFuture<Integer> update(UserDiaryEntity diaryEntity);
    
    @Delete
    ListenableFuture<Integer> delete(UserDiaryEntity diaryEntity);
    
    @Query("SELECT * FROM DIARY WHERE _date = :date")
    ListenableFuture<UserDiaryEntity> getDiaryEntry(LocalDate date);
    
    @Query("SELECT * FROM DIARY WHERE _date >= :dateStart AND _date <= :dateEnd")
    ListenableFuture<List<UserDiaryEntity>> getDiaryEntriesRange(LocalDate dateStart, LocalDate dateEnd);
    
    
    // Non async methods, were used for testing
    // might rewrite tests and remove these later
    
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertNonAsync(UserDiaryEntity diaryEntity);
    
    @Update
    void updateNonAsync(UserDiaryEntity diaryEntity);
    
    @Delete
    void deleteNonAsync(UserDiaryEntity diaryEntity);
    
    @Query("SELECT * FROM DIARY WHERE _date = :date")
    UserDiaryEntity getDiaryEntryNonAsync(LocalDate date);
    
    @Query("SELECT * FROM DIARY WHERE _date >= :dateStart AND _date <= :dateEnd")
    UserDiaryEntity[] getDiaryEntriesRangeNonAsync(LocalDate dateStart, LocalDate dateEnd);
}
