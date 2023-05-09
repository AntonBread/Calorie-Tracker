package com.app.calorietracker.ui.food.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.calorietracker.R;
import com.app.calorietracker.ui.food.AddFoodActivity;
import com.app.calorietracker.ui.food.list.FoodItem;
import com.app.calorietracker.ui.food.list.FoodItemAdapter;
import com.app.calorietracker.ui.food.list.FoodSelectionManager;

import java.util.ArrayList;

public class HistoryFoodFragment extends Fragment {
    
    ArrayList<FoodItem> foodItems = new ArrayList<>();
    
    public HistoryFoodFragment() {
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
        
        RecyclerView recyclerView = getView().findViewById(R.id.food_history_list);
        FoodItemAdapter adapter = new FoodItemAdapter(getContext(), foodItems, foodSelectionManager);
        recyclerView.setAdapter(adapter);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history_food, container, false);
    }
}