package com.tapatuniforms.pos.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.tapatuniforms.pos.model.Box;

import java.util.List;

@Dao
public interface BoxDao {
    @Query("SELECT * FROM Box")
    List<Box> getAll();

    @Query("SELECT * FROM Box WHERE id = :id")
    Box getBox(long id);

    @Query("SELECT * FROM Box WHERE id = :id")
    Box getBoxByOutlet(long id);

    @Insert
    void insertAll(List<Box> BoxList);

    @Insert
    long insert(Box Box);
}
