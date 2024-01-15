package com.app.calorietracker.ui.stats;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.app.calorietracker.R;
import com.app.calorietracker.ui.main.MainActivity;
import com.app.calorietracker.ui.settings.SettingsActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class StatsActivity extends AppCompatActivity {
    
    private enum StatType {
        WEIGHT,
        CALORIES
    }
    
    private enum TimeRange {
        WEEK,
        MONTH,
        MONTH_3,
        YEAR,
        ALL
    }
    
    private StatType mStatType;
    private TimeRange mTimeRange;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);
        
        initTypeSelector();
        initTimeSelector();
        
        initNavbar();
    }
    
    private void initNavbar() {
        BottomNavigationView navbar = findViewById(R.id.stats_navbar);
        navbar.setSelectedItemId(R.id.navigation_progress);
        navbar.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.navigation_main) {
                startActivity(new Intent(getBaseContext(), MainActivity.class));
                return true;
            }
            else if (id == R.id.navigation_settings) {
                startActivity(new Intent(getBaseContext(), SettingsActivity.class));
                return true;
            }
            else {
                return true;
            }
        });
    }
    
    private void initTypeSelector() {
        RadioGroup typeRadioGroup = findViewById(R.id.stats_type_radio_group);
        typeRadioGroup.setOnCheckedChangeListener((radioGroup, checkedId) -> {
            if (checkedId == R.id.stats_type_calories) {
                mStatType = StatType.CALORIES;
            }
            // Weight is the default stat type option
            else {
                mStatType = StatType.WEIGHT;
            }
            updateDisplayedStats();
        });
        ((RadioButton) findViewById(R.id.stats_type_weight)).setChecked(true);
    }
    
    private void initTimeSelector() {
        RadioGroup timeRadioGroup = findViewById(R.id.stats_time_radio_group);
        timeRadioGroup.setOnCheckedChangeListener((radioGroup, checkedId) -> {
            if (checkedId == R.id.stats_time_month) {
                mTimeRange = TimeRange.MONTH;
            }
            else if (checkedId == R.id.stats_time_month_3) {
                mTimeRange = TimeRange.MONTH_3;
            }
            else if (checkedId == R.id.stats_time_year) {
                mTimeRange = TimeRange.YEAR;
            }
            else if (checkedId == R.id.stats_time_all) {
                mTimeRange = TimeRange.ALL;
            }
            // 1 Week is the default time range option
            else {
                mTimeRange = TimeRange.WEEK;
            }
            updateDisplayedStats();
        });
        ((RadioButton) findViewById(R.id.stats_time_week)).setChecked(true);
    }
    
    private void updateDisplayedStats() {
        if (mStatType == null || mTimeRange == null) {
            return;
        }
        Log.d("DEBUG", "UPDATED");
    }
}