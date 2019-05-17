package com.tapatuniforms.pos.helper;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.tapatuniforms.pos.dao.StockItemDao;
import com.tapatuniforms.pos.model.StockItem;

@Database(version = 1, entities = {StockItem.class}, exportSchema = false)
public abstract class DatabaseSingleton extends RoomDatabase {
    abstract public StockItemDao stockItemDao();
}
