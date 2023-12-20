package com.app.calorietracker.ui.food;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.app.calorietracker.R;
import com.app.calorietracker.ui.food.fragments.AddNewFoodFragment;
import com.app.calorietracker.ui.food.fragments.FoodListFragment;
import com.app.calorietracker.ui.food.fragments.SearchAllFoodListFragment;
import com.app.calorietracker.ui.food.fragments.SearchFavoriteFoodListFragment;
import com.app.calorietracker.ui.food.fragments.SearchHistoryFoodListFragment;
import com.app.calorietracker.ui.food.list.FoodItem;
import com.app.calorietracker.ui.food.list.FoodItemAdapter;
import com.app.calorietracker.ui.food.list.FoodSelectionManager;
import com.app.calorietracker.ui.food.list.SelectionHistoryCacheManager;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

public class AddFoodActivity extends AppCompatActivity {
    
    private final ArrayList<FoodItem> selectedFoodItems = new ArrayList<>();
    private FoodItemAdapter adapter;
    private FoodSelectionManager foodSelectionManager;
    private SelectionHistoryCacheManager selectionHistoryCacheManager;
    
    private RecyclerView selectedFoodsRecyclerView;
    
    private TextView selectionCountView;
    private AppCompatImageButton doneButton;
    
    private FoodActivityIntentVars.MealType mealType;
    private LocalDate date;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);     // Force light theme
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);
        
        Bundle intentData = getIntent().getExtras();
        
        mealType = (FoodActivityIntentVars.MealType) intentData.get(FoodActivityIntentVars.MEAL_TYPE_KEY);
        date = (LocalDate) intentData.get(FoodActivityIntentVars.DATE_KEY);
        
        foodSelectionManager = new FoodSelectionManager(this);
        selectionHistoryCacheManager = new SelectionHistoryCacheManager(this);
        
        RadioGroup modeSelector = findViewById(R.id.food_mode_selector);
        modeSelector.setOnCheckedChangeListener(switchModeListener);
        ((AppCompatRadioButton) findViewById(R.id.food_mode_select_search)).setChecked(true);  // Search mode by default
        
        selectionCountView = findViewById(R.id.food_text_selected_count);
        doneButton = findViewById(R.id.food_btn_done);
        
        setTitleText();
        
        selectedFoodsRecyclerView = findViewById(R.id.food_selected_list);
        adapter = new FoodItemAdapter(this, selectedFoodItems, foodSelectionManager);
        selectedFoodsRecyclerView.setAdapter(adapter);
    
        try {
            populateInitialSelectionList(intentData);
        }
        catch (ClassCastException e) {
            selectedFoodItems.clear();
            adapter.notifyDataSetChanged();
            updateSelectionCount(0);
            Toast.makeText(this, R.string.food_initial_selection_fail, Toast.LENGTH_LONG).show();
        }
    }
    
    public void cancelButtonListener(View v) {
        setResult(FoodActivityIntentVars.ADD_FOOD_CANCEL);
        finish();
    }
    
    public void doneButtonListener(View v) {
        Intent intent = new Intent();
        intent.putExtra(FoodActivityIntentVars.DATE_KEY, date);
        intent.putExtra(FoodActivityIntentVars.MEAL_TYPE_KEY, mealType);
        
        intent.putExtra(FoodActivityIntentVars.FOOD_LIST_KEY, selectedFoodItems);
        setResult(FoodActivityIntentVars.ADD_FOOD_DONE, intent);
        
        try {
            selectionHistoryCacheManager.addItemIDs(selectedFoodItems);
        }
        catch (IOException e) {
            Toast.makeText(this, R.string.food_history_add_fail, Toast.LENGTH_LONG).show();
        }
        
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
            setFragmentView(SearchHistoryFoodListFragment.class);
        }
        else if (checkedId == R.id.food_mode_select_favorite) {
            setFragmentView(SearchFavoriteFoodListFragment.class);
        }
        else if (checkedId == R.id.food_mode_select_add) {
            setFragmentView(AddNewFoodFragment.class);
        }
        // The default option
        else {
            setFragmentView(SearchAllFoodListFragment.class);
        }
    };
    
    public int getFoodItemViewHeight() {
        return selectedFoodsRecyclerView.getChildAt(0).getHeight();
    }
    
    public FoodSelectionManager getFoodSelectionManager() {
        return foodSelectionManager;
    }
    
    public SelectionHistoryCacheManager getSelectionHistoryCacheManager() {
        return selectionHistoryCacheManager;
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
    
    public void transferFoodItemToSelectionList(FoodItem foodItem, int position) {
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.food_mode_fragment_container);
        if (f instanceof FoodListFragment) {
            ((FoodListFragment) f).removeFoodItem(foodItem, position);
        }
        
        selectedFoodItems.add(foodItem);
        adapter.notifyItemInserted(selectedFoodItems.size() - 1);
        // Auto scroll list to bottom
        selectedFoodsRecyclerView.scrollToPosition(selectedFoodItems.size() - 1);
    }
    
    public void transferFoodItemToSearchList(FoodItem foodItem, int position) {
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.food_mode_fragment_container);
        if (f instanceof FoodListFragment) {
            ((FoodListFragment) f).addFoodItem(foodItem);
        }
        
        selectedFoodItems.remove(foodItem);
        adapter.notifyItemRemoved(position);
    }
    
    private void populateInitialSelectionList(Bundle intentData) throws ClassCastException {
        @SuppressWarnings("unchecked")
        ArrayList<FoodItem> initialFoodItems =
                (ArrayList<FoodItem>) intentData.get(FoodActivityIntentVars.FOOD_LIST_KEY);
        
        if (initialFoodItems == null || initialFoodItems.size() == 0) {
            updateSelectionCount(0);
            return;
        }
        
        for (FoodItem foodItem : initialFoodItems) {
            foodSelectionManager.addItem(foodItem, -1);
        }
    }
}
