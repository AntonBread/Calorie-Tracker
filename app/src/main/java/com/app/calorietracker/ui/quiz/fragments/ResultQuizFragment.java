package com.app.calorietracker.ui.quiz.fragments;

import static com.app.calorietracker.ui.quiz.fragments.QuizFragment.ARGS_KEY;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.app.calorietracker.R;
import com.app.calorietracker.database.AppDatabase;
import com.app.calorietracker.database.user.UserDiaryEntity;
import com.app.calorietracker.ui.quiz.QuizData;
import com.app.calorietracker.ui.quiz.QuizData.Sex;
import com.app.calorietracker.ui.settings.SettingsManager;

import java.time.LocalDate;
import java.util.concurrent.ExecutionException;

public class ResultQuizFragment extends Fragment {
    
    public ResultQuizFragment() {
        // Required empty constructor
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_quiz_result, container, false);
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        SettingsManager settingsManager = new SettingsManager(requireContext());
        
        Bundle args = getArguments();
        assert args != null;
        QuizData quizData = (QuizData) args.get(ARGS_KEY);
        
        if (quizData.wasSkipped()) {
            return;
        }
        
        int calories = calculateCalorieNorm(quizData);
        int water = calculateWaterNorm(quizData);
        float carbs = calculateCarbsNorm(calories);
        float fat = calculateFatNorm(calories);
        float protein = calculateProteinNorm(calories);
        
        settingsManager.setBaselinePrefs(calories, carbs, fat, protein, water);
        settingsManager.setUserHeight_cm(quizData.getHeight());
        setUserDiaryWeight(quizData.getWeight());
        
        initTextViews(view, calories, carbs, fat, protein, water);
    }
    
    private void initTextViews(View v, int calories, float carbs, float fat, float protein, int water) {
        TextView caloriesView = v.findViewById(R.id.quiz_result_cals);
        TextView carbsView = v.findViewById(R.id.quiz_result_carbs);
        TextView fatView = v.findViewById(R.id.quiz_result_fat);
        TextView proteinView = v.findViewById(R.id.quiz_result_protein);
        TextView waterView = v.findViewById(R.id.quiz_result_water);
        
        String caloriesText = getString(R.string.quiz_result_calories_format, calories);
        String carbsText = getString(R.string.quiz_result_carbs_format, carbs);
        String fatText = getString(R.string.quiz_result_fat_format, fat);
        String proteinText = getString(R.string.quiz_result_protein_format, protein);
        String waterText = getString(R.string.quiz_result_water_format, water);
        
        caloriesView.setText(caloriesText);
        carbsView.setText(carbsText);
        fatView.setText(fatText);
        proteinView.setText(proteinText);
        waterView.setText(waterText);
    }
    
    // Uses Mifflin-St Jeor formula
    private int calculateCalorieNorm(QuizData quizResultData) {
        float weightKg = (float) quizResultData.getWeight() / 1000;
        float height = quizResultData.getHeight();
        float age = quizResultData.getAge();
        
        // Even though resulting calorie number is integer,
        // float32 is used during calculation for more precision
        float dailyCals = (weightKg * 10) + (height * 6.25f) - (age * 5);
        if (quizResultData.getSex() == Sex.MALE) {
            dailyCals += 5;
        }
        else {
            dailyCals -= 161;
        }
        dailyCals *= quizResultData.getActivityLevel().getCalsMult();
        dailyCals *= quizResultData.getGoal().getGoalMult();
        
        return (int) (dailyCals + 0.5f);
    }
    
    public int calculateWaterNorm(QuizData quizResultData) {
        int dailyWater = 1500;
        float weightKg = (float) quizResultData.getWeight() / 1000;
        weightKg -= 20;
        if (quizResultData.getSex() == Sex.MALE) {
            dailyWater += (int) (weightKg * 25 + 0.5f);
        }
        else {
            dailyWater += (int) (weightKg * 20 + 0.5f);
        }
        dailyWater += quizResultData.getActivityLevel().getWaterAdd();
        return dailyWater;
    }
    
    // Macronutrients are calculated using 50% carbs/30% fat/20% protein proportion
    private float calculateCarbsNorm(int calories) {
        float carbsCaloriesFraction = calories * 0.5f;
        // 1g of carbs = 4 calories
        float carbs = carbsCaloriesFraction / 4;
        // Round to 1 decimal place
        return Math.round(carbs * 10.0) / 10.0f;
    }
    
    private float calculateFatNorm(int calories) {
        float fatCaloriesFraction = calories * 0.3f;
        // 1g of fat = 9 calories
        float fat = fatCaloriesFraction / 9;
        return Math.round(fat * 10.0) / 10.0f;
    }
    
    private float calculateProteinNorm(int calories) {
        float proteinCaloriesFraction = calories * 0.2f;
        // 1g of protein = 4 calories
        float protein = proteinCaloriesFraction / 4;
        return Math.round(protein * 10.0) / 10.0f;
    }
    
    private void setUserDiaryWeight(int weight) {
        AppDatabase db;
        try {
            db = AppDatabase.getInstance();
        }
        catch (NullPointerException e) {
            AppDatabase.instanceInit(requireActivity().getApplicationContext(), AppDatabase.MODE_STANDARD);
            db = AppDatabase.getInstance();
        }
        
        LocalDate date = LocalDate.now();
        
        try {
            UserDiaryEntity entity = db.userDiaryDao().getDiaryEntry(date).get();
            if (entity != null) {
                entity.setWeight_g(weight);
                db.userDiaryDao().update(entity);
            }
            else {
                entity = new UserDiaryEntity();
                entity.set_date(date);
                entity.setWeight_g(weight);
                db.userDiaryDao().insert(entity);
            }
        }
        catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }
    
}
