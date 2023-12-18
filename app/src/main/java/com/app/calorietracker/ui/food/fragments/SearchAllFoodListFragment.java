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

import java.util.List;

public class SearchAllFoodListFragment extends FoodListFragment {
    
    public SearchAllFoodListFragment() {
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
    
    // Initial list is empty in "All" search mode
    @Override
    void populateInitialList() {}
    
    @Override
    boolean handleSearchQuerySubmit(String query) {
        AppDatabase db = AppDatabase.getInstance();
        List<FoodItemEntity> entities = FoodItemDatabaseManager.getListFromSearchQuery(db.foodItemDao(), query);
        if (entities == null) {
            return true;
        }
        replaceFoodListFromEntities(entities);
        return true;
    }
    
    @Override
    boolean handleSearchQueryChange(String query) {
        return true;
    }
    
    @Override
    public void addFoodItem(FoodItem item) {
        scaleBottomPadding();
        foodItems.add(0, item);
        adapter.notifyItemInserted(0);
    }
    
}
