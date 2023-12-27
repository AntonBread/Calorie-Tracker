package com.app.calorietracker.ui.food.fragments.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.DialogFragment;

import com.app.calorietracker.R;
import com.app.calorietracker.ui.food.list.FoodItem;

import java.util.ArrayList;

public class FoodItemEditDialogFragment extends DialogFragment {
    
    public static final String REQUEST_KEY = "edit_request";
    public static final String RESULT_KEY = "edit_result";
    public static final String ARGS_KEY = "edit_args";
    
    private long foodItemID;
    private boolean foodItemFavorite;
    
    private final ArrayList<EditText> editTexts = new ArrayList<>();
    private EditText nameEditText;
    private EditText calsEditText;
    private EditText carbsEditText;
    private EditText fatEditText;
    private EditText proteinEditText;
    
    private AppCompatButton btnPos;
    
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext())
                .setView(R.layout.fragment_add_new_food);
        
        return builder.create();
    }
    
    @Override
    public void onStart() {
        super.onStart();
        AlertDialog dialog = (AlertDialog) requireDialog();
        
        nameEditText = dialog.findViewById(R.id.food_new_name);
        calsEditText = dialog.findViewById(R.id.food_new_cals);
        carbsEditText = dialog.findViewById(R.id.food_new_carbs);
        fatEditText = dialog.findViewById(R.id.food_new_fat);
        proteinEditText = dialog.findViewById(R.id.food_new_protein);
        
        // "Add to current meal" checkbox is not needed in this context,
        // but its visibility is not set to "GONE" to preserve space between inputs and buttons
        dialog.findViewById(R.id.food_new_check_add).setVisibility(View.INVISIBLE);
        
        Bundle args = requireArguments();
        extractArgsData(args);
    
        btnPos = dialog.findViewById(R.id.food_new_btn_save);
        AppCompatButton btnCancel = dialog.findViewById(R.id.food_new_btn_cancel);
        btnPos.setOnClickListener(this::handlePositiveButtonClick);
        btnCancel.setOnClickListener(v -> dismiss());
        
        // Unlike AddNewFoodFragment, save button is enabled by default
        btnPos.setEnabled(true);
        btnPos.setText(getString(R.string.food_list_item_edit_btn_commit));
    
        initEditTextListeners();
    }
    
    private void extractArgsData(Bundle args) {
        FoodItem foodItem = (FoodItem) args.get(ARGS_KEY);
        
        foodItemID = foodItem.getId();
        foodItemFavorite = foodItem.isFavorite();
        
        nameEditText.setText(foodItem.getName());
        calsEditText.setText(String.valueOf(foodItem.getKcalPer100g()));
        carbsEditText.setText(String.valueOf(foodItem.getCarbsPer100g()));
        fatEditText.setText(String.valueOf(foodItem.getFatPer100g()));
        proteinEditText.setText(String.valueOf(foodItem.getProteinPer100g()));
    }
    
    private void initEditTextListeners() {
        editTexts.add(nameEditText);
        editTexts.add(calsEditText);
        editTexts.add(carbsEditText);
        editTexts.add(fatEditText);
        editTexts.add(proteinEditText);
    
        TextWatcher inputTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
    
            @Override
            public void afterTextChanged(Editable editable) {
                updateSaveButtonState();
            }
        };
        
        for (EditText input : editTexts) {
            input.addTextChangedListener(inputTextWatcher);
        }
    }
    
    private void updateSaveButtonState() {
        for (EditText input : editTexts) {
            if (input.getText().length() == 0) {
                btnPos.setEnabled(false);
                return;
            }
        }
        btnPos.setEnabled(true);
    }
    
    private void handlePositiveButtonClick(View v) {
        Bundle result = new Bundle();
        result.putSerializable(RESULT_KEY, extractEditTextData());
        requireActivity().getSupportFragmentManager().setFragmentResult(REQUEST_KEY, result);
        dismiss();
    }
    
    private FoodItem extractEditTextData() {
        String name = nameEditText.getText().toString().trim();
        int cals = Integer.parseInt(calsEditText.getText().toString().trim());
        float carbs = Float.parseFloat(carbsEditText.getText().toString().trim());
        float fat = Float.parseFloat(fatEditText.getText().toString().trim());
        float protein = Float.parseFloat(proteinEditText.getText().toString().trim());
        return new FoodItem(foodItemID, name, cals, carbs, fat, protein, foodItemFavorite);
    }
}
