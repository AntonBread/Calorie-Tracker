package com.app.calorietracker.ui.food.list;

import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;

import com.app.calorietracker.database.AppDatabase;
import com.app.calorietracker.database.foods.FoodItemEntity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class SelectionHistoryCacheManager {
    private static final String HISTORY_CACHE_FILE_NAME = "history";
    // Cache file stores up to HISTORY_CACHE_LIMIT IDs of previously selected items
    private final File cacheFile;
    
    public static final int HISTORY_CACHE_LIMIT = 50;
    
    public SelectionHistoryCacheManager(Context context) {
        String filePath = context.getCacheDir().getAbsolutePath();
        this.cacheFile = new File(filePath, HISTORY_CACHE_FILE_NAME);
    }
    
    private boolean createFile() {
        if (cacheFile.exists()) return true;
        
        try {
            return cacheFile.createNewFile();
        }
        catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public void addItemIDs(ArrayList<FoodItem> foodItems) throws IOException {
        if (!createFile()) {
            throw new IOException("Cache file creation exception");
        }
        
        List<String> newIDs = getIdStringsFromItems(foodItems);
        List<String> oldIDs = getIdStringsFromFile();
        
        BufferedWriter writer = new BufferedWriter(new FileWriter(cacheFile));
        
        writeNewIDs(writer, newIDs);
        int remaining = HISTORY_CACHE_LIMIT - newIDs.size();
        if (remaining < 1) {
            writer.close();
            return;
        }
        writeOldIDs(writer, oldIDs, newIDs, remaining);
        writer.close();
    }
    
    private List<String> getIdStringsFromItems(ArrayList<FoodItem> foodItems) {
        return foodItems.stream()
                        .map(FoodItem::getId)
                        .map(id -> Long.toString(id))
                        .collect(Collectors.toList());
    }
    
    private List<String> getIdStringsFromFile() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(cacheFile));
        List<String> IDs = reader.lines().collect(Collectors.toList());
        reader.close();
        return IDs;
    }
    
    private void writeNewIDs(BufferedWriter writer, List<String> newIDs) throws IOException {
        int size = newIDs.size();
        int i = 0;
        while (i < size && i < HISTORY_CACHE_LIMIT) {
            String id = newIDs.get(i);
            writer.append(id);
            writer.newLine();
            i++;
        }
    }
    
    private void writeOldIDs(BufferedWriter writer, List<String> oldIDs, List<String> newIDs, int remaining) throws IOException {
        int size = oldIDs.size();
        int i = 0;
        while (i < size && remaining > 0) {
            String id = oldIDs.get(i++);
            // prevent writing duplicate IDs
            if (newIDs.contains(id)) {
                continue;
            }
            writer.append(id);
            writer.newLine();
            remaining--;
        }
    }
    
    @Nullable
    public List<Long> getIDs() {
        if (!createFile()) return null;
        
        try {
            BufferedReader reader = new BufferedReader(new FileReader(cacheFile));
            List<Long> IDs = reader.lines().map(Long::parseLong).collect(Collectors.toList());
            reader.close();
            return IDs;
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    @Nullable
    public List<FoodItemEntity> getRecentFoodItemEntities() throws ExecutionException, InterruptedException {
        List<Long> IDs = getIDs();
        if (IDs == null || IDs.size() == 0) {
            return null;
        }
        
        List<FoodItemEntity> entities = AppDatabase.getInstance().foodItemDao().getFoodsByIds(IDs).get();
        if (entities == null || entities.size() == 0) {
            return null;
        }
        
        return orderFoodItemEntities(entities, IDs);
    }
    
    // When selecting with list of IDs,
    // food entities are ordered by ID in ascending order.
    // These entities must be reordered to accurately
    // display history of selection
    private List<FoodItemEntity> orderFoodItemEntities(List<FoodItemEntity> entities, List<Long> orderList) {
        List<FoodItemEntity> orderedEntities = new ArrayList<>();
        
        for (long id : orderList) {
            FoodItemEntity orderedEntity = entities.stream()
                                          .filter(entity -> id == entity.getId())
                                          .findFirst()
                                          .orElse(null);
            
            if (orderedEntity == null) {
                continue;
            }
            
            orderedEntities.add(orderedEntity);
        }
        
        return orderedEntities;
    }
    
    public void clearFile() {
        cacheFile.delete();
        createFile();
    }
    
}
