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

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tapatuniforms.pos.R;
import com.tapatuniforms.pos.adapter.CartAdapter;
import com.tapatuniforms.pos.adapter.CategoryAdapter;
import com.tapatuniforms.pos.adapter.ProductAdapter;
import com.tapatuniforms.pos.dialog.CartItemDialog;
import com.tapatuniforms.pos.dialog.DiscountDialog;
import com.tapatuniforms.pos.dialog.PaymentDialog;
import com.tapatuniforms.pos.dialog.UserDetailDialog;
import com.tapatuniforms.pos.helper.DataHelper;
import com.tapatuniforms.pos.helper.DatabaseHelper;
import com.tapatuniforms.pos.helper.DatabaseSingleton;
import com.tapatuniforms.pos.helper.GridItemDecoration;
import com.tapatuniforms.pos.helper.ViewHelper;
import com.tapatuniforms.pos.model.CartItem;
import com.tapatuniforms.pos.model.Category;
import com.tapatuniforms.pos.model.Order;
import com.tapatuniforms.pos.model.Product;
import com.tapatuniforms.pos.model.SubOrder;
import com.tapatuniforms.pos.model.Transaction;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Objects;

public class POSFragment extends Fragment implements CategoryAdapter.CategoryClickListener {
    private static final String TAG = "POSFragment";

    private RecyclerView categoryRecycler, productRecycler, cartRecyclerView;
    private Button paymentButton;
    private TextView subTotalView, discountView, textNumberItems;

    private ArrayList<Category> categoryList;
    private ArrayList<Product> allProducts, productList;
    private ArrayList<CartItem> cartList;
    private CategoryAdapter categoryAdapter;
    private ProductAdapter productAdapter;
    private CartAdapter cartAdapter;
    private DatabaseSingleton db;

    private View emptyCartView;
    private ImageView emptyCartIcon, discountButton;

    private double subTotal, discount, tax, total;
    private DiscountDialog.Discount discountType = DiscountDialog.Discount.OTHER;

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
        discountButton = view.findViewById(R.id.addDiscountButton);
        discountView = view.findViewById(R.id.discountView);

        // Initialize Variables
        categoryList = new ArrayList<>();
        categoryAdapter = new CategoryAdapter(categoryList);
        productList = new ArrayList<>();
        allProducts = new ArrayList<>();
        productAdapter = new ProductAdapter(getContext(), productList);
        cartList = new ArrayList<>();
        cartAdapter = new CartAdapter(cartList);

        db = DatabaseHelper.getDatabase(getContext());

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

        discountButton.setOnClickListener((v) -> showDiscountDialog());

        // Fetch Data
        DataHelper.fetchCategories(getContext(), categoryList, categoryAdapter);
        DataHelper.fetchProducts(getContext(), allProducts, productList, productAdapter);
    }

    /**
     * This method will clear cart
     */
    private void clearCartData() {
        // DeleteAll Clicked clear cart data
        cartList.clear();
        cartAdapter.loadNewData(cartList);
        ViewHelper.showView(emptyCartView);
        updatePriceView();
    }

    @Override
    public void onCategorySelected(Category category) {
        productList.clear();

        for(Product product: allProducts) {
            if (product.getCategory() == category.getApiId()) {
                productList.add(product);
            }

            if (category.getApiId() == -2)
                productList.add(product);
        }

        productAdapter.notifyDataSetChanged();
    }

    /**
     * This method will Show User Dialog
     */
    private void showUserDialog() {
        UserDetailDialog dialog = new UserDetailDialog(getContext());
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        Objects.requireNonNull(dialog.getWindow()).clearFlags(WindowManager.
                LayoutParams.FLAG_NOT_FOCUSABLE  | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    /**
     * This method will create a Discount Dialog
     */
    private void showDiscountDialog() {
        DiscountDialog dialog = new DiscountDialog(getContext());
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
        Objects.requireNonNull(dialog.getWindow()).clearFlags(WindowManager.
                LayoutParams.FLAG_NOT_FOCUSABLE  | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        dialog.setOnDiscountChangeListener((type, amount) -> {
//            switch (type) {
//                case PERCENTAGE:
//                    discountView.setText("" + amount + "%");
//                    break;
//                case MONEY:
//                    discount = amount;
//                    DecimalFormat decimalFormatter = new DecimalFormat("₹#,##,###.##");
//                    discountView.setText(decimalFormatter.format(amount));
//                    break;
//            }
            discountType = type;
            discount = amount;
            updatePriceView();
        });
    }

    /**
     * Handle click for the payment button
     */
    private void onPaymentButtonClicked() {
        final PaymentDialog dialog = new PaymentDialog(getContext(), total);
        dialog.show();
        dialog.setOnOrderCompleteListener(transactionList -> {
            orderCompleted(transactionList);
            dialog.dismiss();
            showUserDialog();
        });
        Objects.requireNonNull(dialog.getWindow()).clearFlags(WindowManager.
                LayoutParams.FLAG_NOT_FOCUSABLE  | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    /**
     * This method will save order to database and call sync function
     */
    private void orderCompleted(ArrayList<Transaction> transactionList) {
        Order order = new Order(0, -1, "Vivek", "8826317151",
                "me@vivekkaushik.com", "",
                total, discount, false);

        ArrayList<SubOrder> subOrderList = new ArrayList<>();

        for (CartItem cartItem: cartList) {
            Product product = cartItem.getProduct();
            subOrderList.add(new SubOrder(0, product.getName(), product.getSku(), product.getPrice(),
                    cartItem.getQuantity(), 0, 0, 0, 0, false));
        }

        DataHelper.saveAndSyncOrder(getContext(), db, order, subOrderList, transactionList);
    }

    /**
     * Helper method to calculate and update price on the UI
     */
    private void updatePriceView() {
        double subTotal = 0;
        int cartQuantity = 0;
        double calculatedDiscount = 0;

        DecimalFormat decimalFormatter = new DecimalFormat("₹#,##,###.##");

        for (CartItem cartItem : cartList) {
            cartQuantity += cartItem.getQuantity();
            subTotal += cartItem.getQuantity() * cartItem.getProduct().getPrice();
        }

        switch (discountType) {
            case MONEY:
                calculatedDiscount = discount;
                break;
            case PERCENTAGE:
                calculatedDiscount = (subTotal * discount)/100;
                break;
        }

        subTotalView.setText(decimalFormatter.format(subTotal));
        textNumberItems.setText("(" + cartQuantity + " items)");
        discountView.setText(decimalFormatter.format(calculatedDiscount));
        total = subTotal - calculatedDiscount;
        paymentButton.setText(decimalFormatter.format(total));
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
                if (cartItem.getId() == product.getApiId()) {
                    cartItem.setQuantity(cartItem.getQuantity() + 1);
                    cartAdapter.notifyDataSetChanged();
                    updatePriceView();
                    return;
                }
            }

            cartList.add(new CartItem(product.getApiId(), 1, product));
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
