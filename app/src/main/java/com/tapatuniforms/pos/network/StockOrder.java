package com.tapatuniforms.pos.network;

import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.civilmachines.drfapi.DjangoJSONArrayResponseRequest;
import com.tapatuniforms.pos.adapter.StockBoxAdapter;
import com.tapatuniforms.pos.adapter.StockBoxItemAdapter;
import com.tapatuniforms.pos.adapter.StockIndentAdapter;
import com.tapatuniforms.pos.dialog.StockItemDialog;
import com.tapatuniforms.pos.fragment.StockEntryFragment;
import com.tapatuniforms.pos.helper.APIErrorListener;
import com.tapatuniforms.pos.helper.APIStatic;
import com.tapatuniforms.pos.helper.DatabaseSingleton;
import com.tapatuniforms.pos.helper.Validator;
import com.tapatuniforms.pos.helper.VolleySingleton;
import com.tapatuniforms.pos.model.Box;
import com.tapatuniforms.pos.model.BoxItem;
import com.tapatuniforms.pos.model.Indent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class StockOrder {
    private Context context;
    private static StockOrder instance;

    private StockOrder(Context context) {
        this.context = context;
    }

    public static synchronized StockOrder getInstance(Context context) {
        if (instance == null) {
            instance = new StockOrder(context);
        }

        return instance;
    }

    /**
     * Method to get indent list
     * stores the data when online and displays them if offline
     *
     * @param indentList List of Indent, used to update the fetched data
     * @param adapter reference to the adapter which is used to notify any changes
     * @param instance reference of the calling class
     * @param db DatabaseSingleton reference for db transactions
     * */
    public void getIndentList(ArrayList<Indent> indentList, StockIndentAdapter adapter, StockEntryFragment instance, DatabaseSingleton db) {
        if (!Validator.isNetworkConnected(context)) {
            indentList.addAll(db.indentDao().getAll());
            adapter.selectFirstIndent();
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

                    if (indentList.size() != db.indentDao().getAll().size()) {
                        db.indentDao().insertAll(indentList);
                    }

                    adapter.selectFirstIndent();
                    instance.checkIndentsAvailability();
                    adapter.notifyDataSetChanged();
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
     * @param boxList List of Box, used to update the fetched data
     * @param allBoxList List of Box, this list is used for any changes in the fetched list
     * @param adapter reference to the adapter which is used to notify any changes
     * @param instance reference of the calling class
     * @param db DatabaseSingleton reference for db transactions
     * */
    public void getBoxList(ArrayList<Box> boxList, ArrayList<Box> allBoxList, StockBoxAdapter adapter,
                           StockEntryFragment instance, DatabaseSingleton db) {
        if (!Validator.isNetworkConnected(context)) {
            boxList.addAll(db.boxDao().getAll());
            allBoxList.addAll(db.boxDao().getAll());
            instance.checkBoxAvailability();
            return;
        }

        DjangoJSONArrayResponseRequest request = new DjangoJSONArrayResponseRequest(
                Request.Method.GET,
                APIStatic.StockOrder.getBoxUrl,
                null,
                response -> {
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject jsonObject = response.getJSONObject(i);
                            boxList.add(new Box(jsonObject));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    allBoxList.addAll(boxList);

                    if (boxList.size() != db.boxDao().getAll().size()) {
                        db.boxDao().insertAll(boxList);
                    }

                    instance.checkBoxAvailability();
                    adapter.notifyDataSetChanged();
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
     * @param adapter reference to the adapter which is used to notify any changes
     * @param id Id of the box, used to show only the relevant box item
     * @param stockItemDialog reference of the calling class
     * @param db DatabaseSingleton reference for db transactions
     * */
    public void getBoxItem(ArrayList<BoxItem> boxItemList, StockBoxItemAdapter adapter, long id,
                           StockItemDialog stockItemDialog, DatabaseSingleton db) {

        if (!Validator.isNetworkConnected(context)) {

            for (BoxItem boxItem : db.boxItemDao().getAll()) {
                if (boxItem.getBoxId() == id) {
                    boxItemList.add(boxItem);
                }
            }

            stockItemDialog.checkAvailability();
            return;
        }

        DjangoJSONArrayResponseRequest request = new DjangoJSONArrayResponseRequest(
                Request.Method.GET,
                APIStatic.StockOrder.getBoxItem,
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

                    if (boxItemList.size() != db.boxItemDao().getAll().size()) {
                        db.boxItemDao().insertAll(boxItemList);
                    }

                    stockItemDialog.checkAvailability();
                    adapter.notifyDataSetChanged();
                },
                new APIErrorListener(context),
                context);

        request.setRetryPolicy(new DefaultRetryPolicy(0, -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(context).getRequestQueue().add(request);
    }
}
