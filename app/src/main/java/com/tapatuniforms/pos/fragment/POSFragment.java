package com.tapatuniforms.pos.fragment;

import android.os.Bundle;
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

public class POSFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "POSFragment";

    private RecyclerView categoryRecycler, productRecycler, cartRecyclerView;
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

        return view;
    }

    private void initViews(View view) {
        emptyCartView = view.findViewById(R.id.emptyCartView);
        emptyCartIcon = view.findViewById(R.id.emptyCartIcon);
        emptyCartIcon.setOnClickListener(this);
        // Category Views
        categoryRecycler = view.findViewById(R.id.categoryRecycler);
        categoryRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayout.HORIZONTAL, false));
        categoryList = getPlaceholderCategory();
        categoryAdapter = new CategoryAdapter(categoryList);
        categoryRecycler.setAdapter(categoryAdapter);

        // product Views
        productRecycler = view.findViewById(R.id.productRecycler);
        productRecycler.setLayoutManager(new GridLayoutManager(getContext(), 3));
        productRecycler.addItemDecoration(new GridItemDecoration(8, 8));
        productList = getProductList();
        productAdapter = new ProductAdapter(getContext(), productList);
        productRecycler.setAdapter(productAdapter);

        // cart Views
        cartRecyclerView = view.findViewById(R.id.cartRecyclerView);
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
                PaymentDialog dialog = new PaymentDialog(getContext());
                dialog.show();
                dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE  | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            }
        });
    }

    private ArrayList<Category> getPlaceholderCategory() {
        ArrayList<Category> list = new ArrayList<>();

        list.add(new Category(1, "All Category"));
        list.add(new Category(1, "Men's Fashion"));
        list.add(new Category(1, "Women's Fashion"));
        list.add(new Category(1, "Kids Wear"));
        list.add(new Category(1, "Some Category"));
        list.add(new Category(1, "Category"));

        emptyCartView.setVisibility(View.INVISIBLE);
        return list;
    }

    private ArrayList<Product> getProductList() {
        ArrayList<Product> list = new ArrayList<>();

        list.add(new Product(1, "Blue and Yellow Shirt", R.drawable.model4, "Medium", 2343.23));
        list.add(new Product(1, "Blue and Yellow Shirt", R.drawable.model2, "Medium", 2343.23));
        list.add(new Product(1, "Blue and Yellow Shirt", R.drawable.model3, "Medium", 2343.23));
        list.add(new Product(1, "Blue and Yellow Shirt", R.drawable.model2, "Medium", 2343.23));
        list.add(new Product(1, "Blue and Yellow Shirt", R.drawable.model4, "Medium", 2343.23));
        list.add(new Product(1, "Blue and Yellow Shirt", R.drawable.model2, "Medium", 2343.23));
        list.add(new Product(1, "Blue and Yellow Shirt", R.drawable.model4, "Medium", 2343.23));
        list.add(new Product(1, "Blue and Yellow Shirt", R.drawable.model2, "Medium", 2343.23));
        list.add(new Product(1, "Blue and Yellow Shirt", R.drawable.model3, "Medium", 2343.23));
        list.add(new Product(1, "Blue and Yellow Shirt", R.drawable.model2, "Medium", 2343.23));
        list.add(new Product(1, "Blue and Yellow Shirt", R.drawable.model4, "Medium", 2343.23));
        list.add(new Product(1, "Blue and Yellow Shirt", R.drawable.model2, "Medium", 2343.23));

        return list;
    }

    private ArrayList<CartItem> getCartList() {
        ArrayList<CartItem> list = new ArrayList<>();

        Product product = new Product(1, "Blue and Yellow Shirt", R.drawable.model2, "Medium", 2453);

        list.add(new CartItem(1, 4, product));
        list.add(new CartItem(2, 3, product));
        list.add(new CartItem(3, 6, product));
        list.add(new CartItem(4, 1, product));
        list.add(new CartItem(5, 3, product));
        list.add(new CartItem(6, 3, product));
        list.add(new CartItem(7, 2, product));
        list.add(new CartItem(8, 5, product));
        list.add(new CartItem(9, 1, product));

        return list;
    }

    @Override
    public void onClick(View v) {
        // Set to emptyCartIcon click listener
        cartList.clear();
        cartAdapter.loadNewData(cartList);
        emptyCartView.setVisibility(View.VISIBLE);
    }
}
