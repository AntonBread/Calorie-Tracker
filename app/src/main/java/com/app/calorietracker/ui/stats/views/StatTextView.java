package com.app.calorietracker.ui.stats.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.app.calorietracker.R;

public class StatTextView extends ConstraintLayout {
    
    private String mTitleString;
    private TextView mTitleTextView;
    
    private String mValueString;
    private TextView mValueTextView;
    
    private Drawable mIconDrawable;
    private ImageView mIconImageView;
    
    public StatTextView(Context context) {
        super(context);
        init(null, 0);
    }
    
    public StatTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }
    
    public StatTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }
    
    private void init(AttributeSet attrs, int defStyle) {
        View.inflate(getContext(), R.layout.view_stat_text, this);
        
        // Find child views
        mTitleTextView = findViewById(R.id.stats_text_view_title);
        mIconImageView = findViewById(R.id.stats_text_view_icon);
        mValueTextView = findViewById(R.id.stats_text_view_value);
        
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.StatTextView, defStyle, 0);
        
        mTitleString = a.getString(R.styleable.StatTextView_statTitle);
        mIconDrawable = a.getDrawable(R.styleable.StatTextView_statIcon);
        
        if (a.hasValue(R.styleable.StatTextView_statValue)) {
            mValueString = a.getString(R.styleable.StatTextView_statValue);
        }
        else {
            mValueString = "";
        }
        
        // Set views' content
        mTitleTextView.setText(mTitleString);
        mIconImageView.setImageDrawable(mIconDrawable);
        mValueTextView.setText(mValueString);
        
        a.recycle();
    }
    
    public void setValueText(String valueText) {
        mValueString = valueText;
        mValueTextView.setText(mValueString);
    }
    
    public void setTitleText(String titleText) {
        mTitleString = titleText;
        mTitleTextView.setText(mTitleString);
    }
    
    public void setIcon(Drawable icon) {
        mIconDrawable = icon;
        mIconImageView.setImageDrawable(mIconDrawable);
    }
    
    public void setValueVisibility(boolean visible) {
        if (visible) {
            mValueTextView.setVisibility(View.VISIBLE);
            mIconImageView.setVisibility(View.VISIBLE);
        }
        else {
            mValueTextView.setVisibility(View.GONE);
            mIconImageView.setVisibility(View.GONE);
        }
    }
    
}