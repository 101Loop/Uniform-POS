package com.tapatuniforms.pos.helper;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.tapatuniforms.pos.dao.OrderDao;
import com.tapatuniforms.pos.dao.StockItemDao;
import com.tapatuniforms.pos.dao.SubOrderDao;
import com.tapatuniforms.pos.dao.TransactionDao;
import com.tapatuniforms.pos.dao.UserDao;
import com.tapatuniforms.pos.model.Order;
import com.tapatuniforms.pos.model.StockItem;
import com.tapatuniforms.pos.model.SubOrder;
import com.tapatuniforms.pos.model.Transaction;
import com.tapatuniforms.pos.model.User;

@Database(version = 3, entities = {StockItem.class, User.class, Order.class, SubOrder.class,
        Transaction.class}, exportSchema = false)
public abstract class DatabaseSingleton extends RoomDatabase {
    abstract public StockItemDao stockItemDao();
    abstract public UserDao userDao();
    abstract public OrderDao orderDao();
    abstract public SubOrderDao subOrderDao();
    abstract public TransactionDao transactionDao();
}
