package com.app.calorietracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.room.Room;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.ScaleAnimation;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.calorietracker.database.user.DiaryEntry;
import com.app.calorietracker.database.user.UserDiaryDao;
import com.app.calorietracker.database.user.UserDiaryDatabase;
import com.app.calorietracker.food.AddFoodActivity;
import com.app.calorietracker.utils.ChartUtils;
import com.app.calorietracker.utils.DateUtils;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Locale;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.observers.DisposableCompletableObserver;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    
    // TODO: cleanup db related code
    
    private LocalDate selectedDate;
    private Locale locale;
    
    UserDiaryDatabase userDb;
    UserDiaryDao userDao;
    private DiaryEntry currentEntry;
    
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
        
        userDb = Room.databaseBuilder(this, UserDiaryDatabase.class, "User_diary")
                     .addMigrations(UserDiaryDatabase.MIGRATION_1_2)
                     .build();
        userDao = userDb.userDiaryDao();
        
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
        updateDiaryData();
    }
    
    private void updateSelectedDateText() {
        TextView dateView = findViewById(R.id.main_text_date);
        dateView.setText(DateUtils.getDateText(selectedDate));
    }
    
    private void updateDiaryData() {
        /*
         * Get entry from db with currently selected date
         * and update layout with data (onSuccess() method)
         *
         * if returns null (onError() method), create new entry in db with selected date
         * call updateDiaryData() again
         */
        userDao.getEntry(selectedDate.toString())
               .subscribeOn(Schedulers.io())
               .observeOn(AndroidSchedulers.mainThread())
               .subscribe(new DisposableSingleObserver<DiaryEntry>() {
                   @Override
                   public void onSuccess(@NonNull DiaryEntry entry) {
                       currentEntry = entry;
                       updateCalorieData();
                       updateNutrientData();
                       updateWaterData();
                   }
            
                   @Override
                   public void onError(@NonNull Throwable e) {
                       DiaryEntry newEntry = new DiaryEntry();
                       newEntry._date = selectedDate.toString();
                       userDao.insertEntry(newEntry)
                              .subscribeOn(Schedulers.io())
                              .observeOn(AndroidSchedulers.mainThread())
                              .subscribe(new DisposableCompletableObserver() {
                                  @Override
                                  public void onComplete() {
                                      updateDiaryData();
                                  }
                    
                                  @Override
                                  public void onError(@NonNull Throwable e) {
                                      updateDiaryData();
                                  }
                              });
                   }
               });
    }
    
    private void updateCalorieData() {
        TextView remaining = findViewById(R.id.main_cal_text_remaining);
        remaining.setText(
                String.format(getString(R.string.main_cal_remaining), (CALORIE_BASELINE - currentEntry.calories)));
        
        TextView consumed = findViewById(R.id.main_cal_text_consumed);
        consumed.setText(String.format(getString(R.string.main_cal_consumed), currentEntry.calories));
        
        int pct = Math.round((float) currentEntry.calories / CALORIE_BASELINE * 100);
        TextView percent = findViewById(R.id.main_cal_text_percentage);
        percent.setText(String.format(getString(R.string.main_cal_percentage), pct));
        
        ProgressBar progress = findViewById(R.id.main_cal_progress);
        progress.setProgress(Math.min(pct, progress.getMax()));
    }
    
    private void updateNutrientData() {
        TextView carbs = findViewById(R.id.main_nutrients_text_carbs);
        carbs.setText(String.format(getString(R.string.main_nutrients_carbs), currentEntry.carbs_g, CARBS_BASELINE));
        
        TextView fat = findViewById(R.id.main_nutrients_text_fat);
        fat.setText(String.format(getString(R.string.main_nutrients_fat), currentEntry.fat_g, FAT_BASELINE));
        
        TextView protein = findViewById(R.id.main_nutrients_text_protein);
        protein.setText(
                String.format(getString(R.string.main_nutrients_protein), currentEntry.protein_g, PROTEIN_BASELINE));
        
        PieChart chart = findViewById(R.id.main_nutrients_chart);
        ChartUtils.initNutrientPieChart(chart, currentEntry.carbs_g, currentEntry.fat_g, currentEntry.protein_g, this);
    }
    
    private void updateWaterData() {
        TextView progressText = findViewById(R.id.main_water_text_progress);
        ProgressBar progressBar = findViewById(R.id.main_water_progress);
        
        int waterInteger = currentEntry.water_mL / 1000;
        int waterFraction = currentEntry.water_mL % 1000 / 100;
        int baseInteger = WATER_BASELINE / 1000;
        int baseFraction = WATER_BASELINE % 1000 / 100;
        progressText.setText(
                String.format(getString(R.string.main_water_consumption), waterInteger, waterFraction, baseInteger,
                              baseFraction));
        
        int pct = Math.round((float) currentEntry.water_mL / WATER_BASELINE * 100);
        progressBar.setProgress(Math.min(pct, progressBar.getMax()));
    }
    
    public void incrementWaterProgress(View v) {
        currentEntry.water_mL += WATER_STEP;
        updateDatabaseEntry();
    }
    
    public void decrementWaterProgress(View v) {
        if (currentEntry.water_mL == 0) {
            return;
        }
        currentEntry.water_mL -= WATER_STEP;
        updateDatabaseEntry();
    }
    
    private void updateDatabaseEntry() {
        userDao.updateEntry(currentEntry)
               .subscribeOn(Schedulers.io())
               .observeOn(AndroidSchedulers.mainThread())
               .subscribe(new DisposableCompletableObserver() {
                   @Override
                   public void onComplete() {
                       updateDiaryData();
                   }
            
                   @Override
                   public void onError(@NonNull Throwable e) {
                       updateDatabaseEntry();
                   }
               });
    }
}