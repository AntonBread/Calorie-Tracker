package com.app.calorietracker.ui.quiz.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.app.calorietracker.R;
import com.app.calorietracker.ui.quiz.QuizData;
import com.app.calorietracker.ui.quiz.QuizData.Sex;

public class SexQuizFragment extends Fragment implements QuizFragment {
    
    private RadioButton buttonMale;
    private RadioButton buttonFemale;
    private RadioGroup radioGroup;
    
    public SexQuizFragment() {
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
        return inflater.inflate(R.layout.fragment_quiz_sex, container, false);
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        buttonMale = view.findViewById(R.id.quiz_gender_radio_male);
        buttonFemale = view.findViewById(R.id.quiz_gender_radio_female);
        radioGroup = view.findViewById(R.id.quiz_gender_radio_group);
        setStartValue();
    }
    
    @Override
    public void writeSelectedValue(QuizData quizData) {
        if (buttonFemale.isChecked()) {
            quizData.setSex(Sex.FEMALE);
        }
        else {
            quizData.setSex(Sex.MALE);
        }
    }
    
    @Override
    public void setStartValue() {
        Bundle args = getArguments();
        if (args == null) {
            return;
        }
        
        QuizData quizData = (QuizData) args.get(ARGS_KEY);
        
        radioGroup.clearCheck();
        if (quizData.getSex() == Sex.FEMALE) {
            buttonFemale.setChecked(true);
        }
        else {
            buttonMale.setChecked(true);
        }
    }
}
