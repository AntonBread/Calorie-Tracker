package com.app.calorietracker.food;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.calorietracker.R;
import com.app.calorietracker.food.list.FoodItem;
import com.app.calorietracker.food.list.FoodItemAdapter;

import java.util.ArrayList;

public class SearchFoodFragment extends Fragment {
    
    ArrayList<FoodItem> foodItems = new ArrayList<>();
    
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
        
        foodItems.add(new FoodItem("Instant oatmeal", 91, 18, 1, 2, false));
        foodItems.add(new FoodItem("Multigrain oatmeal", 63, 14, 1, 1, false));
        foodItems.add(new FoodItem("Oatmeal muesli", 377, 67, 6, 11, false));
        foodItems.add(new FoodItem("Overnight oats with PB&J", 184, 24, 7, 7, true));
        foodItems.add(new FoodItem("Oat milk", 45, 6, 2, 1, false));
        RecyclerView recyclerView = getView().findViewById(R.id.food_search_list);
        FoodItemAdapter adapter = new FoodItemAdapter(getContext(), foodItems);
        recyclerView.setAdapter(adapter);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_food, container, false);
    }
}