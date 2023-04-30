package com.app.calorietracker.food;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.fragment.app.Fragment;

import com.app.calorietracker.MainActivity;
import com.app.calorietracker.R;
import com.app.calorietracker.food.list.FoodSelectionManager;

import java.io.IOException;

public class AddFoodActivity extends AppCompatActivity {
    
    private FoodSelectionManager foodSelectionManager;
    private TextView selectionCountView;
    private AppCompatImageButton doneButton;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);     // Force light theme
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);
    
        foodSelectionManager = new FoodSelectionManager(this);
        
        RadioGroup modeSelector = findViewById(R.id.food_mode_selector);
        modeSelector.setOnCheckedChangeListener(switchModeListener);
        ((AppCompatRadioButton) findViewById(R.id.food_mode_select_search)).setChecked(true);  // Search mode by default
        
        selectionCountView = findViewById(R.id.food_text_selected_count);
        doneButton = findViewById(R.id.food_btn_done);
    }
    
    @Override
    public void onStart() {
        super.onStart();
        updateSelectionCount(0);    // init selection count with zero
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
    
    public FoodSelectionManager getFoodSelectionManager() {
        return foodSelectionManager;
    }
    
    public void updateSelectionCount(int count) {
        if (count <= 0) {
            selectionCountView.setVisibility(View.INVISIBLE);
            doneButton.setVisibility(View.INVISIBLE);
            doneButton.setEnabled(false);
            return;
        }
        else if (selectionCountView.getVisibility() == View.INVISIBLE) {
            selectionCountView.setVisibility(View.VISIBLE);
            doneButton.setVisibility(View.VISIBLE);
            doneButton.setEnabled(true);
        }
        selectionCountView.setText(Integer.toString(count));
    }
}