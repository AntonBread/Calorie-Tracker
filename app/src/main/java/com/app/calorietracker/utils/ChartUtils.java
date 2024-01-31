package com.app.calorietracker.utils;

import android.content.Context;
import android.graphics.Color;

import com.app.calorietracker.R;
import com.app.calorietracker.ui.stats.data.CaloriesStatsData;
import com.app.calorietracker.ui.stats.data.StatsData;
import com.app.calorietracker.ui.stats.data.WeightStatsData;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;
import java.util.List;

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
    
    public static void updateStatsChartCalories(BarChart chart, Context context, List<CaloriesStatsData> dataList) {
        if (dataList.size() > 10) {
            dataList = reduceCaloriesStatsList(dataList);
        }
        
        ArrayList<BarEntry> entries = new ArrayList<>();
        int i = 1;
        for (CaloriesStatsData data : dataList) {
            entries.add(new BarEntry(i++, data.getCalories()));
        }
        
        BarDataSet barDataSet = new BarDataSet(entries, "calories");
        barDataSet.setColor(context.getColor(R.color.orange_bright));
        barDataSet.setDrawValues(false);
        chart.setData(new BarData(barDataSet));
        chart.invalidate();
    }
    
    public static void updateStatsChartWeight(LineChart chart, Context context, List<WeightStatsData> dataList) {
        if (dataList.size() > 10) {
            dataList = reduceWeightStatsList(dataList);
        }
        
        ArrayList<Entry> entries = new ArrayList<>();
        int i = 1;
        for (WeightStatsData data : dataList) {
            entries.add(new Entry(i++, data.getWeight()));
        }
        
        LineDataSet lineDataSet = new LineDataSet(entries, "weight");
        lineDataSet.setColor(context.getColor(R.color.orange_bright));
        lineDataSet.setCircleColor(context.getColor(R.color.orange_bright));
        lineDataSet.setDrawCircleHole(false);
        lineDataSet.setValueTextSize(14);
        lineDataSet.setCircleRadius(8);
        chart.setData(new LineData(lineDataSet));
        chart.invalidate();
    }
    
    public static void initWeightStatsChartConfig(LineChart chart, Context context) {
        initCommonStatsChartConfig(chart, context);
    }
    
    public static void initCaloriesStatsChartConfig(BarChart chart, Context context) {
        initCommonStatsChartConfig(chart, context);
        
        chart.setHighlightFullBarEnabled(false);
    }
    
    private static void initCommonStatsChartConfig(BarLineChartBase<?> chart, Context context) {
        XAxis xAxis = chart.getXAxis();
        YAxis leftAxis = chart.getAxisLeft();
        
        int clrGrayDark = context.getColor(R.color.gray_dark);
        
        chart.getAxisRight().setEnabled(false);
        
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setTextSize(13f);
        xAxis.setAvoidFirstLastClipping(true);
        xAxis.setTextColor(clrGrayDark);
        xAxis.setDrawAxisLine(false);
        xAxis.setSpaceMin(1f);
        xAxis.setSpaceMax(1f);
        
        leftAxis.setDrawAxisLine(false);
        leftAxis.setGridColor(clrGrayDark);
        leftAxis.setTextSize(13f);
        leftAxis.setMinWidth(24f);
        leftAxis.setTextColor(clrGrayDark);
        leftAxis.setAxisMinimum(0f);
        
        chart.getLegend().setEnabled(false);
        chart.getDescription().setEnabled(false);
        chart.setHighlightPerTapEnabled(false);
        chart.setHighlightPerDragEnabled(false);
        chart.setScaleEnabled(false);
        chart.setExtraBottomOffset(12f);
        chart.setMaxVisibleValueCount(10);
    }
    
    private static List<CaloriesStatsData> reduceCaloriesStatsList(List<CaloriesStatsData> original) {
        // TODO: fix method stub
        return original;
    }
    
    private static List<WeightStatsData> reduceWeightStatsList(List<WeightStatsData> original) {
        // TODO: fix method stub
        return original;
    }
    
    private static ValueFormatter createXAxisFormatter(List<StatsData> dataList) {
        // TODO: fix method stub
        return null;
    }
}
