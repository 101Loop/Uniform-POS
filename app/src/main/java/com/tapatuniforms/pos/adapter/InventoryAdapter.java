package com.tapatuniforms.pos.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tapatuniforms.pos.R;
import com.tapatuniforms.pos.model.InventoryItem;

import java.util.ArrayList;

public class InventoryAdapter extends RecyclerView.Adapter<InventoryAdapter.ViewHolder> {
    private ArrayList<InventoryItem> itemList;
    private ButtonClickListener listener;
    private ArrayList<String> sizeList;
    private Context context;

    public interface ButtonClickListener {
        void onTransferButtonClick(InventoryItem item);
    }

    public InventoryAdapter(Context context, ArrayList<InventoryItem> itemList) {
        this.itemList = itemList;
        this.context = context;
        sizeList = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.inventory_transfer_item_layout,
                parent, false);
        return new InventoryAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final InventoryItem item = itemList.get(position);

        holder.itemNameView.setText(item.getName());

        sizeList.clear();
        sizeList.add("24");
        sizeList.add("26");
        sizeList.add("28");
        sizeList.add("30");
        SizeAdapter sizeAdapter = new SizeAdapter(context, sizeList);
        holder.sizeRecyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
        holder.sizeRecyclerView.setAdapter(sizeAdapter);

        holder.addToOrderButton.setOnClickListener(view -> {
            if (listener != null) {
                listener.onTransferButtonClick(item);
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
        TextView itemNameView, addToOrderButton, itemWarehouseCount, itemDisplayCount;
//        Button transferButton;
        RecyclerView sizeRecyclerView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemNameView = itemView.findViewById(R.id.itemNameView);
            addToOrderButton = itemView.findViewById(R.id.addToOrderButton);
            sizeRecyclerView = itemView.findViewById(R.id.sizeRecyclerView);
        }
    }
}
