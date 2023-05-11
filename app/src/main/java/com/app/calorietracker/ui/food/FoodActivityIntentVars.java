package com.app.calorietracker.ui.food;

public class FoodActivityIntentVars {
    public static final String MEAL_TYPE_KEY = "meal_type";
    public static final String DATE_KEY = "date";
    public static final String FOOD_LIST_KEY = "food_list";
    
    // Result codes
    public static final int ADD_FOOD_CANCEL = -1;
    public static final int ADD_FOOD_DONE = 0;
    
    public enum MealType {
        BREAKFAST,
        LUNCH,
        DINNER,
        SNACKS
    }
}
