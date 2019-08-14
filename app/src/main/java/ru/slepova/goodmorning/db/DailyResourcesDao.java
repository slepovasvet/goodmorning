package ru.slepova.goodmorning.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DailyResourcesDao {
    @Query("SELECT * FROM dailyresources")
    List<DailyResources> getAll();

    @Query("SELECT * FROM dailyresources WHERE id = (:id)")
    List<DailyResources> get(int id);

    @Query("SELECT * FROM dailyresources WHERE date = (:date)")
    List<DailyResources> getDailyResources(String date);

    @Insert
    void insertAll(DailyResources... list);

    @Delete
    void delete(DailyResources item);
}
