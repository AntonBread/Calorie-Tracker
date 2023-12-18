package com.app.calorietracker.ui.food.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.calorietracker.R;
import com.app.calorietracker.database.AppDatabase;
import com.app.calorietracker.database.foods.FoodItemDatabaseManager;
import com.app.calorietracker.database.foods.FoodItemEntity;
import com.app.calorietracker.ui.food.list.FoodItem;
import com.app.calorietracker.ui.food.list.FoodSelectionManager;
import com.app.calorietracker.utils.FoodListUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
        return inflater.inflate(R.layout.fragment_favorite_food, container, false);
    }
    
    @Override
    void populateInitialList() {
        AppDatabase db = AppDatabase.getInstance();
        List<FoodItemEntity> entities = FoodItemDatabaseManager.getFavoriteFoodsList(db.foodItemDao());
        if (entities == null) {
            return;
        }
        replaceFoodListFromEntities(entities);
        favoriteEntities = entities;
    }
    
    @Override
    boolean handleSearchQuerySubmit(String query) {
        List<FoodItemEntity> nameFilteredEntities = FoodListUtils.filterByName(favoriteEntities, query);
        if (nameFilteredEntities == null) {
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
        scaleBottomPadding();
        if (!item.isFavorite()) {
            return;
        }
        foodItems.add(0, item);
        adapter.notifyItemInserted(0);
    }
    
}
