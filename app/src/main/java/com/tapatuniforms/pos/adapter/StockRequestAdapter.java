package com.tapatuniforms.pos.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tapatuniforms.pos.R;

public class StockRequestAdapter extends RecyclerView.Adapter<StockRequestAdapter.ViewHolder> {
    private OnBoxClickListener listener;

    public interface OnBoxClickListener {
        void onBoxSelected();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.stock_list_item_right_layout,
                parent, false);
        return new StockRequestAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onBoxSelected();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public void setOnBoxClickListener(OnBoxClickListener listener) {
        this.listener = listener;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        View rootView;
        TextView boxNameView, boxSerialView, itemTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rootView = itemView;
            boxNameView = itemView.findViewById(R.id.boxNameView);
            boxSerialView = itemView.findViewById(R.id.boxSerialView);
            itemTextView = itemView.findViewById(R.id.itemTextView);
        }
    }
}
