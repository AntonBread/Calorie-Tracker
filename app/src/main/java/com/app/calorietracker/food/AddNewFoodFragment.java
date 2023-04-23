package com.app.calorietracker.food;

import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.app.calorietracker.R;
import com.app.calorietracker.database.AppDatabase;
import com.app.calorietracker.database.foods.FoodItemEntity;
import com.app.calorietracker.food.list.FoodItem;

import java.util.ArrayList;
import java.util.Arrays;

public class AddNewFoodFragment extends Fragment {
    
    private ArrayList<EditText> inputFields;
    private AppCompatButton saveButton;
    private AppCompatCheckBox addCheck;
    
    public AddNewFoodFragment() {
        // Required empty public constructor
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    
    @Override
    public void onStart() {
        super.onStart();
    
        EditText nameInput = getView().findViewById(R.id.food_new_name);
        EditText calsInput = getView().findViewById(R.id.food_new_cals);
        EditText carbsInput = getView().findViewById(R.id.food_new_carbs);
        EditText fatInput = getView().findViewById(R.id.food_new_fat);
        EditText proteinInput = getView().findViewById(R.id.food_new_protein);
    
        saveButton = getView().findViewById(R.id.food_new_btn_save);
        addCheck = getView().findViewById(R.id.food_new_check_add);
    
        inputFields = new ArrayList<>(Arrays.asList(nameInput, calsInput, carbsInput, fatInput, proteinInput));
        for (EditText input : inputFields) {
            input.addTextChangedListener(inputTextWatcher);
        }
        
        saveButton.setOnClickListener(saveButtonListener);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_new_food, container, false);
    }
    
    private void saveButtonEnabledUpdate() {
        for (EditText input : inputFields) {
            if (input.getText().length() == 0) {
                saveButton.setEnabled(false);
                addCheck.setChecked(false);
                addCheck.setEnabled(false);
                return;
            }
        }
        saveButton.setEnabled(true);
        addCheck.setEnabled(true);
    }
    
    private final TextWatcher inputTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        
        }
        
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        
        }
        
        @Override
        public void afterTextChanged(Editable s) {
            saveButtonEnabledUpdate();
        }
    };
    
    private final View.OnClickListener saveButtonListener = v -> {
        FoodItemEntity foodItemEntity = createFoodItemEntity();
        AppDatabase db = AppDatabase.getInstance();
        db.foodItemDao().insert(foodItemEntity);
        clearInputFields();
    };
    
    private FoodItemEntity createFoodItemEntity() {
        // The order is the same as when forming inputFields list
        String name = inputFields.get(0).getText().toString();
        int cals = Integer.parseInt(inputFields.get(1).getText().toString());
        int carbs = Integer.parseInt(inputFields.get(2).getText().toString());
        int fat = Integer.parseInt(inputFields.get(3).getText().toString());
        int protein = Integer.parseInt(inputFields.get(4).getText().toString());
    
        FoodItemEntity foodItemEntity = new FoodItemEntity();
        foodItemEntity.setName(name);
        foodItemEntity.setKcal(cals);
        foodItemEntity.setCarbs(carbs);
        foodItemEntity.setFat(fat);
        foodItemEntity.setProtein(protein);
        
        return foodItemEntity;
    }
    
    private void clearInputFields() {
        for (EditText input: inputFields) {
            input.getText().clear();
        }
    }
}