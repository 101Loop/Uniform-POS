package com.tapatuniforms.pos.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.tapatuniforms.pos.model.Stock;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface StockDao {
    @Query("SELECT * FROM Stock")
    List<Stock> getAll();

    @Query("SELECT * FROM Stock WHERE productId = :productId")
    Stock getStock(long productId);

    @Insert
    void insertAll(List<Stock> StockList);

    @Insert
    long insert(Stock Stock);

    @Query("DELETE FROM Stock")
    void deleteAll();

    @Query("UPDATE Stock SET displayStockList = :displayStockList WHERE productId = :productId")
    void updateDisplayStockList(ArrayList<String> displayStockList, int productId);
}
