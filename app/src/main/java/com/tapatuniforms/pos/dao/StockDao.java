package com.tapatuniforms.pos.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.tapatuniforms.pos.model.Stock;

import java.util.List;

@Dao
public interface StockDao {
    @Query("SELECT * FROM Stock")
    List<Stock> getAll();

    @Query("SELECT * FROM Stock WHERE variantId=:variantId")
    List<Stock> getStocksById(int variantId);

    @Query("UPDATE Stock SET display=:displayStock WHERE variantId=:variantId")
    void updateDisplayStock(int displayStock, int variantId);

    @Query("UPDATE Stock SET warehouse=:warehouseStock WHERE variantId=:variantId")
    void updateWarehouseStock(int warehouseStock, int variantId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Stock Stock);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Stock> StockList);

    @Query("DELETE FROM Stock WHERE id=:id")
    void delete(int id);

    @Query("DELETE FROM Stock")
    void deleteAll();
}
