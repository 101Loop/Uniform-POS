package com.tapatuniforms.pos.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tapatuniforms.pos.R;
import com.tapatuniforms.pos.model.Product;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    private static final String TAG = "ProductAdapter";

    private ArrayList<Product> productList;
    private Context context;

    private ProductClickListener listener;

    public ProductAdapter(Context context, ArrayList<Product> productList) {
        this.context = context;
        this.productList = productList;
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

        holder.productName.setText(product.getName());

        Glide.with(context)
                .load(product.getImage())
                .centerCrop()
                .into(holder.productImage);

        holder.rootView.setOnClickListener((v) -> {
            if (listener != null) {
                listener.onProductClicked(product);
            }
        });

        holder.closeButton.setOnClickListener(view -> {
            holder.sizeLayout.setVisibility(View.GONE);
        });

        holder.addToCartButton.setOnClickListener(view -> {
            holder.sizeLayout.setVisibility(View.VISIBLE);
        });
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
         * @param product Product that was clicked
         */
        void onProductClicked(Product product);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View rootView;
        ImageView productImage;
        TextView productName;
        TextView closeButton;
        RelativeLayout sizeLayout;
        Button addToCartButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rootView = itemView;
            productImage = itemView.findViewById(R.id.productImageView);
            productName = itemView.findViewById(R.id.productName);
            closeButton = itemView.findViewById(R.id.closeButton);
            sizeLayout = itemView.findViewById(R.id.sizeLayout);
            addToCartButton = itemView.findViewById(R.id.addToCartButton);
        }
    }
}
