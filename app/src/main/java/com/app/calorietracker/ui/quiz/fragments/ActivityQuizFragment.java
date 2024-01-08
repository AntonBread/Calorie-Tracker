package com.app.calorietracker.ui.quiz.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.app.calorietracker.R;
import com.app.calorietracker.ui.quiz.QuizData;
import com.app.calorietracker.ui.quiz.QuizData.ActivityLevel;
import com.app.calorietracker.ui.quiz.views.ActivityLevelSelectionView;
import com.app.calorietracker.ui.quiz.views.QuizRadioSelectionGroup;
import com.app.calorietracker.ui.quiz.views.QuizRadioSelectionView;

public class ActivityQuizFragment extends Fragment implements QuizFragment {
    
    QuizRadioSelectionGroup radioGroup;
    
    public ActivityQuizFragment() {
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
        return inflater.inflate(R.layout.fragment_quiz_activity, container, false);
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        radioGroup = view.findViewById(R.id.quiz_activity_radio_group);
        setStartValue();
    }
    
    @Override
    public void writeSelectedValue(QuizData quizData) {
        ActivityLevelSelectionView checkedView = (ActivityLevelSelectionView) radioGroup.getCheckedView();
        ActivityLevel selectedAl;
        if (checkedView == null) {
            selectedAl = ActivityLevel.NOT_SELECTED;
        }
        else {
            selectedAl = checkedView.getActivityLevel();
        }
        quizData.setActivityLevel(selectedAl);
    }
    
    @Override
    public void setStartValue() {
        Bundle args = getArguments();
        if (args == null) {
            return;
        }
        
        QuizData quizData = (QuizData) args.get(ARGS_KEY);
        ActivityLevel al = quizData.getActivityLevel();
        switch (al) {
            case VERY_LOW:
                radioGroup.setCheck(R.id.quiz_activity_level_1);
                break;
            case NOT_SELECTED:
            case LOW:
            default:
                radioGroup.setCheck(R.id.quiz_activity_level_2);
                break;
            case MEDIUM:
                radioGroup.setCheck(R.id.quiz_activity_level_3);
                break;
            case HIGH:
                radioGroup.setCheck(R.id.quiz_activity_level_4);
                break;
            case VERY_HIGH:
                radioGroup.setCheck(R.id.quiz_activity_level_5);
        }
    }
}
