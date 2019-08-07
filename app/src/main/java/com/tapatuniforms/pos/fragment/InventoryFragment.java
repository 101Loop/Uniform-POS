package com.tapatuniforms.pos.fragment;


import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tapatuniforms.pos.R;
import com.tapatuniforms.pos.adapter.CategoryAdapter;
import com.tapatuniforms.pos.adapter.InventoryAdapter;
import com.tapatuniforms.pos.adapter.InventoryOrderAdapter;
import com.tapatuniforms.pos.dialog.InventoryDialog;
import com.tapatuniforms.pos.helper.APIStatic;
import com.tapatuniforms.pos.helper.DatabaseHelper;
import com.tapatuniforms.pos.helper.DatabaseSingleton;
import com.tapatuniforms.pos.helper.GridItemDecoration;
import com.tapatuniforms.pos.model.Category;
import com.tapatuniforms.pos.model.ProductHeader;
import com.tapatuniforms.pos.model.ProductVariant;
import com.tapatuniforms.pos.network.ProductAPI;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InventoryFragment extends Fragment implements InventoryAdapter.ButtonClickListener,
        InventoryOrderAdapter.ButtonClickListener, CategoryAdapter.CategoryClickListener, InventoryDialog.DialogDismissedListener {
    private static final String TAG = "InventoryFragment";
    private RecyclerView inventoryRecyclerView, recommendedRecyclerView;
    private InventoryAdapter inventoryAdapter;
    private InventoryOrderAdapter inventoryOrderAdapter;
    private ArrayList<Category> categoryList;
    private CategoryAdapter categoryAdapter;
    private RecyclerView categoryRecyclerView;
    private ArrayList<ProductHeader> allProducts;
    private ArrayList<ProductHeader> productList;
    private DatabaseSingleton db;
    private TextView maleView;
    private TextView femaleView;
    private boolean isMaleSelected;
    private boolean notSelectedYet = true;
    private ArrayList<ProductHeader> maleList;
    private ArrayList<ProductHeader> femaleList;
    private TextView noProductText;
    private ArrayList<ProductHeader> recommendedProductList;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inventory, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        db = DatabaseHelper.getDatabase(getContext());

        categoryList = new ArrayList<>();
        categoryRecyclerView = view.findViewById(R.id.categoryRecycler);
        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        categoryAdapter = new CategoryAdapter(getContext(), categoryList);
        categoryRecyclerView.addItemDecoration(new GridItemDecoration(16, 16));
        categoryRecyclerView.setAdapter(categoryAdapter);
        categoryAdapter.setOnCategorySelectedListener(this);

        allProducts = new ArrayList<>();
        productList = new ArrayList<>();
        inventoryRecyclerView = view.findViewById(R.id.inventoryRecyclerView);
        inventoryRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        inventoryRecyclerView.addItemDecoration(new GridItemDecoration(12, 12));
        inventoryAdapter = new InventoryAdapter(getContext(), productList);
        inventoryRecyclerView.setAdapter(inventoryAdapter);

        recommendedRecyclerView = view.findViewById(R.id.recommendedRecyclerView);
        recommendedRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL, false));
        recommendedProductList = new ArrayList<>();
        inventoryOrderAdapter = new InventoryOrderAdapter(getContext(), recommendedProductList);
        inventoryOrderAdapter.setOnButtonClickListener(this);
        recommendedRecyclerView.setAdapter(inventoryOrderAdapter);

        inventoryAdapter.setOnClickListener(this);

        maleView = view.findViewById(R.id.maleButton);
        femaleView = view.findViewById(R.id.femaleButton);
        noProductText = view.findViewById(R.id.noProductText);
        maleList = new ArrayList<>();
        femaleList = new ArrayList<>();

        //gender_type button
        maleView.setOnClickListener(v -> {

            if (!isMaleSelected || notSelectedYet) {

                isMaleSelected = true;
                notSelectedYet = false;

                categoryAdapter.clearBackground();
                //change button views
                maleView.setBackgroundColor(getResources().getColor(R.color.denim1));
                maleView.setTextColor(getResources().getColor(R.color.white1));

                femaleView.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                femaleView.setTextColor(getResources().getColor(R.color.black1));

                //apply changes to products
                if (productList != null && productList.size() > 0) {
                    productList.clear();
                }
                maleList.clear();

                for (ProductHeader currentProduct : allProducts) {
                    if (currentProduct.getGender().equals(APIStatic.Constants.MALE)) {
                        productList.add(currentProduct);
                        maleList.add(currentProduct);
                    }
                }

                checkAvailability();
                inventoryAdapter.notifyDataSetChanged();
            }
        });

        femaleView.setOnClickListener(v -> {

            if (isMaleSelected || notSelectedYet) {

                isMaleSelected = false;
                notSelectedYet = false;
                categoryAdapter.clearBackground();
                femaleView.setBackgroundColor(getResources().getColor(R.color.denim1));
                femaleView.setTextColor(getResources().getColor(R.color.white1));

                maleView.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                maleView.setTextColor(getResources().getColor(R.color.black1));

                //apply changes to products
                if (productList != null && productList.size() > 0) {
                    productList.clear();
                }
                femaleList.clear();

                for (ProductHeader currentProduct : allProducts) {
                    if (currentProduct.getGender().equals(APIStatic.Constants.FEMALE)) {
                        productList.add(currentProduct);
                        femaleList.add(currentProduct);
                    }
                }

                checkAvailability();
                inventoryAdapter.notifyDataSetChanged();
            }
        });

        ProductAPI.fetchCategories(getContext(), categoryList, categoryAdapter, db);
        ProductAPI.fetchProducts(getContext(), allProducts, productList, null, inventoryAdapter, db, inventoryOrderAdapter, this);
    }

    /**
     * if no products are available, show an empty state text
     */
    private void checkAvailability() {
        if (productList != null) {
            if (productList.size() == 0) {
                noProductText.setVisibility(View.VISIBLE);
                inventoryRecyclerView.setVisibility(View.GONE);
            } else {
                noProductText.setVisibility(View.GONE);
                inventoryRecyclerView.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * Method to list the products of the selected categories
     *
     * @param category selected category
     */
    @Override
    public void onCategorySelected(Category category) {

        productList.clear();

        ArrayList<ProductHeader> tempList;
        if (notSelectedYet) {
            tempList = allProducts;
        } else if (isMaleSelected) {
            tempList = maleList;
        } else {
            tempList = femaleList;
        }

        for (ProductHeader product : tempList) {
            if (product.getCategory() == category.getApiId()) {
                productList.add(product);
            }
        }

        checkAvailability();
        inventoryAdapter.notifyDataSetChanged();
    }

    /**
     * Method to show dialog
     *
     * @param product Product for which the dialog is opened
     * @param title   Title to differentiate their intentions
     */
    @Override
    public void onTransferButtonClick(ProductHeader product, String title) {
        InventoryDialog dialog = new InventoryDialog(getContext(), product, title);

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        dialog.setOnDialogDismissListener(this);

        dialog.show();
    }

    /**
     * Method to get recommended products
     */
    public void getRecommendedProductList() {
        recommendedProductList.clear();

        for (ProductHeader productHeader : allProducts) {
            List<ProductVariant> productVariantList = db.productVariantDao().getProductVariantsById(productHeader.getId());

            int stock = 0;
            for (ProductVariant currentVariant : productVariantList) {
                stock += currentVariant.getWarehouseStock();
            }
            productHeader.setTotalWarehouseStock(stock);
        }

        Collections.sort(allProducts, (productHeader, t1) -> productHeader.getTotalWarehouseStock() - t1.getTotalWarehouseStock());

        for (int i = 0; i < 5; i++) {
            recommendedProductList.add(allProducts.get(i));
        }
    }

    /**
     * Method to handle events when the inventory dialog is dismissed
     */
    @Override
    public void onDialogDismissListener() {
        getRecommendedProductList();
        inventoryAdapter.notifyDataSetChanged();
        inventoryOrderAdapter.notifyDataSetChanged();
    }
}
