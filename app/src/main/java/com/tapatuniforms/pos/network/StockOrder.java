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

    public void getIndentList(ArrayList<Indent> indentList, StockIndentAdapter adapter, StockEntryFragment instance) {
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

    public void getBoxList(ArrayList<Box> boxList, ArrayList<Box> allBoxList, StockBoxAdapter adapter, StockEntryFragment instance) {
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
                    instance.checkBoxAvailability();
                    adapter.notifyDataSetChanged();
                },
                new APIErrorListener(context),
                context);

        request.setRetryPolicy(new DefaultRetryPolicy(0, -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(context).getRequestQueue().add(request);
    }

    public void getBoxItem(ArrayList<BoxItem> boxItemList, StockBoxItemAdapter adapter, long id, StockItemDialog stockItemDialog) {
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
