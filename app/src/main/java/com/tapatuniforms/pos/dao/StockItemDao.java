package com.tapatuniforms.pos.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.tapatuniforms.pos.model.StockItem;

import java.util.List;

@Dao
public interface StockItemDao {
    @Query("SELECT * FROM StockItem")
    List<StockItem> loadAll();

    @Insert
    void insertAll(List<StockItem> categories);
}
