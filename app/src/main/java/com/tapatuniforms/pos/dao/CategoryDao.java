package com.tapatuniforms.pos.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Category> categoryList);

    @Query("DELETE FROM Category")
    void deleteAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Category category);
}
