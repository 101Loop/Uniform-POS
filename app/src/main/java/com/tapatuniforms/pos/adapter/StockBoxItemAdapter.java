package com.tapatuniforms.pos.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tapatuniforms.pos.R;

public class StockBoxItemAdapter extends RecyclerView.Adapter<StockBoxItemAdapter.ViewHolder> {
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.stock_list_item_layout,
                parent, false);
        return new StockBoxItemAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 20;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView itemNameView, itemSentView;
        EditText itemReceivedView;
        ImageView itemCheckedStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemNameView = itemView.findViewById(R.id.itemNameView);
            itemSentView = itemView.findViewById(R.id.itemSentView);
            itemReceivedView = itemView.findViewById(R.id.itemReceivedView);
            itemCheckedStatus = itemView.findViewById(R.id.itemCheckedStatus);
        }
    }
}
