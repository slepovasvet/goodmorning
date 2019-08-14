package ru.slepova.goodmorning.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {DailyResources.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract DailyResourcesDao dailyResourcesDao();
}
