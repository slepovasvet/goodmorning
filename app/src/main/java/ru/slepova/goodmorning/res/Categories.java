package ru.slepova.goodmorning.res;

import android.util.SparseArray;

public class Categories {
    public SparseArray<String> Names;
    public Categories() {
        Names = new SparseArray<>();
        Names.put(1, "Пожелания доброго утра");
        Names.put(2, "Правила этикета");
        Names.put(3, "Правила моды");
    }
}
