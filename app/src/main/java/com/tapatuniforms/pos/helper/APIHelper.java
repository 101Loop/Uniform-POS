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
import com.tapatuniforms.pos.model.Product;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class APIHelper {

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
}
