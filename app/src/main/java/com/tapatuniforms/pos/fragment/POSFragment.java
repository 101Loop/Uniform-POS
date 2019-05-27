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
import com.tapatuniforms.pos.helper.ViewHelper;
import com.tapatuniforms.pos.model.CartItem;
import com.tapatuniforms.pos.model.Category;
import com.tapatuniforms.pos.model.Product;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class POSFragment extends Fragment implements CategoryAdapter.CategoryClickListener {
    private static final String TAG = "POSFragment";

    private RecyclerView categoryRecycler, productRecycler, cartRecyclerView;
    private Button paymentButton;
    private TextView subTotalView, discountView, textNumberItems;

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

    /**
     * This method will initialize Views and variables
     * @param view inflated Root View
     */
    private void initViews(View view) {
        // Initialize Views
        emptyCartView = view.findViewById(R.id.emptyCartView);
        emptyCartIcon = view.findViewById(R.id.emptyCartIcon);
        categoryRecycler = view.findViewById(R.id.categoryRecycler);
        productRecycler = view.findViewById(R.id.productRecycler);
        cartRecyclerView = view.findViewById(R.id.cartRecyclerView);
        paymentButton = view.findViewById(R.id.paymentButton);
        subTotalView = view.findViewById(R.id.subTotalView);
        textNumberItems = view.findViewById(R.id.textNumberItems);

        // Initialize Variables
        categoryList = getPlaceholderCategory();
        categoryAdapter = new CategoryAdapter(categoryList);
        productList = getProductList();
        productAdapter = new ProductAdapter(getContext(), productList);
        cartList = new ArrayList<>();
        cartAdapter = new CartAdapter(cartList);

        // Call setView()
        setView();
    }

    /**
     * This method will set data or initial value
     */
    private void setView() {
        // Empty Cart Icon
        emptyCartIcon.setOnClickListener((v) -> clearCartData());

        // Category Views
        categoryRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayout.HORIZONTAL, false));
        categoryAdapter.setOnCategorySelectedListener(this);
        categoryRecycler.setAdapter(categoryAdapter);

        // Product Views
        productRecycler.setLayoutManager(new GridLayoutManager(getContext(), 3));
        productRecycler.addItemDecoration(new GridItemDecoration(8, 8));
        productRecycler.setAdapter(productAdapter);
        productAdapter.setOnProductClickListener(new ProductItemListener());

        // Cart Views
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        cartRecyclerView.setAdapter(cartAdapter);
        cartAdapter.setOnClickListener(new CartItemsListener());

        // Payment Button
        paymentButton.setOnClickListener((v) -> onPaymentButtonClicked());
    }

    private ArrayList<Category> getPlaceholderCategory() {
        ArrayList<Category> list = new ArrayList<>();

        list.add(new Category(1, "All Category"));
        list.add(new Category(2, "Trouser"));
        list.add(new Category(3, "Shirt"));
        list.add(new Category(4, "T-Shirt"));
        list.add(new Category(5, "Skirt"));
        list.add(new Category(6, "Accessories"));

        return list;
    }

    private ArrayList<Product> getProductList() {
        ArrayList<Product> list = new ArrayList<>();

        list.add(new Product(1, "Shirt type 1",  R.drawable.shirt, "Shirt", "Male", "Medium", 443.23));
        list.add(new Product(2, "Shirt type 2", R.drawable.shirt, "Shirt", "Male","Small", 543.23));
        list.add(new Product(3, "Trouser type 3", R.drawable.trouser, "Trouser", "Female","Medium", 743.23));
        list.add(new Product(4, "T-Shirt type 4", R.drawable.t_shirt, "T-Shirt", "Female","Large", 253.23));
        list.add(new Product(5, "Shirt type 5", R.drawable.shirt,  "Shirt","Male","Medium", 543.23));
        list.add(new Product(6, "Skirt type 6", R.drawable.skirt,  "Skirt", "Female","Large", 253.23));
        list.add(new Product(7, "Shirt type 7", R.drawable.shirt, "Shirt", "Male","Medium", 543.23));
        list.add(new Product(8, "Shirt type 8", R.drawable.shirt, "Shirt", "Male","Small", 436.23));
        list.add(new Product(9, "Trouser type 9", R.drawable.trouser, "Trouser", "Male","Medium", 843.23));
        list.add(new Product(10, "Shirt type 10", R.drawable.belt, "Accessories", "Male","Small", 643.23));
        list.add(new Product(11, "Shirt type 11", R.drawable.shirt, "Shirt", "Male","Medium", 243.23));
        list.add(new Product(12, "Skirt type 12", R.drawable.skirt, "Skirt", "Female","Large", 234.23));

        return list;
    }

    private void clearCartData() {
        // DeleteAll Clicked clear cart data
        cartList.clear();
        cartAdapter.loadNewData(cartList);
        ViewHelper.showView(emptyCartView);
        updatePriceView();
    }

    @Override
    public void onCategorySelected(String category) {
        filterByCategory(category);
    }

    private void showUserDialog() {
        UserDetailDialog dialog = new UserDetailDialog(getContext());
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        Objects.requireNonNull(dialog.getWindow()).clearFlags(WindowManager.
                LayoutParams.FLAG_NOT_FOCUSABLE  | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
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

    private void onPaymentButtonClicked() {
        final PaymentDialog dialog = new PaymentDialog(getContext());
        dialog.show();
        dialog.setOnOrderCompleteListener(() -> {
            dialog.dismiss();
            showUserDialog();
        });
        Objects.requireNonNull(dialog.getWindow()).clearFlags(WindowManager.
                LayoutParams.FLAG_NOT_FOCUSABLE  | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    private void updatePriceView() {
        double subTotal = 0;
        int cartQuantity = 0;

        DecimalFormat decimalFormatter = new DecimalFormat("₹#,##,###.##");

        for (CartItem cartItem : cartList) {
            cartQuantity += cartItem.getQuantity();
            subTotal += cartItem.getQuantity() * cartItem.getProduct().getPrice();
        }

        subTotalView.setText(decimalFormatter.format(subTotal));
        textNumberItems.setText("(" + cartQuantity + " items)");
        paymentButton.setText(decimalFormatter.format(subTotal));
    }

    /**
     * This class extends the ProductClickListener interface and will handle the item click inside
     * the productRecycler.
     */
    class ProductItemListener implements ProductAdapter.ProductClickListener {
        @Override
        public void onProductClicked(Product product) {
            ViewHelper.hideView(emptyCartView);

            for (CartItem cartItem: cartList) {
                if (cartItem.getId() == product.getId()) {
                    cartItem.setQuantity(cartItem.getQuantity() + 1);
                    cartAdapter.notifyDataSetChanged();
                    updatePriceView();
                    return;
                }
            }

            cartList.add(new CartItem(product.getId(), 1, product));
            cartAdapter.notifyDataSetChanged();
            updatePriceView();
        }
    }

    /**
     * This class extends the CartItemListener interface and will handle the item click inside
     * the cart.
     */
    class CartItemsListener implements CartAdapter.CartItemListener {

        @Override
        public void onCartItemClicked(CartItem item) {
            final CartItemDialog dialog = new CartItemDialog(getContext(), item);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();

            dialog.setOnButtonClickListener(new CartItemDialog.CartItemDialogListener() {
                @Override
                public void onDoneButtonClicked() {
                    cartAdapter.notifyDataSetChanged();
                    updatePriceView();
                    dialog.dismiss();
                }

                @Override
                public void onRemoveButtonClicked(CartItem item) {
                    cartList.remove(item);
                    cartAdapter.loadNewData(cartList);

                    if (cartList.size() < 1) {
                        emptyCartView.setVisibility(View.VISIBLE);
                    }

                    updatePriceView();
                    dialog.dismiss();
                }
            });
        }
    }
}
