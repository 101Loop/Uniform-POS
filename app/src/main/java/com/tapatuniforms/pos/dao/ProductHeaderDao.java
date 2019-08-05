package com.tapatuniforms.pos.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.tapatuniforms.pos.model.ProductHeader;

import java.util.List;

@Dao
public interface ProductHeaderDao {
    @Query("SELECT * FROM ProductHeader")
    List<ProductHeader> getAllProductHeader();

    @Insert
    void insertProductHeader(ProductHeader productHeader);

    @Insert
    void insertAllProductHeader(List<ProductHeader> productHeaderList);

    @Query("SELECT * FROM ProductHeader WHERE id=:productId")
    ProductHeader getProductHeaderById(int productId);

    @Query("DELETE FROM ProductHeader WHERE id=:id")
    void deleteProductVariant(int id);

    @Query("DELETE FROM ProductHeader")
    void deleteAllProductHeaders();

    @Query("UPDATE ProductHeader SET isSynced=:syncStatus WHERE id=:id")
    void setSyncState(int id, boolean syncStatus);
}
