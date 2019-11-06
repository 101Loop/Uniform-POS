package com.tapatuniforms.pos.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.tapatuniforms.pos.R;
import com.tapatuniforms.pos.adapter.CartAdapter;
import com.tapatuniforms.pos.adapter.CategoryAdapter;
import com.tapatuniforms.pos.adapter.DiscountAdapter;
import com.tapatuniforms.pos.adapter.ProductAdapter;
import com.tapatuniforms.pos.adapter.ReturnItemAdapter;
import com.tapatuniforms.pos.dialog.PaymentDialog;
import com.tapatuniforms.pos.helper.APIStatic;
import com.tapatuniforms.pos.helper.DatabaseHelper;
import com.tapatuniforms.pos.helper.DatabaseSingleton;
import com.tapatuniforms.pos.helper.GridItemDecoration;
import com.tapatuniforms.pos.helper.Validator;
import com.tapatuniforms.pos.helper.ViewHelper;
import com.tapatuniforms.pos.listener.ReturnItemClickListener;
import com.tapatuniforms.pos.model.CartItem;
import com.tapatuniforms.pos.model.Category;
import com.tapatuniforms.pos.model.Discount;
import com.tapatuniforms.pos.model.Order;
import com.tapatuniforms.pos.model.ProductHeader;
import com.tapatuniforms.pos.model.ProductVariant;
import com.tapatuniforms.pos.model.Student;
import com.tapatuniforms.pos.model.SubOrder;
import com.tapatuniforms.pos.model.Transaction;
import com.tapatuniforms.pos.network.OrderAPI;
import com.tapatuniforms.pos.network.ProductAPI;
import com.tapatuniforms.pos.network.SchoolAPI;

import org.json.JSONException;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.tapatuniforms.pos.helper.APIStatic.Constants.AMOUNT;
import static com.tapatuniforms.pos.helper.APIStatic.Constants.OTHER;
import static com.tapatuniforms.pos.helper.APIStatic.Constants.PERCENTAGE;

public class POSFragment extends BaseFragment implements CategoryAdapter.CategoryClickListener, CartAdapter.UpdateItemListener, DiscountAdapter.DiscountInterface, ReturnItemClickListener {
    private static final String TAG = "POSFragment";
    private RecyclerView categoryRecycler, productRecycler, cartRecyclerView, discountRecyclerView;
    private Button paymentButton;
    private Button addDetailsButton;
    private TextView subTotalView, discountView, textNumberItems, totalView, maleView, femaleView,
            noProductText, categoryText, cartNotification, discountNotification;
    private ArrayList<Category> categoryList;
    private ArrayList<ProductHeader> maleList;
    private ArrayList<ProductHeader> femaleList;
    private ArrayList<ProductHeader> allProducts, productList;
    private ArrayList<CartItem> cartList;
    private CategoryAdapter categoryAdapter;
    private ProductAdapter productAdapter;
    private CartAdapter cartAdapter;
    private DatabaseSingleton db;
    private View emptyCartView;
    private ImageView discountButton;
    private ImageView cartButton;
    private double total;
    private boolean isMaleSelected;
    private boolean notSelectedYet = true;
    private TextInputLayout studentIDLayout;
    private TextInputLayout studentNameLayout;
    private TextInputLayout fatherNameLayout;
    private TextInputLayout phoneLayout;
    private TextInputLayout emailLayout;
    private TextView genderErrorText;
    private String studentID;
    private String studentName;
    private String standard;
    private String section;
    private String fatherName;
    private String gender;
    private String phone;
    private String email;
    private ProgressDialog progressDialog;
    private EditText studentIdText;
    private Button submitButton;
    private Button returnButton;
    private Student[] studentDetails = new Student[1];
    private ArrayList<Discount> discountList;
    private String discountType = OTHER;
    private int discount;
    private TextView noDiscountText;
    private ArrayList<Student> studentList;
    private ArrayList<String> itemList;
    private TextView noItemsText;
    private RecyclerView itemRecyclerView;
    private CardView backButton;
    private ReturnItemAdapter returnItemAdapter;

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
        categoryText = view.findViewById(R.id.categoryText);
        cartNotification = view.findViewById(R.id.cartNotification);
        discountNotification = view.findViewById(R.id.discountNotification);
        studentIdText = view.findViewById(R.id.studentIDText);
        submitButton = view.findViewById(R.id.submit);
        returnButton = view.findViewById(R.id.returnButton);
        noDiscountText = view.findViewById(R.id.noDiscountText);
        backButton = view.findViewById(R.id.backButton);

