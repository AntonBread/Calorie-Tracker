package com.app.calorietracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.room.Room;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.calorietracker.database.AppDatabase;
import com.app.calorietracker.database.user.UserDiaryEntity;
import com.app.calorietracker.food.AddFoodActivity;
import com.app.calorietracker.utils.ChartUtils;
import com.app.calorietracker.utils.DateUtils;
import com.github.mikephil.charting.charts.PieChart;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.time.LocalDate;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.ExecutionException;


public class MainActivity extends AppCompatActivity {
    
    // TODO: cleanup db related code
    
    private LocalDate selectedDate;
    private Locale locale;
    
    private UserDiaryEntity currentEntry;
    
    private AppDatabase db;
    
    private final int CALORIE_BASELINE = 2500;
    private final int CARBS_BASELINE = 200;
    private final int FAT_BASELINE = 80;
    private final int PROTEIN_BASELINE = 150;
    private final int WATER_BASELINE = 3000;
    private final int WATER_STEP = 100;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);     // Force light theme
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        locale = new Locale("en");
    
        db = Room.inMemoryDatabaseBuilder(getApplicationContext(), AppDatabase.class).build();
        
        initDateSelector();
        
        findViewById(R.id.main_btn_breakfast_add).setOnClickListener(v -> {
            startActivity(new Intent(this, AddFoodActivity.class));
        });
        
        BottomNavigationView bottomNav = findViewById(R.id.main_navbar);
        bottomNav.setSelectedItemId(R.id.navigation_main);
    }
    
    private void initDateSelector() {
        selectedDate = LocalDate.now();
        DateUtils.init(getString(R.string.main_date_today), getString(R.string.main_date_yesterday), locale);
        updateDate();
        initDateButtons();
    }
    
    private void initDateButtons() {
        AppCompatImageButton btnPrev = findViewById(R.id.main_btn_date_prev);
        AppCompatImageButton btnNext = findViewById(R.id.main_btn_date_next);
        TextView dateView = findViewById(R.id.main_text_date);
        
        btnPrev.setOnClickListener(v -> {
            selectedDate = selectedDate.minusDays(1);
            updateDate();
        });
        
        btnNext.setOnClickListener(v -> {
            selectedDate = selectedDate.plusDays(1);
            updateDate();
        });
        
        // Manual date picking via dialog window on text click
        // LocalDate and DatePickerDialog have difference of 1 between months
        dateView.setOnClickListener(v -> new DatePickerDialog(this, datePickerDialogListener,
                                                              selectedDate.getYear(),
                                                              selectedDate.getMonthValue() - 1,
                                                              selectedDate.getDayOfMonth()).show());
    }
    
    DatePickerDialog.OnDateSetListener datePickerDialogListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            selectedDate = LocalDate.of(year, (month + 1),
                                        dayOfMonth);
            updateSelectedDateText();
        }
    };
    
    private void updateDate() {
        updateSelectedDateText();
        UserDiaryEntity entity = new UserDiaryEntity();
        entity.set_date(selectedDate);
        Random rnd = new Random();
        entity.setCalories(rnd.nextInt(2500));
        entity.setCarbs_mg(rnd.nextInt(200));
        entity.setFat_mg(rnd.nextInt(80));
        entity.setProtein_mg(rnd.nextInt(150));
        try {
            long id = db.userDiaryDao().insert(entity).get();
            Log.d("DEBUG", Long.toString(id));
        }
        catch (ExecutionException e) {
            e.printStackTrace();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        updateDiaryData();
    }
    
    private void updateSelectedDateText() {
        TextView dateView = findViewById(R.id.main_text_date);
        dateView.setText(DateUtils.getDateText(selectedDate));
    }
    
    private void updateDiaryData() {
        try {
            currentEntry = db.userDiaryDao().getDiaryEntry(selectedDate).get();
        }
        catch (ExecutionException e) {
            e.printStackTrace();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        updateCalorieData();
        updateNutrientData();
        updateWaterData();
    }
    
    private void updateCalorieData() {
        TextView remaining = findViewById(R.id.main_cal_text_remaining);
        remaining.setText(
                String.format(getString(R.string.main_cal_remaining), (CALORIE_BASELINE - currentEntry.getCalories())));
        
        TextView consumed = findViewById(R.id.main_cal_text_consumed);
        consumed.setText(String.format(getString(R.string.main_cal_consumed), currentEntry.getCalories()));
        
        int pct = Math.round((float) currentEntry.getCalories() / CALORIE_BASELINE * 100);
        TextView percent = findViewById(R.id.main_cal_text_percentage);
        percent.setText(String.format(getString(R.string.main_cal_percentage), pct));
        
        ProgressBar progress = findViewById(R.id.main_cal_progress);
        progress.setProgress(Math.min(pct, progress.getMax()));
    }
    
    private void updateNutrientData() {
        TextView carbs = findViewById(R.id.main_nutrients_text_carbs);
        carbs.setText(String.format(getString(R.string.main_nutrients_carbs), currentEntry.getCarbs_mg(), CARBS_BASELINE));
        
        TextView fat = findViewById(R.id.main_nutrients_text_fat);
        fat.setText(String.format(getString(R.string.main_nutrients_fat), currentEntry.getFat_mg(), FAT_BASELINE));
        
        TextView protein = findViewById(R.id.main_nutrients_text_protein);
        protein.setText(
                String.format(getString(R.string.main_nutrients_protein), currentEntry.getProtein_mg(), PROTEIN_BASELINE));
        
        PieChart chart = findViewById(R.id.main_nutrients_chart);
        ChartUtils.initNutrientPieChart(chart, currentEntry.getCarbs_mg(), currentEntry.getFat_mg(), currentEntry.getProtein_mg(), this);
    }
    
    private void updateWaterData() {
        TextView progressText = findViewById(R.id.main_water_text_progress);
        ProgressBar progressBar = findViewById(R.id.main_water_progress);
        
        int waterInteger = currentEntry.getWater_ml() / 1000;
        int waterFraction = currentEntry.getWater_ml() % 1000 / 100;
        int baseInteger = WATER_BASELINE / 1000;
        int baseFraction = WATER_BASELINE % 1000 / 100;
        progressText.setText(
                String.format(getString(R.string.main_water_consumption), waterInteger, waterFraction, baseInteger,
                              baseFraction));
        
        int pct = Math.round((float) currentEntry.getWater_ml() / WATER_BASELINE * 100);
        progressBar.setProgress(Math.min(pct, progressBar.getMax()));
    }
    
    public void incrementWaterProgress(View v) {
        currentEntry.setWater_ml(currentEntry.getWater_ml() + WATER_STEP);
        updateDatabaseEntry();
    }
    
    public void decrementWaterProgress(View v) {
        if (currentEntry.getWater_ml() == 0) {
            return;
        }
        currentEntry.setWater_ml(currentEntry.getWater_ml() - WATER_STEP);
        updateDatabaseEntry();
    }
    
    private void updateDatabaseEntry() {
        db.userDiaryDao().update(currentEntry);
        updateDiaryData();
    }
}