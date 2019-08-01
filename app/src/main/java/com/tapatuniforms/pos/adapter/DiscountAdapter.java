package com.tapatuniforms.pos.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tapatuniforms.pos.R;
import com.tapatuniforms.pos.helper.APIStatic;
import com.tapatuniforms.pos.model.Discount;

import java.util.ArrayList;

public class DiscountAdapter extends RecyclerView.Adapter<DiscountAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Discount> discountList;
    private int discountQuantity;
    private DiscountInterface listener;

    public DiscountAdapter(Context context, ArrayList<Discount> discountList) {
        this.context = context;
        this.discountList = discountList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.discount_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Discount discount = discountList.get(position);
        discountQuantity = discount.getProductQuantity();

        String value;
        String discountType = discount.getDiscountType();

        if (discountType.equals(APIStatic.Constants.AMOUNT)) {
            value = "₹ " + discount.getValue() + " Off";
        } else if (discountType.equals(APIStatic.Constants.PERCENTAGE)) {
            value = discount.getValue() + " % Off";
        } else {
            value = discount.getValue() + " Off";
        }

        holder.extraOfferText.setText("");
        holder.valueOfferText.setText(value);
        holder.rootView.setOnClickListener(view -> {
//            discountQuantity--;

            if (listener != null) {
                listener.onDiscountClickListener(discount.getValue(), discount.getDiscountType());
            }

            /*if (discountQuantity < 1) {
                discountList.remove(discount);
                notifyDataSetChanged();
            }*/
        });
    }

    @Override
    public int getItemCount() {
        return discountList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View rootView;
        TextView extraOfferText, valueOfferText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            rootView = itemView;
            extraOfferText = itemView.findViewById(R.id.extraOfferText);
            valueOfferText = itemView.findViewById(R.id.valueOfferText);
        }
    }

    public interface DiscountInterface {
        void onDiscountClickListener(int discountValue, String value);
    }

    public void setOnDiscountClickListener (DiscountInterface listener) {
        this.listener = listener;
    }
}
