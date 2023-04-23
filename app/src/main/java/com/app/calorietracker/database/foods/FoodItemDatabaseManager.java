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
}
