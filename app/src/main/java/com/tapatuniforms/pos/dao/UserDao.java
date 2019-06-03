package com.tapatuniforms.pos.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.tapatuniforms.pos.model.User;

import java.util.List;

@Dao
public interface UserDao {
    @Query("SELECT * FROM User")
    List<User> getAll();

    @Query("SELECT * FROM User WHERE id = :id")
    User getUser(String id);

    @Query("SELECT * FROM User WHERE mobile = :mobile")
    User getUserByMobile(String mobile);

    @Insert
    void insertAll(List<User> userList);

    @Insert
    long insert(User user);

    @Query("UPDATE user SET pin=:pin WHERE id = :id")
    void setPin(long id, String pin);

    @Query("UPDATE user SET lastLogin = :lastLogin WHERE id = :id")
    void updateLastLogin(long id, String lastLogin);
}
