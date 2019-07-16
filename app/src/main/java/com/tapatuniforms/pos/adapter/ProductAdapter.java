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
import com.tapatuniforms.pos.helper.GridItemDecoration;
import com.tapatuniforms.pos.model.Product;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Size;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    private static final String TAG = "ProductAdapter";

    private ArrayList<Product> productList;
    private Context context;

    private ProductClickListener listener;

    private SizeAdapter adapter;

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

        holder.closeButton.setOnClickListener(view -> holder.sizeLayout.setVisibility(View.GONE));

        holder.addToCartButton.setOnClickListener(view -> holder.sizeLayout.setVisibility(View.VISIBLE));

        //get a list of sizes
        ArrayList<Integer> sizes = getSizes();
        adapter = new SizeAdapter(this.context, sizes);
        holder.sizeRecyclerView.setLayoutManager(new GridLayoutManager(this.context, 5));
        holder.sizeRecyclerView.addItemDecoration(new GridItemDecoration(5, 5));
        holder.sizeRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private ArrayList<Integer> getSizes() {
        ArrayList<Integer> sizes = new ArrayList<>();

        int size = 22;
        for (int i = 0; i < 9; i++) {
            sizes.add(size);
            size += 2;
        }

        return sizes;
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
        void onProductClicked(Product product);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View rootView;
        ImageView productImage;
        TextView productName;
        TextView closeButton;
        RelativeLayout sizeLayout;
        Button addToCartButton;
        RecyclerView sizeRecyclerView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rootView = itemView;
            productImage = itemView.findViewById(R.id.productImageView);
            productName = itemView.findViewById(R.id.productName);
            closeButton = itemView.findViewById(R.id.closeButton);
            sizeLayout = itemView.findViewById(R.id.sizeLayout);
            addToCartButton = itemView.findViewById(R.id.addToCartButton);
            sizeRecyclerView = itemView.findViewById(R.id.sizeRecyclerView);
        }
    }
}
