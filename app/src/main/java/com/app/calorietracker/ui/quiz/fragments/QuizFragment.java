package com.app.calorietracker.ui.quiz.fragments;

import com.app.calorietracker.ui.quiz.QuizData;

public interface QuizFragment {
     
     String ARGS_KEY = "quiz_args_key";
     
     void writeSelectedValue(QuizData quizData);
     
     void setStartValue();
}
