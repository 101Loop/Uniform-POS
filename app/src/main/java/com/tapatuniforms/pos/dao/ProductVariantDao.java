package com.tapatuniforms.pos.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.tapatuniforms.pos.model.ProductVariant;

import java.util.List;

@Dao
public interface ProductVariantDao {
    @Query("SELECT * FROM ProductVariant")
    List<ProductVariant> getAllProductVariants();

    @Query("SELECT * FROM ProductVariant WHERE productId=:productId")
    List<ProductVariant> getProductVariantsById(int productId);

    @Query("UPDATE ProductVariant SET displayStock=:displayStock WHERE id=:id")
    void updateDisplayStock(int displayStock, int id);

    @Insert
    void insertProductVariant(ProductVariant productVariant);

    @Insert
    void insertAllProductVariants(List<ProductVariant> productVariantList);

    @Query("DELETE FROM ProductVariant WHERE id=:id")
    void deleteProductVariant(int id);

    @Query("DELETE FROM ProductVariant")
    void deleteAllProductVariants();
}
