package com.app.calorietracker.ui.stats;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.app.calorietracker.database.user.UserDiaryEntity;
import com.app.calorietracker.ui.main.MealData;
import com.app.calorietracker.ui.settings.SettingsManager;

import java.util.List;

public class StatsCalculator {
    
    public enum WeightChangeSpeedInterval {
        NONE,
        WEEK,
        MONTH
    }
    
    private final SettingsManager settingsManager;
    
    public StatsCalculator(Context context) {
        settingsManager = new SettingsManager(context);
    }
    
    public int totalCalories(@NonNull List<UserDiaryEntity> dataList) {
        int caloriesTotal = 0;
        for (UserDiaryEntity entity : dataList) {
            caloriesTotal += new MealData(entity.getBreakfast()).getCals();
            caloriesTotal += new MealData(entity.getLunch()).getCals();
            caloriesTotal += new MealData(entity.getDinner()).getCals();
            caloriesTotal += new MealData(entity.getOther()).getCals();
        }
        return caloriesTotal;
    }
    
    public int averageCalories(@NonNull List<UserDiaryEntity> dataList, int total) {
        return total / dataList.size();
    }
    
    @Nullable
    private List<UserDiaryEntity> weightDataList;
    
    public float weightDelta(@NonNull List<UserDiaryEntity> dataList) {
        trimDataListForWeight(dataList);
        
        if (weightDataList == null) {
            return Float.MIN_VALUE;
        }
        int weightStart = weightDataList.get(0).getWeight_g();
        int weightEnd = weightDataList.get(weightDataList.size() - 1).getWeight_g();
        // Weight gain – positive delta
        // Weight loss – negative delta
        float delta = weightEnd - weightStart;
        // convert integer grams to float kgs
        return delta / 1000;
    }
    
    public WeightChangeSpeedInterval changeSpeedInterval() {
        if (weightDataList == null) {
            return WeightChangeSpeedInterval.NONE;
        }
        int dataListSize = weightDataList.size();
        if (dataListSize <= 8) {
            return WeightChangeSpeedInterval.NONE;
        }
        else if (dataListSize <= 180) {
            return WeightChangeSpeedInterval.WEEK;
        }
        else {
            return WeightChangeSpeedInterval.MONTH;
        }
    }
    
    public float weightChangeSpeed(float delta, WeightChangeSpeedInterval interval) {
        if (weightDataList == null || delta == 0) {
            return Float.MIN_VALUE;
        }
        
        delta = Math.abs(delta);
        switch (interval) {
            default:
            case NONE:
                return Float.MIN_VALUE;
            case WEEK:
                float weekCount = (float) weightDataList.size() / 7;
                return delta / weekCount;
            case MONTH:
                float monthCount = (float) weightDataList.size() / 30;
                return delta / monthCount;
        }
    }
    
    // Removes all leading entries with zero weight if present
    // If all entries have zero weight then weight list is set to null
    private void trimDataListForWeight(@NonNull List<UserDiaryEntity> dataList) {
        int i = 0;
        for (; i < dataList.size(); i++) {
            if (dataList.get(i).getWeight_g() > 0) {
                weightDataList = dataList.subList(i, dataList.size());
                return;
            }
        }
        weightDataList = null;
    }
    
    public float currentBodyMassIndex(@NonNull List<UserDiaryEntity> dataList) {
        int height_cm = settingsManager.getUserHeight_cm();
        int currentWeight_g = dataList.get(dataList.size() - 1).getWeight_g();
        
        if (height_cm < 0 || currentWeight_g <= 0) {
            return Float.MIN_VALUE;
        }
        
        float height_m = (float) height_cm / 100;
        float currentWeight_kg = (float) currentWeight_g / 1000;
        
        return currentWeight_kg / height_m / height_m;
    }
}
