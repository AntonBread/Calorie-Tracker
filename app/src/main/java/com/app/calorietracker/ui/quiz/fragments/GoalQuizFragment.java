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
import com.app.calorietracker.ui.quiz.QuizData.Goal;
import com.app.calorietracker.ui.quiz.views.GoalSelectionView;
import com.app.calorietracker.ui.quiz.views.QuizRadioSelectionGroup;

public class GoalQuizFragment extends Fragment implements QuizFragment {
    
    private QuizRadioSelectionGroup radioGroup;
    
    public GoalQuizFragment() {
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
        return inflater.inflate(R.layout.fragment_quiz_goal, container, false);
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        radioGroup = view.findViewById(R.id.quiz_goal_radio_group);
        setStartValue();
    }
    
    @Override
    public void writeSelectedValue(QuizData quizData) {
        GoalSelectionView checkedView = (GoalSelectionView) radioGroup.getCheckedView();
        Goal selectedGoal;
        if (checkedView == null) {
            selectedGoal = Goal.NOT_SELECTED;
        }
        else {
            selectedGoal = checkedView.getSelectedGoal();
        }
        quizData.setGoal(selectedGoal);
    }
    
    @Override
    public void setStartValue() {
        Bundle args = getArguments();
        if (args == null) {
            return;
        }
        
        QuizData quizData = (QuizData) args.get(ARGS_KEY);
        Goal goal = quizData.getGoal();
        switch (goal) {
            case LOSE:
                radioGroup.setCheck(R.id.quiz_goal_1);
                break;
            case NOT_SELECTED:
            case MAINTAIN:
            default:
                radioGroup.setCheck(R.id.quiz_goal_2);
                break;
            case GAIN:
                radioGroup.setCheck(R.id.quiz_goal_3);
        }
    }
}
