package com.app.calorietracker.ui.main.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Button;
import android.widget.NumberPicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.app.calorietracker.R;
import com.app.calorietracker.ui.main.DateFormatter;

import java.time.LocalDate;

public class ChangeWeightDialogFragment extends DialogFragment {
    
    public static final String REQUEST_KEY = "weight_dialog_request";
    public static final String ARGS_DATE_KEY = "weight_dialog_args_date";
    public static final String ARGS_WEIGHT_KEY = "weight_dialog_args_weight";
    public static final String RESULT_KEY = "weight_dialog_result";
    
    private NumberPicker weightPicker_kg;
    private NumberPicker weightPicker_g;
    private final String[] multiples100 = {"0", "100", "200", "300", "400", "500", "600", "700", "800", "900"};
    
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext())
                .setView(R.layout.fragment_quiz_weight)
                // Have to set title with whatever non-empty string during builder stage,
                // otherwise, setting correct title using bundle args later won't work
                .setTitle("s")
                .setNegativeButton(android.R.string.cancel, this::onNegativeButtonClick)
                .setPositiveButton(R.string.main_weight_dialog_btn_positive, this::onPositiveButtonClick);
        
        return builder.create();
    }
    
    @Override
    public void onStart() {
        super.onStart();
        
        Bundle args = requireArguments();
        LocalDate date = (LocalDate) args.get(ARGS_DATE_KEY);
        int oldWeight = args.getInt(ARGS_WEIGHT_KEY);
        
        AlertDialog dialog = (AlertDialog) requireDialog();
    
        weightPicker_kg = dialog.findViewById(R.id.quiz_weight_picker_kg);
        weightPicker_g = dialog.findViewById(R.id.quiz_weight_picker_g);
        
        setDialogTitle(dialog, date);
        setNegativeButtonColor(dialog);
        
        setNumberPickersAttrs();
        setNumberPickersStartValue(oldWeight);
    }
    
    private void setDialogTitle(AlertDialog dialog, LocalDate date) {
        String dateString = DateFormatter.getWeightDialogDateText(date);
        String title = getString(R.string.main_weight_dialog_title, dateString);
        dialog.setTitle(title);
    }
    
    private void setNegativeButtonColor(AlertDialog dialog) {
        int color = getResources().getColor(R.color.text_secondary, requireContext().getTheme());
        Button btn = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        btn.setTextColor(color);
    }
    
    private void setNumberPickersAttrs() {
        weightPicker_kg.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        weightPicker_kg.setWrapSelectorWheel(false);
    
        weightPicker_kg.setMinValue(30);
        weightPicker_kg.setMaxValue(300);
    
        weightPicker_g.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        weightPicker_g.setWrapSelectorWheel(false);
    
        weightPicker_g.setMinValue(0);
        weightPicker_g.setMaxValue(multiples100.length - 1);
        weightPicker_g.setDisplayedValues(multiples100);
    }
    
    private void setNumberPickersStartValue(int weight) {
        if (weight <= 0) {
            weightPicker_kg.setValue(60);
            weightPicker_g.setValue(0);
            return;
        }
        
        int weightFraction_kg = weight / 1000;
        int weightFraction_g = weight % 1000;
    
        weightPicker_kg.setValue(weightFraction_kg);
        
        String weightFractionString_g = String.valueOf(weightFraction_g);
        for (int i = 0; i < multiples100.length; i++) {
            if (multiples100[i].equals(weightFractionString_g)) {
                weightPicker_g.setValue(i);
            }
        }
    }
    
    private int getSelectedWeight() {
        int weightFraction_kg = weightPicker_kg.getValue();
        int weightFraction_g = Integer.parseInt(multiples100[weightPicker_g.getValue()]);
        
        return weightFraction_kg * 1000 + weightFraction_g;
    }
    
    private void onNegativeButtonClick(DialogInterface dialogInterface, int i) {
        dismiss();
    }
    
    private void onPositiveButtonClick(DialogInterface dialogInterface, int i) {
        int newWeight = getSelectedWeight();
        
        Bundle result = new Bundle();
        result.putInt(RESULT_KEY, newWeight);
        requireActivity().getSupportFragmentManager().setFragmentResult(REQUEST_KEY, result);
        dismiss();
    }
}
