package com.app.calorietracker.ui.food.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

public class FavoriteFoodFragment extends Fragment {
    
    ArrayList<FoodItem> foodItems = new ArrayList<>();
    
    public FavoriteFoodFragment() {
        // Required empty public constructor
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    
    @Override
    public void onStart() {
        super.onStart();
    
        getFavoriteFoodsFromDb();
    
        FoodSelectionManager foodSelectionManager = ((AddFoodActivity) requireActivity()).getFoodSelectionManager();
        
        RecyclerView recyclerView = getView().findViewById(R.id.food_favorite_list);
        FoodItemAdapter adapter = new FoodItemAdapter(getContext(), foodItems, foodSelectionManager);
        recyclerView.setAdapter(adapter);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorite_food, container, false);
    }
    
    private void getFavoriteFoodsFromDb() {
        AppDatabase db = AppDatabase.getInstance();
        List<FoodItemEntity> entities = FoodItemDatabaseManager.getFavoriteFoodsList(db.foodItemDao());
        if (entities == null) {
            return;
        }
        for (FoodItemEntity entity : entities) {
            foodItems.add(new FoodItem(entity));
        }
    }
}
