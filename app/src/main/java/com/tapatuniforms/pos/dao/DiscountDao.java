package com.tapatuniforms.pos.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.tapatuniforms.pos.model.Discount;

import java.util.List;

@Dao
public interface DiscountDao {
    @Query("SELECT * FROM Discount")
    List<Discount> getAll();

    @Query("SELECT * FROM Discount WHERE id = :id")
    Discount getDiscount(long id);

    @Query("SELECT * FROM Discount WHERE id = :id")
    Discount getDiscountByOutlet(long id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Discount> DiscountList);

    @Query("DELETE FROM Discount")
    void deleteAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Discount Discount);
}
