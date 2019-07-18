package com.tapatuniforms.pos.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.tapatuniforms.pos.R;

import java.util.ArrayList;

public class SizeAdapter extends RecyclerView.Adapter<SizeAdapter.ViewHolder> {
    private Context context;
    private ArrayList<String> sizes;


    public SizeAdapter(Context context, ArrayList<String> sizes) {
        this.context = context;
        this.sizes = sizes;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_size, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.sizeText.setText(String.valueOf(sizes.get(position)));
    }

    @Override
    public int getItemCount() {
        return sizes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView sizeText;
        LinearLayout rootLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            sizeText = itemView.findViewById(R.id.sizeText);
            rootLayout = itemView.findViewById(R.id.rootLayout);
        }
    }
}
