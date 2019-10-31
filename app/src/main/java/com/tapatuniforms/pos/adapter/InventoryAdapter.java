package com.tapatuniforms.pos.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tapatuniforms.pos.R;
import com.tapatuniforms.pos.helper.DatabaseHelper;
import com.tapatuniforms.pos.helper.DatabaseSingleton;
import com.tapatuniforms.pos.helper.RoundedCornerLayout;
import com.tapatuniforms.pos.model.ProductHeader;
import com.tapatuniforms.pos.model.ProductVariant;

import java.util.ArrayList;
import java.util.List;

public class InventoryAdapter extends RecyclerView.Adapter<InventoryAdapter.ViewHolder> {
    private ArrayList<ProductHeader> itemList;
    private ButtonClickListener listener;
    private Context context;
    private DatabaseSingleton db;

    public interface ButtonClickListener {
        void onTransferButtonClick(ProductHeader item, String title);
    }

    public InventoryAdapter(Context context, ArrayList<ProductHeader> itemList) {
        this.itemList = itemList;
        this.context = context;
        db = DatabaseHelper.getDatabase(context);
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
        final ProductHeader item = itemList.get(position);
        List<ProductVariant> productVariantList = db.productVariantDao().getProductVariantsById(item.getId());

        holder.itemNameView.setText(item.getName());

        holder.colorName.setText(item.getColor());

        String hexColor = item.getColorCode();

        if (hexColor.length() == 4) {
            String[] arrHexColor = hexColor.split("#");
            hexColor = "#" + arrHexColor[1] + arrHexColor[1];
        }
        holder.colorImage.setBackgroundColor(Color.parseColor(hexColor));

        ArrayList<String> sizeList = new ArrayList<>();
        int totalWarehouseStock = 0;
        int totalDisplayStock = 0;
        for (ProductVariant currentVariant : productVariantList) {
            totalWarehouseStock += currentVariant.getWarehouseStock();
            totalDisplayStock += currentVariant.getDisplayStock();
            sizeList.add(currentVariant.getSize());
        }
        holder.itemWarehouseCount.setText(String.valueOf(totalWarehouseStock));

        holder.itemDisplayCount.setText(String.valueOf(totalDisplayStock));

        String type = item.getProductType();
        if (type == null || type.isEmpty() || type.equalsIgnoreCase("null")) {
            type = "";
        }
        holder.productType.setText(type);

        SizeAdapter sizeAdapter = new SizeAdapter(context, sizeList);
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
