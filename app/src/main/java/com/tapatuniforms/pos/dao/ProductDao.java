package com.tapatuniforms.pos.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.tapatuniforms.pos.model.Product;

import java.util.List;

@Dao
public interface ProductDao {
    @Query("SELECT * FROM Product")
    List<Product> getAll();

    @Query("SELECT * FROM Product WHERE apiId = :apiId")
    Product getProduct(long apiId);

    @Insert
    void insertAll(List<Product> productList);

    @Insert
    long insert(Product product);

    @Query("DELETE FROM Product")
    void deleteAll();
}
