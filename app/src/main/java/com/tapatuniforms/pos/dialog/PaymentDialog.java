package com.tapatuniforms.pos.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tapatuniforms.pos.R;
import com.tapatuniforms.pos.adapter.PaymentButtonAdapter;
import com.tapatuniforms.pos.adapter.TransactionListAdapter;
import com.tapatuniforms.pos.helper.GridItemDecoration;
import com.tapatuniforms.pos.model.Transaction;

import java.util.ArrayList;

public class PaymentDialog extends AlertDialog implements PaymentButtonAdapter.ButtonClickListener {
    RecyclerView paymentButtonRecycler, transactionRecycler;
    ArrayList<Transaction> transactionList;
    EditText transAmountEditText;
    TextView outstandingAmountView;

    private double total;
    private double paid;

    public PaymentDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_dialog);

        transAmountEditText = findViewById(R.id.transAmountEditText);
        outstandingAmountView = findViewById(R.id.outstandingAmountView);

        paymentButtonRecycler = findViewById(R.id.paymentButtonRecycler);
        paymentButtonRecycler.setLayoutManager(new GridLayoutManager(getContext(), 4));
        paymentButtonRecycler.addItemDecoration(new GridItemDecoration(8, 8));
        PaymentButtonAdapter adapter = new PaymentButtonAdapter(getContext(), getPaymentList());
        adapter.setOnButtonClickListener(this);
        paymentButtonRecycler.setAdapter(adapter);

        transactionRecycler = findViewById(R.id.transactionRecycler);
        transactionRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        transactionList = new ArrayList<>();
        TransactionListAdapter adapter1 = new TransactionListAdapter(getContext(), transactionList);
        transactionRecycler.setAdapter(adapter1);

        total = 5000;
        paid = 0;
        transAmountEditText.setText("" + total);
    }

    private ArrayList<String> getPaymentList() {
        ArrayList<String> list = new ArrayList<>();

        list.add("Cash");
        list.add("Card");
        list.add("PayTM");
        list.add("Google Pay");
        list.add("Phone Pe");
        list.add("Paypal");
        return list;
    }

    private ArrayList<Transaction> getTransactionList() {
        ArrayList<Transaction> list = new ArrayList<>();

        list.add(new Transaction("Google Pay", "df", 500));
        list.add(new Transaction("Card", "CD", 2500));
        list.add(new Transaction("Phone Pe", "PP", 1500));
        return list;
    }

    @Override
    public void onPaymentButtonClicked(String name) {
        double amount = Double.valueOf(transAmountEditText.getText().toString().trim());
        transactionList.add(new Transaction(name, "fs", amount));

        paid += amount;
        transAmountEditText.setText("" + (total - paid));
        outstandingAmountView.setText("₹" + (total - paid));
    }
}
