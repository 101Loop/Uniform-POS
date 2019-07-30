package com.tapatuniforms.pos.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.tapatuniforms.pos.model.Order;

import java.util.List;

@Dao
public interface OrderDao {
    @Query("SELECT * FROM `Order`")
    List<Order> getAll();

    @Query("SELECT * FROM `Order` WHERE id = :id")
    Order getOrder(long id);

    @Query("SELECT * FROM `Order` WHERE isSynced = :isSynced")
    Order getOrder(boolean isSynced);

    @Insert
    void insertAll(List<Order> orderList);

    @Insert
    long insert(Order order);

    @Query("UPDATE `Order` SET isSynced=:syncStatus WHERE id = :id")
    void setSync(long id, boolean syncStatus);

    @Query("UPDATE `Order` SET apiId = :apiId WHERE id = :id")
    void setApiId(long id, long apiId);

    @Query("DELETE FROM `Order`")
    void deleteAll();
}
