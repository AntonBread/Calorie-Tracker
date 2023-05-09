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
import java.util.stream.Collectors;

public class SelectionHistoryCacheManager {
    private final Context context;
    private static final String HISTORY_CACHE_FILE_NAME = "history";
    // Cache file stores up to HISTORY_CACHE_LIMIT IDs of previously selected items
    private final File cacheFile;
    
    private final int HISTORY_CACHE_LIMIT = 5;
    
    public SelectionHistoryCacheManager(Context context) {
        this.context = context;
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
    
    public boolean addItemIDs(ArrayList<FoodItem> foodItems) {
        if (!createFile()) return false;
        
        ArrayList<String> newIDs = foodItems.stream().map(FoodItem::getId)
                                            .map(id -> Long.toString(id))
                                            .collect(Collectors.toCollection(ArrayList::new));
        
        try {
            ArrayList<String> currentIDs = getItemIds();
            if ((currentIDs.size() + newIDs.size()) > HISTORY_CACHE_LIMIT) {
                handleAddOverflow(currentIDs, newIDs);
            }
            else {
                handleAddDefault(newIDs);
            }
            return true;
        }
        catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    private ArrayList<String> getItemIds() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(cacheFile));
        ArrayList<String> ids = reader.lines().collect(Collectors.toCollection(ArrayList::new));
        reader.close();
        return ids;
    }
    
    private void handleAddOverflow(ArrayList<String> currentIDs, ArrayList<String> newIDs) throws IOException {
        int newCount = newIDs.size();
        BufferedWriter writer = new BufferedWriter(new FileWriter(cacheFile));
        
        for (int i = newCount; i < HISTORY_CACHE_LIMIT; i++) {
            writer.append(currentIDs.get(i));
            writer.newLine();
        }
        
        // If amount of new IDs exceeds the limit,
        // first few elements are trimmed so that the size
        // of list is exactly HISTORY_CACHE_LIMIT
        if (newCount > HISTORY_CACHE_LIMIT) {
            int diff = HISTORY_CACHE_LIMIT - newCount;
            newIDs = (ArrayList<String>) newIDs.subList(diff, newIDs.size());
        }
        for (String newID : newIDs) {
            writer.append(newID);
            writer.newLine();
        }
        writer.close();
    }
    
    private void handleAddDefault(ArrayList<String> newIDs) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(cacheFile, true));
        for (String newID : newIDs) {
            writer.append(newID);
            writer.newLine();
        }
        writer.close();
    }
    
    @Nullable
    public ArrayList<Long> getIDs() {
        if (!createFile()) return null;
        
        try {
            BufferedReader reader = new BufferedReader(new FileReader(cacheFile));
            ArrayList<Long> IDs = reader.lines().map(Long::parseLong).collect(Collectors.toCollection(ArrayList::new));
            reader.close();
            return IDs;
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
}
