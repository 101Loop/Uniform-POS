package com.tapatuniforms.pos.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tapatuniforms.pos.R;
import com.tapatuniforms.pos.helper.RoundedCornerLayout;
import com.tapatuniforms.pos.model.Product;

import java.util.ArrayList;

public class InventoryAdapter extends RecyclerView.Adapter<InventoryAdapter.ViewHolder> {
    private ArrayList<Product> itemList;
    private ButtonClickListener listener;
    private ArrayList<String> sizeList;
    private Context context;

    public interface ButtonClickListener {
        void onTransferButtonClick(Product item, String title);
    }

    public InventoryAdapter(Context context, ArrayList<Product> itemList) {
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
        final Product item = itemList.get(position);

        holder.itemNameView.setText(item.getName());

        holder.colorName.setText(item.getColor());
        holder.colorImage.setBackgroundColor(Color.parseColor(item.getColorCode()));

        int totalWarehouseStock = 0;
        for (String currentStock : item.getWarehouseStockList()) {
            totalWarehouseStock += Integer.parseInt(currentStock);
        }
        holder.itemWarehouseCount.setText(String.valueOf(totalWarehouseStock));

        int totalDisplayStock = 0;
        for (String currentStock : item.getDisplayStockList()) {
            totalDisplayStock += Integer.parseInt(currentStock);
        }
        holder.itemDisplayCount.setText(String.valueOf(totalDisplayStock));

        String type = item.getProductType();
        if (type == null || type.isEmpty() || type.equalsIgnoreCase("null")) {
            type = "";
        }
        holder.productType.setText(type);

        SizeAdapter sizeAdapter = new SizeAdapter(context, item.getSizeList());
        holder.sizeRecyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
        holder.sizeRecyclerView.setAdapter(sizeAdapter);

        holder.transferButton.setOnClickListener(view -> {
            if (listener != null) {
                listener.onTransferButtonClick(item, "Transfer");
            }
        });

        holder.addToOrderButton.setOnClickListener(view -> {
            if (listener != null) {
                listener.onTransferButtonClick(item, "Add to Order");
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
        TextView itemNameView, addToOrderButton, productType, itemWarehouseCount, itemDisplayCount, colorName;
        RoundedCornerLayout colorImage;
        RecyclerView sizeRecyclerView;
        LinearLayout transferButton;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemNameView = itemView.findViewById(R.id.itemNameView);
            addToOrderButton = itemView.findViewById(R.id.addToOrderButton);
            sizeRecyclerView = itemView.findViewById(R.id.sizeRecyclerView);
            transferButton = itemView.findViewById(R.id.transferButton);
            colorImage = itemView.findViewById(R.id.colorImage);
            colorName = itemView.findViewById(R.id.colorName);
            itemDisplayCount = itemView.findViewById(R.id.displayItemsCount);
            itemWarehouseCount = itemView.findViewById(R.id.warehouseQuantityText);
            productType = itemView.findViewById(R.id.productType);
        }
    }
}
