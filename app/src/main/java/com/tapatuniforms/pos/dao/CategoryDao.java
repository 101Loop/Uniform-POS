package com.tapatuniforms.pos.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.tapatuniforms.pos.model.Category;

import java.util.List;

@Dao
public interface CategoryDao {
    @Query("SELECT * FROM Category")
    List<Category> getAll();

    @Query("SELECT * FROM Category WHERE id = :id")
    Category getCategory(long id);

    @Query("SELECT * FROM Category WHERE id = :id")
    Category getCategoryByOutlet(long id);

    @Query("SELECT * FROM Category WHERE outlet = :isSynced")
    Category getOutlet(boolean isSynced);

    @Insert
    void insertAll(List<Category> orderList);

    @Insert
    long insert(Category order);

    @Query("UPDATE Category SET isSynced=:syncStatus WHERE id = :id")
    void setSync(long id, boolean syncStatus);

    @Query("UPDATE Category SET apiId = :apiId WHERE id = :id")
    void setApiId(long id, long apiId);
}
