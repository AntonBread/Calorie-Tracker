package com.app.calorietracker.database.foods;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.app.calorietracker.food.list.FoodItem;

import java.util.ArrayList;

@Dao
public interface FoodItemDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(FoodItemEntity foodItemEntity);
    
    @Update
    void update(FoodItemEntity foodItemEntity);
    
    @Delete
    void delete(FoodItemEntity foodItemEntity);
    
    @Query("SELECT * FROM FOODS WHERE name LIKE '%' || :name || '%'")
    FoodItemEntity[] getFoodsByName(String name);
    
    @Query("SELECT * FROM FOODS WHERE is_favorite = 1")
    FoodItemEntity[] getFavoriteFoods();
}
