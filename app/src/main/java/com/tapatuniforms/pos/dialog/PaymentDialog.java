package com.tapatuniforms.pos.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;

import com.tapatuniforms.pos.R;
import com.tapatuniforms.pos.model.Student;
import com.tapatuniforms.pos.model.Transaction;

import java.util.ArrayList;

public class PaymentDialog extends AlertDialog {
    private ArrayList<Transaction> transactionList;
    private Transaction cashTransaction;
    private Transaction paytmTransaction;
    private Transaction cardTransaction;
    private OrderCompleteListener listener;

    private double total;
    private double paid = 0;
    private Student studentDetails;

    private TextView studentIDText, studentNameText, classSectionText, phoneText, emailText, fatherNameText, totalAmountText, cashAmountText, paytmAmountText, cardAmountText;
    private EditText cashEditText, paytmEditText, cardEditText;
    private Button cashAddButton, paytmAddButton, cardAddButton;
    private ImageView cashCancelButton, paytmCancelButton, cardCancelButton;
    private CardView completeOrderButton, closeButton;

    private double cash = 0, payTM = 0, card = 0;

    private Activity activity;

    public PaymentDialog(Context context, double total, Student studentDetails) {
        super(context);
        this.activity = (Activity) context;
        this.total = total;
        this.studentDetails = studentDetails;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_dialog);

        initViews();

        transactionList = new ArrayList<>();

        studentIDText.setText(studentDetails.getStudentId());
        studentNameText.setText(studentDetails.getName());
        String strClass = studentDetails.getStandard() + " - " + studentDetails.getSection();
        classSectionText.setText(strClass);
        phoneText.setText(studentDetails.getMobile());
        emailText.setText(studentDetails.getEmail());
        fatherNameText.setText(studentDetails.getFatherName());
        totalAmountText.setText("₹ " + total);

        //add cash
        cashAddButton.setOnClickListener(view -> onAddClick(getContext().getString(R.string.cash), cashEditText, cash, cashTransaction, cashAmountText));

        //add paytm
        paytmAddButton.setOnClickListener(view -> onAddClick(getContext().getString(R.string.paytm), paytmEditText, payTM, paytmTransaction, paytmAmountText));

        //add card
        cardAddButton.setOnClickListener(view -> onAddClick(getContext().getString(R.string.card), cardEditText, card, cardTransaction, cardAmountText));

        //cancel cash
        cashCancelButton.setOnClickListener(view -> onCancelClick(getContext().getString(R.string.cash), cash, cashEditText, cashAmountText));

        //cancel paytm
        paytmCancelButton.setOnClickListener(view -> onCancelClick(getContext().getString(R.string.paytm), payTM, paytmEditText, paytmAmountText));

        //cancel card
        cardCancelButton.setOnClickListener(view -> onCancelClick(getContext().getString(R.string.card), card, cardEditText, cardAmountText));

        //order click
        completeOrderButton.setOnClickListener(v -> {
            if (listener != null) {

                if (cashTransaction != null) {
                    transactionList.add(cashTransaction);
                }

                if (paytmTransaction != null) {
                    transactionList.add(paytmTransaction);
                }

                if (cardTransaction != null) {
                    transactionList.add(cardTransaction);
                }

                listener.onOrderCompleted(transactionList);
            }
        });

        //close dialog
        closeButton.setOnClickListener(view -> dismiss());
    }

    private void initViews() {

        //init
        studentIDText = findViewById(R.id.studentIDText);
        studentNameText = findViewById(R.id.studentNameText);
        classSectionText = findViewById(R.id.classSectionText);
        phoneText = findViewById(R.id.phoneText);
        emailText = findViewById(R.id.emailText);
        fatherNameText = findViewById(R.id.fatherNameText);
        totalAmountText = findViewById(R.id.totalAmountText);

        cashAmountText = findViewById(R.id.cashAmountText);
        paytmAmountText = findViewById(R.id.paytmAmountText);
        cardAmountText = findViewById(R.id.cardAmountText);

        cashEditText = findViewById(R.id.cashEditText);
        paytmEditText = findViewById(R.id.paytmEditText);
        cardEditText = findViewById(R.id.cardEditText);

        cashAddButton = findViewById(R.id.cashAddButton);
        paytmAddButton = findViewById(R.id.paytmAddButton);
        cardAddButton = findViewById(R.id.cardAddButton);

        cashCancelButton = findViewById(R.id.cashCancel);
        paytmCancelButton = findViewById(R.id.paytmCancel);
        cardCancelButton = findViewById(R.id.cardCancel);

        completeOrderButton = findViewById(R.id.completeOrderButton);
        closeButton = findViewById(R.id.closeButton);
    }

    private void onAddClick(String type, EditText editText, double paidBy, Transaction transactionType, TextView textView) {
        String amountText = editText.getText().toString().trim();

        if (!amountText.isEmpty()) {
            double amount = 0;

            try {
                amount = Double.parseDouble(amountText);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }

            if (amount != 0) {
                if (total == paid) {
                    Toast.makeText(getContext(), "Amount has been fully paid!", Toast.LENGTH_SHORT).show();
                    editText.setText(null);
                    return;
                }

                paid += amount;

                if (paid > total) {
                    Toast.makeText(getContext(), "Amount should not be greater than the total amount", Toast.LENGTH_SHORT).show();
                    paid -= amount;
                    return;
                }

                paidBy += amount;

                if (transactionType == null) {
                    transactionType = new Transaction(type, amount, -1, false);
                } else {
                    double cash = transactionType.getAmount();
                    amount += cash;
                    transactionType.setAmount(amount);
                }

                if (type.equals(getContext().getString(R.string.cash))) {
                    cashTransaction = transactionType;
                    cash = paidBy;
                } else if (type.equals(getContext().getString(R.string.card))) {
                    cardTransaction = transactionType;
                    card = paidBy;
                } else {
                    paytmTransaction = transactionType;
                    payTM = paidBy;
                }

                textView.setText("₹ " + amount);
            }
        }
    }

    private void onCancelClick(String type, double amount, EditText editText, TextView amountText) {

        if (amount > 0) {
            paid -= amount;
            amount = 0;
            editText.setText(null);
            amountText.setText("₹ 0");

            if (type.equals(getContext().getString(R.string.cash))) {
                cashTransaction = null;
                cash = amount;
            } else if (type.equals(getContext().getString(R.string.card))) {
                cardTransaction = null;
                card = amount;
            } else {
                paytmTransaction = null;
                payTM = amount;
            }
        }
    }

    /**
     * Method to set order complete listener
     */
    public void setOnOrderCompleteListener(OrderCompleteListener listener) {
        this.listener = listener;
    }

    /**
     * Order complete listener interface
     */
    public interface OrderCompleteListener {
        void onOrderCompleted(ArrayList<Transaction> transactionList);
    }
}
