package com.tapatuniforms.pos.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tapatuniforms.pos.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class PaymentButtonAdapter extends RecyclerView.Adapter<PaymentButtonAdapter.ViewHolder> {
    Context context;
    ArrayList<String> paymentOptionList;

    private ButtonClickListener listener;

    public interface ButtonClickListener {
        void onPaymentButtonClicked(String name);
    }

    public PaymentButtonAdapter(Context context, ArrayList<String> paymentOptionList) {
        this.context = context;
        this.paymentOptionList = paymentOptionList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.payment_button_layout,
                parent, false);
        return new PaymentButtonAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final String item = paymentOptionList.get(position);

        holder.name.setText(item);
        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onPaymentButtonClicked(item);
                }
            }
        });
    }

    public void setOnButtonClickListener(ButtonClickListener listener) {
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return paymentOptionList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        View rootView;
        TextView name;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            rootView = itemView;
            name = itemView.findViewById(R.id.paymentButtonName);
        }
    }
}
