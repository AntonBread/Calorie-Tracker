package com.app.calorietracker.database.user;

import java.time.LocalDate;
import java.util.concurrent.ExecutionException;

public class UserDiaryDatabaseManager {
    public void insertNew(UserDiaryDao dao, LocalDate date) {
        UserDiaryEntity diaryEntity = new UserDiaryEntity();
        diaryEntity.set_date(date);
        dao.insert(diaryEntity);
    }
    
    public boolean updateEntry(UserDiaryDao dao, UserDiaryEntity diaryEntity) {
        try {
            int updateCount = dao.update(diaryEntity).get();
            return (updateCount == 1);
        }
        catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }
}
