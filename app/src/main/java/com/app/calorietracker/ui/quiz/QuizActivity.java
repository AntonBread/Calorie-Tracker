package com.app.calorietracker.ui.quiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.app.calorietracker.R;
import com.app.calorietracker.ui.quiz.fragments.ActivityQuizFragment;
import com.app.calorietracker.ui.quiz.fragments.AgeQuizFragment;
import com.app.calorietracker.ui.quiz.fragments.SexQuizFragment;
import com.app.calorietracker.ui.quiz.fragments.GoalQuizFragment;
import com.app.calorietracker.ui.quiz.fragments.HeightQuizFragment;
import com.app.calorietracker.ui.quiz.fragments.QuizFragment;
import com.app.calorietracker.ui.quiz.fragments.ResultQuizFragment;
import com.app.calorietracker.ui.quiz.fragments.WeightQuizFragment;
import com.app.calorietracker.ui.quiz.fragments.dialog.QuizSkipDialogFragment;
import com.app.calorietracker.ui.settings.SettingsActivity;

public class QuizActivity extends AppCompatActivity {
    
    private enum QuizStage {
        AGE(0),
        GENDER(1),
        HEIGHT(2),
        WEIGHT(3),
        ACTIVITY(4),
        GOAL(5),
        RESULT(6);
        
        private final int stageNumber;
        
        QuizStage(int stageNumber) {
            this.stageNumber = stageNumber;
        }
        
        public int getStageNumber() {
            return stageNumber;
        }
    }
    
    private int stage;
    private QuizData quizData;
    
    private AppCompatButton buttonNext;
    private AppCompatButton buttonPrev;
    private AppCompatButton buttonSkip;
    
    private TextView questionTitle;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        
        stage = 0;
        quizData = new QuizData();
        questionTitle = findViewById(R.id.quiz_body_title);
        buttonNext = findViewById(R.id.quiz_body_btn_next);
        buttonPrev = findViewById(R.id.quiz_body_btn_prev);
        buttonSkip = findViewById(R.id.quiz_btn_skip);
        
        updateQuizBody();
    }
    
    private void updateQuizBody() {
        updateTitleText();
        updateQuizFragment();
        updateButtonState();
    }
    
    private void updateTitleText() {
        for (QuizStage quizStage : QuizStage.values()) {
            if (quizStage.getStageNumber() == stage) {
                questionTitle.setText(getTitleText(quizStage));
                return;
            }
        }
    }
    
    private String getTitleText(QuizStage quizStage) {
        switch (quizStage) {
            case AGE:
            default:
                return getString(R.string.quiz_title_age);
            case GENDER:
                return getString(R.string.quiz_title_sex);
            case HEIGHT:
                return getString(R.string.quiz_title_height);
            case WEIGHT:
                return getString(R.string.quiz_title_weight);
            case ACTIVITY:
                return getString(R.string.quiz_title_activity);
            case GOAL:
                return getString(R.string.quiz_title_goal);
            case RESULT:
                return getString(R.string.quiz_title_result);
        }
    }
    
    private void updateQuizFragment() {
        Bundle args = new Bundle();
        args.putSerializable(QuizFragment.ARGS_KEY, quizData);
        for (QuizStage quizStage : QuizStage.values()) {
            if (quizStage.getStageNumber() == stage) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.quiz_body_fragment_container, getQuizFragment(quizStage), args)
                        .commit();
            }
        }
    }
    
    private Class<? extends Fragment> getQuizFragment(QuizStage quizStage) {
        switch (quizStage) {
            case AGE:
            default:
                return AgeQuizFragment.class;
            case GENDER:
                return SexQuizFragment.class;
            case HEIGHT:
                return HeightQuizFragment.class;
            case WEIGHT:
                return WeightQuizFragment.class;
            case ACTIVITY:
                return ActivityQuizFragment.class;
            case GOAL:
                return GoalQuizFragment.class;
            case RESULT:
                return ResultQuizFragment.class;
        }
    }
    
    private void updateButtonState() {
        if (stage == 0) {
            buttonPrev.setEnabled(false);
            buttonPrev.setVisibility(View.GONE);
            return;
        }
        else {
            buttonPrev.setEnabled(true);
            buttonPrev.setVisibility(View.VISIBLE);
        }
    
        if (stage == (QuizStage.RESULT.getStageNumber() - 1)) {
            buttonNext.setText(R.string.quiz_btn_finish);
        }
        else if (stage == QuizStage.RESULT.getStageNumber()) {
            buttonNext.setText(R.string.quiz_btn_confirm_result);
            buttonNext.setOnClickListener(this::handleResultConfirmButtonClick);
            
            buttonPrev.setEnabled(false);
            buttonPrev.setVisibility(View.GONE);
            
            buttonSkip.setEnabled(false);
            buttonSkip.setVisibility(View.GONE);
        }
        else {
            buttonNext.setText(R.string.quiz_btn_next);
            buttonNext.setOnClickListener(this::handleNextButtonClick);
        }
    }
    
    public void handlePreviousButtonClick(View v) {
        if (stage <= 0) {
            return;
        }
        stage--;
        updateQuizBody();
    }
    
    public void handleNextButtonClick(View v) {
        saveStageData();
        if (stage >= QuizStage.RESULT.getStageNumber()) {
            return;
        }
        stage++;
        updateQuizBody();
    }
    
    public void handleSkipButtonClick(View v) {
        FragmentManager fm = getSupportFragmentManager();
        fm.clearFragmentResultListener(QuizSkipDialogFragment.REQUEST_KEY);
        fm.setFragmentResultListener(QuizSkipDialogFragment.REQUEST_KEY,
                                     this,
                                     this::handleSkipDialogFragmentResult);
        new QuizSkipDialogFragment().show(fm, null);
    }
    
    public void handleSkipDialogFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
        boolean skip = result.getBoolean(QuizSkipDialogFragment.RESULT_KEY);
        if (!skip) {
            return;
        }
        
        stage = QuizStage.RESULT.getStageNumber();
        quizData.setSkipped(true);
        
        // Cut the use case that utilized skipped boolean,
        // but left the value regardless.
        // Quiz activity just ends after skip confirmation
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
        finish();
    }
    
    public void handleResultConfirmButtonClick(View v) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
        finish();
    }
    
    private void saveStageData() {
        QuizFragment qf =
                (QuizFragment) getSupportFragmentManager().findFragmentById(R.id.quiz_body_fragment_container);
        assert qf != null;
        qf.writeSelectedValue(quizData);
    }
    
}