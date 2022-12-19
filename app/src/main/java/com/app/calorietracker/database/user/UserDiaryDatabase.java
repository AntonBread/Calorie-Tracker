package com.app.calorietracker.database.user;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {DiaryEntry.class}, version = 1, exportSchema = false)
public abstract class UserDiaryDatabase extends RoomDatabase {
    
    public abstract UserDiaryDao userDiaryDao();
    
    public static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@androidx.annotation.NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE DIARY_ENTRIES ADD COLUMN weight_g INTEGER");
        }
    };
}
