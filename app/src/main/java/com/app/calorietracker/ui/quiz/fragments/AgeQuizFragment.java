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

public class AgeQuizFragment extends Fragment implements QuizFragment {
    
    private NumberPicker agePicker;
    
    public AgeQuizFragment() {
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
        return inflater.inflate(R.layout.fragment_quiz_age, container, false);
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        agePicker = view.findViewById(R.id.quiz_age_picker);
        setAgePickerAttributes();
        setStartValue();
    }
    
    @Override
    public void writeSelectedValue(QuizData quizData) {
        int selectedAge = agePicker.getValue();
        quizData.setAge(selectedAge);
    }
    
    private void setAgePickerAttributes() {
        // For some reason these attributes cannot be set in xml
        agePicker.setMinValue(14);
        agePicker.setMaxValue(150);
        agePicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        agePicker.setWrapSelectorWheel(false);
        
        float scale = getResources().getDisplayMetrics().density;
        int divHeight_dp = 4;
        int divHeight_px = (int) (divHeight_dp * scale + 0.5f);
        agePicker.setSelectionDividerHeight(divHeight_px);
    }
    
    @Override
    public void setStartValue() {
        Bundle args = getArguments();
        if (args == null) {
            return;
        }
        
        QuizData quizData = (QuizData) args.get(ARGS_KEY);
        int startAge = quizData.getAge();
        if (startAge < 0) {
            agePicker.setValue(18);
        }
        else {
            agePicker.setValue(startAge);
        }
    }
    
}
