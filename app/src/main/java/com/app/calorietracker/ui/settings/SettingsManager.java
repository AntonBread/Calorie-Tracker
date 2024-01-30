package com.app.calorietracker.ui.settings;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.os.LocaleListCompat;
import androidx.preference.PreferenceManager;

import java.util.Locale;

public class SettingsManager {
    
    private static final int DEFAULT_CALORIES = 2500;
    private static final float DEFAULT_CARBS = 300.0f;
    private static final float DEFAULT_FAT = 80.0f;
    private static final float DEFAULT_PROTEIN = 125.0f;
    private static final int DEFAULT_WATER = 3000;
    private static final int DEFAULT_WATER_STEP = 100;
    
    private final SharedPreferences prefs;
    
    public SettingsManager(Context context) {
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
    }
    
    public int getCalorieBaseline() {
        try {
            return Integer.parseInt(prefs.getString("calories", String.valueOf(DEFAULT_CALORIES)));
        }
        catch (NumberFormatException e) {
            return DEFAULT_CALORIES;
        }
    }
    
    public float getCarbsBaseline() {
        try {
            return Float.parseFloat(prefs.getString("carbs", String.valueOf(DEFAULT_CARBS)));
        }
        catch (NumberFormatException e) {
            return DEFAULT_CARBS;
        }
    }
    
    public float getFatBaseline() {
        try {
            return Float.parseFloat(prefs.getString("fat", String.valueOf(DEFAULT_FAT)));
        }
        catch (NumberFormatException e) {
            return DEFAULT_FAT;
        }
    }
    
    public float getProteinBaseline() {
        try {
            return Float.parseFloat(prefs.getString("protein", String.valueOf(DEFAULT_PROTEIN)));
        }
        catch (NumberFormatException e) {
            return DEFAULT_PROTEIN;
        }
    }
    
    public int getWaterBaseline() {
        try {
            return Integer.parseInt(prefs.getString("water", String.valueOf(DEFAULT_WATER)));
        }
        catch (NumberFormatException e) {
            return DEFAULT_WATER;
        }
    }
    
    public int getWaterStep() {
        try {
            return Integer.parseInt(prefs.getString("water_step", String.valueOf(DEFAULT_WATER_STEP)));
        }
        catch (NumberFormatException e) {
            return DEFAULT_WATER_STEP;
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
    
    public int getUserHeight_cm() {
        return prefs.getInt("height", -1);
    }
    
    public void setUserHeight_cm(int height) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("height", height);
        editor.apply();
    }
    
    public int getCurrentWeight_g() {
        return prefs.getInt("weight", 0);
    }
    
    public void setCurrentWeight_g(int weight_g) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("weight", weight_g);
        editor.apply();
    }
    
    public void resetBaselinePrefs() {
        setBaselinePrefs(DEFAULT_CALORIES, DEFAULT_CARBS, DEFAULT_FAT, DEFAULT_PROTEIN, DEFAULT_WATER);
    }
    
    public void setBaselinePrefs(int calories, float carbs, float fat, float protein, int water) {
        SharedPreferences.Editor editor = prefs.edit();
        
        editor.putString("calories", String.valueOf(calories));
        editor.putString("carbs", String.valueOf(carbs));
        editor.putString("fat", String.valueOf(fat));
        editor.putString("protein", String.valueOf(protein));
        editor.putString("water", String.valueOf(water));
        
        editor.apply();
    }
    
}
