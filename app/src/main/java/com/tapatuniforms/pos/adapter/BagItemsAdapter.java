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
import com.tapatuniforms.pos.model.CartItem;
import com.tapatuniforms.pos.model.ProductHeader;

import java.util.List;

public class BagItemsAdapter extends RecyclerView.Adapter<BagItemsAdapter.ViewHolder> {
    private Context context;
    private List<CartItem> itemList;

    public BagItemsAdapter(Context context, List<CartItem> productVariantList) {
        this.context = context;
        this.itemList = productVariantList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bag_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CartItem item = itemList.get(position);

        ProductHeader product = item.getProductHeader();
        String title = product.getName() + " (Size - " + item.getSize() + ")";
        String quantity = "Quantity: " + item.getQuantity();
        String price = "Selling Price: " + item.getPrice();

        Glide.with(context)
                .load(product.getImage())
                .centerCrop().into(holder
                .productImage);

        holder.title.setText(title);
        holder.quantity.setText(quantity);
        holder.price.setText(price);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView productImage;
        private TextView title, quantity, price;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            productImage = itemView.findViewById(R.id.productImage);
            title = itemView.findViewById(R.id.productTitle);
            quantity = itemView.findViewById(R.id.quantity);
            price = itemView.findViewById(R.id.productPrice);
        }
    }
}
