package com.app.calorietracker.database.foods;

import android.annotation.SuppressLint;

import androidx.annotation.Nullable;

import com.app.calorietracker.ui.food.list.FoodItem;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class FoodItemDatabaseManager {
    @Nullable
    public static List<FoodItemEntity> getListFromSearchQuery(FoodItemDao dao, String query) {
        try {
            List<FoodItemEntity> foodItemEntities = dao.getFoodsByName(query).get();
            if (foodItemEntities.size() == 0) {
                throw new NullPointerException("Search result is empty");
            }
            return foodItemEntities;
        }
        catch (ExecutionException | InterruptedException | NullPointerException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    @Nullable
    public static List<FoodItemEntity> getFavoriteFoodsList(FoodItemDao dao) {
        try {
            List<FoodItemEntity> foodItemEntities = dao.getFavoriteFoods().get();
            if (foodItemEntities.size() == 0) {
                throw new NullPointerException("No favorite foods found");
            }
            return foodItemEntities;
        }
        catch (ExecutionException | InterruptedException | NullPointerException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static boolean updateFavoriteCheck(FoodItemDao dao, long id, boolean isChecked) {
        try {
            FoodItemEntity foodItemEntity = dao.getFoodById(id).get();
            foodItemEntity.setFavorite(isChecked);
            int updatedCount = dao.update(foodItemEntity).get();
            return (updatedCount == 1); // if a single record has been updated, that means success
        }
        catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public static boolean markItemDeleted(FoodItemDao dao, long id) {
        try {
            int updRowCount = dao.markDeleted(id).get();
            // This operation must update exactly one db record
            checkUpdatedRowCount(updRowCount);
            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public static boolean updateItem(FoodItemDao dao, FoodItem newItem) {
        try {
            FoodItemEntity entity = newItem.toEntity();
            entity.setId(newItem.getId());
            
            int updRowCount = dao.update(entity).get();
            checkUpdatedRowCount(updRowCount);
            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    private static void checkUpdatedRowCount(int updRowCount) throws Exception {
        if (updRowCount == 1) {
            return;
        }
    
        @SuppressLint("DefaultLocale")
        String eMsg = String.format("Wrong updated row count %d. Expected 1.", updRowCount);
        throw new Exception(eMsg);
    }
}
