package com.tapatuniforms.pos.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tapatuniforms.pos.R;
import com.tapatuniforms.pos.model.InventoryItem;

import java.util.ArrayList;

public class InventoryAdapter extends RecyclerView.Adapter<InventoryAdapter.ViewHolder> {
    private ArrayList<InventoryItem> itemList;
    private ButtonClickListener listener;

    public interface ButtonClickListener {
        void onTransferButtonClick(InventoryItem item);
    }

    public InventoryAdapter(ArrayList<InventoryItem> itemList) {
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.inventory_list_item_layout,
                parent, false);
        return new InventoryAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final InventoryItem item = itemList.get(position);

        holder.itemNameView.setText(item.getName());
        holder.itemSerialView.setText("#" + item.getSerialNumber());
        holder.itemWarehouseCount.setText(item.getWarehouseQuantity() + " in Warehouse");
        holder.itemDisplayCount.setText(item.getDisplayQuantity() + " On Display");

        holder.transferButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onTransferButtonClick(item);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public void setOnClickListener(ButtonClickListener listener) {
        this.listener = listener;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView itemNameView, itemSerialView, itemWarehouseCount, itemDisplayCount;
        Button transferButton;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemNameView = itemView.findViewById(R.id.itemNameView);
            itemSerialView = itemView.findViewById(R.id.itemSerialView);
            itemWarehouseCount = itemView.findViewById(R.id.itemWarehouseCount);
            itemDisplayCount = itemView.findViewById(R.id.itemDisplayCount);
            transferButton = itemView.findViewById(R.id.transferButton);
        }
    }
}
