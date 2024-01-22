package com.app.calorietracker.ui.stats;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;

import com.app.calorietracker.R;
import com.app.calorietracker.database.AppDatabase;
import com.app.calorietracker.database.user.UserDiaryEntity;
import com.app.calorietracker.ui.main.MainActivity;
import com.app.calorietracker.ui.settings.SettingsActivity;
import com.app.calorietracker.ui.stats.StatsCalculator.WeightChangeSpeedInterval;
import com.app.calorietracker.ui.stats.views.StatTextView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

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
    
    private boolean mTimeRangeChanged;
    
    private StatType mStatType;
    private TimeRange mTimeRange;
    
    private List<UserDiaryEntity> dataList;
    
    private StatsCalculator calculator;
    private StatsStringFormatter stringFormatter;
    
    private StatTextView weightDeltaView;
    private StatTextView weightSpeedView;
    private StatTextView weightBmiView;
    private StatTextView caloriesTotalView;
    private StatTextView caloriesAvgView;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);
        
        weightDeltaView = findViewById(R.id.stats_text_weight_delta);
        weightSpeedView = findViewById(R.id.stats_text_weight_speed);
        weightBmiView = findViewById(R.id.stats_text_weight_bmi);
        caloriesTotalView = findViewById(R.id.stats_text_calories_total);
        caloriesAvgView = findViewById(R.id.stats_text_calories_avg);
        
        calculator = new StatsCalculator(this);
        stringFormatter = new StatsStringFormatter(this);
        
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
            mTimeRangeChanged = true;
            updateDisplayedStats();
        });
        ((RadioButton) findViewById(R.id.stats_time_week)).setChecked(true);
    }
    
    private void updateDisplayedStats() {
        if (mStatType == null || mTimeRange == null) {
            return;
        }
        if (mTimeRangeChanged) {
            dataList = fetchDiaryData();
            if (dataList == null || dataList.size() == 0) {
                displayDataListEmptyInfo();
                return;
            }
            mTimeRangeChanged = false;
        }
        updateStatsChart();
        updateStatsText();
    }
    
    @Nullable
    private List<UserDiaryEntity> fetchDiaryData() {
        LocalDate dateEnd = LocalDate.now();
        LocalDate dateStart;
        switch (mTimeRange) {
            case WEEK:
            default:
                dateStart = dateEnd.minusWeeks(1);
                break;
            case MONTH:
                dateStart = dateEnd.minusMonths(1);
                break;
            case MONTH_3:
                dateStart = dateEnd.minusMonths(3);
                break;
            case YEAR:
                dateStart = dateEnd.minusYears(1);
                break;
            case ALL:
                dateStart = LocalDate.MIN;
        }
        try {
            return AppDatabase.getInstance().userDiaryDao().getDiaryEntriesRange(dateStart, dateEnd).get();
        }
        catch (ExecutionException | InterruptedException e) {
            return null;
        }
    }
    
    private void updateStatsChart() {
        // TODO: fix method stub
    }
    
    private void updateStatsText() {
        updateTextViewsVisibility();
        if (mStatType == StatType.CALORIES) {
            updateCaloriesTextViews();
        }
        else {
            updateWeightTextViews();
        }
    }
    
    private void updateTextViewsVisibility() {
        if (mStatType == StatType.CALORIES) {
            weightDeltaView.setVisibility(View.GONE);
            weightSpeedView.setVisibility(View.GONE);
            weightBmiView.setVisibility(View.GONE);
            
            caloriesTotalView.setVisibility(View.VISIBLE);
            caloriesAvgView.setVisibility(View.VISIBLE);
        }
        else {
            caloriesTotalView.setVisibility(View.GONE);
            caloriesAvgView.setVisibility(View.GONE);
            
            weightDeltaView.setVisibility(View.VISIBLE);
            weightSpeedView.setVisibility(View.VISIBLE);
            weightBmiView.setVisibility(View.VISIBLE);
        }
    }
    
    private void updateCaloriesTextViews() {
        int caloriesTotal = calculator.totalCalories(dataList);
        int caloriesAvg = calculator.averageCalories(dataList, caloriesTotal);
        
        String caloriesTotalText = stringFormatter.getCaloriesTotalString(caloriesTotal);
        String caloriesAvgText = stringFormatter.getCaloriesAvgString(caloriesAvg);
        
        caloriesTotalView.setValueText(caloriesTotalText);
        caloriesAvgView.setValueText(caloriesAvgText);
    }
    
    private void updateWeightTextViews() {
        float delta = calculator.weightDelta(dataList);
        WeightChangeSpeedInterval interval = calculator.changeSpeedInterval();
        float changeSpeed = calculator.weightChangeSpeed(delta, interval);
        float bmi = calculator.currentBodyMassIndex(dataList);
        
        updateWeightDeltaTextView(delta);
        updateWeightChangeSpeedTextView(changeSpeed, interval);
        updateWeightBMITextView(bmi);
    }
    
    private void updateWeightDeltaTextView(float delta) {
        String deltaText = stringFormatter.getWeightDeltaString(delta);
        String deltaTitle = stringFormatter.getWeightDeltaTitle(delta);
        Drawable deltaIcon = delta > 0 ?
                AppCompatResources.getDrawable(this, R.drawable.ic_weight_delta_gain) :
                AppCompatResources.getDrawable(this, R.drawable.ic_weight_delta_loss);
        
        if (deltaText == null) {
            weightDeltaView.setVisibility(View.GONE);
            return;
        }
        
        weightDeltaView.setTitleText(deltaTitle);
        
        if (delta == 0) {
            weightDeltaView.setValueVisibility(false);
            return;
        }
        
        weightDeltaView.setValueVisibility(true);
        weightDeltaView.setValueText(deltaText);
        weightDeltaView.setIcon(deltaIcon);
    }
    
    private void updateWeightChangeSpeedTextView(float changeSpeed, WeightChangeSpeedInterval interval) {
        String changeSpeedText = stringFormatter.getWeightChangeSpeedString(changeSpeed, interval);
        
        if (changeSpeedText == null) {
            weightSpeedView.setVisibility(View.GONE);
            return;
        }
        
        weightSpeedView.setValueText(changeSpeedText);
    }
    
    private void updateWeightBMITextView(float bmi) {
        String bmiText = stringFormatter.getBMIString(bmi);
        
        if (bmiText == null) {
            weightBmiView.setVisibility(View.GONE);
            return;
        }
        
        weightBmiView.setValueText(bmiText);
    }
    
    private void displayDataListEmptyInfo() {
        // TODO: fix method stub
    }
}
