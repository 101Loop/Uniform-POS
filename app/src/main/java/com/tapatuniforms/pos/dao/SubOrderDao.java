package com.tapatuniforms.pos.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.tapatuniforms.pos.model.SubOrder;

import java.util.List;

@Dao
public interface SubOrderDao {
    @Query("SELECT * FROM SubOrder")
    List<SubOrder> getAll();

    @Query("SELECT * FROM SubOrder WHERE id = :id")
    SubOrder getSubOrder(long id);

    @Query("SELECT * FROM SubOrder WHERE isSynced = :isSynced")
    List<SubOrder> getSubOrder(boolean isSynced);

    @Query("SELECT * FROM SubOrder WHERE orderId = :orderId AND isSynced = 0")
    List<SubOrder> getSubOrderByOrderId(long orderId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<SubOrder> subOrderList);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(SubOrder order);

    @Query("UPDATE SubOrder SET isSynced=:syncStatus WHERE id = :id")
    void setSync(long id, boolean syncStatus);

    @Query("UPDATE SubOrder SET orderId = :orderId WHERE id = :id")
    void setOrderId(long id, long orderId);

    @Query("DELETE FROM SubOrder")
    void deleteAll();
}
