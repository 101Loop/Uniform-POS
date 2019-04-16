package com.tapatuniforms.pos.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tapatuniforms.pos.R;
import com.tapatuniforms.pos.model.CartItem;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    private static final String TAG = "CartAdapter";

    ArrayList<CartItem> cartList;

    public CartAdapter(ArrayList<CartItem> cartList) {
        this.cartList = cartList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item_layout,
                parent, false);
        return new CartAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CartItem cartItem = cartList.get(position);

        holder.itemCount.setText("" + cartItem.getQuantity());
        holder.itemName.setText(cartItem.getProduct().getName());
        holder.itemSize.setText(cartItem.getProduct().getSize());
        holder.itemPrice.setText("" + cartItem.getProduct().getPrice());
        holder.itemDiscount.setText("Discount @ 10%");
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView itemName, itemCount, itemSize, itemPrice, itemDiscount;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemCount = itemView.findViewById(R.id.cartItemCount);
            itemName = itemView.findViewById(R.id.cartItemName);
            itemSize = itemView.findViewById(R.id.cartItemSize);
            itemPrice = itemView.findViewById(R.id.cartItemPrice);
            itemDiscount = itemView.findViewById(R.id.cartItemDiscount);
        }
    }
}
