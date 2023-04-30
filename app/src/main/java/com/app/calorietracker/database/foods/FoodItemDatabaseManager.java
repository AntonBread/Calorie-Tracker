package com.app.calorietracker.database.foods;

import com.app.calorietracker.food.list.FoodItem;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class FoodItemDatabaseManager {
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
}
