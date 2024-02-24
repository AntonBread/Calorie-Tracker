package com.app.calorietracker.ui.stats.charting;

import android.content.Context;

import com.app.calorietracker.R;
import com.app.calorietracker.ui.stats.StatsActivity;
import com.github.mikephil.charting.formatter.ValueFormatter;

public class YAxisLabelFormatter extends ValueFormatter {
    final StatsActivity.StatType statType;
    final Context context;
    
    public YAxisLabelFormatter(StatsActivity.StatType statType, Context context) {
        this.statType = statType;
        this.context = context;
    }
    
    @Override
    public String getFormattedValue(float value) {
        if (value == 0f) {
            return "";
        }
        
        String label = String.valueOf((int) value);
        label += "\n";
        String unit;
        switch (statType) {
            case WEIGHT:
                unit = context.getString(R.string.stats_weight_chart_unit);
                break;
            case CALORIES:
                unit = context.getString(R.string.stats_calories_chart_unit);
                break;
            default:
                unit = "";
        }
        label += unit;
        return label;
    }
}
