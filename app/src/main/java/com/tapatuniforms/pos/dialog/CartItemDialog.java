package com.tapatuniforms.pos.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.tapatuniforms.pos.R;
import com.tapatuniforms.pos.model.CartItem;

public class CartItemDialog extends AlertDialog {
    private CartItem item;
    private ImageView minusImage, plusImage;
    private TextView itemCountView;
    private Button removeButton, doneButton;
    private CartItemDialogListener listener;

    public CartItemDialog(Context context, CartItem item) {
        super(context);
        this.item = item;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart_dialog_layout);

        minusImage = findViewById(R.id.minusImage);
        plusImage = findViewById(R.id.plusImage);
        itemCountView = findViewById(R.id.itemCountView);
        removeButton = findViewById(R.id.removeButton);
        doneButton = findViewById(R.id.doneButton);

        itemCountView.setText("" + item.getQuantity());

        minusImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantity = item.getQuantity();
                if (quantity > 1) {
                    quantity--;
                    item.setQuantity(quantity);
                    itemCountView.setText("" + quantity);
                }
            }
        });

        plusImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantity = item.getQuantity() + 1;
                item.setQuantity(quantity);
                itemCountView.setText("" + quantity);
            }
        });

        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onRemoveButtonClicked(item);
                }
            }
        });

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onDoneButtonClicked();
                }
            }
        });
    }

    public void setOnButtonClickListener(CartItemDialogListener listener) {
        this.listener = listener;
    }

    public interface CartItemDialogListener {
        void onDoneButtonClicked();
        void onRemoveButtonClicked(CartItem item);
    }
}
