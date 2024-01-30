package com.app.calorietracker.ui.stats;

import android.content.Context;

import androidx.annotation.NonNull;

import com.app.calorietracker.ui.settings.SettingsManager;
import com.app.calorietracker.ui.stats.data.CaloriesStatsData;
import com.app.calorietracker.ui.stats.data.WeightStatsData;

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
    
    public int totalCalories(@NonNull List<CaloriesStatsData> caloriesDataList) {
        int total = 0;
        for (CaloriesStatsData statsData : caloriesDataList) {
            total += statsData.getCalories();
        }
        return total;
    }
    
    public int averageCalories(@NonNull List<CaloriesStatsData> caloriesDataList, int total) {
        return total / caloriesDataList.size();
    }
    
    public float weightDelta(@NonNull List<WeightStatsData> weightDataList) {
        if (weightDataList.size() == 0) {
            return Float.MIN_VALUE;
        }
        
        float start = weightDataList.get(0).getWeight();
        float end = weightDataList.get(weightDataList.size() - 1).getWeight();
        return end - start;
    }
    
    public WeightChangeSpeedInterval changeSpeedInterval(@NonNull List<WeightStatsData> weightDataList) {
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
    
    public float weightChangeSpeed(float delta,
                                   WeightChangeSpeedInterval interval,
                                   @NonNull List<WeightStatsData> weightDataList) {
        if (delta == Float.MIN_VALUE || delta == 0) {
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
    
    public float currentBodyMassIndex(@NonNull List<WeightStatsData> weightDataList) {
        if (weightDataList.size() == 0) {
            return Float.MIN_VALUE;
        }
        
        int height_cm = settingsManager.getUserHeight_cm();
        float currentWeight = weightDataList.get(weightDataList.size() - 1).getWeight();
        
        if (height_cm < 0 || currentWeight <= 0) {
            return Float.MIN_VALUE;
        }
        
        float height_m = (float) height_cm / 100;
        return currentWeight / height_m / height_m;
    }
}
