package com.app.calorietracker.ui.food.list;

import android.util.Log;

import com.app.calorietracker.database.AppDatabase;
import com.app.calorietracker.database.foods.FoodItemEntity;
import com.app.calorietracker.ui.food.AddFoodActivity;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.Arrays;
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
    
    public void addItem(FoodItem foodItem) {
        long id = foodItem.getId();
        int portionSize = foodItem.getPortionSize();
        selectionMap.put(id, portionSize);
        context.updateSelectionCount(getSelectionCount());
    }
    
    public void updateItemPortionSize(FoodItem foodItem) {
        long id = foodItem.getId();
        int portionSize = foodItem.getPortionSize();
        selectionMap.replace(id, portionSize);
    }
    
    public void removeItem(FoodItem foodItem) {
        long id = foodItem.getId();
        selectionMap.remove(id);
        context.updateSelectionCount(getSelectionCount());
    }
    
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
    
    public ArrayList<FoodItem> getSelectedFoodItems() {
        ArrayList<FoodItem> foods = new ArrayList<>(getSelectionCount());
        
        try {
            ArrayList<FoodItemEntity> entities = (ArrayList<FoodItemEntity>) AppDatabase.getInstance()
                                                                                        .foodItemDao()
                                                                                        .getFoodsByIds(
                                                                                                selectionMap.keySet())
                                                                                        .get();
            
            for (FoodItemEntity entity : entities) {
                FoodItem foodItem = new FoodItem(entity);
                foodItem.setPortionSize(selectionMap.get(foodItem.getId()));
                foods.add(foodItem);
            }
        }
        catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        
        return foods;
    }
}
