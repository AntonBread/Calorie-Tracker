package com.app.calorietracker.ui.quiz.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;

import com.app.calorietracker.R;
import com.app.calorietracker.ui.quiz.QuizData.ActivityLevel;

public class ActivityLevelSelectionView extends QuizRadioSelectionView {
    
    private static final int AL_VERY_LOW = 0;
    private static final int AL_LOW = 1;
    private static final int AL_MEDIUM = 2;
    private static final int AL_HIGH = 3;
    private static final int AL_VERY_HIGH = 4;
    
    private TextView mTitleTextView;
    private TextView mDescriptionTextView;
    
    private int mActivityLevelNum;
    
    public ActivityLevelSelectionView(Context context) {
        super(context);
        init(null, 0);
    }
    
    public ActivityLevelSelectionView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }
    
    public ActivityLevelSelectionView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }
    
    @Override
    void init(AttributeSet attrs, int defStyle) {
        View.inflate(getContext(), R.layout.view_activity_level_selection, this);
        
        // Find views
        mTitleTextView = findViewById(R.id.quiz_activity_level_title);
        mDescriptionTextView = findViewById(R.id.quiz_activity_level_description);
        ImageView iconImageView = findViewById(R.id.quiz_activity_level_icon);
        
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.ActivityLevelSelectionView, defStyle, 0);
        
        mActivityLevelNum = a.getInt(R.styleable.ActivityLevelSelectionView_activityLevel, AL_VERY_LOW);
        String titleString = a.getString(R.styleable.ActivityLevelSelectionView_levelName);
        String descriptionString = a.getString(R.styleable.ActivityLevelSelectionView_levelDescription);
        Drawable iconDrawable = a.getDrawable(R.styleable.ActivityLevelSelectionView_levelIcon);
        
        mTitleTextView.setText(titleString);
        mDescriptionTextView.setText(descriptionString);
        iconImageView.setImageDrawable(iconDrawable);
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
        }
        else {
            tf = ResourcesCompat.getCachedFont(context, R.font.inter_medium);
            if (tf == null) {
                tf = ResourcesCompat.getFont(context, R.font.inter_medium);
            }
        }
        mTitleTextView.setTypeface(tf);
        mDescriptionTextView.setTypeface(tf);
    }
    
    public ActivityLevel getActivityLevel() {
        switch (mActivityLevelNum) {
            case AL_VERY_LOW:
                return ActivityLevel.VERY_LOW;
            case AL_LOW:
                return ActivityLevel.LOW;
            case AL_MEDIUM:
                return ActivityLevel.MEDIUM;
            case AL_HIGH:
                return ActivityLevel.HIGH;
            case AL_VERY_HIGH:
                return ActivityLevel.VERY_HIGH;
            default:
                return ActivityLevel.NOT_SELECTED;
        }
    }
}