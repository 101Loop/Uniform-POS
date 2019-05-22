package com.tapatuniforms.pos.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tapatuniforms.pos.R;
import com.tapatuniforms.pos.model.Product;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import static com.tapatuniforms.pos.helper.ImageHelper.decodeSampledBitmapFromResource;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    private static final String TAG = "ProductAdapter";

    private ArrayList<Product> productList;
    private Context context;

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
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public void loadNewData(ArrayList<Product> productList) {
        this.productList = productList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.productImageView);
            productName = itemView.findViewById(R.id.productName);
        }
    }
}
