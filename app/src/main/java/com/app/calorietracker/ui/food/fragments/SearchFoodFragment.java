package com.app.calorietracker.ui.food.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.app.calorietracker.R;
import com.app.calorietracker.database.AppDatabase;
import com.app.calorietracker.database.foods.FoodItemDatabaseManager;
import com.app.calorietracker.database.foods.FoodItemEntity;
import com.app.calorietracker.ui.food.AddFoodActivity;
import com.app.calorietracker.ui.food.list.FoodItem;
import com.app.calorietracker.ui.food.list.FoodItemAdapter;
import com.app.calorietracker.ui.food.list.FoodSelectionManager;

import java.util.ArrayList;
import java.util.List;

public class SearchFoodFragment extends Fragment {
    
    ArrayList<FoodItem> foodItems = new ArrayList<>();
    FoodItemAdapter adapter;
    SearchView searchView;
    
    public SearchFoodFragment() {
        // Required empty public constructor
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    
    @Override
    public void onStart() {
        super.onStart();
        
        FoodSelectionManager foodSelectionManager = ((AddFoodActivity) requireActivity()).getFoodSelectionManager();
        
        searchView = getView().findViewById(R.id.food_search_query);
        
        searchView.setOnQueryTextListener(searchQueryListener);
        
        RecyclerView recyclerView = getView().findViewById(R.id.food_search_list);
        adapter = new FoodItemAdapter(getContext(), foodItems, foodSelectionManager);
        recyclerView.setAdapter(adapter);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_food, container, false);
    }
    
    private final SearchView.OnQueryTextListener searchQueryListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            AppDatabase db = AppDatabase.getInstance();
            List<FoodItemEntity> entities = FoodItemDatabaseManager.getListFromSearchQuery(db.foodItemDao(), query);
            if (entities == null) {
                return true;
            }
            foodItems.clear();
            for (FoodItemEntity entity : entities) {
                foodItems.add(new FoodItem(entity));
            }
            adapter.notifyDataSetChanged();
            return true;
        }
        
        @Override
        public boolean onQueryTextChange(String newText) {
            return true;
        }
    };
}
