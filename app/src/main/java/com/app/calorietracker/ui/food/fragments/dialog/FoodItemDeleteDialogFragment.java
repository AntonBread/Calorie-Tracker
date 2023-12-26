package com.app.calorietracker.ui.food.fragments.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.app.calorietracker.R;

public class FoodItemDeleteDialogFragment extends DialogFragment {
    
    public static final String REQUEST_KEY = "delete_confirmation_request";
    public static final String RESULT_KEY = "delete_confirmation_result";
    
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext())
                .setView(R.layout.dialog_food_item_delete);
        
        return builder.create();
    }
    
    @Override
    public void onStart() {
        super.onStart();
        AlertDialog dialog = (AlertDialog) requireDialog();
        
        // Make dialog appear at the bottom of the screen
        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
        
        Button btnPos = dialog.findViewById(R.id.dialog_delete_confirm_btn_positive);
        btnPos.setOnClickListener(this::handlePositiveButtonClick);
    }
    
    private void handlePositiveButtonClick(View v) {
        Bundle result = new Bundle();
        result.putBoolean(RESULT_KEY, true);
        requireActivity().getSupportFragmentManager().setFragmentResult(REQUEST_KEY, result);
        dismiss();
    }

}
