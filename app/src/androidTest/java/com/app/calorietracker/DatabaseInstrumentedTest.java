package com.app.calorietracker;

import android.content.Context;
import android.util.Log;

import androidx.room.Room;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.app.calorietracker.database.AppDatabase;
import com.app.calorietracker.database.foods.FoodItemEntity;
import com.app.calorietracker.database.user.UserDiaryEntity;
import com.app.calorietracker.food.list.FoodItem;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.util.ArrayList;

@RunWith(AndroidJUnit4.class)
public class DatabaseInstrumentedTest {
    @Test
    public void food_entity_conversion_test() {
        FoodItem item = new FoodItem("test", 1, 2, 3, 4, false);
        FoodItemEntity entity = item.toEntity();
        FoodItem item2 = new FoodItem(entity);
        
        assertEquals(item2.getName(), item.getName());
        assertEquals(item2.getName(), entity.getName());
        assertEquals(item.getName(), entity.getName());
        
        assertEquals(item2.getKcal(), item.getKcal());
        assertEquals(item2.getKcal(), entity.getKcal());
        assertEquals(item.getKcal(), entity.getKcal());
        
        assertEquals(item2.getCarbs(), item.getCarbs());
        assertEquals(item2.getCarbs(), entity.getCarbs());
        assertEquals(item.getCarbs(), entity.getCarbs());
        
        assertEquals(item2.getFat(), item.getFat());
        assertEquals(item2.getFat(), entity.getFat());
        assertEquals(item.getFat(), entity.getFat());
        
        assertEquals(item2.getProtein(), item.getProtein());
        assertEquals(item2.getProtein(), entity.getProtein());
        assertEquals(item.getProtein(), entity.getProtein());
        
        assertEquals(item2.isFavorite(), item.isFavorite());
        assertEquals(item2.isFavorite(), entity.isFavorite());
        assertEquals(item.isFavorite(), entity.isFavorite());
    }
    
    @Test
    public void diary_list_conversion_test() {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        AppDatabase db = Room.inMemoryDatabaseBuilder(appContext, AppDatabase.class).allowMainThreadQueries().build();
    
        UserDiaryEntity diaryEntity = new UserDiaryEntity();
        diaryEntity.set_date(LocalDate.now());
    
        ArrayList<FoodItem> foodItems = new ArrayList<>();
        foodItems.add(new FoodItem("item 1", 1, 2, 3, 4, false));
        foodItems.add(new FoodItem("item 2", 10, 20, 30, 40, false));
        foodItems.add(new FoodItem("item 3", 100, 200, 300, 400, true));
        
        diaryEntity.setBreakfast(foodItems);
        
        db.userDiaryDao().insertNonAsync(diaryEntity);
        
        UserDiaryEntity diaryEntity2 = db.userDiaryDao().getDiaryEntryNonAsync(LocalDate.now());
        
        for (int i = 0; i < foodItems.size(); i++) {
            FoodItem item1 = foodItems.get(i);
            FoodItem item2 = diaryEntity2.getBreakfast().get(i);
            
            assertEquals(item1.getName(), item2.getName());
            assertEquals(item1.getKcal(), item2.getKcal());
            assertEquals(item1.getCarbs(), item2.getCarbs());
            assertEquals(item1.getFat(), item2.getFat());
            assertEquals(item1.getProtein(), item2.getProtein());
            assertEquals(item1.isFavorite(), item2.isFavorite());
        }
    }
    
    @Test
    public void diary_range_select_test() {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        AppDatabase db = Room.inMemoryDatabaseBuilder(appContext, AppDatabase.class).allowMainThreadQueries().build();
        
        LocalDate dateBase = LocalDate.now();
        
        UserDiaryEntity diaryEntity1 = new UserDiaryEntity();
        diaryEntity1.set_date(dateBase);
        diaryEntity1.setCalories(1);
        
        UserDiaryEntity diaryEntity2 = new UserDiaryEntity();
        diaryEntity2.set_date(dateBase.plusDays(1));
        diaryEntity2.setCalories(2);
        
        UserDiaryEntity diaryEntity3 = new UserDiaryEntity();
        diaryEntity3.set_date(dateBase.plusDays(2));
        diaryEntity3.setCalories(3);
    
        UserDiaryEntity diaryEntity4 = new UserDiaryEntity();
        diaryEntity4.set_date(dateBase.plusDays(3));
        diaryEntity4.setCalories(4);
        
        UserDiaryEntity diaryEntity5 = new UserDiaryEntity();
        diaryEntity5.set_date(dateBase.plusDays(4));
        diaryEntity5.setCalories(5);
        
        UserDiaryEntity diaryEntity6 = new UserDiaryEntity();
        diaryEntity6.set_date(dateBase.plusMonths(1));
        diaryEntity6.setCalories(6);
        
        UserDiaryEntity diaryEntity7 = new UserDiaryEntity();
        diaryEntity7.set_date(dateBase.plusMonths(1).plusDays(1));
        diaryEntity7.setCalories(7);
        
        db.userDiaryDao().insertNonAsync(diaryEntity1);
        db.userDiaryDao().insertNonAsync(diaryEntity2);
        db.userDiaryDao().insertNonAsync(diaryEntity3);
        db.userDiaryDao().insertNonAsync(diaryEntity4);
        db.userDiaryDao().insertNonAsync(diaryEntity5);
        db.userDiaryDao().insertNonAsync(diaryEntity6);
        db.userDiaryDao().insertNonAsync(diaryEntity7);
        
        UserDiaryEntity[] diaryEntitiesRange = db.userDiaryDao().getDiaryEntriesRangeNonAsync(dateBase.plusDays(1), dateBase.plusMonths(1));
    
        for (int i = 0; i < diaryEntitiesRange.length; i++) {
            Log.d("TEST", diaryEntitiesRange[i].get_date().toString());
            
            assertEquals(diaryEntitiesRange[i].getCalories(), i+2);
        }
    }
    
    @Test
    public void foods_name_select_test() {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        AppDatabase db = Room.inMemoryDatabaseBuilder(appContext, AppDatabase.class).allowMainThreadQueries().build();
        
        FoodItem item1 = new FoodItem("oatmeal", 1, 2, 3, 4, false);
        FoodItem item2 = new FoodItem("overnight oats", 1, 2, 3, 4, false);
        FoodItem item3 = new FoodItem("oat milk", 1, 2, 3, 4, false);
        FoodItem item4 = new FoodItem("Pineapple pizza", 1, 2, 3, 4, false);
        
        db.foodItemDao().insert(item1.toEntity());
        db.foodItemDao().insert(item2.toEntity());
        db.foodItemDao().insert(item3.toEntity());
        db.foodItemDao().insert(item4.toEntity());
        
        FoodItemEntity[] foodEntitiesResult = db.foodItemDao().getFoodsByName("oat");
        
        for (FoodItemEntity entity: foodEntitiesResult) {
            Log.d("TEST", entity.getName());
            assertNotEquals(entity.getName(), "Pineapple pizza");
        }
    }
}
