package com.tapatuniforms.pos.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tapatuniforms.pos.R;
import com.tapatuniforms.pos.helper.DatabaseHelper;
import com.tapatuniforms.pos.helper.DatabaseSingleton;
import com.tapatuniforms.pos.helper.RoundedCornerLayout;
import com.tapatuniforms.pos.model.CartItem;
import com.tapatuniforms.pos.model.Stock;

import java.text.DecimalFormat;
import java.util.ArrayList;

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
        int productId = cartItem.getProduct().getApiId();

        //name + size
        holder.itemName.setText(cartItem.getProduct().getName() + " (Size: " + cartItem.getSize() + ")");

        //item type
        String itemType = cartItem.getProduct().getProductType();
        if (itemType != null && !itemType.isEmpty() && !itemType.equals("null")) {
            holder.itemType.setText(itemType);
        }

        //color name and image
        String color = cartItem.getProduct().getColor();
        holder.itemColor.setText(color);
        holder.itemColorImage.setBackgroundColor(Color.parseColor(cartItem.getProduct().getColorCode()));

        //quantity
        holder.quantityText.setText(String.valueOf(cartItem.getQuantity()));

        //price
        DecimalFormat decimalFormatter = new DecimalFormat("₹ #,##,###");
        holder.itemPrice.setText(decimalFormatter.format(cartItem.getPrice() * cartItem.getQuantity()));

        holder.addButton.setOnClickListener(v -> {
//            Stock stock = db.stockDao().getStock(productId);

//            if (stock != null) {
//                ArrayList<String> displayStockList = stock.getDisplayStockList();

                int pos = cartItem.getPosition();
                int displayStockCount = Integer.parseInt(/*displayStockList.get(pos)*/cartItem.getProduct().getDisplayStockList().get(pos));

                if (cartItem.getQuantity() > displayStockCount - 1) {
                    Toast.makeText(context, "Not enough items in display", Toast.LENGTH_SHORT).show();
                    return;
                }
//            }

            cartItem.setQuantity(cartItem.getQuantity() + 1);
            holder.quantityText.setText(String.valueOf(cartItem.getQuantity()));
            holder.itemPrice.setText(decimalFormatter.format(cartItem.getPrice() * cartItem.getQuantity()));

            if (listener != null) {
                listener.onItemUpdateListener();
            }
        });

        holder.minusButton.setOnClickListener(v -> {
            if (cartItem.getQuantity() > 1) {
                cartItem.setQuantity(cartItem.getQuantity() - 1);
                holder.quantityText.setText(String.valueOf(cartItem.getQuantity()));
                holder.itemPrice.setText(decimalFormatter.format(cartItem.getPrice() * cartItem.getQuantity()));

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
