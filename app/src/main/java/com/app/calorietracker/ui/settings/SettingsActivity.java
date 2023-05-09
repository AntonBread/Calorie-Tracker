package com.app.calorietracker.ui.settings;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.os.LocaleListCompat;
import androidx.preference.EditTextPreference;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.app.calorietracker.R;
import com.app.calorietracker.ui.main.MainActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Arrays;
import java.util.Locale;

public class SettingsActivity extends AppCompatActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new SettingsFragment())
                    .commit();
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        
        initNavbar();
    }
    
    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
            
            findPreference("language").setOnPreferenceChangeListener((preference, newValue) -> {
                LocaleListCompat appLocale = LocaleListCompat.create(new Locale((String) newValue));
                AppCompatDelegate.setApplicationLocales(appLocale);
                return true;
            });
        }
    }
    
    private void initNavbar() {
        BottomNavigationView navbar = findViewById(R.id.main_navbar);
        navbar.setSelectedItemId(R.id.navigation_settings);
        navbar.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.navigation_main) {
                startActivity(new Intent(getBaseContext(), MainActivity.class));
                return true;
            }
            else if (id == R.id.navigation_progress) {
                return true;
            }
            else {
                return true;
            }
        });
    }
    
}