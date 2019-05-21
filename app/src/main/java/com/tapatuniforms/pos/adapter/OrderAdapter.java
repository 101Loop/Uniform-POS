package com.tapatuniforms.pos.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tapatuniforms.pos.R;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder>{
    @NonNull
    @Override
    public OrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_list_item,
                parent, false);
        return new OrderAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView orderSerialView, orderDateView, invoiceNumberView, orderTotalView, personNameView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            orderSerialView = itemView.findViewById(R.id.orderSerialView);
            orderDateView = itemView.findViewById(R.id.orderDateView);
            invoiceNumberView = itemView.findViewById(R.id.invoiceNumberView);
            orderTotalView = itemView.findViewById(R.id.orderTotalView);
            personNameView = itemView.findViewById(R.id.personNameView);
        }
    }
}
