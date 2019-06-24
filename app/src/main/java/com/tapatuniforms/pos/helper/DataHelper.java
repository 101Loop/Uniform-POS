package com.tapatuniforms.pos.helper;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.civilmachines.drfapi.DjangoJSONArrayResponseRequest;
import com.civilmachines.drfapi.DjangoJSONObjectRequest;
import com.tapatuniforms.pos.R;
import com.tapatuniforms.pos.adapter.CategoryAdapter;
import com.tapatuniforms.pos.adapter.ProductAdapter;
import com.tapatuniforms.pos.model.Category;
import com.tapatuniforms.pos.model.Order;
import com.tapatuniforms.pos.model.Product;
import com.tapatuniforms.pos.model.SubOrder;
import com.tapatuniforms.pos.model.Transaction;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DataHelper {
    private static final String TAG = "DataHelper";

    public static ArrayList<Category> getCategories(Context context, DatabaseSingleton db) {
        return null;
    }

    public static void fetchCategories(Context context, ArrayList<Category> categoryList,
                                       CategoryAdapter adapter) {
        if (!Validator.isNetworkConnected(context)) {
            Toast.makeText(context, context.getString(R.string.no_network), Toast.LENGTH_SHORT).show();
            return;
        }

        categoryList.add(new Category(-2, -2, "All Category"));

        DjangoJSONArrayResponseRequest request = new DjangoJSONArrayResponseRequest(
                Request.Method.GET, APIStatic.Category.categoryUrl, null,
                response -> {
                    // Response Received
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject categoryJSON = response.getJSONObject(i);
                            categoryList.add(new Category(categoryJSON));
                        } catch (JSONException e) {
                            Toast.makeText(context, "Category Parse Error", Toast.LENGTH_SHORT)
                                    .show();
                            e.printStackTrace();
                        }

                        adapter.notifyDataSetChanged();
                    }
                }, new APIErrorListener(context), context);

        request.setRetryPolicy(new DefaultRetryPolicy(0, -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(context).getRequestQueue().add(request);
    }

    public static void fetchProducts(Context context, ArrayList<Product> allProducts,
                                     ArrayList<Product> productList, ProductAdapter productAdapter) {
        if (!Validator.isNetworkConnected(context)) {
            Toast.makeText(context, context.getString(R.string.no_network), Toast.LENGTH_SHORT).show();
            return;
        }

        DjangoJSONArrayResponseRequest request = new DjangoJSONArrayResponseRequest(
                Request.Method.GET, APIStatic.Outlet.productUrl, null,
                response -> {
                    // Response Received
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject productJSON = response.getJSONObject(i);
                            productList.add(new Product(productJSON));
                            allProducts.add(new Product(productJSON));
                        } catch (JSONException e) {
                            Toast.makeText(context, "Product Parse Error", Toast.LENGTH_SHORT)
                                    .show();
                            e.printStackTrace();
                        }

                        productAdapter.notifyDataSetChanged();
                    }
                }, new APIErrorListener(context), context);

        request.setRetryPolicy(new DefaultRetryPolicy(0, -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(context).getRequestQueue().add(request);
    }

    public static void saveAndSyncOrder(Context context, DatabaseSingleton db, Order order,
                                        ArrayList<SubOrder> subOrderList,
                                        ArrayList<Transaction> transactionList) {
        // Insert Order to database
        long orderId = db.orderDao().insert(order);
        // Set local orderId
        order.setId(orderId);

        // Set local order Id to subOrders
        for (SubOrder subOrder: subOrderList) {
            subOrder.setOrderId(orderId);
        }

        // Save Sub orders to the local database
        db.subOrderDao().insertAll(subOrderList);

        // Set local Order Id to transaction list items
        for (Transaction transaction: transactionList) {
            transaction.setOrderId(orderId);
        }

        // Save transaction items to database
        db.transactionDao().insertAll(transactionList);

        try {
            syncOrder(context, db, order);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private static void syncOrder(Context context, DatabaseSingleton db, Order order) throws JSONException {
        if (!Validator.isNetworkConnected(context)) {
            Toast.makeText(context, context.getString(R.string.no_network), Toast.LENGTH_SHORT).show();
            return;
        }

        JSONObject orderObject = new JSONObject();
        orderObject.put(APIStatic.Key.name, order.getCustName());
        orderObject.put(APIStatic.Key.mobile, order.getCustMobile());
        orderObject.put(APIStatic.Key.email, order.getCustEmail());
        orderObject.put(APIStatic.Key.discount, order.getDiscount());
        orderObject.put(APIStatic.Key.outlet, "1");

        DjangoJSONObjectRequest request = new DjangoJSONObjectRequest(
                Request.Method.POST, APIStatic.Order.orderUrl, orderObject,
                response -> {
                    // Response Received
                    // Set Sync Order to true
                    db.orderDao().setSync(order.getId(), true);

                    long apiOrderId = response.optLong(APIStatic.Key.id);
                    db.orderDao().setApiId(order.getId(), apiOrderId);

                    try {
                        syncSubOrder(context, order.getId(), apiOrderId, db);
                        syncTransaction(context, order.getId(), apiOrderId, db);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, new APIErrorListener(context), context);

        request.setRetryPolicy(new DefaultRetryPolicy(0, -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(context).getRequestQueue().add(request);
    }

    private static void syncSubOrder(Context context, long orderId, long apiOrderId,
                                     DatabaseSingleton db) throws JSONException {
        if (!Validator.isNetworkConnected(context)) {
            Toast.makeText(context, context.getString(R.string.no_network), Toast.LENGTH_SHORT).show();
            return;
        }

        ArrayList<SubOrder> subOrderList = (ArrayList<SubOrder>) db.subOrderDao()
                .getSubOrderByOrderId(orderId);

        for (SubOrder subOrder: subOrderList) {
            JSONObject object = new JSONObject();
            object.put(APIStatic.Key.order, apiOrderId);
            object.put(APIStatic.Key.product, subOrder.getProductApiId());
            object.put(APIStatic.Key.price, subOrder.getPrice());
            object.put(APIStatic.Key.qunatity, subOrder.getQuantity());


            DjangoJSONObjectRequest request = new DjangoJSONObjectRequest(
                    Request.Method.POST, APIStatic.Order.subOrderUrl, object,
                    response -> {
                        // Response Received
                        db.subOrderDao().setSync(subOrder.getId(), true);
                    }, new APIErrorListener(context), context);

            request.setRetryPolicy(new DefaultRetryPolicy(0, -1,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            VolleySingleton.getInstance(context).getRequestQueue().add(request);
        }
    }

    private static void syncTransaction(Context context, long orderId, long apiOrderId,
                                        DatabaseSingleton db) throws JSONException {
        if (!Validator.isNetworkConnected(context)) {
            Toast.makeText(context, context.getString(R.string.no_network), Toast.LENGTH_SHORT).show();
            return;
        }

        ArrayList<Transaction> transactionList = (ArrayList<Transaction>) db.transactionDao()
                .getTransactionByOrderId(orderId);

        for (Transaction transaction: transactionList) {
            JSONObject object = new JSONObject();
            object.put(APIStatic.Key.order, apiOrderId);
            object.put(APIStatic.Key.amount, transaction.getAmount());
            object.put(APIStatic.Key.mode, transaction.getPaymentOption());


            DjangoJSONObjectRequest request = new DjangoJSONObjectRequest(
                    Request.Method.POST, APIStatic.Order.transactionUrl, object,
                    response -> {
                        // Response Received
                        db.subOrderDao().setSync(transaction.getId(), true);
                    }, new APIErrorListener(context), context);

            request.setRetryPolicy(new DefaultRetryPolicy(0, -1,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            VolleySingleton.getInstance(context).getRequestQueue().add(request);
        }
    }
}
