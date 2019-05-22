package com.tapatuniforms.pos.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tapatuniforms.pos.R;
import com.tapatuniforms.pos.adapter.CartAdapter;
import com.tapatuniforms.pos.adapter.CategoryAdapter;
import com.tapatuniforms.pos.adapter.ProductAdapter;
import com.tapatuniforms.pos.dialog.CartItemDialog;
import com.tapatuniforms.pos.dialog.PaymentDialog;
import com.tapatuniforms.pos.dialog.UserDetailDialog;
import com.tapatuniforms.pos.helper.GridItemDecoration;
import com.tapatuniforms.pos.model.CartItem;
import com.tapatuniforms.pos.model.Category;
import com.tapatuniforms.pos.model.Product;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class POSFragment extends Fragment implements View.OnClickListener, CategoryAdapter.CategoryClickListener {
    private static final String TAG = "POSFragment";

    private RecyclerView categoryRecycler, genderRecycler, productRecycler, cartRecyclerView;
    private Button paymentButton;
    private TextView subTotalView, discountView;

    private ArrayList<Category> categoryList;
    private ArrayList<Product> productList;
    private ArrayList<CartItem> cartList;
    private CategoryAdapter categoryAdapter;
    private ProductAdapter productAdapter;
    private CartAdapter cartAdapter;

    private View emptyCartView;
    private ImageView emptyCartIcon;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_product, container, false);

        initViews(view);

        showUserDialog();

        return view;
    }

    private void initViews(View view) {
        emptyCartView = view.findViewById(R.id.emptyCartView);
        emptyCartIcon = view.findViewById(R.id.emptyCartIcon);
        genderRecycler = view.findViewById(R.id.genderRecycler);
        categoryRecycler = view.findViewById(R.id.categoryRecycler);
        productRecycler = view.findViewById(R.id.productRecycler);
        cartRecyclerView = view.findViewById(R.id.cartRecyclerView);

        emptyCartIcon.setOnClickListener(this);
        // Category Views
        categoryRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayout.HORIZONTAL, false));
        categoryList = getPlaceholderCategory();
        categoryAdapter = new CategoryAdapter(categoryList);
        categoryAdapter.setOnCategorySelectedListener(this);
        categoryRecycler.setAdapter(categoryAdapter);

        // product Views
        productRecycler.setLayoutManager(new GridLayoutManager(getContext(), 3));
        productRecycler.addItemDecoration(new GridItemDecoration(8, 8));
        productList = getProductList();
        productAdapter = new ProductAdapter(getContext(), productList);
        productRecycler.setAdapter(productAdapter);

        // cart Views
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        cartList = getCartList();
        cartAdapter = new CartAdapter(cartList);
        cartRecyclerView.setAdapter(cartAdapter);
        cartAdapter.setOnClickListener(new CartAdapter.CartItemListener() {
            @Override
            public void onCartItemClicked(CartItem item) {
                final CartItemDialog dialog = new CartItemDialog(getContext(), item);
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();

                dialog.setOnButtonClickListener(new CartItemDialog.CartItemDialogListener() {
                    @Override
                    public void onDoneButtonClicked() {
                        cartAdapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }

                    @Override
                    public void onRemoveButtonClicked(CartItem item) {
                        cartList.remove(item);
                        cartAdapter.loadNewData(cartList);

                        if (cartList.size() < 1) {
                            emptyCartView.setVisibility(View.VISIBLE);
                        }

                        dialog.dismiss();
                    }
                });
            }
        });

        paymentButton = view.findViewById(R.id.paymentButton);
        paymentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final PaymentDialog dialog = new PaymentDialog(getContext());
                dialog.show();
                dialog.setOnOrderCompleteListener(new PaymentDialog.OrderCompleteListener() {
                    @Override
                    public void onOrderCompleted() {
                        dialog.dismiss();
                        showUserDialog();
                    }
                });
                dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE  | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            }
        });

        genderRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayout.HORIZONTAL, false));
        CategoryAdapter genderAdapter = new CategoryAdapter(getPlaceholderFilter());
        genderAdapter.setOnCategorySelectedListener(new CategoryAdapter.CategoryClickListener() {
            @Override
            public void onCategorySelected(String category) {
                if (category.equals("All")) {
                    return;
                }
                ArrayList<Product> tempList = productList;
                productList.clear();

                for (Product item: tempList) {
                    if (item.getGender().equals(category)) {
                        productList.add(item);
                    }
                }
                productAdapter.loadNewData(productList);
            }
        });
        genderRecycler.setAdapter(genderAdapter);
    }

    private ArrayList<Category> getPlaceholderCategory() {
        ArrayList<Category> list = new ArrayList<>();

        list.add(new Category(1, "All Category"));
        list.add(new Category(1, "Trouser"));
        list.add(new Category(1, "Shirt"));
        list.add(new Category(1, "T-Shirt"));
        list.add(new Category(1, "Skirt"));
        list.add(new Category(1, "Accessories"));

        emptyCartView.setVisibility(View.INVISIBLE);
        return list;
    }

    private ArrayList<Category> getPlaceholderFilter() {
        ArrayList<Category> list = new ArrayList<>();

        list.add(new Category(1, "All"));
        list.add(new Category(1, "Male"));
        list.add(new Category(1, "Female"));

        emptyCartView.setVisibility(View.INVISIBLE);
        return list;
    }

    private ArrayList<Product> getProductList() {
        ArrayList<Product> list = new ArrayList<>();

        list.add(new Product(1, "Shirt type 1",  R.drawable.model4, "Shirt", "Male", "Medium", 443.23));
        list.add(new Product(1, "Shirt type 2", R.drawable.model2, "Shirt", "Male","Small", 543.23));
        list.add(new Product(1, "Trouser type 3", R.drawable.model3, "Trouser", "Female","Medium", 743.23));
        list.add(new Product(1, "T-Shirt type 4", R.drawable.model2, "T-Shirt", "Female","Large", 253.23));
        list.add(new Product(1, "Shirt type 5", R.drawable.model4,  "Shirt","Male","Medium", 543.23));
        list.add(new Product(1, "Skirt type 6", R.drawable.model2,  "Skirt", "Female","Large", 253.23));
        list.add(new Product(1, "Shirt type 7", R.drawable.model4, "Shirt", "Male","Medium", 543.23));
        list.add(new Product(1, "Shirt type 8", R.drawable.model2, "Shirt", "Male","Small", 436.23));
        list.add(new Product(1, "Trouser type 9", R.drawable.model3, "Trouser", "Male","Medium", 843.23));
        list.add(new Product(1, "Shirt type 10", R.drawable.model2, "Accessories", "Male","Small", 643.23));
        list.add(new Product(1, "Shirt type 11", R.drawable.model4, "Shirt", "Male","Medium", 243.23));
        list.add(new Product(1, "Skirt type 12", R.drawable.model2, "Skirt", "Female","Large", 234.23));

        return list;
    }

    private ArrayList<CartItem> getCartList() {
        ArrayList<CartItem> list = new ArrayList<>();

        ArrayList<Product> productList = getProductList();
        for (int i = 0; i < 8; i++) {
            Product product = productList.get(i);
            list.add(new CartItem(i, i+1, product));
        }

        return list;
    }

    @Override
    public void onClick(View v) {
        // Set to emptyCartIcon click listener
        cartList.clear();
        cartAdapter.loadNewData(cartList);
        emptyCartView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onCategorySelected(String category) {
        filterByCategory(category);
    }


    private void showUserDialog() {
        UserDetailDialog dialog = new UserDetailDialog(getContext());
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE  | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    private void filterByCategory(String category) {
        productList.clear();
        if (category.equals("All Category")) {
            productList = getProductList();
            productAdapter.loadNewData(productList);
            return;
        }

        for(Product product: getProductList()) {
            if (product.getType().equals(category)) {
                productList.add(product);
            }
        }

        productAdapter.notifyDataSetChanged();
    }
}
