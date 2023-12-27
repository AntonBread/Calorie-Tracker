package com.app.calorietracker.database.foods;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.google.common.util.concurrent.ListenableFuture;

import java.util.List;

@Dao
public interface FoodItemDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    ListenableFuture<Long> insert(FoodItemEntity foodItemEntity);
    
    @Update(onConflict = OnConflictStrategy.REPLACE)
    ListenableFuture<Integer> update(FoodItemEntity foodItemEntity);
    
    @Delete
    ListenableFuture<Integer> delete(FoodItemEntity foodItemEntity);
    
    @Query("UPDATE FOODS SET deleted_flag = 1 WHERE id = :id")
    ListenableFuture<Integer> markDeleted(long id);
    
    @Query("SELECT * FROM FOODS WHERE name LIKE '%' || :name || '%' AND deleted_flag <> 1")
    ListenableFuture<List<FoodItemEntity>> getFoodsByName(String name);
    
    @Query("SELECT * FROM FOODS WHERE is_favorite = 1 AND deleted_flag <> 1")
    ListenableFuture<List<FoodItemEntity>> getFavoriteFoods();
    
    @Query("SELECT * FROM FOODS WHERE id = :id AND deleted_flag <> 1")
    ListenableFuture<FoodItemEntity> getFoodById(long id);
    
    @Query("SELECT * FROM FOODS WHERE id IN (:ids) AND deleted_flag <> 1")
    ListenableFuture<List<FoodItemEntity>> getFoodsByIds(List<Long> ids);
    
    
    
    // Non async methods, were used for testing
    // might rewrite tests and remove these later
    
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insertNonAsync(FoodItemEntity foodItemEntity);
    
    @Update
    void updateNonAsync(FoodItemEntity foodItemEntity);
    
    @Delete
    void deleteNonAsync(FoodItemEntity foodItemEntity);
    
    @Query("SELECT * FROM FOODS WHERE name LIKE '%' || :name || '%'")
    FoodItemEntity[] getFoodsByNameNonAsync(String name);
    
    @Query("SELECT * FROM FOODS WHERE is_favorite = 1")
    FoodItemEntity[] getFavoriteFoodsNonAsync();
}
