package com.app.calorietracker.ui.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.os.LocaleListCompat;
import androidx.preference.PreferenceManager;

import java.util.Locale;

public class SettingsManager {
    private final Context context;
    private final SharedPreferences prefs;
    
    public SettingsManager(Context context) {
        this.context = context;
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
    }
    
    public int getCalorieBaseline() {
        return Integer.parseInt(prefs.getString("calories", "2500"));
    }
    
    public float getCarbsBaseline() {
        return Float.parseFloat(prefs.getString("carbs", "250"));
    }
    
    public float getFatBaseline() {
        return Float.parseFloat(prefs.getString("fat", "80"));
    }
    
    public float getProteinBaseline() {
        return Float.parseFloat(prefs.getString("protein", "175"));
    }
    
    public int getWaterBaseline() {
        return Integer.parseInt(prefs.getString("water", "2000"));
    }
    
    public int getWaterStep() {
        return Integer.parseInt(prefs.getString("water_step", "200"));
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
