package com.app.calorietracker.food;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.fragment.app.Fragment;

import com.app.calorietracker.MainActivity;
import com.app.calorietracker.R;

public class AddFoodActivity extends AppCompatActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);     // Force light theme
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);
        
        RadioGroup modeSelector = findViewById(R.id.food_mode_selector);
        modeSelector.setOnCheckedChangeListener(switchModeListener);
        ((AppCompatRadioButton) findViewById(R.id.food_mode_select_search)).setChecked(true);  // Search mode by default
        
        ((TextView) findViewById(R.id.food_text_selected_count)).setText(String.valueOf(2));
    }
    
    public void cancelButtonListener(View v) {
        startActivity(new Intent(this, MainActivity.class));
    }
    
    private void setFragmentView(Class<? extends Fragment> fragment) {
        getSupportFragmentManager().beginTransaction()
                                   .setCustomAnimations(
                                           R.anim.food_fragment_slide_in,
                                           R.anim.food_fragment_fade_out,
                                           R.anim.food_fragment_slide_in,
                                           R.anim.food_fragment_fade_out
                                   )
                                   .replace(R.id.food_mode_fragment_container, fragment, null)
                                   .commit();
    }
    
    private final RadioGroup.OnCheckedChangeListener switchModeListener = (group, checkedId) -> {
        if (checkedId == R.id.food_mode_select_history) {
            setFragmentView(HistoryFoodFragment.class);
        }
        else if (checkedId == R.id.food_mode_select_favorite) {
            setFragmentView(FavoriteFoodFragment.class);
        }
        else if (checkedId == R.id.food_mode_select_add) {
            setFragmentView(AddNewFoodFragment.class);
        }
        // The default option
        else {
            setFragmentView(SearchFoodFragment.class);
        }
    };
}