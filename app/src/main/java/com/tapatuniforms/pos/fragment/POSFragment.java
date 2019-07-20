package com.tapatuniforms.pos.fragment;

import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;
import com.tapatuniforms.pos.R;
import com.tapatuniforms.pos.adapter.CartAdapter;
import com.tapatuniforms.pos.adapter.CategoryAdapter;
import com.tapatuniforms.pos.adapter.DiscountAdapter;
import com.tapatuniforms.pos.adapter.ProductAdapter;
import com.tapatuniforms.pos.dialog.DiscountDialog;
import com.tapatuniforms.pos.dialog.PaymentDialog;
import com.tapatuniforms.pos.helper.APIStatic;
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
import com.tapatuniforms.pos.network.Billing;

import org.json.JSONException;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Objects;

public class POSFragment extends Fragment implements CategoryAdapter.CategoryClickListener, CartAdapter.UpdateItemListener {
    private static final String TAG = "POSFragment";

    private RecyclerView categoryRecycler, productRecycler, cartRecyclerView, discountRecyclerView;
    private Button paymentButton;
    private Button addDetailsButton;
    private TextView subTotalView, discountView, textNumberItems, totalView, maleView, femaleView, noProductText;

    private ArrayList<Category> categoryList;
    private ArrayList<Product> allProducts, productList, maleList, femaleList;
    private ArrayList<CartItem> cartList;
    private ArrayList<String> sizeList;
    private CategoryAdapter categoryAdapter;
    private ProductAdapter productAdapter;
    private CartAdapter cartAdapter;
    private DatabaseSingleton db;

    private View emptyCartView;
    private ImageView discountButton;
    private ImageView cartButton;

    private double subTotal, discount, tax, total;
    private DiscountDialog.Discount discountType = DiscountDialog.Discount.OTHER;
    private boolean isMaleSelected;
    private boolean notSelectedYet = true;

