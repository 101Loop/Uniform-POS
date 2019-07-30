package com.tapatuniforms.pos.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.tapatuniforms.pos.model.Transaction;

import java.util.List;

@Dao
public interface TransactionDao {
    @Query("SELECT * FROM `Transaction`")
    List<Transaction> getAll();

    @Query("SELECT * FROM `Transaction` WHERE id = :id")
    Transaction getTransaction(long id);

    @Query("SELECT * FROM `Transaction` WHERE isSynced = :isSynced")
    List<Transaction> getTransaction(boolean isSynced);

    @Query("SELECT * FROM `Transaction` WHERE orderId = :orderId AND isSynced = 0")
    List<Transaction> getTransactionByOrderId(long orderId);

    @Insert
    void insertAll(List<Transaction> transactionList);

    @Insert
    long insert(Transaction transaction);

    @Query("UPDATE `Transaction` SET isSynced=:syncStatus WHERE id = :id")
    void setSync(long id, boolean syncStatus);

    @Query("UPDATE `Transaction` SET orderId = :orderId WHERE id = :id")
    void setOrderId(long id, long orderId);

    @Query("DELETE FROM `Transaction`")
    void deleteAll();
}
