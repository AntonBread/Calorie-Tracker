package com.app.calorietracker.ui.food.list;

import com.app.calorietracker.database.foods.FoodItemEntity;

import java.io.Serializable;

// Have to implement Serializable
// to put foodItem objects inside intent bundle
public class FoodItem implements Serializable {
    private final long id;  // db record id
    
    private final String name;
    private final int kcal_per_100g;
    private final float carbs_g_per_100g;
    private final float fat_g_per_100g;
    private final float protein_g_per_100g;
    private boolean isFavorite;
    
    // Default portion size is 100 g
    private int portionSize_g = 100;
    // Default selection state is false
    private boolean selected = false;
    // Variables that track current nutritional value based on portion size:
    private int kcal;
    private float carbs_g;
    private float fat_g;
    private float protein_g;
    
    private final float nutrients_g_per_100g;
    
    public FoodItem(long id, String name, int kcal, int carbs_g, int fat_g, int protein_g, boolean isFavorite) {
        this.id = id;
        this.name = name;
        this.kcal_per_100g = kcal;
        this.kcal = kcal;
        this.carbs_g_per_100g = carbs_g;
        this.carbs_g = carbs_g;
        this.fat_g_per_100g = fat_g;
        this.fat_g = fat_g;
        this.protein_g_per_100g = protein_g;
        this.protein_g = protein_g;
        this.isFavorite = isFavorite;
        
        this.nutrients_g_per_100g = carbs_g + fat_g + protein_g;
    }
    
    public FoodItem(FoodItemEntity entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.kcal_per_100g = entity.getKcal();
        this.kcal = this.kcal_per_100g;
        this.carbs_g_per_100g = entity.getCarbs();
        this.carbs_g = this.carbs_g_per_100g;
        this.fat_g_per_100g = entity.getFat();
        this.fat_g = this.fat_g_per_100g;
        this.protein_g_per_100g = entity.getProtein();
        this.protein_g = this.protein_g_per_100g;
        this.isFavorite = entity.isFavorite();
        
        this.nutrients_g_per_100g = this.carbs_g_per_100g + this.fat_g_per_100g + this.protein_g_per_100g;
    }
    
    public FoodItemEntity toEntity() {
        FoodItemEntity entity = new FoodItemEntity();
        entity.setName(this.name);
        entity.setKcal(this.kcal_per_100g);
        entity.setCarbs(this.carbs_g_per_100g);
        entity.setFat(this.fat_g_per_100g);
        entity.setProtein(this.protein_g_per_100g);
        entity.setFavorite(this.isFavorite);
        return entity;
    }
    
    public long getId() {
        return this.id;
    }
    
    public String getName() {
        return name;
    }
    
    public int getKcalPer100g() {
        return kcal_per_100g;
    }
    
    public int getKcalCurrent() {
        return this.kcal;
    }
    
    public float getCarbsPer100g() {
        return carbs_g_per_100g;
    }
    
    public float getCarbsCurrent() {
        return this.carbs_g;
    }
    
    public float getFatPer100g() {
        return fat_g_per_100g;
    }
    
    public float getFatCurrent() {
        return this.fat_g;
    }
    
    public float getProteinPer100g() {
        return protein_g_per_100g;
    }
    
    public float getProteinCurrent() {
        return this.protein_g;
    }
    
    public boolean isFavorite() {
        return isFavorite;
    }
    
    public int getPortionSize() {return this.portionSize_g;}
    
    public boolean isSelected() {
        return selected;
    }
    
    public void setFavorite(boolean isFavorite) {
        this.isFavorite = isFavorite;
    }
    
    public void setPortionSize(int portionSize_g) {
        this.portionSize_g = portionSize_g;
        updateNutritionalValue();
    }
    
    private void updateNutritionalValue() {
        float portionSizeMult = portionSize_g / 100.0f;
        kcal = (int)(kcal_per_100g * portionSizeMult);
        carbs_g = carbs_g_per_100g * portionSizeMult;
        fat_g = fat_g_per_100g * portionSizeMult;
        protein_g = protein_g_per_100g * portionSizeMult;
    }
    
    public void setSelected(boolean selected) {
        this.selected = selected;
    }
    
    public float getCarbsFraction() {
        return carbs_g_per_100g / nutrients_g_per_100g * 100;
    }
    
    public float getFatFraction() {
        return fat_g_per_100g / nutrients_g_per_100g * 100;
    }
    
    public float getProteinFraction() {
        return protein_g_per_100g / nutrients_g_per_100g * 100;
    }
}
