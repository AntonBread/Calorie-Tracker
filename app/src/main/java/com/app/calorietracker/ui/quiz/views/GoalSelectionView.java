package com.app.calorietracker.ui.quiz.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;

import com.app.calorietracker.R;
import com.app.calorietracker.ui.quiz.QuizData.Goal;

public class GoalSelectionView extends QuizRadioSelectionView {
    
    private static final int GOAL_LOSS = 0;
    private static final int GOAL_MAINTAIN = 1;
    private static final int GOAL_GAIN = 2;
    
    private TextView mTitleTextView;
    private TransitionDrawable mIconDrawable;
    
    private int mGoalNum;
    
    public GoalSelectionView(Context context) {
        super(context);
        init(null, 0);
    }
    
    public GoalSelectionView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }
    
    public GoalSelectionView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }
    
    @Override
    void init(AttributeSet attrs, int defStyle) {
        View.inflate(getContext(), R.layout.view_goal_selection, this);
        
        // Find views
        mTitleTextView = findViewById(R.id.quiz_goal_title);
        ImageView iconImageView = findViewById(R.id.quiz_goal_icon);
        
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.GoalSelectionView, defStyle, 0);
        
        mGoalNum = a.getInt(R.styleable.GoalSelectionView_goal, GOAL_LOSS);
        String titleString = a.getString(R.styleable.GoalSelectionView_goalName);
        mIconDrawable = (TransitionDrawable) a.getDrawable(R.styleable.GoalSelectionView_goalIcon);
        
        mTitleTextView.setText(titleString);
        iconImageView.setImageDrawable(mIconDrawable);
        updateViewUI();
        
        a.recycle();
    }
    
    @Override
    void updateViewUI() {
        super.updateViewUI();
        
        Context context = getContext();
        Typeface tf;
        if (isChecked()) {
            tf = ResourcesCompat.getCachedFont(context, R.font.inter_bold);
            if (tf == null) {
                tf = ResourcesCompat.getFont(context, R.font.inter_bold);
            }
            // Smooth transition doesn't seem to play. No idea why
            mIconDrawable.startTransition(250);
        }
        else {
            tf = ResourcesCompat.getCachedFont(context, R.font.inter_medium);
            if (tf == null) {
                tf = ResourcesCompat.getFont(context, R.font.inter_medium);
            }
            mIconDrawable.resetTransition();
        }
        mTitleTextView.setTypeface(tf);
    }
    
    public Goal getSelectedGoal() {
        switch (mGoalNum) {
            case GOAL_LOSS:
                return Goal.LOSE;
            case GOAL_MAINTAIN:
                return Goal.MAINTAIN;
            case GOAL_GAIN:
                return Goal.GAIN;
            default:
                return Goal.NOT_SELECTED;
        }
    }
    
}