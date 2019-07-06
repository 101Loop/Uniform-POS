package com.tapatuniforms.pos.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tapatuniforms.pos.R;
import com.tapatuniforms.pos.model.Box;

import java.util.ArrayList;

public class StockBoxAdapter extends RecyclerView.Adapter<StockBoxAdapter.ViewHolder> {
    private ArrayList<Box> boxList;

    private OnBoxClickListener listener;

    public interface OnBoxClickListener {
        void onBoxSelected();
    }

    public StockBoxAdapter(ArrayList<Box> boxList) {
        this.boxList = boxList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.stock_list_item_right_layout,
                parent, false);
        return new StockBoxAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    }

    @Override
    public int getItemCount() {
        return 13;
    }

    public void setOnBoxClickListener(OnBoxClickListener listener) {
        this.listener = listener;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        View rootView;
        TextView boxNameView, boxSerialView, itemTextView, boxDateView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rootView = itemView;
            boxNameView = itemView.findViewById(R.id.boxNameView);
            itemTextView = itemView.findViewById(R.id.itemTextView);
        }
    }
}
