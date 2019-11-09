package com.tapatuniforms.pos.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.tapatuniforms.pos.adapter.DashboardAdapter;
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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<User> userList);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(User user);

    @Query("UPDATE user SET token=:token WHERE mobile = :mobile")
    void updateToken(String mobile, String token);

    @Query("UPDATE user SET pin=:pin WHERE id = :id")
    void setPin(long id, String pin);

    @Query("UPDATE user SET lastLogin = :lastLogin WHERE id = :id")
    void updateLastLogin(long id, String lastLogin);

    @Query("DELETE FROM User WHERE id = :id")
    void delete(long id);
}
