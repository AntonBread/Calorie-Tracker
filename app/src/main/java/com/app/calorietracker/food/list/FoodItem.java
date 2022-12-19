package com.app.calorietracker.food.list;

public class FoodItem {
    
    private final String name;
    private final int kcal_per_100g;
    private final int carbs_per_100g;
    private final int fat_per_100g;
    private final int protein_per_100g;
    private final boolean isFavorite;
    
    private final int nutrient_per_100g;
    
    public FoodItem(String name, int kcal, int carbs_g, int fat_g, int protein_g, boolean isFavorite) {
        this.name = name;
        this.kcal_per_100g = kcal;
        this.carbs_per_100g = carbs_g;
        this.fat_per_100g = fat_g;
        this.protein_per_100g = protein_g;
        this.isFavorite = isFavorite;
        
        this.nutrient_per_100g = carbs_g + fat_g + protein_g;
    }
    
    public String getName() {
        return name;
    }
    
    public int getKcal() {
        return kcal_per_100g;
    }
    
    public int getCarbs_g() {
        return carbs_per_100g;
    }
    
    public int getFat_g() {
        return fat_per_100g;
    }
    
    public int getProtein_g() {
        return protein_per_100g;
    }
    
    public boolean isFavorite() {
        return isFavorite;
    }
    
    public float getCarbsFraction() {
        return (float)carbs_per_100g / nutrient_per_100g * 100;
    }
    
    public float getFatFraction() {
        return (float)fat_per_100g / nutrient_per_100g * 100;
    }
    
    public float getProteinFraction() {
        return (float)protein_per_100g / nutrient_per_100g * 100;
    }
}
