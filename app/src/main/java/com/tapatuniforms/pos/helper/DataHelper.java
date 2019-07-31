package com.tapatuniforms.pos.helper;

import android.content.Context;
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

    /**
     * Method to fetch the categories from server
     *
     * @param context Context of calling activity
     * @param categoryList list of categories
     * @param adapter reference to CategoryAdapter
     * */
    public static void fetchCategories(Context context, ArrayList<Category> categoryList,
                                       CategoryAdapter adapter) {
        if (!Validator.isNetworkConnected(context)) {
            Toast.makeText(context, context.getString(R.string.no_network), Toast.LENGTH_SHORT).show();
            return;
        }

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

    /**
     * Method to fetch products from server
     *
     * @param context Context of calling activity
     * @param allProducts list of products, this list is never changes
     * @param productList list of products, changes according to the requirement
     * @param productAdapter reference of product adapter to notify changes
     * @param db DatabaseSingleton reference to store and fetch from database
     * */
    public static void fetchProducts(Context context, ArrayList<Product> allProducts,
                                     ArrayList<Product> productList, ProductAdapter productAdapter, DatabaseSingleton db) {

        ArrayList<Product> localProductList = (ArrayList<Product>) db.productDao().getAll();
        if (!Validator.isNetworkConnected(context)) {
            Toast.makeText(context, context.getString(R.string.no_network), Toast.LENGTH_SHORT).show();
            productList.clear();
            productList.addAll(localProductList);

            allProducts.clear();
            allProducts.addAll(localProductList);
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

                    if (localProductList.size() != productList.size()) {
                        db.productDao().deleteAll();
                        db.productDao().insertAll(productList);
                    }

                }, new APIErrorListener(context), context);

        request.setRetryPolicy(new DefaultRetryPolicy(0, -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(context).getRequestQueue().add(request);
    }

    /**
     * Method to save and sync the order
     *
     * @param context Context of the calling activity
     * @param db DatabaseSingleton reference to store the data in the database
     * @param order Order, which is to be saved and synced
     * @param subOrderList list of SubOrder
     * @param transactionList list of Transaction
     * */
    public static void saveAndSyncOrder(Context context, DatabaseSingleton db, Order order,
                                        ArrayList<SubOrder> subOrderList,
                                        ArrayList<Transaction> transactionList) {
        // Insert Order to database
        long orderId = db.orderDao().insert(order);
        // Set local orderId
        order.setId(orderId);

        // Set local order Id to subOrders
        for (SubOrder subOrder : subOrderList) {
            subOrder.setOrderId(orderId);
        }

        // Save Sub orders to the local database
        db.subOrderDao().insertAll(subOrderList);

        // Set local Order Id to transaction list items
        for (Transaction transaction : transactionList) {
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

    /**
     * Method to save the order if not connected to the internet, otherwise make an API call
     *
     * @param context Context of the calling activity
     * @param db DatabaseSingleton reference to save order from database
     * @param order Order
     * */
    public static void syncOrder(Context context, DatabaseSingleton db, Order order) throws JSONException {

        if (!Validator.isNetworkConnected(context)) {
            Toast.makeText(context, context.getString(R.string.no_network), Toast.LENGTH_SHORT).show();
            db.orderDao().setSync(order.getId(), true);

            long apiOrderId = 0;
            db.orderDao().setApiId(order.getId(), apiOrderId);
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

    /**
     * Method to either save SubOrder in db or make an API call
     *
     * @param context Context of calling activity
     * @param orderId id of the order to which, the SubOrder is related to
     * @param db DatabaseSingleton reference to save SubOrder in db
     * */
    private static void syncSubOrder(Context context, long orderId, long apiOrderId,
                                     DatabaseSingleton db) throws JSONException {
        if (!Validator.isNetworkConnected(context)) {
            Toast.makeText(context, context.getString(R.string.no_network), Toast.LENGTH_SHORT).show();
            db.subOrderDao().setSync(orderId, true);
            return;
        }

        ArrayList<SubOrder> subOrderList = (ArrayList<SubOrder>) db.subOrderDao()
                .getSubOrderByOrderId(orderId);

        for (SubOrder subOrder : subOrderList) {
            JSONObject object = new JSONObject();
            object.put(APIStatic.Key.order, apiOrderId);
            object.put(APIStatic.Key.product, subOrder.getProductApiId());
            object.put(APIStatic.Key.price, subOrder.getPrice());
            object.put(APIStatic.Key.qunatity, subOrder.getQuantity());

            DjangoJSONObjectRequest request = new DjangoJSONObjectRequest(
                    Request.Method.POST, APIStatic.Order.subOrderUrl, object,
                    response -> {
                    }, new APIErrorListener(context), context);

            request.setRetryPolicy(new DefaultRetryPolicy(0, -1,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            VolleySingleton.getInstance(context).getRequestQueue().add(request);
        }
    }

    /**
     * Method to either save Transaction or make API call
     *
     * @param context Context of the calling activity
     * @param orderId id of the order to which, the transaction is related to
     * @param apiOrderId API's order id TODO: this is to be fetched from server, currently it is static
     * @param db DatabaseSingleton reference to save the transaction in db
     * */
    private static void syncTransaction(Context context, long orderId, long apiOrderId,
                                        DatabaseSingleton db) throws JSONException {
        if (!Validator.isNetworkConnected(context)) {
            Toast.makeText(context, context.getString(R.string.no_network), Toast.LENGTH_SHORT).show();
            db.subOrderDao().setSync(orderId, true);
            return;
        }

        ArrayList<Transaction> transactionList = (ArrayList<Transaction>) db.transactionDao()
                .getTransactionByOrderId(orderId);

        for (Transaction transaction : transactionList) {
            JSONObject object = new JSONObject();
            object.put(APIStatic.Key.order, apiOrderId);
            object.put(APIStatic.Key.amount, transaction.getAmount());
            object.put(APIStatic.Key.mode, transaction.getPaymentOption());

            DjangoJSONObjectRequest request = new DjangoJSONObjectRequest(
                    Request.Method.POST, APIStatic.Order.transactionUrl, object,
                    response -> {

                        //setting sync status as false in order to avoid unnecessary update to server
                        db.orderDao().setSync(orderId, false);
                        db.subOrderDao().setSync(orderId, false);
                        db.transactionDao().setSync(orderId, false);
                    }, new APIErrorListener(context), context);

            request.setRetryPolicy(new DefaultRetryPolicy(0, -1,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            VolleySingleton.getInstance(context).getRequestQueue().add(request);
        }
    }
}
