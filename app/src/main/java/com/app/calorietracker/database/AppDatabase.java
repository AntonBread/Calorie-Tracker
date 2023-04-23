package com.app.calorietracker.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.app.calorietracker.database.foods.FoodItemDao;
import com.app.calorietracker.database.foods.FoodItemEntity;
import com.app.calorietracker.database.user.UserDiaryDao;
import com.app.calorietracker.database.user.UserDiaryEntity;

@Database(entities = {UserDiaryEntity.class, FoodItemEntity.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDiaryDao userDiaryDao();
    public abstract FoodItemDao foodItemDao();
}
