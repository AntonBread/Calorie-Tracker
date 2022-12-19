package com.app.calorietracker.food.list;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.AutoTransition;
import android.transition.ChangeBounds;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.recyclerview.widget.RecyclerView;

import com.app.calorietracker.R;
import com.app.calorietracker.utils.ChartUtils;
import com.github.mikephil.charting.charts.PieChart;

import java.util.List;

public class FoodItemAdapter extends RecyclerView.Adapter<FoodItemAdapter.ViewHolder>{
    
    private final LayoutInflater inflater;
    private final List<FoodItem> itemList;
    private final Context context;
    private final Resources res;
    
    public FoodItemAdapter(Context context, List<FoodItem> itemList) {
        this.itemList = itemList;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        res = context.getResources();
    }
    
    @NonNull
    @Override
    public FoodItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.list_food_item, parent, false);
        v.setBackground(res.getDrawable(R.drawable.back_section_reduced_radius, context.getTheme()));       // Setting background in XML doesn't apply border radius
        return new ViewHolder(v);
    }
    
    @Override
    public void onBindViewHolder(@NonNull FoodItemAdapter.ViewHolder holder, int position) {
        FoodItem foodItem = itemList.get(position);
        holder.nameView.setText(foodItem.getName());
        holder.calsView.setText(String.format(res.getString(R.string.food_list_item_cals), foodItem.getKcal()));
        holder.portionSizeView.setText(String.format(res.getString(R.string.food_list_item_portion_size), 100));
        holder.carbsMassView.setText(String.format(res.getString(R.string.food_list_item_carbs), foodItem.getCarbs_g()));
        holder.fatMassView.setText(String.format(res.getString(R.string.food_list_item_fat), foodItem.getFat_g()));
        holder.proteinMassView.setText(String.format(res.getString(R.string.food_list_item_protein), foodItem.getProtein_g()));
        holder.carbsPctView.setText(String.format(res.getString(R.string.food_list_item_nutrient_pct), foodItem.getCarbsFraction()));
        holder.fatPctView.setText(String.format(res.getString(R.string.food_list_item_nutrient_pct), foodItem.getFatFraction()));
        holder.proteinPctView.setText(String.format(res.getString(R.string.food_list_item_nutrient_pct), foodItem.getProteinFraction()));
        holder.favoriteCheckView.setChecked(foodItem.isFavorite());
        holder.favoriteCheckView.setOnCheckedChangeListener(favoriteCheckListener);
        holder.selectionCheckView.setOnCheckedChangeListener(selectionCheckListener);
        holder.portionInputView.addTextChangedListener(portionSizeInputWatcher);
        ChartUtils.initNutrientPieChart(holder.nutrientChartView, foodItem.getCarbs_g(), foodItem.getFat_g(), foodItem.getProtein_g(), context);
    }
    
    @Override
    public int getItemCount() {
        return itemList.size();
    }
    
    public static class ViewHolder extends RecyclerView.ViewHolder {
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
        
        ViewHolder(View v) {
            super(v);
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
        }
    
        View.OnClickListener expandViewListener = (v) -> {
            View expandableView = v.findViewById(R.id.food_list_item_expandable);
            if (expandableView.getVisibility() == View.GONE) {
                final ChangeBounds transition = new ChangeBounds();
                transition.setDuration(400);
                TransitionManager.beginDelayedTransition((ViewGroup) v, transition);
                expandableView.setVisibility(View.VISIBLE);
            }
            else {
                final ChangeBounds transition = new ChangeBounds();
                transition.setDuration(200);
                TransitionManager.beginDelayedTransition((ViewGroup) v, transition);
                expandableView.setVisibility(View.GONE);
            }
        };
    }
    
    
    
    CompoundButton.OnCheckedChangeListener favoriteCheckListener = (buttonView, isChecked) -> {
    
    };
    
    CompoundButton.OnCheckedChangeListener selectionCheckListener = (buttonView, isChecked) -> {
    
    };
    
    TextWatcher portionSizeInputWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        
        }
    
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        
        }
    
        @Override
        public void afterTextChanged(Editable s) {
        
        }
    };
}
