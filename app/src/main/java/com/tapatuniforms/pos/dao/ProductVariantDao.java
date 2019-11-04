package com.tapatuniforms.pos.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.tapatuniforms.pos.model.ProductVariant;

import java.util.List;

@Dao
public interface ProductVariantDao {
    @Query("SELECT * FROM ProductVariant")
    List<ProductVariant> getAll();

    @Query("SELECT * FROM ProductVariant WHERE productId=:productId")
    List<ProductVariant> getProductVariantsById(int productId);

    @Query("UPDATE ProductVariant SET displayStock=:displayStock WHERE id=:id")
    void updateDisplayStock(int displayStock, int id);

    @Query("UPDATE ProductVariant SET warehouseStock=:warehouseStock WHERE id=:id")
    void updateWarehouseStock(int warehouseStock, int id);

    @Query("UPDATE ProductVariant SET transferOrderCount=:transferOrderCount WHERE id=:id")
    void updateTransferOrderCount(int transferOrderCount, int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ProductVariant productVariant);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<ProductVariant> productVariantList);

    @Query("DELETE FROM ProductVariant WHERE id=:id")
    void delete(int id);

    @Query("DELETE FROM ProductVariant")
    void deleteAll();

    @Query("UPDATE productvariant SET isSynced = :syncStatus WHERE id = :id")
    void setSyncStatus(boolean syncStatus, int id);
}
