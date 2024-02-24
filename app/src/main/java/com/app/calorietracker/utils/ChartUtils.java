package com.app.calorietracker.utils;

import android.content.Context;
import android.graphics.Color;

import com.app.calorietracker.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;

public class ChartUtils {
    
    public static void initNutrientPieChart(PieChart chart, float carbs, float fat, float protein, Context context) {
        initPieChartConfig(chart);
        PieData chartData = getNutrientPieChartData(context, carbs, fat, protein);
        showPieChart(chart, chartData);
    }
    
    private static void initPieChartConfig(PieChart chart) {
        chart.setUsePercentValues(true);
        chart.getLegend().setEnabled(false);
        chart.getDescription().setEnabled(false);
        chart.setRotationEnabled(false);
        chart.setRotationAngle(0);
        chart.setHighlightPerTapEnabled(false);
        chart.setHoleRadius(55);
        chart.setTransparentCircleRadius(0);
        chart.setDrawEntryLabels(false);
    }
    
    private static PieData getNutrientPieChartData(Context context, float carbs, float fat, float protein) {
        ArrayList<PieEntry> entries = new ArrayList<>(3);
        entries.add(new PieEntry(carbs, "carbs"));
        entries.add(new PieEntry(fat, "fat"));
        entries.add(new PieEntry(protein, "protein"));
        
        ArrayList<Integer> colors = new ArrayList<>(3);
        colors.add(context.getColor(R.color.nutrient_carbs));
        colors.add(context.getColor(R.color.nutrient_fat));
        colors.add(context.getColor(R.color.nutrient_protein));
        
        PieDataSet dataSet = new PieDataSet(entries, "nutrients");
        dataSet.setColors(colors);
        dataSet.setValueTextColor(Color.parseColor("#FFFFFFFF"));
        dataSet.setValueTextSize(14);
        
        return new PieData(dataSet);
    }
    
    private static void showPieChart(PieChart chart, PieData data) {
        data.setDrawValues(false);
        chart.setData(data);
        chart.invalidate();
    }
    
}
