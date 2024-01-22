package com.app.calorietracker.ui.stats;

import android.content.Context;

import androidx.annotation.Nullable;

import com.app.calorietracker.R;
import com.app.calorietracker.ui.stats.StatsCalculator.WeightChangeSpeedInterval;

import java.util.Locale;

public class StatsStringFormatter {
    
    private final Context context;
    
    public StatsStringFormatter(Context context) {
        this.context = context;
    }
    
    public String getCaloriesTotalString(int total) {
        return context.getString(R.string.stats_calories_value_format, total);
    }
    
    // Different methods with same body for potential changes in the future
    public String getCaloriesAvgString(int avg) {
        return context.getString(R.string.stats_calories_value_format, avg);
    }
    
    @Nullable
    public String getWeightDeltaString(float delta) {
        if (delta == Float.MIN_VALUE) {
            return null;
        }
        String fmt = context.getString(R.string.stats_weight_value_delta_format);
        return String.format(Locale.US, fmt, Math.abs(delta));
    }
    
    public String getWeightDeltaTitle(float delta) {
        if (delta > 0) {
            return context.getString(R.string.stats_weight_title_delta_gain);
        }
        else if (delta < 0) {
            return context.getString(R.string.stats_weight_title_delta_loss);
        }
        else {
            return context.getString(R.string.stats_weight_title_delta_no);
        }
    }
    
    @Nullable
    public String getWeightChangeSpeedString(float changeSpeed, WeightChangeSpeedInterval interval) {
        if (changeSpeed == Float.MIN_VALUE || interval == WeightChangeSpeedInterval.NONE) {
            return null;
        }
        String fmt = context.getString(R.string.stats_weight_value_speed_format);
        String s = String.format(Locale.US, fmt, changeSpeed);
        switch (interval) {
            case WEEK:
                s += context.getString(R.string.stats_weight_value_speed_suffix_week);
                break;
            case MONTH:
                s += context.getString(R.string.stats_weight_value_speed_suffix_month);
        }
        return s;
    }
    
    @Nullable
    public String getBMIString(float bmi) {
        if (bmi == Float.MIN_VALUE) {
            return null;
        }
        return String.format(Locale.US, "%.1f", bmi);
    }
}
