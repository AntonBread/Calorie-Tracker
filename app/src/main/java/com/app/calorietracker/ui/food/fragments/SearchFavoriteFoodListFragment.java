package com.app.calorietracker.ui.food.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.calorietracker.R;
import com.app.calorietracker.database.AppDatabase;
import com.app.calorietracker.database.foods.FoodItemDao;
import com.app.calorietracker.database.foods.FoodItemDatabaseManager;
import com.app.calorietracker.database.foods.FoodItemEntity;
import com.app.calorietracker.ui.food.list.FoodItem;
import com.app.calorietracker.ui.food.list.FoodListUtils;

import java.util.List;

public class SearchFavoriteFoodListFragment extends FoodListFragment {
    
    private List<FoodItemEntity> favoriteEntities;
    
    public SearchFavoriteFoodListFragment() {
        // Required empty public constructor
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    
    @Override
    public void onStart() {
        super.onStart();
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_food, container, false);
    }
    
    @Override
    void populateInitialList() {
        AppDatabase db = AppDatabase.getInstance();
        List<FoodItemEntity> entities = FoodItemDatabaseManager.getFavoriteFoodsList(db.foodItemDao());
        if (entities == null) {
            showListEmptyMessage(getString(R.string.food_empty_favorite_initial));
            return;
        }
        replaceFoodListFromEntities(entities);
        favoriteEntities = entities;
    }
    
    @Override
    void invalidateInitialEntityList() {
        FoodItemDao dao = AppDatabase.getInstance().foodItemDao();
        List<FoodItemEntity> entities = FoodItemDatabaseManager.getFavoriteFoodsList(dao);
        if (entities == null && favoriteEntities != null) {
            favoriteEntities.clear();
        }
        else {
            favoriteEntities = entities;
        }
    }
    
    @Override
    boolean handleSearchQuerySubmit(String query) {
        List<FoodItemEntity> nameFilteredEntities = FoodListUtils.filterByName(favoriteEntities, query);
        if (nameFilteredEntities == null || nameFilteredEntities.size() == 0) {
            showListEmptyMessage(getString(R.string.food_empty_favorite_result));
            return true;
        }
        replaceFoodListFromEntities(nameFilteredEntities);
        return true;
    }
    
    @Override
    boolean handleSearchQueryChange(String query) {
        return true;
    }
    
    @Override
    public void addFoodItem(FoodItem item) {
        if (!item.isFavorite()) {
            return;
        }
        super.addFoodItem(item);
    }
    
}
