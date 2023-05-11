package com.app.calorietracker.ui.food.list;

import android.content.Context;

import androidx.annotation.Nullable;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class SelectionHistoryCacheManager {
    private static final String HISTORY_CACHE_FILE_NAME = "history";
    // Cache file stores up to HISTORY_CACHE_LIMIT IDs of previously selected items
    private final File cacheFile;
    
    public static final int HISTORY_CACHE_LIMIT = 10;
    
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
    
    // LinkedHashSet implementation is used exclusively
    // in order to keep insertion order
    public boolean addItemIDs(ArrayList<FoodItem> foodItems) {
        if (!createFile()) return false;
        
        Set<String> newIDs = foodItems.stream().map(FoodItem::getId)
                                            .map(id -> Long.toString(id))
                                            .collect(Collectors.toCollection(LinkedHashSet::new));
        
        try {
            Set<String> currentIDs = getCurrentItemIds();
            Set<String> ids = mergeIdSets(newIDs, currentIDs);
            if (ids.size() > HISTORY_CACHE_LIMIT) {
                ids = trimSetToLimit(ids);
            }
            handleAdd(ids);
            return true;
        }
        catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    private Set<String> getCurrentItemIds() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(cacheFile));
        Set<String> ids = reader.lines().collect(Collectors.toCollection(LinkedHashSet::new));
        reader.close();
        return ids;
    }
    
    private Set<String> mergeIdSets(Set<String> newIDs, Set<String> currentIDs) {
        Set<String> ids = new LinkedHashSet<>(HISTORY_CACHE_LIMIT);
        ids.addAll(currentIDs);
        ids.addAll(newIDs);
        return ids;
    }
    
    private Set<String> trimSetToLimit(Set<String> ids) {
        // Since currentIDs were added to the set first,
        // only last selected food ids will be skipped
        int diff = ids.size() - HISTORY_CACHE_LIMIT;
        return ids.stream().skip(diff).collect(Collectors.toCollection(LinkedHashSet::new));
    }
    
    private void handleAdd(Set<String> ids) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(cacheFile));
        for (String id : ids) {
            writer.append(id);
            writer.newLine();
        }
        writer.close();
    }
    
    @Nullable
    public Set<Long> getIDs() {
        if (!createFile()) return null;
        
        try {
            BufferedReader reader = new BufferedReader(new FileReader(cacheFile));
            Set<Long> IDs = reader.lines().map(Long::parseLong).collect(Collectors.toCollection(LinkedHashSet::new));
            reader.close();
            return IDs;
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public void clearFile() {
        cacheFile.delete();
        createFile();
    }
    
}
