package com.app.calorietracker.ui.stats.data;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.app.calorietracker.database.user.UserDiaryEntity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class WeightStatsData {
    
    private final LocalDate date;
    private final float weight;
    
    public WeightStatsData(LocalDate date, float weight) {
        this.date = date;
        this.weight = weight;
    }
    
    public LocalDate getDate() {
        return date;
    }
    
    public float getWeight() {
        return weight;
    }
    
    @NonNull
    public static ArrayList<WeightStatsData> createStatsDataList(List<UserDiaryEntity> dataList) {
        List<UserDiaryEntity> trimmedDataList = trimZeroWeightEntities(dataList);
        ArrayList<WeightStatsData> statsDataList = new ArrayList<>(dataList.size());
        if (trimmedDataList == null) {
            // If all records have zero weight -> return empty list
            return statsDataList;
        }
        
        for (UserDiaryEntity entity : trimmedDataList) {
            LocalDate date = entity.get_date();
            int weight_g = entity.getWeight_g();
            float weight = (float) weight_g / 1000;
            statsDataList.add(new WeightStatsData(date, weight));
        }
        
        return statsDataList;
    }
    
    @Nullable
    private static List<UserDiaryEntity> trimZeroWeightEntities(List<UserDiaryEntity> dataList) {
        for (int i = 0; i < dataList.size(); i++) {
            if (dataList.get(i).getWeight_g() > 0) {
                return dataList.subList(i, dataList.size());
            }
        }
        return null;
    }
}
