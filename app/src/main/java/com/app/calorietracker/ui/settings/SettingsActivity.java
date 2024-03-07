package com.app.calorietracker.ui.settings;

import android.content.Context;
import android.content.Intent;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.os.LocaleListCompat;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.app.calorietracker.R;
import com.app.calorietracker.ui.main.MainActivity;
import com.app.calorietracker.ui.quiz.QuizActivity;
import com.app.calorietracker.ui.settings.fileio.stats.PDFExporter;
import com.app.calorietracker.ui.settings.fileio.stats.StatsPDFGenerator;
import com.app.calorietracker.ui.stats.StatsActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

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
                startActivity(new Intent(getBaseContext(), StatsActivity.class));
                return true;
            }
            else {
                return true;
            }
        });
    }
    
    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
            
            Preference languagePreference = findPreference("language");
            if (languagePreference != null) {
                languagePreference.setOnPreferenceChangeListener(this::handleLocaleChange);
            }
            
            Preference startQuizButton = findPreference("quiz");
            if (startQuizButton != null) {
                startQuizButton.setOnPreferenceClickListener(this::handleStartQuizClick);
            }
            
            Preference exportStatsButton = findPreference("export_stats");
            if (exportStatsButton != null) {
                exportStatsButton.setOnPreferenceClickListener(this::handleExportStatsClick);
            }
            
            Preference exportDbButton = findPreference("export_db");
            if (exportDbButton != null) {
                exportDbButton.setOnPreferenceClickListener(this::handleExportDbClick);
            }
            
            Preference importDbButton = findPreference("import_db");
            if (importDbButton != null) {
                importDbButton.setOnPreferenceClickListener(this::handleImportDbClick);
            }
        }
        
        boolean handleLocaleChange(@NonNull Preference preference, Object newValue) {
            Locale appLocale = new Locale((String) newValue);
            LocaleListCompat localeList = LocaleListCompat.create(appLocale);
            AppCompatDelegate.setApplicationLocales(localeList);
            return true;
        }
        
        boolean handleStartQuizClick(@NonNull Preference preference) {
            Intent intent = new Intent(requireActivity(), QuizActivity.class);
            startActivity(intent);
            return true;
        }
        
        boolean handleExportStatsClick(@NonNull Preference preference) {
            Context context = requireActivity();
            PdfDocument statsPdf = StatsPDFGenerator.generate(context);
            if (PDFExporter.export(statsPdf, context)) {
                Toast.makeText(context, R.string.settings_file_export_stats_success, Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(context, R.string.settings_file_export_stats_fail, Toast.LENGTH_LONG).show();
            }
            return true;
        }
        
        boolean handleExportDbClick(@NonNull Preference preference) {
            // TODO: fix method stub
            Toast.makeText(getContext(), "Export Database", Toast.LENGTH_LONG).show();
            return true;
        }
        
        boolean handleImportDbClick(@NonNull Preference preference) {
            // TODO: fix method stub
            Toast.makeText(getContext(), "Import Database", Toast.LENGTH_LONG).show();
            return true;
        }
    }
    
}
