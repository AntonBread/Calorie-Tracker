package com.app.calorietracker.ui.quiz.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import java.util.ArrayList;

/*
 * Didn't find a way to achieve what I wanted with default RadioButton and RadioGroup,
 * so I had to create this rigid and bare-bones substitute for those views
 */
public class QuizRadioSelectionGroup extends LinearLayout {
    
    private final ArrayList<QuizRadioSelectionView> radioViews = new ArrayList<>();
    private int checkedId = -1;
    
    public QuizRadioSelectionGroup(Context context) {
        super(context);
    }
    
    public QuizRadioSelectionGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    
    public QuizRadioSelectionGroup(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    
    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (child instanceof QuizRadioSelectionView) {
            QuizRadioSelectionView selectionViewChild = (QuizRadioSelectionView) child;
            if (selectionViewChild.isChecked()) {
                if (checkedId == -1) {
                    checkedId = selectionViewChild.getId();
                }
                else {
                    selectionViewChild.setChecked(false);
                }
            }
            radioViews.add(selectionViewChild);
            selectionViewChild.setOnClickListener(this::handleRadioViewClick);
        }
        
        super.addView(child, index, params);
    }
    
    private void handleRadioViewClick(View v) {
        int clickedId = v.getId();
        if (clickedId == checkedId) {
            return;
        }
        
        clearCheck();
        setCheck(clickedId);
    }
    
    public int getCheckedId() {
        return checkedId;
    }
    
    @Nullable
    public QuizRadioSelectionView getCheckedView() {
        return radioViews.stream().filter(v -> v.getId() == checkedId).findAny().orElse(null);
    }
    
    public void clearCheck() {
        if (checkedId == -1) {
            return;
        }
        
        QuizRadioSelectionView checkedView =
                radioViews.stream().filter(v -> v.getId() == checkedId).findAny().orElse(null);
        
        if (checkedView == null) {
            return;
        }
        checkedView.setChecked(false);
        checkedId = -1;
    }
    
    public void setCheck(int id) {
        QuizRadioSelectionView view =
                radioViews.stream().filter(v -> v.getId() == id).findAny().orElse(null);
        
        if (view == null) {
            return;
        }
        view.setChecked(true);
        checkedId = id;
    }
}
