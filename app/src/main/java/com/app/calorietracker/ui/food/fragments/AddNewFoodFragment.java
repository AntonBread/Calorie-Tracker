package com.app.calorietracker.ui.food.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.app.calorietracker.R;
import com.app.calorietracker.database.AppDatabase;
import com.app.calorietracker.database.foods.FoodItemEntity;
import com.app.calorietracker.ui.food.AddFoodActivity;
import com.app.calorietracker.ui.food.list.FoodItem;
import com.app.calorietracker.ui.food.list.FoodSelectionManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

public class AddNewFoodFragment extends Fragment {
    
    private ArrayList<EditText> inputFields;
    private AppCompatButton saveButton;
    private AppCompatCheckBox addCheck;
    private EditText portionSizeInput;
    private TextView portionSizeUnitText;
    
    private FoodSelectionManager foodSelectionManager;
    
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
        portionSizeInput = getView().findViewById(R.id.food_new_portion_size);
        portionSizeUnitText = getView().findViewById(R.id.food_new_portion_size_unit);
        
        inputFields = new ArrayList<>(Arrays.asList(nameInput, calsInput, carbsInput, fatInput, proteinInput));
        for (EditText input : inputFields) {
            input.addTextChangedListener(inputTextWatcher);
        }
        
        saveButton.setOnClickListener(saveButtonListener);
        addCheck.setOnCheckedChangeListener(addCheckListener);
        
        foodSelectionManager = ((AddFoodActivity) requireActivity()).getFoodSelectionManager();
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_new_food, container, false);
    }
    
    private void updateSaveButtonEnabled() {
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
            updateSaveButtonEnabled();
        }
    };
    
    private final View.OnClickListener saveButtonListener = v -> {
        FoodItemEntity foodItemEntity = createFoodItemEntity();
        AppDatabase db = AppDatabase.getInstance();
        try {
            long id = db.foodItemDao().insert(foodItemEntity).get();
            if (addCheck.isChecked()) {
                addFoodItemAsSelected(id);
            }
        }
        catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), R.string.food_new_save_fail, Toast.LENGTH_LONG).show();
        }
        
        clearInputFields();
    };
    
    private final CompoundButton.OnCheckedChangeListener addCheckListener = (v, checked) -> {
        if (checked) {
            portionSizeInput.setEnabled(true);
            portionSizeInput.setVisibility(View.VISIBLE);
            portionSizeUnitText.setVisibility(View.VISIBLE);
        }
        else {
            portionSizeInput.setText("");
            portionSizeInput.setEnabled(false);
            portionSizeInput.setVisibility(View.GONE);
            portionSizeUnitText.setVisibility(View.GONE);
        }
    };
    
    @SuppressLint("DefaultLocale")
    // Debug method. Should delete?
    private final View.OnClickListener debugPopulateDb = v -> {
        FoodItemEntity foodItemEntity = createFoodItemEntity();
        for (int i = 0; i < 20; i++) {
            foodItemEntity.setName(String.format("Еда %d", i));
            AppDatabase db = AppDatabase.getInstance();
            db.foodItemDao().insert(foodItemEntity);
            clearInputFields();
        }
    };
    
    private FoodItemEntity createFoodItemEntity() {
        // The order of EditText views is the same as when forming inputFields list
        String name = inputFields.get(0).getText().toString();
        int cals = Integer.parseInt(inputFields.get(1).getText().toString());
        float carbs = Float.parseFloat(inputFields.get(2).getText().toString());
        float fat = Float.parseFloat(inputFields.get(3).getText().toString());
        float protein = Float.parseFloat(inputFields.get(4).getText().toString());
        
        FoodItemEntity foodItemEntity = new FoodItemEntity();
        foodItemEntity.setName(name);
        foodItemEntity.setKcal(cals);
        foodItemEntity.setCarbs(carbs);
        foodItemEntity.setFat(fat);
        foodItemEntity.setProtein(protein);
        
        return foodItemEntity;
    }
    
    private void addFoodItemAsSelected(long id) throws ExecutionException, InterruptedException {
        FoodItemEntity entity = AppDatabase.getInstance().foodItemDao().getFoodById(id).get();
        FoodItem foodItem = new FoodItem(entity);
        int portionSize;
        try {
            portionSize = Integer.parseInt(portionSizeInput.getText().toString());
        }
        catch (NumberFormatException e) {
            // If portion size was not provided by user, default it to 100g
            portionSize = 100;
        }
        foodItem.setPortionSize(portionSize);
        foodSelectionManager.addItem(foodItem);
    }
    
    private void clearInputFields() {
        for (EditText input : inputFields) {
            input.getText().clear();
        }
    }
}