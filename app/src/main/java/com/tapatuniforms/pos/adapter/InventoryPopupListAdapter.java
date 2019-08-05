package com.tapatuniforms.pos.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tapatuniforms.pos.R;
import com.tapatuniforms.pos.helper.DatabaseHelper;
import com.tapatuniforms.pos.helper.DatabaseSingleton;
import com.tapatuniforms.pos.model.ProductHeader;
import com.tapatuniforms.pos.model.ProductVariant;

import java.util.ArrayList;
import java.util.List;

public class InventoryPopupListAdapter extends RecyclerView.Adapter<InventoryPopupListAdapter.ViewHolder> {
    private Context context;
    private ProductHeader item;
    private ArrayList<String> sizeList;
    private ArrayList<String> warehouseStockList;
    private ItemCountChangeListener listener;
    private DatabaseSingleton db;

    public InventoryPopupListAdapter(Context context, ProductHeader item) {
        this.context = context;
        this.item = item;
        sizeList = new ArrayList<>();
        warehouseStockList = new ArrayList<>();
        db = DatabaseHelper.getDatabase(context);

        List<ProductVariant> productVariantList = db.productVariantDao().getProductVariantsById(item.getId());
        for (ProductVariant currentVariant : productVariantList) {
            sizeList.add(currentVariant.getSize());
            warehouseStockList.add(String.valueOf(currentVariant.getWarehouseStock()));
        }
    }

    public interface ItemCountChangeListener{
        void onItemChangeListener(int count);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.inventory_popup_table_layout, parent, false);

        return new InventoryPopupListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.sizeView.setText(sizeList.get(position));
        holder.stockView.setText(warehouseStockList.get(position));

        holder.plusButton.setOnClickListener(view -> {
            int count = Integer.parseInt(holder.quantityEditText.getText().toString());
            ++count;
            holder.quantityEditText.setText(String.valueOf(count));
            holder.quantityTextView.setText(String.valueOf(count));

            if (listener != null) {
                listener.onItemChangeListener(count);
            }
        });

        holder.minusButton.setOnClickListener(view -> {
            int count = Integer.parseInt(holder.quantityEditText.getText().toString());

            if (count > 1) {
                --count;
                holder.quantityEditText.setText(String.valueOf(count));
                holder.quantityTextView.setText(String.valueOf(count));
            }

            if (listener != null) {
                listener.onItemChangeListener(count);
            }
        });
    }

    @Override
    public int getItemCount() {
        return sizeList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView sizeView, stockView, minusButton, plusButton, quantityTextView;
        EditText quantityEditText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            sizeView = itemView.findViewById(R.id.sizeView);
            stockView = itemView.findViewById(R.id.stockNumberView);
            minusButton = itemView.findViewById(R.id.minusButton);
            plusButton = itemView.findViewById(R.id.plusButton);
            quantityTextView = itemView.findViewById(R.id.itemQuantityView);
            quantityEditText = itemView.findViewById(R.id.itemQuantityEditText);
        }
    }

    public void setOnItemCountChangeListener(ItemCountChangeListener listener) {
        this.listener = listener;
    }
}
