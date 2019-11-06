package com.tapatuniforms.pos.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
    private static final String TAG = InventoryPopupListAdapter.class.getName();
    private Context context;
    private ProductHeader item;
    private ArrayList<String> sizeList;
    private ArrayList<String> warehouseStockList;
    private ArrayList<Integer> displayStockList;
    private ItemCountChangeListener listener;
    private DatabaseSingleton db;
    private String title;
    private Activity activity;
    private View view;
    private int previousCount = -1;
    private boolean isDoneClicked = false;

    public InventoryPopupListAdapter(Context context, Activity activity, ProductHeader item, String title) {
        this.context = context;
        this.activity = activity;
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
        view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.inventory_popup_table_layout, parent, false);

        return new InventoryPopupListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProductVariant productVariant = getCurrentVariant(position);

        holder.sizeView.setText(sizeList.get(position));
        holder.stockView.setText(warehouseStockList.get(position));
        int variantId = productVariant.getId();

        holder.quantityEditText.setOnEditorActionListener(((textView, actionId, keyEvent) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                onDoneClick(holder, position);
                return true;
            }

            return false;
        }));

        holder.quantityEditText.setOnFocusChangeListener((view, hasFocus) -> {
            if (hasFocus) {
                isDoneClicked = false;
                previousCount = Integer.parseInt(holder.quantityEditText.getText().toString());
            } else if (!isDoneClicked) {
                onDoneClick(holder, position);
            }
        });

        holder.plusButton.setOnClickListener(view -> {
            int count = -1;

            try{
                count = Integer.parseInt(holder.quantityEditText.getText().toString());
            }catch (NumberFormatException e) {
                e.printStackTrace();
            }

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
            int count = 1;

            try{
                count = Integer.parseInt(holder.quantityEditText.getText().toString());
            }catch (NumberFormatException e) {
                e.printStackTrace();
            }

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

    private void onDoneClick(ViewHolder holder, int position) {
        isDoneClicked = true;
        hideKeyboard();
        holder.quantityEditText.clearFocus();

        ProductVariant productVariant = getCurrentVariant(position);

        int count = 0;
        int currentCount = 0;

        try {
            count = Integer.parseInt(holder.quantityEditText.getText().toString());
            currentCount = count;
        } catch (NumberFormatException e) {
            e.printStackTrace();

            if (previousCount == -1)
                previousCount = 0;

            holder.quantityEditText.setText(String.valueOf(previousCount));
            Toast.makeText(context, "Value too long", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "integer value too long");
        }

        if (title.equalsIgnoreCase("transfer")) {
            int warehouseStock = db.stockDao().getStocksById(productVariant.getId()).get(0).getWarehouse();
            if (count > warehouseStock) {
                Toast.makeText(context, "Not enough items in stock", Toast.LENGTH_SHORT).show();

                if (previousCount == -1)
                    previousCount = 0;

                holder.quantityEditText.setText(String.valueOf(previousCount));
                return;
            }
        }

        if (previousCount != -1)
            if (previousCount == count) {
                return;
            } else {
                count -= previousCount;
            }

        db.productVariantDao().updateTransferOrderCount(currentCount, productVariant.getId());
        ProductVariant currentVariant = getCurrentVariant(position);
        currentVariant.setTransferOrderCount(currentCount);
        holder.quantityEditText.setText(String.valueOf(currentCount));
        holder.quantityTextView.setText(String.valueOf(currentCount));

        if (listener != null) {
            listener.onItemChangeListener(count, 1);
        }
    }

    private void hideKeyboard() {
        final InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);

        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
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
