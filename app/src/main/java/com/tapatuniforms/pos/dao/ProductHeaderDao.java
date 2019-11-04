package com.tapatuniforms.pos.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.tapatuniforms.pos.model.ProductHeader;

import java.util.List;

@Dao
public interface ProductHeaderDao {
    @Query("SELECT * FROM ProductHeader")
    List<ProductHeader> getAllProductHeader();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ProductHeader productHeader);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<ProductHeader> productHeaderList);

    @Query("SELECT * FROM ProductHeader WHERE id=:productId")
    ProductHeader getProductHeaderById(int productId);

    @Query("DELETE FROM ProductHeader WHERE id=:id")
    void delete(int id);

    @Query("DELETE FROM ProductHeader")
    void deleteAll();

    @Query("UPDATE ProductHeader SET isSynced=:syncStatus WHERE id=:id")
    void setSync(int id, boolean syncStatus);

    @Query("DELETE FROM ProductHeader WHERE id = :id")
    void deleteById(int id);
}
