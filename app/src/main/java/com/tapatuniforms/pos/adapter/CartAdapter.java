package com.tapatuniforms.pos.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tapatuniforms.pos.R;
import com.tapatuniforms.pos.model.CartItem;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    private static final String TAG = "CartAdapter";

    private ArrayList<CartItem> cartList;
    private UpdateItemListener listener;

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

        DecimalFormat decimalFormatter = new DecimalFormat("₹#,##,###.##");

        holder.itemName.setText(cartItem.getProduct().getName());

        //TODO: check if it works or not(product)
        holder.itemSize.setText(String.valueOf(cartItem.getProduct().getSizeList().get(cartItem.getPosition())));
        holder.itemPrice.setText(decimalFormatter.format(cartItem.getProduct().getPriceList().get(cartItem.getPosition())));

        holder.quantityText.setText(String.valueOf(cartItem.getQuantity()));

        holder.addButton.setOnClickListener(v -> {
            cartItem.setQuantity(cartItem.getQuantity() + 1);
            holder.quantityText.setText(String.valueOf(cartItem.getQuantity()));

            if (listener != null) {
                listener.onItemUpdateListener();
            }
        });

        holder.minusButton.setOnClickListener(v -> {
            if (cartItem.getQuantity() > 1) {
                cartItem.setQuantity(cartItem.getQuantity() - 1);
                holder.quantityText.setText(String.valueOf(cartItem.getQuantity()));

                if (listener != null) {
                    listener.onItemUpdateListener();
                }
            }
        });

        holder.removeButton.setOnClickListener(view -> {
            cartList.remove(position);
            notifyDataSetChanged();

            if (listener != null) {
                listener.onItemUpdateListener();
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public interface UpdateItemListener {
        void onItemUpdateListener();
    }

    public void setOnItemUpdateListener(UpdateItemListener listener) {
        this.listener = listener;
    }

    /**
     * This method will notify data set changed and will update the view
     *
     * @param cartList ArrayList of CartItem
     */
    public void loadNewData(ArrayList<CartItem> cartList) {
        this.cartList = cartList;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        View rootView;
        TextView itemName, itemSize, itemPrice, itemDiscount, minusButton, addButton, quantityText, removeButton;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            rootView = itemView;
            itemName = itemView.findViewById(R.id.cartItemName);
            itemSize = itemView.findViewById(R.id.cartItemSize);
            itemPrice = itemView.findViewById(R.id.cartItemPrice);
            itemDiscount = itemView.findViewById(R.id.cartItemDiscount);
            minusButton = itemView.findViewById(R.id.minusButton);
            addButton = itemView.findViewById(R.id.addButton);
            quantityText = itemView.findViewById(R.id.quantity);
            removeButton = itemView.findViewById(R.id.removeButton);
        }
    }
}
