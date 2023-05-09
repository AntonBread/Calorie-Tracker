package com.app.calorietracker.database.user;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.app.calorietracker.ui.food.list.FoodItem;

import java.time.LocalDate;
import java.util.ArrayList;

@Entity(tableName = "DIARY", indices = {
        @Index(name = "date_index", value = {"_date"})
})
public class UserDiaryEntity {
    
    @NonNull
    @PrimaryKey
    private LocalDate _date;
    
    @ColumnInfo(defaultValue = "0")
    private int water_ml;
    
    private ArrayList<FoodItem> breakfast;
    private ArrayList<FoodItem> lunch;
    private ArrayList<FoodItem> dinner;
    private ArrayList<FoodItem> other;
    
    @ColumnInfo(defaultValue = "0")
    private int weight_g;
    
    @NonNull
    public LocalDate get_date() {
        return _date;
    }
    
    public int getWater_ml() {
        return water_ml;
    }
    
    public ArrayList<FoodItem> getBreakfast() {
        return breakfast;
    }
    
    public ArrayList<FoodItem> getLunch() {
        return lunch;
    }
    
    public ArrayList<FoodItem> getDinner() {
        return dinner;
    }
    
    public ArrayList<FoodItem> getOther() {
        return other;
    }
    
    public int getWeight_g() {
        return weight_g;
    }
    
    public void set_date(@NonNull LocalDate _date) {
        this._date = _date;
    }
    
    public void setWater_ml(int water_ml) {
        this.water_ml = water_ml;
    }
    
    public void setBreakfast(ArrayList<FoodItem> breakfast) {
        this.breakfast = breakfast;
    }
    
    public void setLunch(ArrayList<FoodItem> lunch) {
        this.lunch = lunch;
    }
    
    public void setDinner(ArrayList<FoodItem> dinner) {
        this.dinner = dinner;
    }
    
    public void setOther(ArrayList<FoodItem> other) {
        this.other = other;
    }
    
    public void setWeight_g(int weight_g) {
        this.weight_g = weight_g;
    }
}
