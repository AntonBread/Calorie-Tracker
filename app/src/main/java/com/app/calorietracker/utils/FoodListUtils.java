package com.app.calorietracker.utils;

import androidx.annotation.Nullable;

import com.app.calorietracker.database.foods.FoodItemEntity;

import java.util.List;
import java.util.stream.Collectors;

public class FoodListUtils {
    
    @Nullable
    public static List<FoodItemEntity> filterByName(List<FoodItemEntity> input, String s) {
        if (input == null || input.size() == 0) {
            return null;
        }
        
        final String fs = s.trim().toLowerCase();
    
        return input.stream()
                    .filter(ent -> ent.getName().toLowerCase().contains(fs))
                    .collect(Collectors.toList());
    }
}
