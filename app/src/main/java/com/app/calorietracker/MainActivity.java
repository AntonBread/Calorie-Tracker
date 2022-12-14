package com.app.calorietracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatImageButton;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.calorietracker.utils.DateUtils;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    
    private LocalDate selectedDate;
    private Locale locale;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);     // Force day theme
        setContentView(R.layout.activity_main);
        locale = new Locale("en");
        
        initDateSelector();
        
        ProgressBar waterProgress = findViewById(R.id.main_water_progress);
        
        AppCompatImageButton btnAddWater = findViewById(R.id.main_btn_water_add);
        AppCompatImageButton btnSubWater = findViewById(R.id.main_btn_water_sub);
        
        btnAddWater.setOnClickListener(v -> {
            waterProgress.incrementProgressBy(10);
        });
        
        btnSubWater.setOnClickListener(v -> {
            waterProgress.incrementProgressBy(-10);
        });
        
        PieChart piechart = findViewById(R.id.main_nutrients_chart);
        
        piechart.setUsePercentValues(true);
        piechart.getLegend().setEnabled(false);
        piechart.getDescription().setEnabled(false);
        piechart.setRotationEnabled(false);
        piechart.setRotationAngle(0);
        piechart.setHighlightPerTapEnabled(true);
        piechart.animateY(1400, Easing.EaseInOutQuad);
        piechart.setHoleRadius(50f);
        piechart.setTransparentCircleRadius(0f);
        piechart.setDrawEntryLabels(false);
        
        ArrayList<PieEntry> pieEntries = new ArrayList<>();
        pieEntries.add(new PieEntry(92, "#1"));
        pieEntries.add(new PieEntry(66, "#2"));
        pieEntries.add(new PieEntry(42, "#3"));
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(getColor(R.color.nutrient_carbs));
        colors.add(getColor(R.color.nutrient_fat));
        colors.add(getColor(R.color.nutrient_protein));
        PieDataSet pieDataSet = new PieDataSet(pieEntries, "label");
        pieDataSet.setColors(colors);
        pieDataSet.setValueTextColor(Color.parseColor("#ffffff"));
        pieDataSet.setValueTextSize(14);
        PieData pieData = new PieData(pieDataSet);
        pieData.setDrawValues(false);
        piechart.setData(pieData);
        piechart.invalidate();
        
        BottomNavigationView bottomNav = findViewById(R.id.main_navbar);
        bottomNav.setSelectedItemId(R.id.navigation_main);
    }
    
    private void initDateSelector() {
        selectedDate = LocalDate.now();
        DateUtils.init(getString(R.string.main_date_today), getString(R.string.main_date_yesterday), locale);
        updateSelectedDateText();
        initDateButtons();
    }
    
    private void initDateButtons() {
        AppCompatImageButton btnPrev = findViewById(R.id.main_btn_date_prev);
        AppCompatImageButton btnNext = findViewById(R.id.main_btn_date_next);
        TextView dateView = findViewById(R.id.main_text_date);
        
        btnPrev.setOnClickListener(v -> {
            selectedDate = selectedDate.minusDays(1);
            updateSelectedDateText();
        });
        
        btnNext.setOnClickListener(v -> {
            selectedDate = selectedDate.plusDays(1);
            updateSelectedDateText();
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
    
    private void updateSelectedDateText() {
        TextView dateView = findViewById(R.id.main_text_date);
        dateView.setText(DateUtils.getDateText(selectedDate));
    }
}