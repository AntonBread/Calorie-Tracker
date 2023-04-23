package com.app.calorietracker.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
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
    
    private static final String DATABASE_NAME = "CALORIE_TRACKER_DB";
    public static final int MODE_IN_MEMORY = 0;
    public static final int MODE_STANDARD = 1;
    
    private static AppDatabase instance = null;
    
    public static AppDatabase getInstance() {
        if (instance == null) {
            throw new NullPointerException("instanceInit() must be called before getting the instance.");
        }
        return instance;
    }
    
    public static boolean instanceInit(Context context, int mode) {
        if (instance != null) {
            return true;
        }
        if (mode == MODE_STANDARD) {
            instance = Room.databaseBuilder(context, AppDatabase.class, DATABASE_NAME).build();
            return true;
        }
        else if (mode == MODE_IN_MEMORY) {
            instance = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();
            return true;
        }
        else {
            return false;
        }
    }
}
