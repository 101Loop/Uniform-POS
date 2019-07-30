package com.tapatuniforms.pos.helper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.tapatuniforms.pos.model.Order;

import org.json.JSONException;

import java.util.ArrayList;

public class NetworkChangeReceiver extends BroadcastReceiver {
    private DatabaseSingleton db;
    private ArrayList<Order> orderList;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Validator.isNetworkConnected(context)) {

            db = DatabaseHelper.getDatabase(context);

            orderList = new ArrayList<>();

            fetchOrderList();

            if (orderList != null && !orderList.isEmpty()) {
                for (Order order : orderList) {

                    if (order.isSynced()) {
                        try {
                            DataHelper.syncOrder(context, db, order);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    private void fetchOrderList() {
        orderList.addAll(db.orderDao().getAll());
    }
}
