package com.app.calorietracker.ui.food.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.calorietracker.R;
import com.app.calorietracker.database.AppDatabase;
import com.app.calorietracker.database.foods.FoodItemEntity;
import com.app.calorietracker.ui.food.AddFoodActivity;
import com.app.calorietracker.ui.food.list.FoodItem;
import com.app.calorietracker.ui.food.list.SelectionHistoryCacheManager;
import com.app.calorietracker.utils.FoodListUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class SearchHistoryFoodListFragment extends FoodListFragment {
    
    List<Long> ids;
    List<FoodItemEntity> recentEntities;
    
    public SearchHistoryFoodListFragment() {
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
        return inflater.inflate(R.layout.fragment_history_food, container, false);
    }
    
    @Override
    void populateInitialList() {
        SelectionHistoryCacheManager selectionHistoryCacheManager =
                ((AddFoodActivity) requireActivity()).getSelectionHistoryCacheManager();
        ids = selectionHistoryCacheManager.getIDs();
        if (ids == null || ids.size() == 0) return;
        
        try {
            List<FoodItemEntity> entities = selectionHistoryCacheManager.getRecentFoodItemEntities();
            if (entities == null) {
                return;
            }
            recentEntities = entities;
            replaceFoodListFromEntities(recentEntities);
        }
        catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    void invalidateInitialEntityList() {
        SelectionHistoryCacheManager selectionHistoryCacheManager =
                ((AddFoodActivity) requireActivity()).getSelectionHistoryCacheManager();
        ids = selectionHistoryCacheManager.getIDs();
        
        if (ids == null || ids.size() == 0) {
            if (recentEntities != null) {
                recentEntities.clear();
            }
            return;
        }
    
        try {
            List<FoodItemEntity> entities = selectionHistoryCacheManager.getRecentFoodItemEntities();
            if (entities == null) {
                if (recentEntities != null) {
                    recentEntities.clear();
                }
            }
            else {
                recentEntities = entities;
            }
        }
        catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    boolean handleSearchQuerySubmit(String query) {
        List<FoodItemEntity> nameFilteredEntities = FoodListUtils.filterByName(recentEntities, query);
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
        if (ids == null || ids.size() == 0 || !(ids.contains(item.getId()))) {
            return;
        }
        foodItems.add(0, item);
        adapter.notifyItemInserted(0);
    }
    
}
