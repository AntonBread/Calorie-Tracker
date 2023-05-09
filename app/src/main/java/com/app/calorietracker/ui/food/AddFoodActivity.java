package com.app.calorietracker.ui.food;

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

import com.app.calorietracker.R;
import com.app.calorietracker.ui.food.fragments.AddNewFoodFragment;
import com.app.calorietracker.ui.food.fragments.FavoriteFoodFragment;
import com.app.calorietracker.ui.food.fragments.HistoryFoodFragment;
import com.app.calorietracker.ui.food.fragments.SearchFoodFragment;
import com.app.calorietracker.ui.food.list.FoodSelectionManager;

import java.time.LocalDate;

public class AddFoodActivity extends AppCompatActivity {
    
    private FoodSelectionManager foodSelectionManager;
    private TextView selectionCountView;
    private AppCompatImageButton doneButton;
    
    private FoodActivityIntentVars.MealType mealType;
    private LocalDate date;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);     // Force light theme
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);
        
        mealType = (FoodActivityIntentVars.MealType) getIntent().getExtras().get(FoodActivityIntentVars.MEAL_TYPE_KEY);
        date = (LocalDate) getIntent().getExtras().get(FoodActivityIntentVars.DATE_KEY);
        
        foodSelectionManager = new FoodSelectionManager(this);
        
        RadioGroup modeSelector = findViewById(R.id.food_mode_selector);
        modeSelector.setOnCheckedChangeListener(switchModeListener);
        ((AppCompatRadioButton) findViewById(R.id.food_mode_select_search)).setChecked(true);  // Search mode by default
        
        selectionCountView = findViewById(R.id.food_text_selected_count);
        doneButton = findViewById(R.id.food_btn_done);
        
        setTitleText();
    }
    
    @Override
    public void onStart() {
        super.onStart();
        updateSelectionCount(0);    // init selection count with zero
    }
    
    public void cancelButtonListener(View v) {
        setResult(FoodActivityIntentVars.ADD_FOOD_CANCEL);
        finish();
    }
    
    public void doneButtonListener(View v) {
        Intent intent = new Intent();
        intent.putExtra(FoodActivityIntentVars.DATE_KEY, date);
        intent.putExtra(FoodActivityIntentVars.MEAL_TYPE_KEY, mealType);
        intent.putExtra(FoodActivityIntentVars.FOOD_LIST_KEY, foodSelectionManager.getSelectedFoodItems());
        setResult(FoodActivityIntentVars.ADD_FOOD_DONE, intent);
        finish();
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
        selectionCountView.setText(String.format(getString(R.string.placeholder_number), count));
    }
    
    private void setTitleText() {
        TextView titleView = findViewById(R.id.food_text_meal_title);
        String title;
        switch (mealType) {
            case BREAKFAST:
            default:
                title = getString(R.string.food_title_breakfast);
                break;
            case LUNCH:
                title = getString(R.string.food_title_lunch);
                break;
            case DINNER:
                title = getString(R.string.food_title_dinner);
                break;
            case SNACKS:
                title = getString(R.string.food_title_snacks);
        }
        titleView.setText(title);
    }
}