        // Initialize Variables
        categoryList = new ArrayList<>();
        categoryAdapter = new CategoryAdapter(getContext(), categoryList);
        productList = new ArrayList<>();
        allProducts = new ArrayList<>();
        studentList = new ArrayList<>();
        maleList = new ArrayList<>();
        femaleList = new ArrayList<>();
        productAdapter = new ProductAdapter(getContext(), productList);
        cartList = new ArrayList<>();
        cartAdapter = new CartAdapter(getContext(), cartList);

        db = DatabaseHelper.getDatabase(getContext());

        // Call setView()
        setView();
    }

    /**
     * This method will set data or initial value
     */
    private void setView() {
        // Category Views
        categoryRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        cartRecyclerView.addItemDecoration(new GridItemDecoration(8, 8));

        categoryRecycler.addItemDecoration(new GridItemDecoration(16, 16));
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
        discountList = new ArrayList<>();
        DiscountAdapter discountAdapter = new DiscountAdapter(getContext(), discountList);
        discountRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        discountRecyclerView.setAdapter(discountAdapter);
        OrderAPI.getInstance(getContext()).getDiscount(discountList, discountAdapter, this, db);
        discountAdapter.setOnDiscountClickListener(this);

        SchoolAPI.getInstance(getActivity()).getStudents(studentList, db);

        //back button
        backButton.setOnClickListener(view -> onBackPressClicked());

        // Payment Button
        paymentButton.setOnClickListener((v) -> onPaymentButtonClicked());

        discountButton.setOnClickListener((v) -> {
            /*showDiscountDialog()*/
            discountRecyclerView.setVisibility(View.VISIBLE);
            cartRecyclerView.setVisibility(View.GONE);
            emptyCartView.setVisibility(View.GONE);

            updateDiscountNotification(discountList.size());
            checkDiscountAvailability();
        });

        cartButton.setOnClickListener(view -> {
            discountRecyclerView.setVisibility(View.GONE);
            noDiscountText.setVisibility(View.GONE);

            if (cartList.size() > 0) {
                cartRecyclerView.setVisibility(View.VISIBLE);
                emptyCartView.setVisibility(View.GONE);
            } else {
                cartRecyclerView.setVisibility(View.GONE);
                emptyCartView.setVisibility(View.VISIBLE);
            }
        });

        studentIdText.setOnEditorActionListener(((textView, actionId, keyEvent) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                onSubmitClick();
                return true;
            }

            return false;
        }));

        //fetch student details
        submitButton.setOnClickListener(view -> onSubmitClick());

        //return items
        returnButton.setOnClickListener(view -> showReturnItemDialog());

        //add details button
        addDetailsButton.setOnClickListener(view -> showAddDetailsDialog());

        //gender_type button
        maleView.setOnClickListener(view -> {

            if (!isMaleSelected || notSelectedYet) {
                categoryText.setText(null);

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
                productAdapter.notifyDataSetChanged();
            }
        });

        femaleView.setOnClickListener(view -> {

            if (isMaleSelected || notSelectedYet) {
                categoryText.setText(null);

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
                productAdapter.notifyDataSetChanged();
            }
        });

        // Fetch Data
        ProductAPI.fetchCategories(getContext(), categoryList, categoryAdapter, db);
        ProductAPI.fetchProducts(getContext(), allProducts, productList, productAdapter, null, db, null, null);
    }

    private void onSubmitClick() {
        String studentId = studentIdText.getText().toString();

        Validator.hideKeyboard(Objects.requireNonNull(getActivity()));
        studentIdText.clearFocus();

        List<Student> localStudentList = db.studentDao().getAll();
        if (localStudentList.size() != studentList.size()) {
            studentList.clear();
            studentList.addAll(localStudentList);
        }

        if (!studentId.isEmpty()) {
            for (Student student : studentList) {
                if (student.getStudentId().equals(studentId)) {
                    studentDetails[0] = student;
                    Toast.makeText(getContext(), "Order will be placed for: " + student.getName(), Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            Toast.makeText(getContext(), "No student found", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Method to check the availability of the discounts
     */
    private void checkDiscountAvailability() {
        if (discountList != null && discountList.size() > 0) {
            noDiscountText.setVisibility(View.GONE);
            discountRecyclerView.setVisibility(View.VISIBLE);
        } else {
            noDiscountText.setVisibility(View.VISIBLE);
            discountRecyclerView.setVisibility(View.GONE);
        }
    }

    /**
     * Method to create add details progressDialog
     */
    private void showAddDetailsDialog() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        AlertDialog dialog = alertDialog.create();

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        }

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.add_student_details_layout, null);
        dialog.setView(view);

        final EditText studentIDText = view.findViewById(R.id.studentIDText);
        final EditText studentNameText = view.findViewById(R.id.studentNameText);
        final EditText fatherNameText = view.findViewById(R.id.fatherNameText);
        final EditText phoneText = view.findViewById(R.id.phoneText);
        final EditText emailText = view.findViewById(R.id.emailText);
        final MaterialSpinner standardDropdown = view.findViewById(R.id.standardDropdown);
        final MaterialSpinner sectionDropdown = view.findViewById(R.id.sectionDropdown);

        //just in case,, error is to be generated
        studentIDLayout = view.findViewById(R.id.studentIDInputLayout);
        studentNameLayout = view.findViewById(R.id.studentNameInputLayout);
        fatherNameLayout = view.findViewById(R.id.fatherNameInputLayout);
        phoneLayout = view.findViewById(R.id.phoneInputLayout);
        emailLayout = view.findViewById(R.id.emailInputLayout);
        genderErrorText = view.findViewById(R.id.genderErrorText);

        final RadioButton maleRadio = view.findViewById(R.id.maleRadio);
        final RadioButton femaleRadio = view.findViewById(R.id.femaleRadio);

        final CardView addDetailsCard = view.findViewById(R.id.addDetailsButton);
        final CardView closeCard = view.findViewById(R.id.closeButton);

        final String[] arrStandard = getResources().getStringArray(R.array.standard_array);
        standard = arrStandard[0];
        if (standardDropdown != null) {
            standardDropdown.setItems(arrStandard);
            standardDropdown.setOnItemSelectedListener((MaterialSpinner.OnItemSelectedListener<String>) (view12, position, id, item) -> standard = item);
        }

        final String[] arrSection = getResources().getStringArray(R.array.section_array);
        section = arrSection[0];
        if (sectionDropdown != null) {
            sectionDropdown.setItems(arrSection);
            sectionDropdown.setOnItemSelectedListener((MaterialSpinner.OnItemSelectedListener<String>) (view12, position, id, item) -> section = item);
        }

        if (addDetailsCard != null) {
            addDetailsCard.setOnClickListener(view1 -> {
                studentID = studentIDText.getText().toString();
                studentName = studentNameText.getText().toString();
                fatherName = fatherNameText.getText().toString();
                phone = phoneText.getText().toString();
                int school = db.schoolDao().getAll().get(0).getId();
                email = emailText.getText().toString();

                if (maleRadio != null && femaleRadio != null) {
                    if (maleRadio.isChecked()) {
                        gender = "M";
                    } else if (femaleRadio.isChecked()) {
                        gender = "F";
                    }
                }

                //input validation
                if (isInputValid()) {
                    try {
                        SchoolAPI.getInstance(getContext()).addStudentDetails(studentID, studentName, school, email, phone, this.standard, section, gender, fatherName, dialog, db);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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
     * Method to create return item alert dialog
     */
    private void showReturnItemDialog() {
        //TODO: fetch items from server and implement return

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        AlertDialog dialog = alertDialog.create();

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        }

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.return_popup_layout, null);
        dialog.setView(view);

        itemList = new ArrayList<>();

        noItemsText = view.findViewById(R.id.noItemsText);
        itemRecyclerView = view.findViewById(R.id.itemRecyclerView);

        final EditText billingIdText = view.findViewById(R.id.billingIDText);
        final EditText studentIdText = view.findViewById(R.id.studentIDText);
        final CardView checkButton = view.findViewById(R.id.checkButton);
        checkButton.setOnClickListener(view1 -> {
            String billingId = billingIdText.getText().toString();
            String studentId = studentIdText.getText().toString();

            if (!billingId.isEmpty() && !studentId.isEmpty())
                checkReturnItems(billingId, studentId);
            else {
                Toast.makeText(getContext(), "Billing ID and Student ID both are required", Toast.LENGTH_SHORT).show();
            }
        });

        returnItemAdapter = new ReturnItemAdapter(getContext(), itemList);

        itemRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        itemRecyclerView.setAdapter(returnItemAdapter);
        returnItemAdapter.setOnReturnItemClickListener(this);
        checkItemsAvailability();

        dialog.show();
    }

    private void checkReturnItems(String billingId, String studentId) {
        itemList.clear();
        for (int i = 0; i < 3; i++) {
            itemList.add("Shirt type: " + i);
        }
        returnItemAdapter.notifyDataSetChanged();
        checkItemsAvailability();
    }

    /**
     * Method to check if there are any items to return or not
     */
    private void checkItemsAvailability() {
        if (noItemsText != null && itemList != null && itemRecyclerView != null) {
            if (itemList.size() < 1) {
                itemRecyclerView.setVisibility(View.GONE);
                noItemsText.setVisibility(View.VISIBLE);
            } else {
                noItemsText.setVisibility(View.GONE);
                itemRecyclerView.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * Method to add client side input verification
     *
     * @return return true if all the inputs are valid else false
     */
    private boolean isInputValid() {
        boolean isValidInput = true;

        if (TextUtils.isEmpty(studentID)) {
            Validator.setEmptyError(studentIDLayout);
            isValidInput = false;
        } else {
            studentIDLayout.setError(null);
            studentIDLayout.setErrorEnabled(false);
        }

        if (TextUtils.isEmpty(studentName)) {
            Validator.setEmptyError(studentNameLayout);
            isValidInput = false;
        } else {
            studentNameLayout.setError(null);
            studentNameLayout.setErrorEnabled(false);
        }

        if (TextUtils.isEmpty(fatherName)) {
            Validator.setEmptyError(fatherNameLayout);
            isValidInput = false;
        } else {
            fatherNameLayout.setError(null);
            fatherNameLayout.setErrorEnabled(false);
        }

        if (TextUtils.isEmpty(gender)) {
            genderErrorText.setVisibility(View.VISIBLE);
            isValidInput = false;
        } else {
            genderErrorText.setVisibility(View.GONE);
        }

        if (TextUtils.isEmpty(phone)) {
            Validator.setEmptyError(phoneLayout);
            isValidInput = false;
        } else if (!Validator.isValidMobile(phone)) {
            isValidInput = false;
            phoneLayout.setError("This phone is not valid");
        } else {
            phoneLayout.setError(null);
            phoneLayout.setErrorEnabled(false);
        }

        if (TextUtils.isEmpty(email)) {
            Validator.setEmptyError(emailLayout);
            isValidInput = false;
        } else if (!Validator.isValidEmail(email)) {
            isValidInput = false;
            emailLayout.setError("This email is not valid");
        } else {
            emailLayout.setError(null);
            emailLayout.setErrorEnabled(false);
        }

        return isValidInput;
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
            if (product.getCategory() == category.getId()) {
                productList.add(product);
            }
        }

        categoryText.setText(category.getName());

        checkAvailability();
        productAdapter.notifyDataSetChanged();
    }

    /**
     * Handle click for the payment button
     */
    private void onPaymentButtonClicked() {
        if (cartList == null || cartList.size() < 1) {
            Toast.makeText(getContext(), "Your cart is empty!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (studentDetails[0] == null) {
            Toast.makeText(getContext(), "Enter Student ID first", Toast.LENGTH_SHORT).show();
            studentIdText.requestFocus();
            return;
        }

        final PaymentDialog dialog = new PaymentDialog(getActivity(), total, studentDetails[0]);

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        }

        dialog.setCancelable(false);
        dialog.show();
        dialog.setOnOrderCompleteListener(transactionList -> {
            showProgressDialog();
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
        String customerName = studentDetails[0].getName();
        String customerMobile = studentDetails[0].getMobile();
        String customerEmail = studentDetails[0].getEmail();
        Order order = new Order(-1, customerName, customerMobile, customerEmail
                , "",
                total, discount, false);

        ArrayList<SubOrder> subOrderList = new ArrayList<>();

        for (CartItem cartItem : cartList) {
            ProductHeader product = cartItem.getProductHeader();
            subOrderList.add(new SubOrder(product.getName(), product.getId(),
                    product.getSku(), cartItem.getPrice(), cartItem.getQuantity(),
                    0, 0, 0, 0, false));
        }

        ProductAPI.saveAndSyncOrder(getContext(), db, order, subOrderList, transactionList);

        Toast.makeText(getContext(), "Order completed", Toast.LENGTH_SHORT).show();
        cartList.clear();
        cartAdapter.notifyDataSetChanged();
        updatePriceView();

        cartRecyclerView.setVisibility(View.GONE);
        emptyCartView.setVisibility(View.VISIBLE);
        dismissDialog();
    }

    /**
     * Helper method to calculate and update price on the UI
     */
    private void updatePriceView() {
        int subTotal = 0;
        int cartQuantity = 0;
        int calculatedDiscount = 0;

        DecimalFormat decimalFormatter = new DecimalFormat("₹#,##,###.##");

        for (CartItem cartItem : cartList) {
            cartQuantity += cartItem.getQuantity();
            subTotal += cartItem.getQuantity() * cartItem.getPrice();
        }

        switch (discountType) {
            case AMOUNT:
                calculatedDiscount = discount;
                break;
            case PERCENTAGE:
                calculatedDiscount = (subTotal * discount) / 100;
                break;
        }

        subTotalView.setText(decimalFormatter.format(subTotal));

        String count = "(" + cartQuantity;

        if (cartQuantity < 2) {
            count += " Item)";
        } else {
            count += " Items)";
        }

        textNumberItems.setText(count);
        discountView.setText(decimalFormatter.format(calculatedDiscount));
        total = subTotal - calculatedDiscount;
        totalView.setText(decimalFormatter.format(total));
        updateCartNotification(cartQuantity);
    }

    /**
     * updates the price and if there isn't any item shows the empty state
     *
     * @param cartItem Cart item
     */
    @Override
    public void onItemUpdateListener(CartItem cartItem) {
        updatePriceView();

        if (cartList != null && cartList.size() == 0) {
            emptyCartView.setVisibility(View.VISIBLE);
            cartRecyclerView.setVisibility(View.GONE);
        }
    }

    /**
     * Method to listen discount click
     *
     * @param discount     value of discount
     * @param discountType type of discount
     */
    @Override
    public void onDiscountClickListener(int discount, String discountType) {

        if (cartList.size() < 1) {
            Toast.makeText(getContext(), "Your cart is empty", Toast.LENGTH_SHORT).show();
            return;
        }

        discountRecyclerView.setVisibility(View.GONE);
        cartRecyclerView.setVisibility(View.VISIBLE);

        this.discount = discount;
        this.discountType = discountType;
        updatePriceView();
    }

    @Override
    public void onReturnItemClick() {
        checkItemsAvailability();
    }

    /**
     * This class extends the ProductClickListener interface and will handle the item click inside
     * the productRecycler.
     */
    class ProductItemListener implements ProductAdapter.ProductClickListener {
        @Override
        public void onProductClicked(ProductHeader product, String size) {

            ViewHelper.hideView(emptyCartView);
            ViewHelper.showView(cartRecyclerView);
            ViewHelper.hideView(discountRecyclerView);
            List<ProductVariant> productVariantList = db.productVariantDao().getProductVariantsById(product.getId());

            if (cartList.size() > 0) {
                CartItem lastCartItem = cartList.get(cartList.size() - 1);

                ProductVariant productVariant = null;
                for (ProductVariant currentVariant : productVariantList) {
                    if (currentVariant.getSize().equals(lastCartItem.getSize())) {
                        productVariant = currentVariant;
                    }
                }

                if (productVariant != null && productVariant.getDisplayStock() - lastCartItem.getQuantity() < 1) {
                    Toast.makeText(getContext(), APIStatic.Constants.OUT_OF_STOCK, Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            for (CartItem cartItem : cartList) {
                if (cartItem.getProductHeader().getId() == product.getId()) {
                    if (cartItem.getSize().equals(size)) {
                        cartItem.setQuantity(cartItem.getQuantity() + 1);
                        cartAdapter.notifyDataSetChanged();
                        updatePriceView();
                        return;
                    }
                }
            }

            double price = 0;
            for (ProductVariant currentVariant : productVariantList) {
                if (size.equals(currentVariant.getSize())) {
                    price = currentVariant.getPrice();
                }
            }

            cartList.add(new CartItem(product.getId(), 1, product, size, price));
            cartAdapter.notifyDataSetChanged();
            updatePriceView();
        }
    }

    /**
     * if no products are available, show an empty state text
     */
    private void checkAvailability() {
        if (productList != null) {
            if (productList.size() == 0) {
                noProductText.setVisibility(View.VISIBLE);
            } else {
                noProductText.setVisibility(View.GONE);
            }
        }
    }

    /**
     * updates cart notification count
     */
    private void updateCartNotification(int cartQuantity) {
        cartNotification.setText(String.valueOf(cartQuantity));
    }

    /**
     * updates discount notification count
     */
    public void updateDiscountNotification(int discountQuantity) {
        discountNotification.setText(String.valueOf(discountQuantity));
    }

    /**
     * Method to show progress dialog
     */
    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getContext());
        }

        progressDialog.setTitle(R.string.dialog_title);
        progressDialog.setMessage(getResources().getString(R.string.dialog_message));
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    /**
     * Method to dismiss progress dialog
     */
    private void dismissDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }
}
