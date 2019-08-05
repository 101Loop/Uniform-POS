package com.tapatuniforms.pos.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.tapatuniforms.pos.R;
import com.tapatuniforms.pos.helper.DatabaseHelper;
import com.tapatuniforms.pos.helper.DatabaseSingleton;
import com.tapatuniforms.pos.helper.RoundedCornerLayout;
import com.tapatuniforms.pos.model.ProductHeader;
import com.tapatuniforms.pos.model.ProductVariant;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    private static final String TAG = "ProductAdapter";

    private ArrayList<ProductHeader> productList;
    private ArrayList<String> sizeList;
    private Context context;

    private ProductClickListener listener;

    private SizeAdapter sizeAdapter;
    private DatabaseSingleton db;

    public ProductAdapter(Context context, ArrayList<ProductHeader> productList) {
        this.context = context;
        this.productList = productList;
        sizeList = new ArrayList<>();
        db = DatabaseHelper.getDatabase(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent,
                false);
        return new ProductAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProductHeader product = productList.get(position);
        List<ProductVariant> productVariantList = db.productVariantDao().getProductVariantsById(product.getId());

        holder.sizeLayout.setVisibility(View.GONE);
        holder.productName.setText(product.getName());
        holder.colorText.setText(product.getColor());

        //product type
        String productType = product.getProductType();

        if (productType != null && !productType.equalsIgnoreCase("null")) {
            holder.productType.setText(productType);
        } else {
            holder.productType.setText("");
        }

        //color
        String hexColor = product.getColorCode();

        if (hexColor.length() == 4) {
            String[] arrHexColor = hexColor.split("#");
            hexColor = "#" + arrHexColor[1] + arrHexColor[1];
        }
        holder.colorImage.setBackgroundColor(Color.parseColor(hexColor));

        //product image
        Glide.with(context)
                .load(product.getImage())
                .centerCrop()
                .into(holder.productImage);

        holder.closeButton.setOnClickListener(view -> {
            product.setSizeAlreadyOpened(false);
            holder.sizeLayout.setVisibility(View.GONE);
        });

        holder.addToCartButton.setOnClickListener(view -> {

            if (!product.isSizeAlreadyOpened()) {
                holder.sizeLayout.setVisibility(View.VISIBLE);
                product.setSizeAlreadyOpened(true);

                sizeList.clear();
                for (ProductVariant currentVariant : productVariantList) {
                    sizeList.add(currentVariant.getSize());
                }

                holder.sizeRecyclerView.setLayoutManager(new GridLayoutManager(this.context, 5));
                sizeAdapter = new SizeAdapter(this.context, sizeList);
                holder.sizeRecyclerView.setAdapter(sizeAdapter);

                sizeAdapter.setOnSizeClickListener((pos, size) -> {
                    ProductVariant productVariant = null;
                    for (ProductVariant currentVariant : productVariantList) {
                        if (currentVariant.getSize().equals(size)) {
                            productVariant = currentVariant;
                        }
                    }

                    assert productVariant != null;
                    if (productVariant.getDisplayStock() < 1) {
                        Toast.makeText(context, "Not enough items in display", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (listener != null) {
                        listener.onProductClicked(product, size);

                        holder.sizeLayout.setVisibility(View.GONE);
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    /**
     * Method to set the Product Click listener
     *
     * @param listener ProductClickListener
     */
    public void setOnProductClickListener(ProductClickListener listener) {
        this.listener = listener;
    }

    /**
     * Callback interface for ProductClickListener
     */
    public interface ProductClickListener {
        /**
         * callback method for product click
         *
         * @param product Product that was clicked
         */
        void onProductClicked(ProductHeader product, String size);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View rootView;
        ImageView productImage;
        TextView productName;
        TextView closeButton;
        RelativeLayout sizeLayout;
        Button addToCartButton;
        RecyclerView sizeRecyclerView;
        TextView colorText;
        TextView productType;
        RoundedCornerLayout colorImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rootView = itemView;
            productImage = itemView.findViewById(R.id.productImageView);
            productName = itemView.findViewById(R.id.productName);
            closeButton = itemView.findViewById(R.id.closeButton);
            sizeLayout = itemView.findViewById(R.id.sizeLayout);
            addToCartButton = itemView.findViewById(R.id.addToCartButton);
            sizeRecyclerView = itemView.findViewById(R.id.sizeRecyclerView);
            colorText = itemView.findViewById(R.id.colorView);
            productType = itemView.findViewById(R.id.productType);
            colorImage = itemView.findViewById(R.id.colorImage);
        }
    }
}
