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

    private ArrayList<CartItem> cartList;
    private CartItemListener listener;

    public interface CartItemListener {
        void onCartItemClicked(CartItem item);
    }

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
        final CartItem cartItem = cartList.get(position);

        holder.itemCount.setText("" + cartItem.getQuantity());
        holder.itemName.setText(cartItem.getProduct().getName());
        holder.itemSize.setText(cartItem.getProduct().getSize());
        holder.itemPrice.setText("₹" + cartItem.getProduct().getPrice());

        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onCartItemClicked(cartItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public void loadNewData(ArrayList<CartItem> cartList) {
        this.cartList = cartList;
        notifyDataSetChanged();
    }

    public void setOnClickListener(CartItemListener listener) {
        this.listener = listener;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        View rootView;
        TextView itemName, itemCount, itemSize, itemPrice, itemDiscount;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            rootView = itemView;
            itemCount = itemView.findViewById(R.id.cartItemCount);
            itemName = itemView.findViewById(R.id.cartItemName);
            itemSize = itemView.findViewById(R.id.cartItemSize);
            itemPrice = itemView.findViewById(R.id.cartItemPrice);
            itemDiscount = itemView.findViewById(R.id.cartItemDiscount);
        }
    }
}
