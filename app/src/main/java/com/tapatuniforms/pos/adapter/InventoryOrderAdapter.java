package com.tapatuniforms.pos.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.tapatuniforms.pos.R;
import com.tapatuniforms.pos.helper.DatabaseHelper;
import com.tapatuniforms.pos.helper.DatabaseSingleton;
import com.tapatuniforms.pos.model.Category;
import com.tapatuniforms.pos.model.ProductHeader;
import com.tapatuniforms.pos.model.ProductVariant;
import com.tapatuniforms.pos.model.Stock;

import java.util.ArrayList;
import java.util.List;

public class InventoryOrderAdapter extends RecyclerView.Adapter<InventoryOrderAdapter.ViewHolder> {
    private Context context;
    private ArrayList<ProductHeader> recommendedProductList;
    private ButtonClickListener listener;
    private DatabaseSingleton db;
    private Stock stock;

    public interface ButtonClickListener {
        void onTransferButtonClick(ProductHeader item, String title);
    }

    public InventoryOrderAdapter(Context context, ArrayList<ProductHeader> recommendedProductList) {
        this.context = context;
        this.recommendedProductList = recommendedProductList;
        db = DatabaseHelper.getDatabase(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.inventory_order_card_item, parent, false);
        return new InventoryOrderAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProductHeader product = recommendedProductList.get(position);
        List<ProductVariant> variantList = db.productVariantDao().getProductVariantsById(product.getId());

        String image = getCategoryImage(product.getCategory());

        //image
        if (image != null)
            Glide.with(context)
                    .load(image)
                    .centerCrop()
                    .error(R.drawable.ic_no_image)
                    .placeholder(R.drawable.ic_no_image)
                    .into(holder.itemImageView);

        holder.itemNameView.setText(product.getName());

        int totalWarehouseCount = 0;
        for (ProductVariant currentVariant : variantList) {
            List<Stock> stockList = db.stockDao().getStocksById(currentVariant.getId());
            if (stockList.size() > 0)
                stock = stockList.get(0);
            totalWarehouseCount += stock.getWarehouse();
        }

        holder.warehouseCount.setText(String.valueOf(totalWarehouseCount));

        holder.orderNowButton.setOnClickListener(view -> {
            if (listener != null) {
                listener.onTransferButtonClick(product, "Add to Order");
            }
        });
    }

    @Override
    public int getItemCount() {
        return recommendedProductList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View rootView;
        ImageView itemImageView;
        TextView itemNameView, warehouseCount, orderNowButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            rootView = itemView;
            itemImageView = itemView.findViewById(R.id.itemImageView);
            itemNameView = itemView.findViewById(R.id.itemNameView);
            warehouseCount = itemView.findViewById(R.id.warehouseCount);
            orderNowButton = itemView.findViewById(R.id.orderNowButton);
        }
    }

    /**
     * Method to set button click listener
     */
    public void setOnButtonClickListener(ButtonClickListener listener) {
        this.listener = listener;
    }

    /**
     * Method to get category image
     *
     * @param category Category id
     * @return Returns image url
     */
    private String getCategoryImage(int category) {
        Category category1 = db.categoryDao().getCategory(category);
        if (category1 != null)
            return category1.getImage();
        return null;
    }
}
