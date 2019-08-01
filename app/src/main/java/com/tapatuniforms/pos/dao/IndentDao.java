package com.tapatuniforms.pos.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.tapatuniforms.pos.model.Indent;

import java.util.List;

@Dao
public interface IndentDao {
    @Query("SELECT * FROM Indent")
    List<Indent> getAll();

    @Query("SELECT * FROM Indent WHERE id = :id")
    Indent getIndent(long id);

    @Query("SELECT * FROM Indent WHERE id = :id")
    Indent getIndentByOutlet(long id);

    @Insert
    void insertAll(List<Indent> IndentList);

    @Insert
    long insert(Indent Indent);
}
