package com.app.calorietracker.database.foods;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "FOODS", indices = {
        @Index(name = "favorite_index", value = {"is_favorite"})
})
public class FoodItemEntity {
    
    @PrimaryKey(autoGenerate = true)
    private long id;
    
    private String name;
    
    @ColumnInfo(name = "kcal_per_100g")
    private int kcal;
    
    @ColumnInfo(name = "carbs_mg_per_100g")
    private float carbs;
    
    @ColumnInfo(name = "fat_mg_per_100g")
    private float fat;
    
    @ColumnInfo(name = "protein_mg_per_100g")
    private float protein;
    
    @ColumnInfo(name = "is_favorite")
    private boolean isFavorite;
    
    public long getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public int getKcal() {
        return kcal;
    }
    
    public float getCarbs() {
        return carbs;
    }
    
    public float getFat() {
        return fat;
    }
    
    public float getProtein() {
        return protein;
    }
    
    public boolean isFavorite() {
        return isFavorite;
    }
    
    public void setId(long id) {
        this.id = id;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public void setKcal(int kcal_per_100g) {
        this.kcal = kcal_per_100g;
    }
    
    public void setCarbs(float carbs_mg_per_100g) {
        this.carbs = carbs_mg_per_100g;
    }
    
    public void setFat(float fat_mg_per_100g) {
        this.fat = fat_mg_per_100g;
    }
    
    public void setProtein(float protein_mg_per_100g) {
        this.protein = protein_mg_per_100g;
    }
    
    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }
}
