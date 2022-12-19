package com.app.calorietracker.database.user;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "DIARY_ENTRIES")
public class DiaryEntry {
    @NonNull
    @PrimaryKey
    public String _date;
    
    @ColumnInfo(defaultValue = "0")
    public int calories;
    @ColumnInfo(defaultValue = "0")
    public int carbs_g;
    @ColumnInfo(defaultValue = "0")
    public int fat_g;
    @ColumnInfo(defaultValue = "0")
    public int protein_g;
    @ColumnInfo(defaultValue = "0")
    public int water_mL;
    
    // These 4 fields store arrays of FoodItem objects in JSON format
    public String breakfast;
    public String lunch;
    public String dinner;
    public String other;
    
    
    @ColumnInfo(defaultValue = "0")
    public int weight_g;    // User weight tracking
}
