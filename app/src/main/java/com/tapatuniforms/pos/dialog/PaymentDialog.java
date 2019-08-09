package com.tapatuniforms.pos.dialog;

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

    public PaymentDialog(Context context, double total, Student studentDetails) {
        super(context);
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
        cashAddButton.setOnClickListener(view -> {
            String cashText = cashEditText.getText().toString().trim();

            if (!cashText.isEmpty()) {
                double cashAmount = Double.parseDouble(cashText);

                if (cashAmount != 0) {
                    if (total == paid) {
                        Toast.makeText(getContext(), "Amount has been fully paid!", Toast.LENGTH_SHORT).show();
                        cashEditText.setText(null);
                        return;
                    }

                    total -= cashAmount;

                    if (paid > total) {
                        Toast.makeText(getContext(), "Amount should not be greater than the total amount", Toast.LENGTH_SHORT).show();
                        total += cashAmount;
                        return;
                    }

                    cash += cashAmount;

                    if (cashTransaction == null) {
                        cashTransaction = new Transaction(getContext().getResources().getString(R.string.cash), cashAmount, -1, false);
                    } else {
                        double cash = cashTransaction.getAmount();
                        cashAmount += cash;
                        cashTransaction.setAmount(cashAmount);
                    }

                    cashAmountText.setText("₹ " + cashAmount);
                    totalAmountText.setText("₹ " + total);
                }
            }
        });

        //cancel cash
        cashCancelButton.setOnClickListener(view -> {

            if (cash > 0) {
                total += cash;
                cash = 0;
                cashEditText.setText(null);
                cashAmountText.setText("₹ 0");
                totalAmountText.setText("₹ " + total);
                cashTransaction = null;
            }
        });

        //add paytm
        paytmAddButton.setOnClickListener(view -> {
            String paytmText = paytmEditText.getText().toString().trim();

            if (!paytmText.isEmpty()) {
                double paytmAmount = Double.parseDouble(paytmText);

                if (paytmAmount != 0) {
                    if (total == paid) {
                        Toast.makeText(getContext(), "Amount has been fully paid!", Toast.LENGTH_SHORT).show();
                        paytmEditText.setText(null);
                        return;
                    }

                    total -= paytmAmount;

                    if (paid > total) {
                        Toast.makeText(getContext(), "Amount should not be greater than the total amount", Toast.LENGTH_SHORT).show();
                        total += paytmAmount;
                        return;
                    }

                    payTM += paytmAmount;

                    if (paytmTransaction == null) {
                        paytmTransaction = new Transaction(getContext().getResources().getString(R.string.paytm), paytmAmount, -1, false);
                    } else {
                        double paytm = paytmTransaction.getAmount();
                        paytmAmount += paytm;
                        paytmTransaction.setAmount(paytmAmount);
                    }

                    paytmAmountText.setText("₹ " + paytmAmount);
                    totalAmountText.setText("₹ " + total);
                }
            }
        });

        //cancel paytm
        paytmCancelButton.setOnClickListener(view -> {

            if (payTM > 0) {
                total += payTM;
                payTM = 0;
                paytmEditText.setText(null);
                paytmAmountText.setText("₹ 0");
                totalAmountText.setText("₹ " + total);
                paytmTransaction = null;
            }
        });

        //add card
        cardAddButton.setOnClickListener(view -> {
            String cardText = cardEditText.getText().toString().trim();

            if (!cardText.isEmpty()) {
                double cardAmount = Double.parseDouble(cardText);

                if (cardAmount != 0) {
                    if (total == paid) {
                        Toast.makeText(getContext(), "Amount has been fully paid!", Toast.LENGTH_SHORT).show();
                        cardEditText.setText(null);
                        return;
                    }

                    total -= cardAmount;

                    if (paid > total) {
                        Toast.makeText(getContext(), "Amount should not be greater than the total amount", Toast.LENGTH_SHORT).show();
                        total += cardAmount;
                        return;
                    }

                    card += cardAmount;

                    if (cardTransaction == null) {
                        cardTransaction = new Transaction(getContext().getResources().getString(R.string.card), cardAmount, -1, false);
                    } else {
                        double cash = cardTransaction.getAmount();
                        cardAmount += cash;
                        cardTransaction.setAmount(cardAmount);
                    }

                    cardAmountText.setText("₹ " + cardAmount);
                    totalAmountText.setText("₹ " + total);
                }
            }
        });

        //cancel card
        cardCancelButton.setOnClickListener(view -> {

            if (card > 0) {
                total += card;
                card = 0;
                cardEditText.setText(null);
                cardAmountText.setText("₹ 0");
                totalAmountText.setText("₹ " + total);
                cardTransaction = null;
            }
        });

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
