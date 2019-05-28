package com.tapatuniforms.pos.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.tapatuniforms.pos.R;

public class DiscountDialog extends AlertDialog {
    private TextView percentageButton, rupeeButton;
    private EditText discountEditText;
    private Discount discountType;
    private Button doneButton;

    private DiscountChangeListener listener;

    public DiscountDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_discount);

        initView();
    }

    private void initView() {
        percentageButton = findViewById(R.id.percentageButton);
        rupeeButton = findViewById(R.id.rupeeButton);
        discountEditText = findViewById(R.id.discountEditText);
        doneButton = findViewById(R.id.doneDiscountButton);

        setView();
    }

    private void setView() {
        discountType = Discount.PERCENTAGE;

        percentageButton.setOnClickListener((v) -> {
            percentageButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
            percentageButton.setTextColor(ContextCompat.getColor(getContext(), R.color.white1));
            rupeeButton.setBackground(null);
            rupeeButton.setTextColor(ContextCompat.getColor(getContext(), R.color.black1));
            discountType = Discount.PERCENTAGE;
        });

        rupeeButton.setOnClickListener((v) -> {
            rupeeButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
            rupeeButton.setTextColor(ContextCompat.getColor(getContext(), R.color.white1));
            percentageButton.setBackground(null);
            percentageButton.setTextColor(ContextCompat.getColor(getContext(), R.color.black1));
            discountType = Discount.MONEY;
        });

        doneButton.setOnClickListener((v) -> {
            dismiss();
            if (listener != null) {
                double discountAmount = 0;
                if (!discountEditText.getText().toString().trim().isEmpty()) {
                    discountAmount = Double.valueOf(discountEditText.getText().toString().trim());
                }

                listener.onDiscountChange(discountType, discountAmount);
            }
        });
    }

    public void setOnDiscountChangeListener(DiscountChangeListener listener) {
        this.listener = listener;
    }

    public interface DiscountChangeListener {
        void onDiscountChange(Discount type, double amount);
    }

    public enum Discount {
        PERCENTAGE, MONEY
    }
}
