package com.tapatuniforms.pos.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tapatuniforms.pos.R;

import java.util.ArrayList;

public class DiscountAdapter extends RecyclerView.Adapter<DiscountAdapter.ViewHolder> {
    private Context context;
    private ArrayList<?> discountList;

    public DiscountAdapter(Context context, ArrayList<?> discountList){
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

    }

    @Override
    public int getItemCount() {
        //TODO: change this with the list size provided
        return 1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
