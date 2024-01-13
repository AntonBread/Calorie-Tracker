package com.app.calorietracker.utils;

import android.content.Context;

import com.app.calorietracker.R;
import com.app.calorietracker.ui.settings.SettingsManager;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DateFormatter {
    
    private static DateTimeFormatter dayOfWeekFmt;
    private static DateTimeFormatter monthDayFmt;
    private static DateTimeFormatter dayMonthYearFmt;
    private static String strToday;
    private static String strYesterday;
    private static Locale locale;
    
    public static void init(Context context) {
        strToday = context.getString(R.string.main_date_today);
        strYesterday = context.getString(R.string.main_date_yesterday);
        locale = new SettingsManager(context).getLocale();
        
        dayOfWeekFmt = DateTimeFormatter.ofPattern("EEEE", locale);
        if (locale.getLanguage().equals("ru")) {
            monthDayFmt = DateTimeFormatter.ofPattern("d MMMM", locale);
        }
        else {
            monthDayFmt = DateTimeFormatter.ofPattern("MMMM d", locale);
        }
        dayMonthYearFmt = DateTimeFormatter.ofPattern("dd.MM.yy", locale);
    }
    
    public static String getDiaryDateText(LocalDate date) {
        StringBuilder text = new StringBuilder();
        text.append(getDatePrefix(date));
        text.append(getDateBody(date));
        
        if (locale.getLanguage().equals("en")) {
            text.append(getDaySuffix(date));
        }
        
        return text.toString();
    }
    
     public static String getWeightDialogDateText(LocalDate date) {
        return date.format(dayMonthYearFmt);
     }
    
    private static String getDatePrefix(LocalDate date) {
        LocalDate today = LocalDate.now();
        StringBuilder prefix = new StringBuilder();
        if (date.equals(today)) {
            prefix.append(strToday);
        }
        else if (date.equals(today.minusDays(1))) {
            prefix.append(strYesterday);
        }
        else {
            // Append day of the week with first letter capitalized
            String weekday = date.format(dayOfWeekFmt);
            weekday = weekday.substring(0, 1).toUpperCase() + weekday.substring(1);
            prefix.append(weekday);
        }
        prefix.append(", ");
        
        return prefix.toString();
    }
    
    private static String getDateBody(LocalDate date) {
        return date.format(monthDayFmt);
    }
    
    private static String getDaySuffix(LocalDate date) {
        int day = date.getDayOfMonth();
        if (day >= 11 && day <= 13) {
            return "th";
        }
        switch (day % 10) {
            case 1: return "st";
            case 2: return "nd";
            case 3: return "rd";
            default: return "th";
        }
    }
}
