package com.app.calorietracker.ui.food.list;

import android.content.Context;
import android.content.res.Resources;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.ChangeBounds;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.recyclerview.widget.RecyclerView;

import com.app.calorietracker.R;
import com.app.calorietracker.database.AppDatabase;
import com.app.calorietracker.database.foods.FoodItemDatabaseManager;
import com.app.calorietracker.utils.ChartUtils;
import com.github.mikephil.charting.charts.PieChart;

import java.util.List;
import java.util.Locale;

public class FoodItemAdapter extends RecyclerView.Adapter<FoodItemAdapter.ViewHolder>{
    
    private final LayoutInflater inflater;
    private final List<FoodItem> itemList;
    private final Context context;
    private final Resources res;
    private final FoodSelectionManager foodSelectionManager;
    
    private final Locale decimalFormatLocale = Locale.US;
    
    public FoodItemAdapter(Context context, List<FoodItem> itemList, FoodSelectionManager foodSelectionManager) {
        this.itemList = itemList;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        res = context.getResources();
        this.foodSelectionManager = foodSelectionManager;
    }
    
    @NonNull
    @Override
    public FoodItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.list_food_item, parent, false);
        v.setBackground(res.getDrawable(R.drawable.back_section_reduced_radius, context.getTheme())); // Setting background in XML doesn't apply border radius
        return new ViewHolder(v);
    }
    
    @Override
    public void onBindViewHolder(@NonNull FoodItemAdapter.ViewHolder holder, int position) {
        FoodItem foodItem = itemList.get(position);
        boolean isSelected = foodSelectionManager.isSelected(foodItem);
        foodItem.setSelected(isSelected);
        if (isSelected) {
            foodItem.setPortionSize(foodSelectionManager.getSelectedPortionSize(foodItem));
        }
        holder.foodItem = foodItem;
        holder.foodSelectionManager = this.foodSelectionManager;
        
        holder.nameView.setText(foodItem.getName());
        holder.calsView.setText(String.format(decimalFormatLocale, res.getString(R.string.food_list_item_cals), foodItem.getKcalCurrent()));
        holder.portionSizeView.setText(String.format(decimalFormatLocale, res.getString(R.string.food_list_item_portion_size), foodItem.getPortionSize()));
        holder.carbsMassView.setText(String.format(decimalFormatLocale, res.getString(R.string.food_list_item_carbs), foodItem.getCarbsCurrent()));
        holder.fatMassView.setText(String.format(decimalFormatLocale, res.getString(R.string.food_list_item_fat), foodItem.getFatCurrent()));
        holder.proteinMassView.setText(String.format(decimalFormatLocale, res.getString(R.string.food_list_item_protein), foodItem.getProteinCurrent()));
        holder.carbsPctView.setText(String.format(decimalFormatLocale, res.getString(R.string.food_list_item_nutrient_pct), foodItem.getCarbsFraction()));
        holder.fatPctView.setText(String.format(decimalFormatLocale, res.getString(R.string.food_list_item_nutrient_pct), foodItem.getFatFraction()));
        holder.proteinPctView.setText(String.format(decimalFormatLocale, res.getString(R.string.food_list_item_nutrient_pct), foodItem.getProteinFraction()));
        holder.favoriteCheckView.setChecked(foodItem.isFavorite());
        holder.selectionCheckView.setChecked(foodItem.isSelected());
        // Don't set text on active EditText views
        if (!holder.portionInputView.hasFocus()) {
            holder.portionInputView.setText(Integer.toString(foodItem.getPortionSize()));
        }
        ChartUtils.initNutrientPieChart(holder.nutrientChartView, foodItem.getCarbsPer100g(), foodItem.getFatPer100g(), foodItem.getProteinPer100g(), context);
        
        holder.adapterNotificationInterface = new AdapterNotificationInterface() {
            @Override
            public void notifyChangeAt(int pos, boolean isExpanded) {
                notifyItemChanged(pos, isExpanded);
            }
        };
    }
    
    @Override
    public void onBindViewHolder(@NonNull FoodItemAdapter.ViewHolder holder, int position, @Nullable List<Object> payloads) {
        onBindViewHolder(holder, position);
        
        if (payloads == null) return;
        
        try {
            boolean isExpanded = (boolean) payloads.get(0);
            if (isExpanded) {
                holder.expandableView.setVisibility(View.VISIBLE);
            }
        }
        catch (IndexOutOfBoundsException ignored){}
    }
    
    @Override
    public int getItemCount() {
        return itemList.size();
    }
    
    interface AdapterNotificationInterface {
        void notifyChangeAt(int pos, boolean isExpanded);
    }
    
    public static class ViewHolder extends RecyclerView.ViewHolder {
        FoodItem foodItem;
    
        AdapterNotificationInterface adapterNotificationInterface;
        
        FoodSelectionManager foodSelectionManager;
        
        final View expandableView;
        
        final TextView nameView;
        final TextView calsView;
        final TextView portionSizeView;
        final TextView carbsPctView;
        final TextView fatPctView;
        final TextView proteinPctView;
        final TextView carbsMassView;
        final TextView fatMassView;
        final TextView proteinMassView;
        
        final PieChart nutrientChartView;
        final EditText portionInputView;
        final AppCompatCheckBox favoriteCheckView;
        final AppCompatCheckBox selectionCheckView;
        
        boolean isExpanded = false;
        
        ViewHolder(View v) {
            super(v);
            expandableView = v.findViewById(R.id.food_list_item_expandable);
            nameView = v.findViewById(R.id.food_list_item_name);
            calsView = v.findViewById(R.id.food_list_item_cals);
            portionSizeView = v.findViewById(R.id.food_list_item_portion_size);
            carbsPctView = v.findViewById(R.id.food_list_item_carbs_pct);
            fatPctView = v.findViewById(R.id.food_list_item_fat_pct);
            proteinPctView = v.findViewById(R.id.food_list_item_protein_pct);
            carbsMassView = v.findViewById(R.id.food_list_item_carbs_g);
            fatMassView = v.findViewById(R.id.food_list_item_fat_g);
            proteinMassView = v.findViewById(R.id.food_list_item_protein_g);
            nutrientChartView = v.findViewById(R.id.food_list_item_nutrient_chart);
            portionInputView = v.findViewById(R.id.food_list_item_portion_size_input);
            favoriteCheckView = v.findViewById(R.id.food_list_item_btn_favorite);
            selectionCheckView = v.findViewById(R.id.food_list_item_btn_select);
            
            v.setOnClickListener(expandViewListener);
            favoriteCheckView.setOnCheckedChangeListener(favoriteCheckListener);
            selectionCheckView.setOnCheckedChangeListener(selectionCheckListener);
            portionInputView.addTextChangedListener(portionSizeInputWatcher);
        }
    
        View.OnClickListener expandViewListener = (v) -> {
            View expandableView = v.findViewById(R.id.food_list_item_expandable);
            final ChangeBounds transition = new ChangeBounds();
            if (!isExpanded) {
                transition.setDuration(400);
                TransitionManager.beginDelayedTransition((ViewGroup) v, transition);
                expandableView.setVisibility(View.VISIBLE);
                isExpanded = true;
            }
            else {
                transition.setDuration(200);
                TransitionManager.beginDelayedTransition((ViewGroup) v, transition);
                expandableView.setVisibility(View.GONE);
                isExpanded = false;
            }
        };
    
        CompoundButton.OnCheckedChangeListener favoriteCheckListener = (buttonView, isChecked) -> {
            if (!buttonView.isPressed()) return;    // Prevent false calls
            
            AppDatabase db = AppDatabase.getInstance();
            boolean success = FoodItemDatabaseManager.updateFavoriteCheck(db.foodItemDao(), foodItem.getId(), isChecked);
            //Log.d("FAVORITE UPDATE SUCCESSFUL", Boolean.toString(success));
            if (success) {
                foodItem.setFavorite(isChecked);
                adapterNotificationInterface.notifyChangeAt(getAdapterPosition(), isExpanded);
            }
        };
    
        CompoundButton.OnCheckedChangeListener selectionCheckListener = (buttonView, isChecked) -> {
            if (!buttonView.isPressed()) return;    // Prevent false calls
            
            foodItem.setSelected(isChecked);
            adapterNotificationInterface.notifyChangeAt(getAdapterPosition(), isExpanded);
            
            if (isChecked) {
                foodSelectionManager.addItem(foodItem);
            }
            else {
                foodSelectionManager.removeItem(foodItem);
            }
            
        };
    
        TextWatcher portionSizeInputWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        
            @Override
            public void afterTextChanged(Editable s) {
                boolean isInFocus = portionInputView.hasFocus();
                if (!isInFocus) return;     // prevent false calls
                
                int portionSize;
                try {
                    portionSize = Integer.parseInt(s.toString().trim());
                }
                catch (NumberFormatException e) {
                    e.printStackTrace();
                    return;
                }
                foodItem.setPortionSize(portionSize);
                foodSelectionManager.updateItemPortionSize(foodItem);
                adapterNotificationInterface.notifyChangeAt(getAdapterPosition(), isExpanded);
            }
        };
    }
    
}
