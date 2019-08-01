package com.tapatuniforms.pos.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.tapatuniforms.pos.model.BoxItem;

import java.util.List;

@Dao
public interface BoxItemDao {
    @Query("SELECT * FROM BoxItem")
    List<BoxItem> getAll();

    @Query("SELECT * FROM BoxItem WHERE id = :id")
    BoxItem getBoxById(long id);

    @Query("SELECT * FROM BoxItem WHERE id = :id")
    BoxItem getBoxItemByOutlet(long id);

    @Insert
    void insertAll(List<BoxItem> BoxItemList);

    @Insert
    long insert(BoxItem BoxItem);

    @Query("DELETE FROM BoxItem")
    void deleteAll();
}
