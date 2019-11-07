package com.tapatuniforms.pos.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.tapatuniforms.pos.model.Indent;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface IndentDao {
    @Query("SELECT * FROM Indent")
    List<Indent> getAll();

    @Query("SELECT * FROM Indent WHERE id = :id")
    Indent getIndent(long id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(ArrayList<Indent> IndentList);

    @Query("DELETE FROM Indent")
    void deleteAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Indent Indent);
}
