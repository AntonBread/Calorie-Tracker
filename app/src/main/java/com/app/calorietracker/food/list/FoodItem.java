package com.app.calorietracker.food.list;

import com.app.calorietracker.database.foods.FoodItemEntity;

public class FoodItem {
    private final String name;
    private final int kcal_per_100g;
    private final int carbs_mg_per_100g;
    private final int fat_mg_per_100g;
    private final int protein_mg_per_100g;
    private final boolean isFavorite;
    
    // Default portion size if 100 g
    private int portionSize_g = 100;
    
    private final int nutrients_mg_per_100g;
    
    public FoodItem(String name, int kcal, int carbs_mg, int fat_mg, int protein_mg, boolean isFavorite) {
        this.name = name;
        this.kcal_per_100g = kcal;
        this.carbs_mg_per_100g = carbs_mg;
        this.fat_mg_per_100g = fat_mg;
        this.protein_mg_per_100g = protein_mg;
        this.isFavorite = isFavorite;
        
        this.nutrients_mg_per_100g = carbs_mg + fat_mg + protein_mg;
    }
    
    public FoodItem(FoodItemEntity entity) {
        this.name = entity.getName();
        this.kcal_per_100g = entity.getKcal();
        this.carbs_mg_per_100g = entity.getCarbs();
        this.fat_mg_per_100g = entity.getFat();
        this.protein_mg_per_100g = entity.getProtein();
        this.isFavorite = entity.isFavorite();
        
        this.nutrients_mg_per_100g = this.carbs_mg_per_100g + this.fat_mg_per_100g + this.protein_mg_per_100g;
    }
    
    public FoodItemEntity toEntity() {
        FoodItemEntity entity = new FoodItemEntity();
        entity.setName(this.name);
        entity.setKcal(this.kcal_per_100g);
        entity.setCarbs(this.carbs_mg_per_100g);
        entity.setFat(this.fat_mg_per_100g);
        entity.setProtein(this.protein_mg_per_100g);
        entity.setFavorite(this.isFavorite);
        return entity;
    }
    
    public String getName() {
        return name;
    }
    
    public int getKcal() {
        return kcal_per_100g;
    }
    
    public int getCarbs() {
        return carbs_mg_per_100g;
    }
    
    public int getFat() {
        return fat_mg_per_100g;
    }
    
    public int getProtein() {
        return protein_mg_per_100g;
    }
    
    public boolean isFavorite() {
        return isFavorite;
    }
    
    public int getPortionSize() {return this.portionSize_g;}
    
    public void setPortionSize(int portionSize_g) {this.portionSize_g = portionSize_g;}
    
    public float getCarbsFraction() {
        return (float) carbs_mg_per_100g / nutrients_mg_per_100g * 100;
    }
    
    public float getFatFraction() {
        return (float) fat_mg_per_100g / nutrients_mg_per_100g * 100;
    }
    
    public float getProteinFraction() {
        return (float) protein_mg_per_100g / nutrients_mg_per_100g * 100;
    }
}
