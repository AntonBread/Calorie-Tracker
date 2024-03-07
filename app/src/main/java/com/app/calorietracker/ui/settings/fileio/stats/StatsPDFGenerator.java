package com.app.calorietracker.ui.settings.fileio.stats;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.pdf.PdfDocument;
import android.graphics.pdf.PdfDocument.Page;
import android.graphics.pdf.PdfDocument.PageInfo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;

import com.app.calorietracker.R;
import com.app.calorietracker.database.AppDatabase;
import com.app.calorietracker.database.user.UserDiaryEntity;
import com.app.calorietracker.ui.stats.StatsCalculator;
import com.app.calorietracker.ui.stats.data.CaloriesStatsData;
import com.app.calorietracker.ui.stats.data.WeightStatsData;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class StatsPDFGenerator {
    
    // A4 portrait page metrics
    // 1 pageInfo unit is 1/72 of an inch
    private static final int PAGE_HEIGHT = 842;
    private static final int PAGE_WIDTH = 595;
    
    private static final int PAGE_BOUND_LEFT = 32;
    private static int y = 40; // variable for tracking currently used y value of canvas
    
    @NonNull
    public static PdfDocument generate(Context context) {
        PdfDocument doc = new PdfDocument();
        PageInfo pageInfo = new PageInfo.Builder(PAGE_WIDTH, PAGE_HEIGHT, 1).create();
        Page page = doc.startPage(pageInfo);
        
        Paint textPaint = new Paint();
        Canvas canvas = page.getCanvas();
        
        // Title style
        textPaint.setTextSize(24f);
        textPaint.setColor(ContextCompat.getColor(context, R.color.gray_dark));
        
        resetY(); // When exporting pdfs in quick succession, previous y value is used
        
        drawTitle(context, canvas, textPaint);
        
        List<UserDiaryEntity> dataList = fetchDiaryData();
        if (dataList == null) {
            drawStatsEmptyText(context, canvas, textPaint);
            doc.finishPage(page);
            return doc;
        }
        
        StatsCalculator statsCalculator = new StatsCalculator(context);
        List<CaloriesStatsData> caloriesDataList = CaloriesStatsData.createStatsDataList(dataList);
        List<WeightStatsData> weightDataList = WeightStatsData.createStatsDataList(dataList);
        
        // Normal text style
        textPaint.setTextSize(20f);
        textPaint.setColor(ContextCompat.getColor(context, R.color.black));
        
        // Write current user weight
        Drawable iconWeightCurrent = AppCompatResources.getDrawable(context, R.drawable.ic_weight_speed);
        String textWeightCurrent = createCurrentWeightString(context, weightDataList);
        drawStatsLine(textWeightCurrent, iconWeightCurrent, canvas, textPaint);
        
        // Write current BMI (if height value exists)
        String textBmi = createBmiString(context, weightDataList, statsCalculator);
        Drawable iconBmi = AppCompatResources.getDrawable(context, R.drawable.ic_weight_bmi);
        if (textBmi != null) {
            drawStatsLine(textBmi, iconBmi, canvas, textPaint);
        }
        
        // Write weight delta for first and last entry
        Drawable iconWeightChange = AppCompatResources.getDrawable(context, R.drawable.ic_weight_delta_gain);
        String textWeightChange = createWeightChangeString(context, weightDataList, statsCalculator);
        drawStatsLine(textWeightChange, iconWeightChange, canvas, textPaint);
        
        // Write total calories consumed
        Drawable iconCalories = AppCompatResources.getDrawable(context, R.drawable.ic_kcal_24);
        String textCalories = createCalorieConsumptionString(context, caloriesDataList, statsCalculator);
        drawStatsLine(textCalories, iconCalories, canvas, textPaint);
        
        doc.finishPage(page);
        return doc;
    }
    
    private static void drawTitle(Context context, Canvas canvas, Paint paint) {
        String title = createTitleString(context);
        String[] lines = title.split("\n");
        
        for (String line : lines) {
            canvas.drawText(line, PAGE_BOUND_LEFT, y, paint);
            y += 40;
        }
        y += 20;
    }
    
    private static void drawStatsLine(String text, @Nullable Drawable icon, Canvas canvas, Paint paint) {
        assert icon != null;
        icon.setBounds(PAGE_BOUND_LEFT, y - 21, PAGE_BOUND_LEFT + 24, y + 3);
        icon.draw(canvas);
        
        String[] lines = text.split("\n");
        canvas.drawText(lines[0], PAGE_BOUND_LEFT + 24 + 4, y, paint);
        for (int i = 1; i < lines.length; i++) {
            y += 40;
            canvas.drawText(lines[i], PAGE_BOUND_LEFT, y, paint);
        }
        y += 60;
    }
    
    private static void drawStatsEmptyText(Context context, Canvas canvas, Paint paint) {
        String s = context.getString(R.string.settings_file_export_stats_empty);
        canvas.drawText(s, PAGE_BOUND_LEFT, y, paint);
    }
    
    private static void resetY() {
        y = 40;
    }
    
    private static String createTitleString(Context context) {
        String dateString = LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        return context.getString(R.string.settings_file_export_stats_title, dateString);
    }
    
    private static String createCurrentWeightString(Context context, List<WeightStatsData> list) {
        float currentWeight = list.get(list.size() - 1).getWeight();
        return context.getString(R.string.settings_file_export_stats_weight_current, currentWeight);
    }
    
    @Nullable
    private static String createBmiString(Context context, List<WeightStatsData> list, StatsCalculator calculator) {
        float bmi = calculator.currentBodyMassIndex(list);
        if (bmi == Float.MIN_VALUE) {
            return null;
        }
        return context.getString(R.string.settings_file_export_stats_bmi, bmi);
    }
    
    private static String createWeightChangeString(Context context, List<WeightStatsData> list,
                                                   StatsCalculator calculator) {
        float delta = calculator.weightDelta(list);
        if (delta == 0f) {
            return context.getString(R.string.settings_file_export_stats_weight_change_none);
        }
        else if (delta > 0f) {
            return context.getString(R.string.settings_file_export_stats_weight_change_gain, delta);
        }
        else {
            return context.getString(R.string.settings_file_export_stats_weight_change_loss, -delta);
        }
    }
    
    private static String createCalorieConsumptionString(Context context, List<CaloriesStatsData> list,
                                                         StatsCalculator calculator) {
        int totalCals = calculator.totalCalories(list);
        String s;
        if (totalCals >= 100_000 && totalCals < 100_000_000) {
            int temp = totalCals / 1000;
            s = String.valueOf(temp).concat("K");
        }
        else if (totalCals >= 100_000_000) {
            int temp = totalCals / 1_000_000;
            s = String.valueOf(temp).concat("M");
        }
        else {
            s = String.valueOf(totalCals);
        }
        return context.getString(R.string.settings_file_export_stats_cals_total, s);
    }
    
    @Nullable
    private static List<UserDiaryEntity> fetchDiaryData() {
        LocalDate dateStart = LocalDate.MIN;
        LocalDate dateEnd = LocalDate.now();
        
        try {
            List<UserDiaryEntity> list = AppDatabase.getInstance()
                                                    .userDiaryDao()
                                                    .getDiaryEntriesRange(dateStart, dateEnd).get();
            
            if (list.size() == 0) {
                return null;
            }
            return list;
        }
        catch (ExecutionException | InterruptedException e) {
            return null;
        }
    }
}
