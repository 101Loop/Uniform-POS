package com.tapatuniforms.pos.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.tapatuniforms.pos.model.Outlet;

import java.util.List;

@Dao
public interface OutletDao {
    @Query("SELECT * FROM Outlet")
    List<Outlet> getAll();

    @Query("SELECT * FROM Outlet WHERE id = :id")
    Outlet getOutlet(long id);

    @Query("SELECT * FROM Outlet WHERE schoolId = :schoolId")
    List<Outlet> getOutletsBySchool(long schoolId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Outlet> OutletList);

    @Query("DELETE FROM Outlet")
    void deleteAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Outlet Outlet);
}
