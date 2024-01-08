package com.app.calorietracker.ui.quiz;

import java.io.Serializable;

public class QuizData implements Serializable {
    
    public enum Sex {
        MALE,
        FEMALE,
        NOT_SELECTED
    }
    
    public enum ActivityLevel {
        VERY_LOW(1.2f, 0),
        LOW(1.375f, 0),
        MEDIUM(1.55f, 200),
        HIGH(1.725f, 400),
        VERY_HIGH(1.9f, 600),
        NOT_SELECTED(0.0f, -1);
        
        private final float calsMult; // Applied to Basal Metabolic Rate to get daily calorie need
        private final int waterAdd;
        
        ActivityLevel(float levelMult, int waterAdd) {
            this.calsMult = levelMult;
            this.waterAdd = waterAdd;
        }
        
        public float getCalsMult() {
            return this.calsMult;
        }
        
        public int getWaterAdd() {
            return this.waterAdd;
        }
    }
    
    public enum Goal {
        LOSE(0.9f),
        MAINTAIN(1.0f),
        GAIN(1.15f),
        NOT_SELECTED(0.0f);
        
        private final float goalMult;
        
        Goal(float goalMult) {
            this.goalMult = goalMult;
        }
    
        public float getGoalMult() {
            return goalMult;
        }
    }
    
    private int age;
    private Sex sex;
    private int height;
    private int weight;
    private ActivityLevel activityLevel;
    private Goal goal;
    
    private boolean skipped;
    
    public QuizData() {
        age = -1;
        sex = Sex.NOT_SELECTED;
        height = -1;
        weight = -1;
        activityLevel = ActivityLevel.NOT_SELECTED;
        goal = Goal.NOT_SELECTED;
        skipped = false;
    }
    
    public int getAge() {
        return age;
    }
    
    public void setAge(int age) {
        this.age = age;
    }
    
    public Sex getSex() {
        return sex;
    }
    
    public void setSex(Sex sex) {
        this.sex = sex;
    }
    
    public int getHeight() {
        return height;
    }
    
    public void setHeight(int height) {
        this.height = height;
    }
    
    public int getWeight() {
        return weight;
    }
    
    public void setWeight(int weight) {
        this.weight = weight;
    }
    
    public ActivityLevel getActivityLevel() {
        return activityLevel;
    }
    
    public void setActivityLevel(ActivityLevel activityLevel) {
        this.activityLevel = activityLevel;
    }
    
    public Goal getGoal() {
        return goal;
    }
    
    public void setGoal(Goal goal) {
        this.goal = goal;
    }
    
    public boolean wasSkipped() {
        return skipped;
    }
    
    public void setSkipped(boolean skipped) {
        this.skipped = skipped;
    }
}
