package com.tapatuniforms.pos.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tapatuniforms.pos.R;
import com.tapatuniforms.pos.adapter.PaymentButtonAdapter;
import com.tapatuniforms.pos.adapter.TransactionListAdapter;
import com.tapatuniforms.pos.helper.GridItemDecoration;
import com.tapatuniforms.pos.model.CartItem;
import com.tapatuniforms.pos.model.Transaction;

import java.util.ArrayList;

public class PaymentDialog extends AlertDialog implements PaymentButtonAdapter.ButtonClickListener, TransactionListAdapter.RemoveButtonListener {
    private RecyclerView paymentButtonRecycler, transactionRecycler;
    private ArrayList<Transaction> transactionList;
    private EditText transAmountEditText;
    private TransactionListAdapter transactionAdapter;
    private Button orderCompleteButton;
    private OrderCompleteListener listener;

    private double total;
    private double paid;

    public PaymentDialog(Context context, double total) {
        super(context);
        this.total = total;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_dialog);

        transAmountEditText = findViewById(R.id.transAmountEditText);

        paymentButtonRecycler = findViewById(R.id.paymentButtonRecycler);
        paymentButtonRecycler.setLayoutManager(new GridLayoutManager(getContext(), 4));
        paymentButtonRecycler.addItemDecoration(new GridItemDecoration(8, 8));
        PaymentButtonAdapter adapter = new PaymentButtonAdapter(getContext(), getPaymentList());
        adapter.setOnButtonClickListener(this);
        paymentButtonRecycler.setAdapter(adapter);

        transactionRecycler = findViewById(R.id.transactionRecycler);
        transactionRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        transactionList = new ArrayList<>();
        transactionAdapter = new TransactionListAdapter(getContext(), transactionList);
        transactionAdapter.setOnRemoveButtonListener(this);
        transactionRecycler.setAdapter(transactionAdapter);

        orderCompleteButton = findViewById(R.id.orderCompleteButton);
        orderCompleteButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onOrderCompleted(transactionList);
            }
        });

        paid = 0;
        transAmountEditText.setText("" + total);
    }

    private ArrayList<String> getPaymentList() {
        ArrayList<String> list = new ArrayList<>();

        list.add("Card");
        list.add("PayTM");
        list.add("Cash");
        return list;
    }

    @Override
    public void onPaymentButtonClicked(String name) {
        if (paid == total) return;

        double amount = Double.valueOf(transAmountEditText.getText().toString().trim());
        transactionList.add(new Transaction(name, amount, -1, false));

        paid += amount;
        transAmountEditText.setText("" + (total - paid));
        transactionAdapter.notifyDataSetChanged();
    }


    @Override
    public void onRemoveButtonClicked(Transaction transaction) {
        transactionList.remove(transaction);

        paid -= transaction.getAmount();
        transAmountEditText.setText("" + (total - paid));
        transactionAdapter.notifyDataSetChanged();
    }

    public void setOnOrderCompleteListener(OrderCompleteListener listener) {
        this.listener = listener;
    }

    public interface OrderCompleteListener {
        void onOrderCompleted(ArrayList<Transaction> transactionList);
    }
}
