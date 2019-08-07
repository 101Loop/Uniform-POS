package com.tapatuniforms.pos.network;

import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.civilmachines.drfapi.DjangoJSONArrayResponseRequest;
import com.tapatuniforms.pos.adapter.DiscountAdapter;
import com.tapatuniforms.pos.fragment.POSFragment;
import com.tapatuniforms.pos.helper.APIErrorListener;
import com.tapatuniforms.pos.helper.APIStatic;
import com.tapatuniforms.pos.helper.DatabaseSingleton;
import com.tapatuniforms.pos.helper.Validator;
import com.tapatuniforms.pos.helper.VolleySingleton;
import com.tapatuniforms.pos.model.Discount;

import org.json.JSONException;

import java.util.ArrayList;

public class OrderAPI {

    private static final String TAG = "Billing";
    private static OrderAPI instance;
    private Context context;

    private OrderAPI(Context context) {
        this.context = context;
    }

    public static synchronized OrderAPI getInstance(Context context) {
        if (instance == null) {
            instance = new OrderAPI(context);
        }
        return instance;
    }

    /**
     * Method to get discount list
     *
     * @param discountList List of discounts
     * @param adapter      Reference of adapter to update changes
     * @param posFragment  Reference to update notification count accordingly
     * @param db           Reference of db for db transaction
     */
    public void getDiscount(ArrayList<Discount> discountList, DiscountAdapter adapter, POSFragment posFragment, DatabaseSingleton db) {
        if (!Validator.isNetworkConnected(context)) {
            discountList.addAll(db.discountDao().getAll());
            posFragment.updateDiscountNotification(discountList.size());
            return;
        }

        DjangoJSONArrayResponseRequest request = new DjangoJSONArrayResponseRequest(
                Request.Method.GET,
                APIStatic.Order.discountUrl,
                null,
                response -> {
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            discountList.add(new Discount(response.getJSONObject(i)));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    if (discountList.size() != db.discountDao().getAll().size()) {
                        db.discountDao().deleteAll();
                        db.discountDao().insertAll(discountList);
                    }

                    posFragment.updateDiscountNotification(discountList.size());
                    adapter.notifyDataSetChanged();
                },
                new APIErrorListener(context),
                context
        );

        request.setRetryPolicy(new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(context).getRequestQueue().add(request);
    }
}
