package com.tapatuniforms.pos.helper;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.tapatuniforms.pos.dao.BoxDao;
import com.tapatuniforms.pos.dao.BoxItemDao;
import com.tapatuniforms.pos.dao.CategoryDao;
import com.tapatuniforms.pos.dao.DiscountDao;
import com.tapatuniforms.pos.dao.IndentDao;
import com.tapatuniforms.pos.dao.OrderDao;
import com.tapatuniforms.pos.dao.ProductHeaderDao;
import com.tapatuniforms.pos.dao.ProductVariantDao;
import com.tapatuniforms.pos.dao.StockItemDao;
import com.tapatuniforms.pos.dao.SubOrderDao;
import com.tapatuniforms.pos.dao.TransactionDao;
import com.tapatuniforms.pos.dao.UserDao;
import com.tapatuniforms.pos.model.Box;
import com.tapatuniforms.pos.model.BoxItem;
import com.tapatuniforms.pos.model.Category;
import com.tapatuniforms.pos.model.Discount;
import com.tapatuniforms.pos.model.Indent;
import com.tapatuniforms.pos.model.Order;
import com.tapatuniforms.pos.model.ProductHeader;
import com.tapatuniforms.pos.model.ProductVariant;
import com.tapatuniforms.pos.model.StockItem;
import com.tapatuniforms.pos.model.SubOrder;
import com.tapatuniforms.pos.model.Transaction;
import com.tapatuniforms.pos.model.User;

@Database(version = 9, entities = {StockItem.class, User.class, Order.class, SubOrder.class,
        Transaction.class, Category.class, Discount.class, Box.class, BoxItem.class,
        Indent.class, ProductHeader.class, ProductVariant.class}, exportSchema = false)

@TypeConverters({Converter.class})
public abstract class DatabaseSingleton extends RoomDatabase {
    abstract public StockItemDao stockItemDao();

    abstract public UserDao userDao();

    abstract public OrderDao orderDao();

    abstract public SubOrderDao subOrderDao();

    abstract public TransactionDao transactionDao();

    abstract public CategoryDao categoryDao();

    abstract public DiscountDao discountDao();

    abstract public BoxDao boxDao();

    abstract public BoxItemDao boxItemDao();

    abstract public IndentDao indentDao();

    abstract public ProductHeaderDao productHeaderDao();

    abstract public ProductVariantDao productVariantDao();
}
