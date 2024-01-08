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

public class HeightQuizFragment extends Fragment implements QuizFragment {
    
    private NumberPicker heightPicker;
    
    public HeightQuizFragment() {
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
        return inflater.inflate(R.layout.fragment_quiz_height, container, false);
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        heightPicker = view.findViewById(R.id.quiz_weight_picker_kg);
        setHeightPickerAttributes();
        setStartValue();
    }
    
    @Override
    public void writeSelectedValue(QuizData quizData) {
        int height = heightPicker.getValue();
        quizData.setHeight(height);
    }
    
    private void setHeightPickerAttributes() {
        // For some reason these attributes cannot be set in xml
        heightPicker.setMinValue(100);
        heightPicker.setMaxValue(250);
        heightPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        heightPicker.setWrapSelectorWheel(false);
        
        float scale = getResources().getDisplayMetrics().density;
        int divHeight_dp = 4;
        int divHeight_px = (int) (divHeight_dp * scale + 0.5f);
        heightPicker.setSelectionDividerHeight(divHeight_px);
    }
    
    @Override
    public void setStartValue() {
        Bundle args = getArguments();
        if (args == null) {
            return;
        }
    
        QuizData quizData = (QuizData) args.get(ARGS_KEY);
        int startHeight = quizData.getHeight();
        if (startHeight < 0) {
            heightPicker.setValue(170);
        }
        else {
            heightPicker.setValue(startHeight);
        }
    }
}
