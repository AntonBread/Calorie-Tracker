package com.app.calorietracker.ui.stats.data;

import androidx.annotation.NonNull;

import com.app.calorietracker.database.user.UserDiaryEntity;
import com.app.calorietracker.ui.main.MealData;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CaloriesStatsData {
    
    private final LocalDate date;
    private final int calories;
    
    public CaloriesStatsData(LocalDate date, int calories) {
        this.date = date;
        this.calories = calories;
    }
    
    public LocalDate getDate() {
        return date;
    }
    
    public int getCalories() {
        return calories;
    }
    
    @NonNull
    public static ArrayList<CaloriesStatsData> createStatsDataList(List<UserDiaryEntity> dataList) {
        ArrayList<CaloriesStatsData> statsDataList = new ArrayList<>(dataList.size());
        for (UserDiaryEntity entity : dataList) {
            LocalDate date = entity.get_date();
            int calories = 0;
            calories += new MealData(entity.getBreakfast()).getCals();
            calories += new MealData(entity.getLunch()).getCals();
            calories += new MealData(entity.getDinner()).getCals();
            calories += new MealData(entity.getOther()).getCals();
            statsDataList.add(new CaloriesStatsData(date, calories));
        }
        return statsDataList;
    }
}
