package com.app.calorietracker.ui.quiz.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.app.calorietracker.R;
import com.app.calorietracker.ui.quiz.QuizData;

public class WeightQuizFragment extends Fragment implements QuizFragment {
    
    private NumberPicker weightPicker_kg;
    private NumberPicker weightPicker_g;
    private final String[] multiples100 = {"0", "100", "200", "300", "400", "500", "600", "700", "800", "900"};
    
    public WeightQuizFragment() {
        // Required empty constructor
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    
    @Override
    public void onStart() {
        super.onStart();
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_quiz_weight, container, false);
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        weightPicker_kg = view.findViewById(R.id.quiz_weight_picker_kg);
        weightPicker_g = view.findViewById(R.id.quiz_weight_picker_g);
        setWeightPickersAttributes();
        setStartValue();
    }
    
    private void setWeightPickersAttributes() {
        float scale = getResources().getDisplayMetrics().density;
        int divHeight_dp = 4;
        int divHeight_px = (int) (divHeight_dp * scale + 0.5f);
        
        setKgPickerAttributes(divHeight_px);
        setGPickerAttributes(divHeight_px);
    }
    
    private void setKgPickerAttributes(int dividerHeight) {
        weightPicker_kg.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        weightPicker_kg.setWrapSelectorWheel(false);
        weightPicker_kg.setSelectionDividerHeight(dividerHeight);
        
        weightPicker_kg.setMinValue(30);
        weightPicker_kg.setMaxValue(300);
    }
    
    private void setGPickerAttributes(int dividerHeight) {
        weightPicker_g.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        weightPicker_g.setWrapSelectorWheel(false);
        weightPicker_g.setSelectionDividerHeight(dividerHeight);
        
        weightPicker_g.setMinValue(0);
        weightPicker_g.setMaxValue(multiples100.length - 1);
        weightPicker_g.setDisplayedValues(multiples100);
    }
    
    @Override
    public void writeSelectedValue(QuizData quizData) {
        int weightFraction_kg = weightPicker_kg.getValue();
        int weightFraction_g = Integer.parseInt(multiples100[weightPicker_g.getValue()]);
        int weightTotal_g = weightFraction_kg * 1000 + weightFraction_g;
        quizData.setWeight(weightTotal_g);
    }
    
    @Override
    public void setStartValue() {
        Bundle args = getArguments();
        if (args == null) {
            return;
        }
        
        QuizData quizData = (QuizData) args.get(ARGS_KEY);
        int weightTotal_g = quizData.getWeight();
        if (weightTotal_g < 0) {
            weightPicker_kg.setValue(60);
            weightPicker_g.setValue(0);
            return;
        }
        int weightFraction_kg = weightTotal_g / 1000;
        int weightFraction_g = weightTotal_g % 1000;
        
        weightPicker_kg.setValue(weightFraction_kg);
        
        String weightFractionString_g = String.valueOf(weightFraction_g);
        for (int i = 0; i < multiples100.length; i++) {
            if (multiples100[i].equals(weightFractionString_g)) {
                weightPicker_g.setValue(i);
            }
        }
    }
}
