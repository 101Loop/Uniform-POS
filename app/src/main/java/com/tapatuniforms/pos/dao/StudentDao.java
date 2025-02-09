package com.tapatuniforms.pos.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.tapatuniforms.pos.model.Student;

import java.util.List;

@Dao
public interface StudentDao {
    @Query("SELECT * FROM Student")
    List<Student> getAll();

    @Query("SELECT * FROM Student WHERE id = :id")
    Student getStudent(long id);

    @Query("SELECT * FROM Student WHERE id = :id")
    Student getStudentByOutlet(long id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Student> studentList);

    @Query("DELETE FROM Student")
    void deleteAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Student student);
}
