package com.app.calorietracker.ui.food.list;

public interface FoodListAction {
    void scrollOnViewHolderExpand(int pos);
    void onFoodItemDelete(int pos, FoodItem foodItem);
}
