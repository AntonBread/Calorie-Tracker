package com.app.calorietracker.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DateUtils {
    
    private static DateTimeFormatter dayOfWeekFmt;
    private static DateTimeFormatter monthDayFmt;
    private static String strToday;
    private static String strYesterday;
    private static Locale locale;
    
    public static void init(String today, String yesterday, Locale l) {
        strToday = today;
        strYesterday = yesterday;
        locale = l;
        
        dayOfWeekFmt = DateTimeFormatter.ofPattern("EEEE", locale);
        monthDayFmt = DateTimeFormatter.ofPattern("MMMM d", locale);
    }
    
    public static String getDateText(LocalDate date) {
        StringBuilder text = new StringBuilder();
        text.append(getDatePrefix(date));
        text.append(getDateBody(date));
        
        if (locale.getLanguage().equals("en")) {
            text.append(getDaySuffix(date));
        }
        
        return text.toString();
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
            prefix.append(date.format(dayOfWeekFmt));
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
