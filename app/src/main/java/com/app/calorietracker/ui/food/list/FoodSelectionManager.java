package com.app.calorietracker.ui.food.list;

import com.app.calorietracker.database.AppDatabase;
import com.app.calorietracker.database.foods.FoodItemEntity;
import com.app.calorietracker.ui.food.AddFoodActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class FoodSelectionManager {
    private final AddFoodActivity context;
    
    // Key - food item ID; Value - portion size
    private final HashMap<Long, Integer> selectionMap;
    
    public FoodSelectionManager(AddFoodActivity context) {
        this.context = context;
        selectionMap = new HashMap<>(10);
    }
    
    public void addItem(FoodItem foodItem, int adapterPosition) {
        long id = foodItem.getId();
        int portionSize = foodItem.getPortionSize();
        selectionMap.put(id, portionSize);
        contextUpdate(foodItem, adapterPosition, true);
    }
    
    public void updateItemPortionSize(FoodItem foodItem) {
        long id = foodItem.getId();
        int portionSize = foodItem.getPortionSize();
        selectionMap.replace(id, portionSize);
    }
    
    public void removeItem(FoodItem foodItem, int adapterPosition) {
        long id = foodItem.getId();
        selectionMap.remove(id);
        contextUpdate(foodItem, adapterPosition, false);
    }
    
    public void contextUpdate(FoodItem foodItem, int adapterPosition, boolean select) {
        context.updateSelectionCount(getSelectionCount());
        if (select) {
            context.transferFoodItemToSelectionList(foodItem, adapterPosition);
        }
        else {
            context.transferFoodItemToSearchList(foodItem, adapterPosition);
        }
    }
    
    @SuppressWarnings("ConstantConditions")
    public int getSelectedPortionSize(FoodItem foodItem) {
        long id = foodItem.getId();
        return selectionMap.get(id);
    }
    
    public boolean isSelected(FoodItem foodItem) {
        return selectionMap.containsKey(foodItem.getId());
    }
    
    public int getSelectionCount() {
        return selectionMap.size();
    }
}
