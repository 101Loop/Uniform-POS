package com.tapatuniforms.pos.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.tapatuniforms.pos.model.School;

import java.util.List;

@Dao
public interface SchoolDao {
    @Query("SELECT * FROM School")
    List<School> getAll();

    @Query("SELECT * FROM School WHERE id = :id")
    School getSchool(long id);

    @Query("SELECT * FROM School WHERE id = :id")
    School getSchoolByOutlet(long id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<School> schoolList);

    @Query("DELETE FROM School")
    void deleteAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(School school);
}
