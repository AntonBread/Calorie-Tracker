package com.app.calorietracker.ui.quiz.views;

import android.content.Context;
import android.graphics.drawable.TransitionDrawable;
import android.util.AttributeSet;
import android.widget.Checkable;

import androidx.constraintlayout.widget.ConstraintLayout;

public abstract class QuizRadioSelectionView extends ConstraintLayout implements Checkable {
    
    private boolean mChecked = false;
    
    public QuizRadioSelectionView(Context context) {
        super(context);
    }
    
    public QuizRadioSelectionView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    
    public QuizRadioSelectionView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    
    abstract void init(AttributeSet attrs, int defStyle);
    
    @Override
    public void setChecked(boolean checked) {
        if (mChecked == checked) {
            return;
        }
        this.mChecked = checked;
        updateViewUI();
    }
    
    @Override
    public boolean isChecked() {
        return mChecked;
    }
    
    @Override
    public void toggle() {
        setChecked(!mChecked);
    }
    
    void updateViewUI() {
        TransitionDrawable background = (TransitionDrawable) getBackground();
        if (mChecked) {
            background.startTransition(250);
        }
        else {
            background.resetTransition();
        }
    }
    
}
