package com.app.calorietracker.ui.food.list;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.ChangeBounds;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.calorietracker.R;
import com.app.calorietracker.database.AppDatabase;
import com.app.calorietracker.database.foods.FoodItemDatabaseManager;
import com.app.calorietracker.ui.food.fragments.dialog.FoodItemDeleteDialogFragment;
import com.app.calorietracker.ui.food.fragments.dialog.FoodItemEditDialogFragment;
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
    private final FoodListAction actionInterface;
    
    private final Locale decimalFormatLocale = Locale.US;
    
    public FoodItemAdapter(Context context, List<FoodItem> itemList, FoodSelectionManager foodSelectionManager, FoodListAction actionInterface) {
        this.itemList = itemList;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        res = context.getResources();
        this.foodSelectionManager = foodSelectionManager;
        this.actionInterface = actionInterface;
    }
    
    @NonNull
    @Override
    public FoodItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.list_food_item, parent, false);
        v.setBackground(ResourcesCompat.getDrawable(res, R.drawable.back_section_reduced_radius, context.getTheme())); // Setting background in XML doesn't apply border radius
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
        
        holder.nameView.setText(holder.trimItemName(foodItem.getName()));
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
            holder.portionInputView.setText(String.format(decimalFormatLocale, "%d", foodItem.getPortionSize()));
        }
        ChartUtils.initNutrientPieChart(holder.nutrientChartView, foodItem.getCarbsPer100g(), foodItem.getFatPer100g(), foodItem.getProteinPer100g(), context);
        
        holder.adapterNotificationInterface = new AdapterNotificationInterface() {
            @Override
            public void notifyChangeAt(int pos, boolean isExpanded) {
                notifyItemChanged(pos, isExpanded);
            }
            
            @Override
            public void notifyExpandAt(int pos) {
                actionInterface.scrollOnViewHolderExpand(pos);
            }
            
            @Override
            public void notifyDelete(int pos, FoodItem item) {
                actionInterface.onFoodItemDelete(pos, item);
            }
            
            @Override
            public void notifyReplaceAt(int pos, FoodItem oldItem, FoodItem newItem) {
                actionInterface.onFoodItemEdit(pos, oldItem, newItem);
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
                holder.nameView.setText(holder.foodItem.getName());
            }
            else {
                holder.expandableView.setVisibility(View.GONE);
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
        void notifyExpandAt(int pos);
        void notifyDelete(int pos, FoodItem item);
        void notifyReplaceAt(int pos, FoodItem oldItem, FoodItem newItem);
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
            
            v.setOnClickListener(this::handleOnClick);
            v.setOnLongClickListener(this::handleOnLongClick);
            favoriteCheckView.setOnCheckedChangeListener(this::handleFavoriteCheckChange);
            selectionCheckView.setOnCheckedChangeListener(this::handleSelectionCheckChange);
            portionInputView.addTextChangedListener(portionSizeInputWatcher);
        }
        
        private void handleOnClick(View v) {
            if (!isExpanded) {
                expand(v);
            }
            else {
                collapse(v);
            }
        }
        
        private void expand(View v) {
            final ChangeBounds transition = new ChangeBounds();
            transition.setDuration(400);
            TransitionManager.beginDelayedTransition((ViewGroup) v, transition);
            expandableView.setVisibility(View.VISIBLE);
            isExpanded = true;
            nameView.setText(foodItem.getName());
            adapterNotificationInterface.notifyExpandAt(getAdapterPosition());
        }
        
        private void collapse(View v) {
            final ChangeBounds transition = new ChangeBounds();
            transition.setDuration(200);
            TransitionManager.beginDelayedTransition((ViewGroup) v, transition);
            expandableView.setVisibility(View.GONE);
            isExpanded = false;
            nameView.setText(trimItemName(foodItem.getName()));
        }
    
        private String trimItemName(String name) {
            if (name.length() < 25) return name;
        
            return name.substring(0, 25).concat("…");
        }
        
        private boolean handleOnLongClick(View v) {
            // Allow edit/delete only on not selected items
            if (foodItem.isSelected()) {
                return true;
            }
            PopupMenu popupMenu = new PopupMenu(v.getContext(), v);
            popupMenu.inflate(R.menu.popup_food_item);
            popupMenu.setOnMenuItemClickListener(this::handleFoodItemMenuItemClick);
            popupMenu.setForceShowIcon(true);   // Why on earth are icons not displayed by default?!
            popupMenu.show();
            
            return true;
        }
        
        private boolean handleFoodItemMenuItemClick(MenuItem menuItem) {
            AppCompatActivity context = (AppCompatActivity) nameView.getContext();
            int id = menuItem.getItemId();
            if (id == R.id.food_item_menu_edit) {
                return handleFoodItemEditClick(context);
            }
            else if (id == R.id.food_item_menu_delete) {
                return handleFoodItemDeleteClick(context);
            }
            else {
                return false;
            }
        }
        
        private boolean handleFoodItemEditClick(AppCompatActivity context) {
            FragmentManager fm = context.getSupportFragmentManager();
    
            // Don't know what happens when you try
            // to assign multiple listeners with the same key,
            // but it doesn't hurt to clear just in case
            fm.clearFragmentResultListener(FoodItemEditDialogFragment.REQUEST_KEY);
            
            fm.setFragmentResultListener(FoodItemEditDialogFragment.REQUEST_KEY,
                                         context,
                                         this::handleEditDialogFragmentResult);
            
            FoodItemEditDialogFragment editDialog = new FoodItemEditDialogFragment();
            Bundle args = new Bundle();
            args.putSerializable(FoodItemEditDialogFragment.ARGS_KEY, this.foodItem);
            editDialog.setArguments(args);
            editDialog.show(fm, null);
            
            return true;
        }
        
        private void handleEditDialogFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
            FoodItem foodItemNew = (FoodItem) result.get(FoodItemEditDialogFragment.RESULT_KEY);
            if (foodItemNew == null) {
                return;
            }
    
            AppCompatActivity context = (AppCompatActivity) nameView.getContext();
            boolean success = FoodItemDatabaseManager.updateItem(AppDatabase.getInstance().foodItemDao(), foodItemNew);
            if (!success) {
                String msg = context.getString(R.string.food_list_item_edit_fail);
                Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
                return;
            }
            adapterNotificationInterface.notifyReplaceAt(getAdapterPosition(), this.foodItem, foodItemNew);
        }
        
        private boolean handleFoodItemDeleteClick(AppCompatActivity context) {
            FragmentManager fm = context.getSupportFragmentManager();
            
            // Same reason as in handleFoodItemEditClick method
            fm.clearFragmentResultListener(FoodItemDeleteDialogFragment.REQUEST_KEY);
            
            fm.setFragmentResultListener(FoodItemDeleteDialogFragment.REQUEST_KEY,
                                         context,
                                         this::handleDeleteDialogFragmentResult);
            new FoodItemDeleteDialogFragment().show(fm, null);
            return true;
        }
        
        private void handleDeleteDialogFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
            boolean delete = result.getBoolean(FoodItemDeleteDialogFragment.RESULT_KEY, false);
            if (!delete) {
                return;
            }
            
            AppCompatActivity context = (AppCompatActivity) nameView.getContext();
            long id = foodItem.getId();
            boolean success = FoodItemDatabaseManager.markItemDeleted(AppDatabase.getInstance().foodItemDao(), id);
            if (!success) {
                String msg = context.getString(R.string.food_list_item_delete_fail);
                Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
                return;
            }
            adapterNotificationInterface.notifyDelete(getAdapterPosition(), this.foodItem);
        }
        
        private void handleFavoriteCheckChange(CompoundButton buttonView, boolean isChecked) {
            if (!buttonView.isPressed()) return;    // Prevent false calls
    
            AppDatabase db = AppDatabase.getInstance();
            boolean success = FoodItemDatabaseManager.updateFavoriteCheck(db.foodItemDao(), foodItem.getId(), isChecked);
            if (success) {
                foodItem.setFavorite(isChecked);
                adapterNotificationInterface.notifyChangeAt(getAdapterPosition(), isExpanded);
            }
        }
        
        private void handleSelectionCheckChange(CompoundButton buttonView, boolean isChecked) {
            if (!buttonView.isPressed()) return;    // Prevent false calls
    
            foodItem.setSelected(isChecked);
            adapterNotificationInterface.notifyChangeAt(getAdapterPosition(), isExpanded);
    
            if (isChecked) {
                foodSelectionManager.addItem(foodItem, getAdapterPosition());
            }
            else {
                foodSelectionManager.removeItem(foodItem, getAdapterPosition());
            }
        }
    
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
