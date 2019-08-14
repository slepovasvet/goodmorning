package ru.slepova.goodmorning.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class DailyResources {
    @PrimaryKey
    public int id;

    @ColumnInfo(name = "date")
    public String date;

    @ColumnInfo(name = "category_id")
    public int category_id;

    @ColumnInfo(name = "phrase_id")
    public int phrase_id;

    @ColumnInfo(name = "image_id")
    public int image_id;
}
