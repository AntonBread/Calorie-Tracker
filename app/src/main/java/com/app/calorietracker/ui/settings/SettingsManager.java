package com.app.calorietracker.ui.settings;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.os.LocaleListCompat;
import androidx.preference.PreferenceManager;

import java.util.Locale;

public class SettingsManager {
    private final SharedPreferences prefs;
    
    public SettingsManager(Context context) {
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
    }
    
    public int getCalorieBaseline() {
        try {
            return Integer.parseInt(prefs.getString("calories", "2500"));
        }
        catch (NumberFormatException e) {
            return 2500;
        }
    }
    
    public float getCarbsBaseline() {
        try {
            return Float.parseFloat(prefs.getString("carbs", "250"));
        }
        catch (NumberFormatException e) {
            return 250.0f;
        }
    }
    
    public float getFatBaseline() {
        try {
            return Float.parseFloat(prefs.getString("fat", "80"));
        }
        catch (NumberFormatException e) {
            return 80.0f;
        }
    }
    
    public float getProteinBaseline() {
        try {
            return Float.parseFloat(prefs.getString("protein", "175"));
        }
        catch (NumberFormatException e) {
            return 175.0f;
        }
    }
    
    public int getWaterBaseline() {
        try {
            return Integer.parseInt(prefs.getString("water", "2000"));
        }
        catch (NumberFormatException e) {
            return 2000;
        }
    }
    
    public int getWaterStep() {
        try {
            return Integer.parseInt(prefs.getString("water_step", "200"));
        }
        catch (NumberFormatException e) {
            return 200;
        }
    }
    
    public Locale getLocale() {
        String langCode = prefs.getString("language", "en");
        return new Locale(langCode);
    }
    
    public void applyLocalePreference() {
        Locale locale = getLocale();
        
        LocaleListCompat appLocale = LocaleListCompat.create(locale);
        AppCompatDelegate.setApplicationLocales(appLocale);
    }
    
}
