package com.app.calorietracker.ui.food.fragments;

import android.os.Bundle;
import android.util.Log;
import android.widget.SearchView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.app.calorietracker.R;
import com.app.calorietracker.database.foods.FoodItemEntity;
import com.app.calorietracker.ui.food.AddFoodActivity;
import com.app.calorietracker.ui.food.list.FoodItem;
import com.app.calorietracker.ui.food.list.FoodItemAdapter;
import com.app.calorietracker.ui.food.list.FoodSelectionManager;

import java.util.ArrayList;
import java.util.List;

public abstract class FoodListFragment extends Fragment {
    
    ArrayList<FoodItem> foodItems = new ArrayList<>();
    FoodItemAdapter adapter;
    SearchView searchView;
    RecyclerView recyclerView;
    FoodSelectionManager foodSelectionManager;
    
    SearchView.OnQueryTextListener searchQueryListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String s) {
            return handleSearchQuerySubmit(s);
        }
    
        @Override
        public boolean onQueryTextChange(String s) {
            return handleSearchQueryChange(s);
        }
    };
    
    abstract void populateInitialList();
    
    abstract boolean handleSearchQuerySubmit(String query);
    
    abstract boolean handleSearchQueryChange(String query);
    
    // Adding foods to list requires custom implementation to avoid
    // situations like adding non-favorite food to favorite list
    // or adding wrong food to history list
    public abstract void addFoodItem(FoodItem item);
    
    public void removeFoodItem(FoodItem item, int position) {
        foodItems.remove(item);
        adapter.notifyItemRemoved(position);
        scaleBottomPadding();
    }
    
    public FoodListFragment() {
        // Required empty public constructor
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    
    @Override
    public void onStart() {
        super.onStart();
        
        foodSelectionManager = ((AddFoodActivity) requireActivity()).getFoodSelectionManager();
        
        searchView = getView().findViewById(R.id.food_search_query);
        searchView.setOnQueryTextListener(searchQueryListener);
        
        recyclerView = getView().findViewById(R.id.food_list);
        adapter = new FoodItemAdapter(getContext(), foodItems, foodSelectionManager);
        recyclerView.setAdapter(adapter);
        
        populateInitialList();
        scaleBottomPadding();
    }
    
    void replaceFoodListFromEntities(List<FoodItemEntity> entities) {
        clearList();
        for (FoodItemEntity entity : entities) {
            // Do not add already selected items
            FoodItem item = new FoodItem(entity);
            if (foodSelectionManager.isSelected(item)) {
                continue;
            }
        
            foodItems.add(new FoodItem(entity));
            adapter.notifyItemInserted(foodItems.size() - 1);
        }
    }
    
    private void clearList() {
        int size = foodItems.size();
        foodItems.clear();
        adapter.notifyItemRangeRemoved(0, size);
    }
    
    void scaleBottomPadding() {
        int paddingBase_dp = 340; // dp
        int selectionListMaxHeight_dp = 240; // dp
        int selectionCount = foodSelectionManager.getSelectionCount();
        float scale = getResources().getDisplayMetrics().density;
    
        int selectionListMaxHeight_px = (int) (selectionListMaxHeight_dp * scale + 0.5f);
        int foodItemViewHeight_px = calculateFoodItemViewHeight(scale);
        // If foodItem view couldn't be measured
        // then both food lists are currently empty,
        // so there is no need to scale padding
        if (foodItemViewHeight_px == -1) {
            return;
        }
        
        int selectionListHeight_px = foodItemViewHeight_px * selectionCount;
        selectionListHeight_px = Math.min(selectionListMaxHeight_px, selectionListHeight_px);
        
        int paddingBase_px = (int) (paddingBase_dp * scale + 0.5f);
        int paddingNew = paddingBase_px + selectionListHeight_px;
        recyclerView.setPadding(0, 0, 0, paddingNew);
    }
    
    private int getFoodItemViewHeight() {
        // Try to get food item card height from search list
        // if it's empty – get height from selection list
        // if selection list is empty too – return -1
        // nested try/catch blocks look nasty, but they get the job done
        int foodItemViewHeight_px;
        try {
            foodItemViewHeight_px = recyclerView.getChildAt(0).getHeight();
        }
        catch (Exception e) {
            try {
                foodItemViewHeight_px = ((AddFoodActivity) requireActivity()).getFoodItemViewHeight();
            }
            catch (Exception e1) {
                return -1;
            }
        }
        return foodItemViewHeight_px;
    }
    
    private int calculateFoodItemViewHeight(float scale) {
        int foodItemViewHeight_px = getFoodItemViewHeight();
        if (foodItemViewHeight_px == - 1) {
            return -1;
        }
        
        int foodItemViewMargin_dp = 8;
        int foodItemViewMargin_px = (int) (foodItemViewMargin_dp * scale + 0.5f);
        return foodItemViewHeight_px + foodItemViewMargin_px;
    }
    
}
