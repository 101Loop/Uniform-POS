package com.tapatuniforms.pos.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tapatuniforms.pos.R;
import com.tapatuniforms.pos.helper.DatabaseHelper;
import com.tapatuniforms.pos.helper.DatabaseSingleton;
import com.tapatuniforms.pos.helper.Validator;
import com.tapatuniforms.pos.helper.ViewHelper;
import com.tapatuniforms.pos.model.ProductHeader;
import com.tapatuniforms.pos.model.ProductVariant;

import java.util.ArrayList;
import java.util.List;

public class InventoryPopupListAdapter extends RecyclerView.Adapter<InventoryPopupListAdapter.ViewHolder> {
    private static final String TAG = InventoryPopupListAdapter.class.getName();
    private Context context;
    private ProductHeader item;
    private ArrayList<String> sizeList;
    private ArrayList<String> warehouseStockList;
    private ArrayList<Integer> displayStockList;
    private ItemCountChangeListener listener;
    private DatabaseSingleton db;
    private String title;
    private ProductVariant productVariant;

    public InventoryPopupListAdapter(Context context, ProductHeader item, String title) {
        this.context = context;
        this.item = item;
        this.title = title;
        sizeList = new ArrayList<>();
        warehouseStockList = new ArrayList<>();
        displayStockList = new ArrayList<>();
        db = DatabaseHelper.getDatabase(context);

        List<ProductVariant> productVariantList = db.productVariantDao().getProductVariantsById(item.getId());
        for (ProductVariant currentVariant : productVariantList) {
            sizeList.add(currentVariant.getSize());
            warehouseStockList.add(String.valueOf(currentVariant.getWarehouseStock()));
            displayStockList.add(currentVariant.getDisplayStock());
        }
    }

    public interface ItemCountChangeListener {
        void onItemChangeListener(int count, int isDone);
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
        productVariant = getCurrentVariant(position);
        int variantId = productVariant.getId();

        holder.quantityEditText.setOnEditorActionListener(((textView, actionId, keyEvent) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                onDoneClick(holder, position);
                return true;
            }

            return false;
        }));

        holder.plusButton.setOnClickListener(view -> {
            int count = Integer.parseInt(holder.quantityEditText.getText().toString());

            if (title.equalsIgnoreCase("transfer")) {
                if (productVariant.getWarehouseStock() - count < 1) {
                    Toast.makeText(context, "Not enough items in stock", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            ++count;
            db.productVariantDao().updateTransferOrderCount(count, variantId);
            ProductVariant currentVariant = getCurrentVariant(position);
            currentVariant.setTransferOrderCount(count);
            holder.quantityEditText.setText(String.valueOf(count));
            holder.quantityTextView.setText(String.valueOf(count));

            if (listener != null) {
                listener.onItemChangeListener(1, 0);
            }
        });

        holder.minusButton.setOnClickListener(view -> {
            int count = Integer.parseInt(holder.quantityEditText.getText().toString());

            if (count > 0) {
                --count;
                db.productVariantDao().updateTransferOrderCount(count, variantId);
                holder.quantityEditText.setText(String.valueOf(count));
                holder.quantityTextView.setText(String.valueOf(count));


                if (listener != null) {
                    listener.onItemChangeListener(-1, 0);
                }
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

    private void onDoneClick(ViewHolder holder, int position){
        Validator.hideKeyboard((Activity) context.getApplicationContext());
        holder.quantityEditText.clearFocus();

        int count = 0;

        try{
            count = Integer.parseInt(holder.quantityEditText.getText().toString());
        }catch (NumberFormatException e) {
            e.printStackTrace();
            Toast.makeText(context, "Value too long", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "integer value too long");
        }

        if (title.equalsIgnoreCase("transfer")) {
            if (count > productVariant.getWarehouseStock()) {
                Toast.makeText(context, "Not enough items in stock", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        db.productVariantDao().updateTransferOrderCount(count, productVariant.getId());
        ProductVariant currentVariant = getCurrentVariant(position);
        currentVariant.setTransferOrderCount(count);
        holder.quantityEditText.setText(String.valueOf(count));
        holder.quantityTextView.setText(String.valueOf(count));

        if (listener != null) {
            listener.onItemChangeListener(count, 1);
        }
    }

    /**
     * Method to set item count change listener
     */
    public void setOnItemCountChangeListener(ItemCountChangeListener listener) {
        this.listener = listener;
    }

    /**
     * Method to get current variant
     *
     * @param position Position to identify current variant
     * @return Returns current variant
     */
    private ProductVariant getCurrentVariant(int position) {
        return db.productVariantDao().getProductVariantsById(item.getId()).get(position);
    }
}
