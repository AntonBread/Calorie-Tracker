package com.app.calorietracker.ui.main;

import com.app.calorietracker.ui.food.list.FoodItem;

import java.util.ArrayList;

public class MealData {
    private int cals;
    private float carbs_g;
    private float fat_g;
    private float protein_g;
    
    public MealData(ArrayList<FoodItem> foods) {
        cals = 0;
        carbs_g = 0.0f;
        fat_g = 0.0f;
        protein_g = 0.0f;
        if (foods == null || foods.size() == 0) return;
        for (FoodItem foodItem : foods) {
            cals += foodItem.getKcalCurrent();
            carbs_g += foodItem.getCarbsCurrent();
            fat_g += foodItem.getFatCurrent();
            protein_g += foodItem.getProteinCurrent();
        }
    }
    
    public int getCals() {
        return cals;
    }
    
    public float getCarbs_g() {
        return carbs_g;
    }
    
    public float getFat_g() {
        return fat_g;
    }
    
    public float getProtein_g() {
        return protein_g;
    }
}
