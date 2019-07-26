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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.tapatuniforms.pos.R;
import com.tapatuniforms.pos.helper.RoundedCornerLayout;
import com.tapatuniforms.pos.model.Product;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    private static final String TAG = "ProductAdapter";

    private ArrayList<Product> productList;
    private ArrayList<String> sizeList;
    private Context context;

    private ProductClickListener listener;

    private SizeAdapter sizeAdapter;

    public ProductAdapter(Context context, ArrayList<Product> productList, ArrayList<String> sizeList) {
        this.context = context;
        this.productList = productList;
        this.sizeList = sizeList;
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
        Product product = productList.get(position);

        holder.sizeLayout.setVisibility(View.GONE);
        holder.productName.setText(product.getName());
        holder.colorText.setText(product.getColor());

        String productType = product.getProductType();

        if (productType != null && !productType.equalsIgnoreCase("null")) {
            holder.productType.setText(productType);
        } else {
            holder.productType.setText("");
        }

        String hexColor = product.getColorCode();

        if (hexColor.length() == 4) {
            String[] arrHexColor = hexColor.split("#");
            hexColor = "#" + arrHexColor[1] + arrHexColor[1];
        }
        holder.colorImage.setBackgroundColor(Color.parseColor(hexColor));

        Glide.with(context)
                .load(product.getImage())
                .centerCrop()
                .into(holder.productImage);

        holder.closeButton.setOnClickListener(view -> {
            product.setSizeAlreadyOpen(false);
            holder.sizeLayout.setVisibility(View.GONE);
        });

        holder.addToCartButton.setOnClickListener(view -> {
            holder.sizeLayout.setVisibility(View.VISIBLE);

            sizeList = getSizes(product);
            holder.sizeRecyclerView.setLayoutManager(new GridLayoutManager(this.context, 5));
            sizeAdapter = new SizeAdapter(this.context, sizeList);
            holder.sizeRecyclerView.setAdapter(sizeAdapter);

            sizeAdapter.setOnSizeClickListener((pos, size) -> {
                if (listener != null) {
                    listener.onProductClicked(product, pos, size);

                    holder.sizeLayout.setVisibility(View.GONE);
                }
            });
        });
    }

    private ArrayList<String> getSizes(Product product) {

        return product.getSizeList();
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public void loadNewData(ArrayList<Product> productList) {
        this.productList = productList;
        notifyDataSetChanged();
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
        void onProductClicked(Product product, int position, String size);
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
