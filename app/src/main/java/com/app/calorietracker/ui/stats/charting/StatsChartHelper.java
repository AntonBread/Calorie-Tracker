package com.app.calorietracker.ui.stats.charting;

import com.app.calorietracker.R;
import com.app.calorietracker.ui.settings.SettingsManager;
import com.app.calorietracker.ui.stats.StatsActivity;
import com.app.calorietracker.ui.stats.data.CaloriesStatsData;
import com.app.calorietracker.ui.stats.data.StatsData;
import com.app.calorietracker.ui.stats.data.WeightStatsData;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class StatsChartHelper {
    
    private final StatsActivity context;
    
    static final int STATS_LIST_MAX_SIZE = 9;
    
    public StatsChartHelper(StatsActivity context) {
        this.context = context;
    }
    
    public void updateCaloriesChart(BarChart chart, List<CaloriesStatsData> dataList) {
        if (dataList.size() > STATS_LIST_MAX_SIZE) {
            dataList = reduceStatsList(dataList);
        }
        
        updateChartXAxisLabelFormat(chart, dataList);
        updateChartXAxisLabelCount(chart, dataList);
        
        ArrayList<BarEntry> entries = new ArrayList<>();
        int i = 0;
        for (CaloriesStatsData data : dataList) {
            entries.add(new BarEntry(i++, data.getCalories()));
        }
        
        BarDataSet barDataSet = new BarDataSet(entries, "calories");
        barDataSet.setColor(context.getColor(R.color.orange_bright));
        barDataSet.setDrawValues(false);
        chart.setData(new BarData(barDataSet));
        chart.invalidate();
    }
    
    public void updateWeightChart(LineChart chart, List<WeightStatsData> dataList) {
        if (dataList.size() > STATS_LIST_MAX_SIZE) {
            dataList = reduceStatsList(dataList);
        }
        
        updateChartXAxisLabelFormat(chart, dataList);
        updateChartXAxisLabelCount(chart, dataList);
        
        ArrayList<Entry> entries = new ArrayList<>();
        int i = 0;
        float maxWeight = 0f;
        for (WeightStatsData data : dataList) {
            float weight = data.getWeight();
            if (weight > maxWeight) {
                maxWeight = weight;
            }
            entries.add(new Entry(i++, weight));
        }
        
        LineDataSet lineDataSet = new LineDataSet(entries, "weight");
        lineDataSet.setColor(context.getColor(R.color.orange_bright));
        lineDataSet.setCircleColor(context.getColor(R.color.orange_bright));
        lineDataSet.setDrawCircleHole(false);
        lineDataSet.setValueTextSize(13);
        lineDataSet.setCircleRadius(8);
        lineDataSet.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                // Has no decimal part
                if (value == (int) value) {
                    return String.format(Locale.US, "%d", (int) value);
                }
                // Has decimal part
                else {
                    return String.format(Locale.US, "%.1f", value);
                }
            }
        });
        chart.setData(new LineData(lineDataSet));
        // Sometimes Y axis maximum is not auto-calculated correctly,
        // have to set it manually on chart update
        chart.getAxisLeft().setAxisMaximum(maxWeight * 1.15f);
        chart.invalidate();
    }
    
    private void updateChartXAxisLabelFormat(BarLineChartBase<?> chart, List<? extends StatsData> dataList) {
        Locale locale = new SettingsManager(context).getLocale();
        StatsActivity.TimeRange timeRange = context.getTimeRange();
        ValueFormatter vf = createXAxisFormatter(dataList, locale);
        
        XAxis xAxis = chart.getXAxis();
        xAxis.setValueFormatter(vf);
        
        switch (timeRange) {
            case WEEK:
                xAxis.setTextSize(13f);
                break;
            case MONTH:
            case MONTH_3:
                xAxis.setTextSize(11f);
                break;
            case YEAR:
            case ALL:
            default:
                xAxis.setTextSize(10f);
        }
    }
    
    private void updateChartXAxisLabelCount(BarLineChartBase<?> chart, List<? extends StatsData> dataList) {
        int labelCount = dataList.size() + 2;
        chart.getXAxis().setLabelCount(labelCount, true);
    }
    
    public void initWeightChartConfig(LineChart chart) {
        initCommonChartConfig(chart);
        
        chart.setMaxVisibleValueCount(STATS_LIST_MAX_SIZE + 2);
        chart.getAxisLeft().setValueFormatter(new YAxisLabelFormatter(StatsActivity.StatType.WEIGHT, context));
    }
    
    public void initCaloriesChartConfig(BarChart chart) {
        initCommonChartConfig(chart);
        
        chart.setHighlightFullBarEnabled(false);
        chart.getAxisLeft().setValueFormatter(new YAxisLabelFormatter(StatsActivity.StatType.CALORIES, context));
    }
    
    private void initCommonChartConfig(BarLineChartBase<?> chart) {
        XAxis xAxis = chart.getXAxis();
        YAxis leftAxis = chart.getAxisLeft();
        
        int clrGrayDark = context.getColor(R.color.gray_dark);
        
        chart.getAxisRight().setEnabled(false);
        
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setAvoidFirstLastClipping(true);
        xAxis.setTextColor(clrGrayDark);
        xAxis.setDrawAxisLine(false);
        xAxis.setSpaceMin(1f);
        xAxis.setSpaceMax(1f);
        
        leftAxis.setDrawAxisLine(false);
        leftAxis.setGridColor(clrGrayDark);
        leftAxis.setTextSize(13f);
        leftAxis.setTextColor(clrGrayDark);
        leftAxis.setAxisMinimum(0f);
        leftAxis.setMinWidth(24f);
        leftAxis.setMaxWidth(36f);
        
        chart.getLegend().setEnabled(false);
        chart.getDescription().setEnabled(false);
        chart.setHighlightPerTapEnabled(false);
        chart.setHighlightPerDragEnabled(false);
        chart.setScaleEnabled(false);
        chart.setExtraBottomOffset(20f);
        chart.setMaxVisibleValueCount(10);
        chart.setXAxisRenderer(new MultiLineXAxisRenderer(chart.getViewPortHandler(), chart.getXAxis(),
                                                          chart.getTransformer(YAxis.AxisDependency.LEFT)));
        chart.setRendererLeftYAxis(new MultiLineYAxisRenderer(chart.getViewPortHandler(), chart.getAxisLeft(),
                                                              chart.getTransformer(YAxis.AxisDependency.LEFT)));
    }
    
    @SuppressWarnings("unchecked")
    private <T extends StatsData> List<T> reduceStatsList(List<T> original) {
        StatsData[] reducedArr = new StatsData[STATS_LIST_MAX_SIZE];
        // First and last elements must remain unchanged
        reducedArr[0] = original.get(0);
        reducedArr[STATS_LIST_MAX_SIZE - 1] = original.get(original.size() - 1);
        
        int origSize = original.size();
        int step = (origSize - 2) / (STATS_LIST_MAX_SIZE - 2); // -2 because of xAxis space
        
        int iOrig = step;
        int iReduced = 1;
        for (; iOrig < origSize; iOrig += step) {
            if (iReduced >= STATS_LIST_MAX_SIZE - 1) {
                break;
            }
            reducedArr[iReduced++] = original.get(iOrig);
        }
        
        return (List<T>) Arrays.asList(reducedArr.clone());
    }
    
    private ValueFormatter createXAxisFormatter(List<? extends StatsData> dataList, Locale locale) {
        ArrayList<String> days = new ArrayList<>(STATS_LIST_MAX_SIZE);
        DateTimeFormatter fmtTop;
        DateTimeFormatter fmtBot;
        
        StatsActivity.TimeRange timeRange = context.getTimeRange();
        
        switch (timeRange) {
            case WEEK:
            default:
                fmtTop = DateTimeFormatter.ofPattern("E", locale);
                fmtBot = DateTimeFormatter.ofPattern("d", locale);
                break;
            case MONTH:
            case MONTH_3:
                fmtTop = DateTimeFormatter.ofPattern("d", locale);
                fmtBot = DateTimeFormatter.ofPattern("MMM", locale);
                break;
            case YEAR:
            case ALL:
                fmtTop = DateTimeFormatter.ofPattern("dd.MM", locale);
                fmtBot = DateTimeFormatter.ofPattern("y", locale);
        }
        
        for (StatsData data : dataList) {
            LocalDate date = data.getDate();
            
            String day = date.format(fmtTop) + "\n" + date.format(fmtBot);
            days.add(day);
        }
        
        return new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                try {
                    return days.get((int) value);
                }
                // Exception is thrown for xAxis space values
                catch (Exception e) {
                    return "";
                }
            }
        };
    }
}
