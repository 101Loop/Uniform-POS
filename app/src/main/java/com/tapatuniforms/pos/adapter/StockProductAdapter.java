package com.tapatuniforms.pos.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tapatuniforms.pos.R;

public class StockProductAdapter extends RecyclerView.Adapter<StockProductAdapter.ViewHolder> {
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.stock_indent_item_layout,
                parent, false);
        return new StockProductAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView indentNameView, indentDateView;
        ImageView indentStatusImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            indentNameView = itemView.findViewById(R.id.indentNameView);
            indentDateView = itemView.findViewById(R.id.indentDateView);
            indentStatusImage = itemView.findViewById(R.id.indentStatusImage);
        }
    }
}
