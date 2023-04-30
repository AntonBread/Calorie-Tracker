package com.app.calorietracker.food.list;

import com.app.calorietracker.food.AddFoodActivity;

import java.util.ArrayList;

public class FoodSelectionManager {
    private final AddFoodActivity context;
    
    private final ArrayList<Long> selectionList;
    
    public FoodSelectionManager(AddFoodActivity context) {
        this.context = context;
        selectionList = new ArrayList<>(10);
    }
    
    public boolean addItem(FoodItem foodItem) {
        long id = foodItem.getId();
        boolean success = selectionList.add(id);
        if (success) {
            context.updateSelectionCount(getSelectionCount());
        }
        return success;
    }
    
    public boolean removeItem(FoodItem foodItem) {
        long id = foodItem.getId();
        boolean success = selectionList.remove(id);
        if (success) {
            context.updateSelectionCount(getSelectionCount());
        }
        return success;
    }
    
    public boolean isSelected(FoodItem foodItem) {
        return selectionList.contains(foodItem.getId());
    }
    
    public int getSelectionCount() {
        return selectionList.size();
    }
}
