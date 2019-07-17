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
        holder.boxNameView.setText(boxList.get(position).getName());
        holder.itemCount.setText(String.valueOf(boxList.get(position).getNumberOfItems()));
        holder.itemTextView.setText(boxList.get(position).getDateTime());

        holder.rootView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onBoxSelected();
            }
        });
    }

    @Override
    public int getItemCount() {
        return boxList.size();
    }

    public void setOnBoxClickListener(OnBoxClickListener listener) {
        this.listener = listener;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        View rootView;
        TextView boxNameView, boxSerialView, itemTextView, boxDateView, itemCount, femaleCountItem, maleCountItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rootView = itemView;
            boxNameView = itemView.findViewById(R.id.boxNameView);
            itemTextView = itemView.findViewById(R.id.itemTextView);
            itemCount = itemView.findViewById(R.id.itemCount);
            femaleCountItem = itemView.findViewById(R.id.femaleCountItem);
            maleCountItem = itemView.findViewById(R.id.maleCountItem);
        }
    }
}
