package com.tapatuniforms.pos.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tapatuniforms.pos.R;

public class SubOrderAdapter extends RecyclerView.Adapter<SubOrderAdapter.ViewHolder> {
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.suborder_item_layout,
                parent, false);
        return new SubOrderAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 7;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView itemImageView;
        TextView itemNameView, itemSerialView, itemQuantityPriceView, itemDiscountView,
                itemTotalPriceView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemImageView = itemView.findViewById(R.id.itemImageView);
            itemNameView = itemView.findViewById(R.id.itemNameView);
            itemSerialView = itemView.findViewById(R.id.itemSerialView);
            itemQuantityPriceView = itemView.findViewById(R.id.itemQuantityPriceView);
            itemDiscountView = itemView.findViewById(R.id.itemDiscountView);
            itemTotalPriceView = itemView.findViewById(R.id.itemTotalPriceView);
        }
    }
}
