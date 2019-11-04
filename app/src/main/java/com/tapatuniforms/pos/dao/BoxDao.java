package com.tapatuniforms.pos.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.tapatuniforms.pos.model.Box;

import java.util.List;

@Dao
public interface BoxDao {
    @Query("SELECT * FROM Box")
    List<Box> getAll();

    @Query("SELECT * FROM Box WHERE id = :id")
    Box getBox(long id);

    @Query("SELECT * FROM BOX WHERE indentId = :indentId")
    List<Box> getBoxesByIndent(long indentId);

    @Query("SELECT * FROM Box WHERE id = :id")
    Box getBoxByOutlet(long id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Box> BoxList);

    @Query("DELETE FROM Box")
    void deleteAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Box Box);
}
