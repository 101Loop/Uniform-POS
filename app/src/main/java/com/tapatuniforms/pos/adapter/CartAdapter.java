package com.tapatuniforms.pos.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tapatuniforms.pos.R;
import com.tapatuniforms.pos.dialog.CartItemDialog;
import com.tapatuniforms.pos.model.CartItem;

import java.text.DecimalFormat;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    private static final String TAG = "CartAdapter";

    private ArrayList<CartItem> cartList;
    private CartItemDialog.CartItemDialogListener listener;

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
        holder.itemSize.setText(cartItem.getProduct().getSize());
        holder.itemPrice.setText(decimalFormatter.format(cartItem.getProduct().getPrice()));

        holder.quantityText.setText(String.valueOf(cartItem.getQuantity()));

        holder.addButton.setOnClickListener(v -> {
            cartItem.setQuantity(cartItem.getQuantity() + 1);
            holder.quantityText.setText(String.valueOf(cartItem.getQuantity()));
            listener.onDoneButtonClicked();
        });

        holder.minusButton.setOnClickListener(v -> {
            if (cartItem.getQuantity() > 1) {
                cartItem.setQuantity(cartItem.getQuantity() - 1);
                holder.quantityText.setText(String.valueOf(cartItem.getQuantity()));
                listener.onDoneButtonClicked();
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    /**
     * This method will notify data set changed and will update the view
     * @param cartList ArrayList of CartItem
     */
    public void loadNewData(ArrayList<CartItem> cartList) {
        this.cartList = cartList;
        notifyDataSetChanged();
    }

    /**
     * Sets the item click listener on CartItems
     * @param listener CartItemListener
     */
    public void setOnClickListener(CartItemDialog.CartItemDialogListener listener) {
        this.listener = listener;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        View rootView;
        TextView itemName, itemSize, itemPrice, itemDiscount, minusButton, addButton, quantityText;

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
        }
    }
}
