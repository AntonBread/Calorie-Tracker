package com.app.calorietracker.database;

import androidx.room.TypeConverter;

import com.app.calorietracker.food.list.FoodItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.ArrayList;

public class Converters {
    @TypeConverter
    public static ArrayList<FoodItem> foodListFromJson(String json) {
        Gson gson = new Gson();
        Type foodItemClassType = new TypeToken<ArrayList<FoodItem>>(){}.getType();
        return gson.fromJson(json, foodItemClassType);
    }
    
    @TypeConverter
    public static String foodListToJson(ArrayList<FoodItem> foodList) {
        Gson gson = new Gson();
        return gson.toJson(foodList);
    }
    
    @TypeConverter
    public static LocalDate localDateFromString(String dateString) {
        return LocalDate.parse(dateString);
    }
    
    @TypeConverter
    public static String localDateToString(LocalDate date) {
        return date.toString();
    }
}
