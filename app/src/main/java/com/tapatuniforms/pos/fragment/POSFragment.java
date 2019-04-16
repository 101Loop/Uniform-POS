package com.tapatuniforms.pos.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tapatuniforms.pos.R;
import com.tapatuniforms.pos.adapter.CartAdapter;
import com.tapatuniforms.pos.adapter.CategoryAdapter;
import com.tapatuniforms.pos.adapter.ProductAdapter;
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

public class POSFragment extends Fragment {
    private static final String TAG = "POSFragment";

    private RecyclerView categoryRecycler, productRecycler, cartRecyclerView;
    private TextView subTotalView, discountView;

    private ArrayList<Category> categoryList;
    private ArrayList<Product> productList;
    private CategoryAdapter categoryAdapter;
    private ProductAdapter productAdapter;
    private CartAdapter cartAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_product, container, false);

        initViews(view);

        return view;
    }

    private void initViews(View view) {
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
        cartAdapter = new CartAdapter(getCartList());
        cartRecyclerView.setAdapter(cartAdapter);
    }

    private ArrayList<Category> getPlaceholderCategory() {
        ArrayList<Category> list = new ArrayList<>();

        list.add(new Category(1, "All Category"));
        list.add(new Category(1, "Men's Fashion"));
        list.add(new Category(1, "Women's Fashion"));
        list.add(new Category(1, "Kids Wear"));
        list.add(new Category(1, "Some Category"));
        list.add(new Category(1, "Category"));

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

        list.add(new CartItem(1, 3, product));
        list.add(new CartItem(1, 3, product));
        list.add(new CartItem(1, 3, product));
        list.add(new CartItem(1, 3, product));
        list.add(new CartItem(1, 3, product));
        list.add(new CartItem(1, 3, product));
        list.add(new CartItem(1, 3, product));
        list.add(new CartItem(1, 3, product));
        list.add(new CartItem(1, 3, product));

        return list;
    }
}
