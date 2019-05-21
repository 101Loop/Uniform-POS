package com.tapatuniforms.pos.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tapatuniforms.pos.R;

public class DayClosingStockAdapter extends RecyclerView.Adapter<DayClosingStockAdapter.ViewHolder> {

    public DayClosingStockAdapter() {
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dayclosing_stock_item_layout,
                parent, false);
        return new DayClosingStockAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 6;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView itemNameView, remainingItemView, requestedItemView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemNameView = itemView.findViewById(R.id.itemNameView);
            remainingItemView = itemView.findViewById(R.id.remainingItemView);
            requestedItemView = itemView.findViewById(R.id.requestedItemView);
        }
    }
}
