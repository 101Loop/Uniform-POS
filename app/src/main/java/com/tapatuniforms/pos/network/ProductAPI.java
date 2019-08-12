package com.tapatuniforms.pos.network;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.civilmachines.drfapi.DjangoJSONArrayResponseRequest;
import com.civilmachines.drfapi.DjangoJSONObjectRequest;
import com.tapatuniforms.pos.R;
import com.tapatuniforms.pos.adapter.CategoryAdapter;
import com.tapatuniforms.pos.adapter.InventoryAdapter;
import com.tapatuniforms.pos.adapter.InventoryOrderAdapter;
import com.tapatuniforms.pos.adapter.ProductAdapter;
import com.tapatuniforms.pos.fragment.InventoryFragment;
import com.tapatuniforms.pos.helper.APIErrorListener;
import com.tapatuniforms.pos.helper.APIStatic;
import com.tapatuniforms.pos.helper.DatabaseSingleton;
import com.tapatuniforms.pos.helper.Validator;
import com.tapatuniforms.pos.helper.VolleySingleton;
import com.tapatuniforms.pos.model.Category;
import com.tapatuniforms.pos.model.Order;
import com.tapatuniforms.pos.model.ProductHeader;
import com.tapatuniforms.pos.model.ProductVariant;
import com.tapatuniforms.pos.model.SubOrder;
import com.tapatuniforms.pos.model.Transaction;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ProductAPI {
    private static final String TAG = "ProductAPI";

    /**
     * Method to fetch the categories from server
     *
     * @param context      Context of calling activity
     * @param categoryList list of categories
     * @param adapter      reference to CategoryAdapter
     */
    public static void fetchCategories(Context context, ArrayList<Category> categoryList,
                                       CategoryAdapter adapter, DatabaseSingleton db) {

        List<Category> categories = db.categoryDao().getAll();
        if (!Validator.isNetworkConnected(context)) {
            Toast.makeText(context, context.getString(R.string.no_network), Toast.LENGTH_SHORT).show();

            categoryList.clear();
            categoryList.addAll(categories);

            if (adapter != null) {
                adapter.notifyDataSetChanged();
            }

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

                    if (categoryList.size() != categories.size()) {
                        db.categoryDao().deleteAll();
                        db.categoryDao().insertAll(categoryList);
                    }

                }, new APIErrorListener(context), context);

        request.setRetryPolicy(new DefaultRetryPolicy(0, -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(context).getRequestQueue().add(request);
    }

    /**
     * Method to fetch products from server
     *
     * @param context               Context of calling activity
     * @param allProducts           list of products, this list is never changes
     * @param productList           list of products, changes according to the requirement
     * @param productAdapter        reference of product adapter to notify changes
     * @param db                    DatabaseSingleton reference to store and fetch from database
     * @param inventoryOrderAdapter Reference of adapter to update changes
     * @param inventoryFragment     Reference of fragment to update the list of products
     */
    public static void fetchProducts(Context context, ArrayList<ProductHeader> allProducts,
                                     ArrayList<ProductHeader> productList, ProductAdapter productAdapter,
                                     InventoryAdapter inventoryAdapter, DatabaseSingleton db,
                                     InventoryOrderAdapter inventoryOrderAdapter, InventoryFragment inventoryFragment) {

        List<ProductHeader> localProductList = db.productHeaderDao().getAllProductHeader();
        if (!Validator.isNetworkConnected(context)) {
            Toast.makeText(context, context.getString(R.string.no_network), Toast.LENGTH_SHORT).show();
            productList.clear();
            productList.addAll(localProductList);

            allProducts.clear();
            allProducts.addAll(localProductList);

            if (inventoryFragment != null) {
                inventoryFragment.getRecommendedProductList();
            }

            if (inventoryOrderAdapter != null) {
                inventoryOrderAdapter.notifyDataSetChanged();
            }

            return;
        }

        DjangoJSONArrayResponseRequest request = new DjangoJSONArrayResponseRequest(
                Request.Method.GET, APIStatic.Outlet.productUrl, null,
                response -> {
                    // Response Received
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject productJSON = response.optJSONObject(i);
                        ProductHeader product = new ProductHeader(productJSON);

                        productList.add(product);
                        allProducts.add(product);

                        //TODO: check sync status, delete the item and insert it again
                        /*if (!product.isSynced()) {
                            db.productHeaderDao().deleteById(product.getId());
                            db.productHeaderDao().insertProductHeader(product);
                        }*/
                    }

                    //TODO: 1. change this logic, products should be synced on the basis of their sync status, not size
                    //TODO: 2. sync status is to be fetched from the server(for each model in the DB)
                    if (localProductList == null || localProductList.size() < 1) {
                        ArrayList<ProductHeader> productHeaderList = new ArrayList<>();
                        ArrayList<ProductVariant> productVariantList = new ArrayList<>();
                        for (int i = 0; i < productList.size(); i++) {
                            ProductHeader productHeader = new ProductHeader(response.optJSONObject(i));
                            productHeaderList.add(productHeader);

                            for (int j = 0; j < productHeader.getVariantSize(); j++) {
                                productVariantList.add(new ProductVariant(response.optJSONObject(i), j));
                            }
                        }
                        db.productHeaderDao().deleteAllProductHeaders();
                        db.productVariantDao().deleteAllProductVariants();
                        db.productHeaderDao().insertAllProductHeader(productHeaderList);
                        db.productVariantDao().insertAllProductVariants(productVariantList);
                    }

                    if (inventoryAdapter != null) {
                        inventoryAdapter.notifyDataSetChanged();
                    }

                    if (inventoryFragment != null) {
                        inventoryFragment.getRecommendedProductList();
                    }

                    if (inventoryOrderAdapter != null) {
                        inventoryOrderAdapter.notifyDataSetChanged();
                    }

                    if (productAdapter != null) {
                        productAdapter.notifyDataSetChanged();
                    }

                }, new APIErrorListener(context), context);

        request.setRetryPolicy(new DefaultRetryPolicy(0, -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(context).getRequestQueue().add(request);
    }

    /**
     * Method to save and sync the order
     *
     * @param context         Context of the calling activity
     * @param db              DatabaseSingleton reference to store the data in the database
     * @param order           Order, which is to be saved and synced
     * @param subOrderList    list of SubOrder
     * @param transactionList list of Transaction
     */
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

        if (subOrderList.size() > 0) {
            for (SubOrder subOrder : subOrderList) {
                List<ProductVariant> productVariantList = db.productVariantDao().getProductVariantsById((int) subOrder.getProductApiId());

                ProductVariant productVariant = null;
                for (ProductVariant currentVariant : productVariantList) {
                    if (currentVariant.getPrice() == subOrder.getPrice()) {
                        productVariant = currentVariant;
                    }
                }
                assert productVariant != null;
                if (subOrder.getQuantity() > productVariant.getDisplayStock()) {
                    Log.e(TAG, "Syncing suborder: items count can't be greater than items in the display");
                    return;
                }

                db.productVariantDao().updateDisplayStock(productVariant.getDisplayStock() - subOrder.getQuantity(), productVariant.getId());
            }
        }

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
     * @param db      DatabaseSingleton reference to save order from database
     * @param order   Order
     */
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
     * @param db      DatabaseSingleton reference to save SubOrder in db
     */
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

//            List<ProductVariant> productVariantList = db.productVariantDao().getProductVariantsById((int) subOrder.getProductApiId());
            /*ProductVariant productVariant = null;
            for (ProductVariant currentVariant : productVariantList) {
                if (currentVariant.getPrice() == subOrder.getPrice()) {
                    productVariant = currentVariant;
                }
            }*/

//            ProductVariant finalProductVariant = productVariant;
            DjangoJSONObjectRequest request = new DjangoJSONObjectRequest(
                    Request.Method.POST, APIStatic.Order.subOrderUrl, object,
                    response -> {
                        /*if (finalProductVariant != null) {

                            *//*if (subOrder.getQuantity() > finalProductVariant.getDisplayStock()) {
                                Log.e(TAG, "Syncing suborder: items count can't be greater than items in the display");
                                return;
                            }*//*

//                            db.productVariantDao().updateDisplayStock(finalProductVariant.getDisplayStock() - subOrder.getQuantity(), finalProductVariant.getId());
                        }*/
                    }, new APIErrorListener(context), context);

            request.setRetryPolicy(new DefaultRetryPolicy(0, -1,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            VolleySingleton.getInstance(context).getRequestQueue().add(request);
        }
    }

    /**
     * Method to either save Transaction or make API call
     *
     * @param context    Context of the calling activity
     * @param orderId    id of the order to which, the transaction is related to
     * @param apiOrderId API's order id TODO: this is to be fetched from server, currently it is static
     * @param db         DatabaseSingleton reference to save the transaction in db
     */
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
