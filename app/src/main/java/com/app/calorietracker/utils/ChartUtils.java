package com.app.calorietracker.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;

import com.app.calorietracker.R;
import com.app.calorietracker.ui.settings.SettingsManager;
import com.app.calorietracker.ui.stats.StatsActivity;
import com.app.calorietracker.ui.stats.StatsActivity.TimeRange;
import com.app.calorietracker.ui.stats.StatsActivity.StatType;
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
import com.github.mikephil.charting.renderer.XAxisRenderer;
import com.github.mikephil.charting.renderer.YAxisRenderer;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

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
    
    static final int STATS_LIST_MAX_SIZE = 9;
    
    public static void updateStatsChartCalories(BarChart chart, StatsActivity context,
                                                List<CaloriesStatsData> dataList) {
        if (dataList.size() > STATS_LIST_MAX_SIZE) {
            dataList = reduceStatsList(dataList);
        }
        
        updateStatsChartXAxisLabelFormat(chart, context, dataList);
        updateStatsChartXAxisLabelCount(chart, dataList);
        
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
    
    public static void updateStatsChartWeight(LineChart chart, StatsActivity context,
                                              List<WeightStatsData> dataList) {
        if (dataList.size() > STATS_LIST_MAX_SIZE) {
            dataList = reduceStatsList(dataList);
        }
        
        updateStatsChartXAxisLabelFormat(chart, context, dataList);
        updateStatsChartXAxisLabelCount(chart, dataList);
        
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
        lineDataSet.setValueTextSize(14);
        lineDataSet.setCircleRadius(8);
        chart.setData(new LineData(lineDataSet));
        // Sometimes Y axis maximum is not auto-calculated correctly,
        // have to set it manually on chart update
        chart.getAxisLeft().setAxisMaximum(maxWeight * 1.15f);
        chart.invalidate();
    }
    
    private static void updateStatsChartXAxisLabelFormat(BarLineChartBase<?> chart,
                                                         StatsActivity context,
                                                         List<? extends StatsData> dataList) {
        Locale locale = new SettingsManager(context).getLocale();
        TimeRange timeRange = context.getTimeRange();
        ValueFormatter vf = createXAxisFormatter(dataList, context.getTimeRange(), locale);
        
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
    
    private static void updateStatsChartXAxisLabelCount(BarLineChartBase<?> chart,
                                                        List<? extends StatsData> dataList) {
        int labelCount = dataList.size() + 2;
        chart.getXAxis().setLabelCount(labelCount, true);
    }
    
    static class YAxisLabelFormatter extends ValueFormatter {
        final StatType statType;
        final Context context;
        
        public YAxisLabelFormatter(StatType statType, Context context) {
            this.statType = statType;
            this.context = context;
        }
        
        @Override
        public String getFormattedValue(float value) {
            if (value == 0f) {
                return "";
            }
            
            String label = String.valueOf((int) value);
            label += "\n";
            String unit;
            switch (statType) {
                case WEIGHT:
                    unit = context.getString(R.string.stats_weight_chart_unit);
                    break;
                case CALORIES:
                    unit = context.getString(R.string.stats_calories_chart_unit);
                    break;
                default:
                    unit = "";
            }
            label += unit;
            return label;
        }
    }
    
    public static void initWeightStatsChartConfig(LineChart chart, Context context) {
        initCommonStatsChartConfig(chart, context);
        
        chart.setMaxVisibleValueCount(STATS_LIST_MAX_SIZE + 2);
        chart.getAxisLeft().setValueFormatter(new YAxisLabelFormatter(StatType.WEIGHT, context));
    }
    
    public static void initCaloriesStatsChartConfig(BarChart chart, Context context) {
        initCommonStatsChartConfig(chart, context);
        
        chart.setHighlightFullBarEnabled(false);
        chart.getAxisLeft().setValueFormatter(new YAxisLabelFormatter(StatType.CALORIES, context));
    }
    
    private static void initCommonStatsChartConfig(BarLineChartBase<?> chart, Context context) {
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
    private static <T extends StatsData> List<T> reduceStatsList(List<T> original) {
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
    
    private static ValueFormatter createXAxisFormatter(List<? extends StatsData> dataList,
                                                       TimeRange timeRange,
                                                       Locale locale) {
        ArrayList<String> days = new ArrayList<>(STATS_LIST_MAX_SIZE);
        DateTimeFormatter fmtTop;
        DateTimeFormatter fmtBot;
        
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
    
    // MPChart doesn't support multi-line labels
    // Copied this piece from here: https://stackoverflow.com/questions/32509174/in-mpandroidchart-library-how-to-wrap-x-axis-labels-to-two-lines-when-long
    static class MultiLineXAxisRenderer extends XAxisRenderer {
        public MultiLineXAxisRenderer(ViewPortHandler viewPortHandler, XAxis xAxis, Transformer trans) {
            super(viewPortHandler, xAxis, trans);
        }
        
        @Override
        protected void drawLabel(Canvas c, String formattedLabel, float x, float y,
                                 MPPointF anchor, float angleDegrees) {
            String[] line = formattedLabel.split("\n");
            Utils.drawXAxisValue(c, line[0], x, y, mAxisLabelPaint, anchor, angleDegrees);
            for (int i = 1; i < line.length; i++) { // we've already processed 1st line
                Utils.drawXAxisValue(c, line[i], x, y + mAxisLabelPaint.getTextSize() * i,
                                     mAxisLabelPaint, anchor, angleDegrees);
            }
        }
    }
    
    static class MultiLineYAxisRenderer extends YAxisRenderer {
        public MultiLineYAxisRenderer(ViewPortHandler viewPortHandler, YAxis yAxis, Transformer trans) {
            super(viewPortHandler, yAxis, trans);
        }
        
        @Override
        protected void drawYLabels(Canvas c, float fixedPosition, float[] positions, float offset) {
            final int from = mYAxis.isDrawBottomYLabelEntryEnabled() ? 0 : 1;
            final int to = mYAxis.isDrawTopYLabelEntryEnabled()
                    ? mYAxis.mEntryCount
                    : (mYAxis.mEntryCount - 1);
            
            // draw
            for (int i = from; i < to; i++) {
                
                String formattedLabel = mYAxis.getFormattedLabel(i);
                String[] line = formattedLabel.split("\n");
                
                c.drawText(line[0], fixedPosition, positions[i * 2 + 1] + offset, mAxisLabelPaint);
                for (int j = 1; j < line.length; j++) {
                    float y = positions[i * 2 + 1] + offset + mAxisLabelPaint.getTextSize() * j;
                    c.drawText(line[j], fixedPosition, y, mAxisLabelPaint);
                }
            }
        }
    }
}
