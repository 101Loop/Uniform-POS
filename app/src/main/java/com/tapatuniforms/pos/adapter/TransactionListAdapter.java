package com.tapatuniforms.pos.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tapatuniforms.pos.R;
import com.tapatuniforms.pos.model.Transaction;

import java.util.ArrayList;

public class TransactionListAdapter extends RecyclerView.Adapter<TransactionListAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Transaction> transactionList;
    private RemoveButtonListener listener;

    public interface RemoveButtonListener {
        void onRemoveButtonClicked(Transaction transaction);
    }

    public TransactionListAdapter(Context context, ArrayList<Transaction> transactionList) {
        this.context = context;
        this.transactionList = transactionList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_item_layout,
                parent, false);
        return new TransactionListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Transaction transaction = transactionList.get(position);

        holder.nameView.setText(transaction.getPaymentOptionName());
        holder.amountView.setText("₹" + transaction.getAmount());

        holder.removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onRemoveButtonClicked(transaction);
                }
            }
        });
    }

    public void setOnRemoveButtonListener(RemoveButtonListener listener) {
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return transactionList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameView, amountView, removeButton;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameView = itemView.findViewById(R.id.nameView);
            amountView = itemView.findViewById(R.id.amountView);
            removeButton = itemView.findViewById(R.id.removeButton);
        }
    }
}
