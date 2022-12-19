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
    
        foodItems.add(new FoodItem("Oatmeal", 100, 35, 5, 3, true));
        foodItems.add(new FoodItem("Food", 999, 11, 22, 33, true));
        foodItems.add(new FoodItem("Banan", 111, 10, 10, 10, true));
        foodItems.add(new FoodItem("Pizza", 500, 50, 5, 0, true));
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