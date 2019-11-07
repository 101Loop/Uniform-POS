package com.tapatuniforms.pos.network;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.civilmachines.drfapi.DjangoJSONArrayResponseRequest;
import com.civilmachines.drfapi.DjangoJSONObjectRequest;
import com.tapatuniforms.pos.R;
import com.tapatuniforms.pos.adapter.StockBoxAdapter;
import com.tapatuniforms.pos.adapter.StockIndentAdapter;
import com.tapatuniforms.pos.dialog.InventoryDialog;
import com.tapatuniforms.pos.fragment.StockEntryFragment;
import com.tapatuniforms.pos.helper.APIErrorListener;
import com.tapatuniforms.pos.helper.APIStatic;
import com.tapatuniforms.pos.helper.DatabaseSingleton;
import com.tapatuniforms.pos.helper.NotifyListener;
import com.tapatuniforms.pos.helper.Validator;
import com.tapatuniforms.pos.helper.VolleySingleton;
import com.tapatuniforms.pos.model.Box;
import com.tapatuniforms.pos.model.BoxItem;
import com.tapatuniforms.pos.model.Indent;
import com.tapatuniforms.pos.model.ProductHeader;
import com.tapatuniforms.pos.model.ProductVariant;
import com.tapatuniforms.pos.model.Stock;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class StockOrderAPI {
    private static final String TAG = "StockOrderAPI";
    private Context context;
    private static StockOrderAPI instance;

    private StockOrderAPI(Context context) {
        this.context = context;
    }

    public static synchronized StockOrderAPI getInstance(Context context) {
        if (instance == null) {
            instance = new StockOrderAPI(context);
        }

        return instance;
    }

    /**
     * Method to get indent list
     * stores the data when online and displays them if offline
     *
     * @param indentList    List of Indent, used to update the fetched data
     * @param allIndentList
     * @param adapter       reference to the adapter which is used to notify any changes
     * @param instance      reference of the calling class
     * @param db            DatabaseSingleton reference for db transactions
     */
    public void getIndentList(ArrayList<Indent> indentList, ArrayList<Indent> allIndentList,
                              StockIndentAdapter adapter, StockEntryFragment instance, DatabaseSingleton db) {
        if (!Validator.isNetworkConnected(context)) {
            indentList.addAll(db.indentDao().getAll());
            return;
        }

        DjangoJSONArrayResponseRequest request = new DjangoJSONArrayResponseRequest(
                Request.Method.GET,
                APIStatic.StockOrder.getIndentUrl,
                null,
                response -> {
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject jsonObject = response.getJSONObject(i);
                            indentList.add(new Indent(jsonObject));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    allIndentList.addAll(indentList);

                    if (indentList.size() != db.indentDao().getAll().size()) {
                        db.indentDao().deleteAll();
                        db.indentDao().insertAll(indentList);
                    }

                    if (instance != null) {
                        instance.checkIndentsAvailability();
                    }

                    if (adapter != null) {
                        adapter.notifyDataSetChanged();
                    }
                },
                new APIErrorListener(context),
                context);

        request.setRetryPolicy(new DefaultRetryPolicy(0, -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(context).getRequestQueue().add(request);
    }

    /**
     * Method to get box list
     * stores the data when online and displays them if offline
     *
     * @param id
     * @param boxList    List of Box, used to update the fetched data
     * @param allBoxList
     * @param adapter    reference to the adapter which is used to notify any changes
     * @param instance   reference of the calling class
     * @param db         DatabaseSingleton reference for db transactions
     */
    public void getBoxList(long id, ArrayList<Box> boxList, ArrayList<Box> allBoxList, StockBoxAdapter adapter,
                           StockEntryFragment instance, DatabaseSingleton db) {
        if (!Validator.isNetworkConnected(context)) {
            List<Box> boxes = db.boxDao().getBoxesByIndent(id);

            boxList.clear();
            boxList.addAll(boxes);

            if (instance != null) {
                instance.checkBoxAvailability();
            }

            if (adapter != null) {
                adapter.notifyDataSetChanged();
            }

            return;
        }

        DjangoJSONArrayResponseRequest request = new DjangoJSONArrayResponseRequest(
                Request.Method.GET,
                APIStatic.StockOrder.getIndentUrl + id + APIStatic.StockOrder.getBoxUrl,
                null,
                response -> {
                    boxList.clear();
                    allBoxList.clear();

                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject jsonObject = response.getJSONObject(i);
                            Box box = new Box(jsonObject);
                            boxList.add(box);
//                            db.boxDao().insert(box);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    allBoxList.addAll(boxList);
                    //TODO: data to be stored on the basis of sync status(for each db instance)
//                    if (boxList.size() != db.boxDao().getAll().size()) {
//                    db.boxDao().deleteAll();
                    db.boxDao().insertAll(boxList);
//                    }

                    if (instance != null) {
                        instance.checkBoxAvailability();
                    }

                    if (adapter != null) {
                        adapter.notifyDataSetChanged();
                    }
                },
                new APIErrorListener(context),
                context);

        request.setRetryPolicy(new DefaultRetryPolicy(0, -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(context).getRequestQueue().add(request);
    }

    /**
     * Method to get box item list
     * stores the data when online and displays them if offline
     *
     * @param boxItemList List of BoxItem, used to update the fetched data
     * @param id          Id of the box, used to show only the relevant box item
     * @param db          DatabaseSingleton reference for db transactions
     */
    public void getBoxItem(ArrayList<BoxItem> boxItemList, long id,
                           DatabaseSingleton db, NotifyListener listener) {

        if (!Validator.isNetworkConnected(context)) {

            for (BoxItem boxItem : db.boxItemDao().getAll()) {
                if (boxItem.getBoxId() == id) {
                    boxItemList.add(boxItem);
                }
            }

            if (listener != null)
                listener.onNotify();

            return;
        }

        DjangoJSONArrayResponseRequest request = new DjangoJSONArrayResponseRequest(
                Request.Method.GET,
                APIStatic.StockOrder.boxItemUrl + id + APIStatic.StockOrder.itemsUrl,
                null,
                response -> {
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject jsonObject = response.getJSONObject(i);

                            if (jsonObject.optInt(APIStatic.Key.box) == id) {
                                boxItemList.add(new BoxItem(jsonObject));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

//                    if (boxItemList.size() != db.boxItemDao().getAll().size()) {
                    db.boxItemDao().insertAll(boxItemList);

                    for (BoxItem boxItem : boxItemList) {
                        ProductHeader product = db.productHeaderDao().getProductHeaderById(boxItem.getProductId());

                        ProductVariant productVariant = db.productVariantDao().getProductVariantsById(product.getId()).get(0);
                        Stock stock = db.stockDao().getStocksById(productVariant.getId()).get(0);
                        if (!productVariant.isSynced()) {
                            db.productVariantDao().setSyncStatus(true, productVariant.getId());

                            int warehouseStock = stock.getWarehouse();
                            int itemScanned = boxItem.getNumberOfScannedItems();

                            db.stockDao().updateWarehouseStock(warehouseStock + itemScanned, productVariant.getId());
                            db.productVariantDao().updateWarehouseStock(stock.getWarehouse(), productVariant.getId());
                        }
                    }

//                    }
                    if (listener != null)
                        listener.onNotify();
                },
                new APIErrorListener(context),
                context);

        request.setRetryPolicy(new DefaultRetryPolicy(0, -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(context).getRequestQueue().add(request);
    }

    /**
     * Method to get indent list
     * stores the data when online and displays them if offline
     */
    public void indentRequestDetails(int productId, int quantity, int outletId, InventoryDialog inventoryDialog) {
        if (!Validator.isNetworkConnected(context)) {
            Toast.makeText(context, context.getString(R.string.no_network), Toast.LENGTH_SHORT).show();
            return;
        }

        JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put(APIStatic.Key.product, productId);
            jsonRequest.put(APIStatic.Key.qunatity, quantity);
            jsonRequest.put(APIStatic.Key.outlet, outletId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        DjangoJSONObjectRequest request = new DjangoJSONObjectRequest(
                Request.Method.POST,
                APIStatic.StockOrder.indentRequest,
                jsonRequest,
                response -> {
                    inventoryDialog.dismiss();
                    Toast.makeText(context, "Successful", Toast.LENGTH_SHORT).show();
                },
                new APIErrorListener(context),
                context);

        request.setRetryPolicy(new DefaultRetryPolicy(0, -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(context).getRequestQueue().add(request);
    }

    public void updateBoxItem(int boxId, int boxItemId, JSONObject jsonObject, DatabaseSingleton db, NotifyListener listener) {
        DjangoJSONObjectRequest request = new DjangoJSONObjectRequest(
                Request.Method.PATCH,
                APIStatic.StockOrder.boxItemUrl + boxId + APIStatic.StockOrder.itemsUrl + boxItemId + "/",
                jsonObject,
                response -> {
                    db.boxItemDao().delete(boxItemId);
                    db.boxItemDao().insert(new BoxItem(response));

                    if (listener != null)
                        listener.onNotifyResponse(new BoxItem(response));
                },
                new APIErrorListener(context),
                context
        );

        request.setRetryPolicy(new DefaultRetryPolicy(0, -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(context).getRequestQueue().add(request);
    }
}
