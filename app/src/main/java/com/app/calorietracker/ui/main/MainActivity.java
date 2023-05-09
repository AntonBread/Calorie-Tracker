package com.app.calorietracker.ui.main;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatImageButton;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.calorietracker.R;
import com.app.calorietracker.database.AppDatabase;
import com.app.calorietracker.database.user.UserDiaryEntity;
import com.app.calorietracker.ui.food.AddFoodActivity;
import com.app.calorietracker.ui.food.FoodActivityIntentVars;
import com.app.calorietracker.ui.food.list.FoodItem;
import com.app.calorietracker.utils.ChartUtils;
import com.app.calorietracker.utils.DateUtils;
import com.github.mikephil.charting.charts.PieChart;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.concurrent.ExecutionException;


public class MainActivity extends AppCompatActivity {
    
    private LocalDate selectedDate;
    private Locale locale;
    
    private UserDiaryEntity currentEntry;
    
    private AppDatabase db;
    
    private final int CALORIE_BASELINE = 2500;
    private final float CARBS_BASELINE = 200.5f;
    private final float FAT_BASELINE = 80.0f;
    private final float PROTEIN_BASELINE = 150.0f;
    private final int WATER_BASELINE = 3000;
    private final int WATER_STEP = 100;
    
    
    ActivityResultLauncher<Intent> foodActivityLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == FoodActivityIntentVars.ADD_FOOD_CANCEL || result.getData() == null) {
                        return;
                    }
                    Bundle data = result.getData().getExtras();
                    selectedDate = (LocalDate) data.get(FoodActivityIntentVars.DATE_KEY);
                    updateDate();
                    
                    FoodActivityIntentVars.MealType mealType =
                            (FoodActivityIntentVars.MealType) data.get(FoodActivityIntentVars.MEAL_TYPE_KEY);
                    ArrayList<FoodItem> foods = (ArrayList<FoodItem>) data.get(FoodActivityIntentVars.FOOD_LIST_KEY);
                    
                    updateMealData(foods, mealType);
                }
            });
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);     // Force light theme
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        locale = new Locale("en");
        
        try {
            db = AppDatabase.getInstance();
        }
        catch (NullPointerException e) {
            AppDatabase.instanceInit(getApplicationContext(), AppDatabase.MODE_IN_MEMORY);
        }
        finally {
            db = AppDatabase.getInstance();
        }
        
        initDateSelector();
        
        initMealAddButtons();
        
        BottomNavigationView bottomNav = findViewById(R.id.main_navbar);
        bottomNav.setSelectedItemId(R.id.navigation_main);
    }
    
    private void initMealAddButtons() {
        String mealTypeKey = FoodActivityIntentVars.MEAL_TYPE_KEY;
        String dateKey = FoodActivityIntentVars.DATE_KEY;
        
        findViewById(R.id.main_btn_breakfast_add).setOnClickListener(v -> {
            Intent intent = new Intent(this, AddFoodActivity.class);
            intent.putExtra(dateKey, selectedDate);
            intent.putExtra(mealTypeKey, FoodActivityIntentVars.MealType.BREAKFAST);
            foodActivityLauncher.launch(intent);
        });
        
        findViewById(R.id.main_btn_lunch_add).setOnClickListener(v -> {
            Intent intent = new Intent(this, AddFoodActivity.class);
            intent.putExtra(dateKey, selectedDate);
            intent.putExtra(mealTypeKey, FoodActivityIntentVars.MealType.LUNCH);
            foodActivityLauncher.launch(intent);
        });
        
        findViewById(R.id.main_btn_dinner_add).setOnClickListener(v -> {
            Intent intent = new Intent(this, AddFoodActivity.class);
            intent.putExtra(dateKey, selectedDate);
            intent.putExtra(mealTypeKey, FoodActivityIntentVars.MealType.DINNER);
            foodActivityLauncher.launch(intent);
        });
        
        findViewById(R.id.main_btn_snacks_add).setOnClickListener(v -> {
            Intent intent = new Intent(this, AddFoodActivity.class);
            intent.putExtra(dateKey, selectedDate);
            intent.putExtra(mealTypeKey, FoodActivityIntentVars.MealType.SNACKS);
            foodActivityLauncher.launch(intent);
        });
    }
    
    private void initDateSelector() {
        initDateSelector(LocalDate.now());
    }
    
    private void initDateSelector(LocalDate date) {
        selectedDate = date;
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
            updateDate();
        }
    };
    
    private void updateDate() {
        updateSelectedDateText();
        UserDiaryEntity entity = new UserDiaryEntity();
        entity.set_date(selectedDate);
        try {
            long id = db.userDiaryDao().insert(entity).get();
        }
        catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        updateActivityDataViews();
    }
    
    private void updateSelectedDateText() {
        TextView dateView = findViewById(R.id.main_text_date);
        dateView.setText(DateUtils.getDateText(selectedDate));
    }
    
    private void updateActivityDataViews() {
        try {
            currentEntry = db.userDiaryDao().getDiaryEntry(selectedDate).get();
        }
        catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        
        MealData breakfast = new MealData(currentEntry.getBreakfast());
        MealData lunch = new MealData(currentEntry.getLunch());
        MealData dinner = new MealData(currentEntry.getDinner());
        MealData other = new MealData(currentEntry.getOther());
        MealData[] meals = {breakfast, lunch, dinner, other};
        
        updateCalorieViews(meals);
        updateNutrientViews(meals);
        updateMealViews(breakfast, lunch, dinner, other);
        updateWaterViews();
    }
    
    private void updateCalorieViews(MealData[] meals) {
        int caloriesTotal = Arrays.stream(meals).mapToInt(MealData::getCals).sum();
        
        TextView remaining = findViewById(R.id.main_cal_text_remaining);
        remaining.setText(
                String.format(locale, getString(R.string.main_cal_remaining), (CALORIE_BASELINE - caloriesTotal)));
        
        TextView consumed = findViewById(R.id.main_cal_text_consumed);
        consumed.setText(String.format(locale, getString(R.string.main_cal_consumed), caloriesTotal));
        
        int pct = Math.round((float) caloriesTotal / CALORIE_BASELINE * 100);
        TextView percent = findViewById(R.id.main_cal_text_percentage);
        percent.setText(String.format(locale, getString(R.string.main_cal_percentage), pct));
        
        ProgressBar progress = findViewById(R.id.main_cal_progress);
        progress.setProgress(Math.min(pct, progress.getMax()));
    }
    
    private void updateNutrientViews(MealData[] meals) {
        float carbs_g = 0;
        float fat_g = 0;
        float protein_g = 0;
        for (MealData m : meals) {
            carbs_g += m.getCarbs_g();
            fat_g += m.getFat_g();
            protein_g += m.getProtein_g();
        }
        
        TextView carbs = findViewById(R.id.main_nutrients_text_carbs);
        carbs.setText(String.format(locale, getString(R.string.main_nutrients_carbs), carbs_g, CARBS_BASELINE));
        
        TextView fat = findViewById(R.id.main_nutrients_text_fat);
        fat.setText(String.format(locale, getString(R.string.main_nutrients_fat), fat_g, FAT_BASELINE));
        
        TextView protein = findViewById(R.id.main_nutrients_text_protein);
        protein.setText(String.format(locale, getString(R.string.main_nutrients_protein), protein_g, PROTEIN_BASELINE));
        
        PieChart chart = findViewById(R.id.main_nutrients_chart);
        ChartUtils.initNutrientPieChart(chart, carbs_g, fat_g, protein_g, this);
    }
    
    private void updateMealViews(MealData breakfast, MealData lunch, MealData dinner, MealData other) {
        TextView breakfastCaloriesView = findViewById(R.id.main_breakfast_cals);
        TextView lunchCaloriesView = findViewById(R.id.main_lunch_cals);
        TextView dinnerCaloriesView = findViewById(R.id.main_dinner_cals);
        TextView otherCaloriesView = findViewById(R.id.main_snacks_cals);
        
        breakfastCaloriesView.setText(String.format(getString(R.string.main_meal_cals), breakfast.getCals()));
        lunchCaloriesView.setText(String.format(getString(R.string.main_meal_cals), lunch.getCals()));
        dinnerCaloriesView.setText(String.format(getString(R.string.main_meal_cals), dinner.getCals()));
        otherCaloriesView.setText(String.format(getString(R.string.main_meal_cals), other.getCals()));
    }
    
    private void updateWaterViews() {
        TextView progressText = findViewById(R.id.main_water_text_progress);
        ProgressBar progressBar = findViewById(R.id.main_water_progress);
        
        int waterInteger = currentEntry.getWater_ml() / 1000;
        int waterFraction = currentEntry.getWater_ml() % 1000 / 100;
        int baseInteger = WATER_BASELINE / 1000;
        int baseFraction = WATER_BASELINE % 1000 / 100;
        progressText.setText(
                String.format(locale, getString(R.string.main_water_consumption), waterInteger, waterFraction, baseInteger,
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
        updateActivityDataViews();
    }
    
    private void updateMealData(ArrayList<FoodItem> foods, FoodActivityIntentVars.MealType mealType) {
        switch (mealType) {
            case BREAKFAST:
                currentEntry.setBreakfast(foods);
                break;
            case LUNCH:
                currentEntry.setLunch(foods);
                break;
            case DINNER:
                currentEntry.setDinner(foods);
                break;
            case SNACKS:
                currentEntry.setOther(foods);
        }
        updateDatabaseEntry();
    }
    
}