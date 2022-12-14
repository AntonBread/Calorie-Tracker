package com.app.calorietracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatImageButton;

import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);     // Force day theme
        setContentView(R.layout.activity_main);
    
        AppCompatImageButton btnPrev = findViewById(R.id.main_btn_date_prev);
        AppCompatImageButton btnNext = findViewById(R.id.main_btn_date_next);
    
        ProgressBar progressBar = findViewById(R.id.main_cal_progress);
        
        btnPrev.setOnClickListener(v -> {
            progressBar.incrementProgressBy(-25);
        });
        
        btnNext.setOnClickListener(v -> {
            progressBar.incrementProgressBy(25);
        });
        
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
}