package com.app.calorietracker.food;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.calorietracker.R;
import com.app.calorietracker.food.list.FoodItem;
import com.app.calorietracker.food.list.FoodItemAdapter;

import java.util.ArrayList;

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
    
        foodItems.add(new FoodItem("Apple Juice", 46, 11, 0, 0, true));
        foodItems.add(new FoodItem("Macaroni", 157, 30, 1, 6, true));
        foodItems.add(new FoodItem("Overnight oats with PB&J", 184, 24, 7, 7, true));
        RecyclerView recyclerView = getView().findViewById(R.id.food_favorite_list);
        FoodItemAdapter adapter = new FoodItemAdapter(getContext(), foodItems);
        recyclerView.setAdapter(adapter);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorite_food, container, false);
    }
}