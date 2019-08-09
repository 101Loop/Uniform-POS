package com.tapatuniforms.pos.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tapatuniforms.pos.R;
import com.tapatuniforms.pos.helper.DatabaseHelper;
import com.tapatuniforms.pos.helper.DatabaseSingleton;
import com.tapatuniforms.pos.helper.RoundedCornerLayout;
import com.tapatuniforms.pos.model.CartItem;
import com.tapatuniforms.pos.model.ProductHeader;
import com.tapatuniforms.pos.model.ProductVariant;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    private static final String TAG = "CartAdapter";

    private ArrayList<CartItem> cartList;
    private UpdateItemListener listener;
    private DatabaseSingleton db;
    private Context context;

    public CartAdapter(Context context, ArrayList<CartItem> cartList) {
        this.context = context;
        this.cartList = cartList;
        db = DatabaseHelper.getDatabase(context);
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
        ProductHeader productHeader = cartItem.getProductHeader();

        //name + size
        holder.itemName.setText(productHeader.getName() + " (Size: " + cartItem.getSize() + ")");

        //item type
        String itemType = productHeader.getProductType();
        if (itemType != null && !itemType.isEmpty() && !itemType.equals("null")) {
            holder.itemType.setText(itemType);
        }

        //color name and image
        String color = productHeader.getColor();
        holder.itemColor.setText(color);
        holder.itemColorImage.setBackgroundColor(Color.parseColor(productHeader.getColorCode()));

        //quantity
        holder.quantityText.setText(String.valueOf(cartItem.getQuantity()));

        //price
        DecimalFormat decimalFormatter = new DecimalFormat("₹ #,##,###");
        holder.itemPrice.setText(decimalFormatter.format(cartItem.getPrice() * cartItem.getQuantity()));

        holder.addButton.setOnClickListener(v -> {

            List<ProductVariant> productVariantList = db.productVariantDao().getProductVariantsById(productHeader.getId());
            ProductVariant productVariant = null;
            for (ProductVariant currentVariant : productVariantList) {
                if (currentVariant.getSize().equals(cartItem.getSize())) {
                    productVariant = currentVariant;
                }
            }

            assert productVariant != null;
            if (productVariant.getDisplayStock() - cartItem.getQuantity() < 1) {
                Toast.makeText(context, "Not enough items in display", Toast.LENGTH_SHORT).show();
                return;
            }

            cartItem.setQuantity(cartItem.getQuantity() + 1);

            if (listener != null) {
                listener.onItemUpdateListener(cartItem);
            }

            holder.quantityText.setText(String.valueOf(cartItem.getQuantity()));
            holder.itemPrice.setText(decimalFormatter.format(cartItem.getPrice() * cartItem.getQuantity()));
        });

        holder.minusButton.setOnClickListener(v -> {
            if (cartItem.getQuantity() > 1) {
                cartItem.setQuantity(cartItem.getQuantity() - 1);
                holder.quantityText.setText(String.valueOf(cartItem.getQuantity()));
                holder.itemPrice.setText(decimalFormatter.format(cartItem.getPrice() * cartItem.getQuantity()));

                if (listener != null) {
                    listener.onItemUpdateListener(cartItem);
                }
            }
        });

        holder.removeButton.setOnClickListener(view -> {
            cartList.remove(position);
            notifyDataSetChanged();

            if (listener != null) {
                listener.onItemUpdateListener(cartItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public interface UpdateItemListener {
        void onItemUpdateListener(CartItem cartItem);
    }

    public void setOnItemUpdateListener(UpdateItemListener listener) {
        this.listener = listener;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        View rootView;
        TextView itemName, itemType, itemColor, itemPrice, minusButton, addButton, quantityText, removeButton;
        RoundedCornerLayout itemColorImage;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            rootView = itemView;
            itemName = itemView.findViewById(R.id.cartItemName);
            itemType = itemView.findViewById(R.id.cartItemType);
            itemColor = itemView.findViewById(R.id.cartItemColor);
            itemPrice = itemView.findViewById(R.id.cartItemPrice);
            minusButton = itemView.findViewById(R.id.minusButton);
            addButton = itemView.findViewById(R.id.addButton);
            quantityText = itemView.findViewById(R.id.quantity);
            removeButton = itemView.findViewById(R.id.removeButton);
            itemColorImage = itemView.findViewById(R.id.colorImage);
        }
    }
}
