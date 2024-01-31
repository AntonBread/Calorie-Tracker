package com.app.calorietracker.ui.stats.data;

import java.time.LocalDate;

public abstract class StatsData {
    
    private final LocalDate date;
    
    public StatsData(LocalDate date) {
        this.date = date;
    }
    
    public LocalDate getDate() {
        return date;
    }
}