    private int position;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_product, container, false);

        initViews(view);

        return view;
    }

    /**
     * This method will initialize Views and variables
     *
     * @param view inflated Root View
     */
    private void initViews(View view) {
        // Initialize Views
        emptyCartView = view.findViewById(R.id.emptyCartView);
        categoryRecycler = view.findViewById(R.id.categoryRecycler);
        productRecycler = view.findViewById(R.id.productRecycler);
        cartRecyclerView = view.findViewById(R.id.cartRecyclerView);
        discountRecyclerView = view.findViewById(R.id.discountRecyclerView);
        paymentButton = view.findViewById(R.id.paymentButton);
        subTotalView = view.findViewById(R.id.subTotalView);
        textNumberItems = view.findViewById(R.id.textNumberItems);
        discountButton = view.findViewById(R.id.addDiscountButton);
        cartButton = view.findViewById(R.id.addCartButton);
        discountView = view.findViewById(R.id.discountView);
        totalView = view.findViewById(R.id.totalView);
        addDetailsButton = view.findViewById(R.id.addDetailsButton);
        maleView = view.findViewById(R.id.maleButton);
        femaleView = view.findViewById(R.id.femaleButton);
        noProductText = view.findViewById(R.id.noProductText);

        // Initialize Variables
        categoryList = new ArrayList<>();
        categoryAdapter = new CategoryAdapter(getContext(), categoryList);
        productList = new ArrayList<>();
        allProducts = new ArrayList<>();
        maleList = new ArrayList<>();
        femaleList = new ArrayList<>();
        sizeList = new ArrayList<>();
        productAdapter = new ProductAdapter(getContext(), productList, sizeList);
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
//        emptyCartIcon.setOnClickListener((v) -> clearCartData());

        // Category Views
        categoryRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        cartRecyclerView.addItemDecoration(new GridItemDecoration(8, 8));
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
        cartAdapter.setOnItemUpdateListener(this);

        //discount
        DiscountAdapter discountAdapter = new DiscountAdapter(getContext(), cartList);
        discountRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        discountRecyclerView.setAdapter(discountAdapter);

        // Payment Button
        paymentButton.setOnClickListener((v) -> onPaymentButtonClicked());

        discountButton.setOnClickListener((v) -> {
            /*showDiscountDialog()*/
            discountRecyclerView.setVisibility(View.VISIBLE);
            cartRecyclerView.setVisibility(View.GONE);
            emptyCartView.setVisibility(View.GONE);
        });

        cartButton.setOnClickListener(view -> {
            discountRecyclerView.setVisibility(View.GONE);

            if (cartList.size() > 0) {
                cartRecyclerView.setVisibility(View.VISIBLE);
                emptyCartView.setVisibility(View.GONE);
            } else {
                cartRecyclerView.setVisibility(View.GONE);
                emptyCartView.setVisibility(View.VISIBLE);
            }
        });

        //add details button
        addDetailsButton.setOnClickListener(view -> {
            showAddDetailsDialog();
        });

        //gender_type button
        maleView.setOnClickListener(view -> {

            if (!isMaleSelected || notSelectedYet) {
                isMaleSelected = true;
                notSelectedYet = false;

                categoryAdapter.clearBackground();
                //change button views
                maleView.setBackgroundColor(getResources().getColor(R.color.denim));
                maleView.setTextColor(getResources().getColor(R.color.white1));

                femaleView.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                femaleView.setTextColor(getResources().getColor(R.color.black1));

                //apply changes to products
                if (productList != null && productList.size() > 0) {
                    productList.clear();
                }
                maleList.clear();

                for (Product currentProduct : allProducts) {
                    if (currentProduct.getGender().equals(APIStatic.Constants.MALE)) {
                        productList.add(currentProduct);
                        maleList.add(currentProduct);
                    }
                }

                checkAvailability();
                productAdapter.notifyDataSetChanged();
            }
        });

        femaleView.setOnClickListener(view -> {

            if (isMaleSelected || notSelectedYet) {
                isMaleSelected = false;
                notSelectedYet = false;
                categoryAdapter.clearBackground();
                femaleView.setBackgroundColor(getResources().getColor(R.color.denim));
                femaleView.setTextColor(getResources().getColor(R.color.white1));

                maleView.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                maleView.setTextColor(getResources().getColor(R.color.black1));

                //apply changes to products
                if (productList != null && productList.size() > 0) {
                    productList.clear();
                }
                femaleList.clear();

                for (Product currentProduct : allProducts) {
                    if (currentProduct.getGender().equals(APIStatic.Constants.FEMALE)) {
                        productList.add(currentProduct);
                        femaleList.add(currentProduct);
                    }
                }

                checkAvailability();
                productAdapter.notifyDataSetChanged();
            }
        });

        // Fetch Data
        DataHelper.fetchCategories(getContext(), categoryList, categoryAdapter);
        DataHelper.fetchProducts(getContext(), allProducts, productList, productAdapter);
        checkAvailability();
    }

    private void showAddDetailsDialog() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        AlertDialog dialog = alertDialog.create();

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.add_student_details_layout, null);
        dialog.setView(view);

        final EditText studentIDText = view.findViewById(R.id.studentIDText);
        final EditText studentNameText = view.findViewById(R.id.studentNameText);
        final EditText classText = view.findViewById(R.id.classText);
        final EditText sectionText = view.findViewById(R.id.sectionText);
        final EditText fatherNameText = view.findViewById(R.id.fatherNameText);
        final EditText phoneText = view.findViewById(R.id.phoneText);
        final EditText emailText = view.findViewById(R.id.emailText);

        //just in case,, error is to be generated
        final TextInputLayout studentIDLayout = view.findViewById(R.id.studentIDInputLayout);
        final TextInputLayout studentNameLayout = view.findViewById(R.id.studentNameInputLayout);
        final TextInputLayout classLayout = view.findViewById(R.id.classInputLayout);
        final TextInputLayout sectionLayout = view.findViewById(R.id.sectionInputLayout);
        final TextInputLayout fatherNameLayout = view.findViewById(R.id.fatherNameInputLayout);
        final TextInputLayout phoneLayout = view.findViewById(R.id.phoneInputLayout);
        final TextInputLayout emailLayout = view.findViewById(R.id.emailInputLayout);

        final RadioButton maleRadio = view.findViewById(R.id.maleRadio);
        final RadioButton femaleRadio = view.findViewById(R.id.femaleRadio);

        final CardView addDetailsCard = view.findViewById(R.id.addDetailsButton);
        final CardView closeCard = view.findViewById(R.id.closeButton);

        //TODO: fetch school from API
        if (addDetailsCard != null) {
            addDetailsCard.setOnClickListener(view1 -> {
                String studentID = studentIDText.getText().toString();
                String studentName = studentNameText.getText().toString();
                String classStr = classText.getText().toString();
                String section = sectionText.getText().toString();
                String fatherName = fatherNameText.getText().toString();
                String phone = phoneText.getText().toString();
                int school = 1;
                String email = emailText.getText().toString();
                String gender = "";

                if (maleRadio != null && femaleRadio != null) {
                    if (maleRadio.isChecked()) {
                        gender = "M";
                    } else {
                        gender = "F";
                    }
                }

                try {
                    Billing.getInstance(getContext()).addStudentDetails(studentID, studentName, school, email, phone, classStr, section, gender, fatherName, dialog);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });
        }

        if (closeCard != null) {
            Log.d(TAG, "close card!!");
            closeCard.setOnClickListener(view1 -> {
                Log.d(TAG, "card closed");
                dialog.dismiss();
            });
        }

        dialog.setCancelable(false);
        dialog.show();
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

        ArrayList<Product> tempList;
        if (notSelectedYet) {
            tempList = allProducts;
        } else if (isMaleSelected) {
            tempList = maleList;
        } else {
            tempList = femaleList;
        }

        for (Product product : tempList) {
            if (product.getCategory() == category.getApiId()) {
                productList.add(product);
            }
        }

        checkAvailability();
        productAdapter.notifyDataSetChanged();
    }

    /**
     * This method will create a Discount Dialog
     */
    private void showDiscountDialog() {
        DiscountDialog dialog = new DiscountDialog(getContext());
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
        Objects.requireNonNull(dialog.getWindow()).clearFlags(WindowManager.
                LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
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
        });
        Objects.requireNonNull(dialog.getWindow()).clearFlags(WindowManager.
                LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
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

        for (CartItem cartItem : cartList) {
            Product product = cartItem.getProduct();
            //TODO: uncomment the following code and apply logic
            subOrderList.add(new SubOrder(0, product.getName(), product.getApiId(),
                    product.getSku(), Double.parseDouble(product.getPriceList().get(position)), cartItem.getQuantity(),
                    0, 0, 0, 0, false));
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
            //TODO: check if it works or not
            subTotal += cartItem.getQuantity() * Integer.parseInt(cartItem.getProduct().getPriceList().get(position));
        }

        switch (discountType) {
            case MONEY:
                calculatedDiscount = discount;
                break;
            case PERCENTAGE:
                calculatedDiscount = (subTotal * discount) / 100;
                break;
        }

        subTotalView.setText(decimalFormatter.format(subTotal));
        textNumberItems.setText("(" + cartQuantity + " items)");
        discountView.setText(decimalFormatter.format(calculatedDiscount));
        total = subTotal - calculatedDiscount;
        totalView.setText(decimalFormatter.format(total));
    }

    @Override
    public void onItemUpdateListener() {
        updatePriceView();

        if (cartList != null && cartList.size() == 0) {
            emptyCartView.setVisibility(View.VISIBLE);
            cartRecyclerView.setVisibility(View.GONE);
        }
    }

    /**
     * This class extends the ProductClickListener interface and will handle the item click inside
     * the productRecycler.
     */
    class ProductItemListener implements ProductAdapter.ProductClickListener {
        @Override
        public void onProductClicked(Product product, int pos) {

            position = pos;
            ViewHelper.hideView(emptyCartView);
            ViewHelper.showView(cartRecyclerView);
            ViewHelper.hideView(discountRecyclerView);

            for (CartItem cartItem : cartList) {
                if (cartItem.getProduct().getSizeList().get(pos).equals(product.getSizeList().get(pos))) {
                    cartItem.setQuantity(cartItem.getQuantity() + 1);
                    cartAdapter.notifyDataSetChanged();
                    updatePriceView();
                    return;
                }
            }

            cartList.add(new CartItem(product.getApiId(), 1, product, pos));
            cartAdapter.notifyDataSetChanged();
            updatePriceView();
        }
    }

    private void checkAvailability() {
        if (productList != null) {
            if (productList.size() == 0) {
                noProductText.setVisibility(View.VISIBLE);
            } else {
                noProductText.setVisibility(View.GONE);
            }
        }
    }
}
