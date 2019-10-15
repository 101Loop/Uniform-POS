package com.tapatuniforms.pos.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tapatuniforms.pos.R;
import com.tapatuniforms.pos.listener.ReturnItemClickListener;

import java.util.ArrayList;

public class ReturnItemAdapter extends RecyclerView.Adapter<ReturnItemAdapter.ViewHolder> {
    private Context context;
    private ArrayList<String> itemList;
    private ReturnItemClickListener listener;

    public ReturnItemAdapter(Context context, ArrayList<String> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.return_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.itemNameText.setText(itemList.get(position));

        holder.closeLayout.setOnClickListener(view -> {
            itemList.remove(position);
            notifyDataSetChanged();

            if (listener != null) {
                listener.onReturnItemClick();
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View rootView;
        TextView itemNameText;
        LinearLayout closeLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            rootView = itemView;
            itemNameText = itemView.findViewById(R.id.itemNameText);
            closeLayout = itemView.findViewById(R.id.closeButton);
        }
    }

    public void setOnReturnItemClickListener(ReturnItemClickListener listener){
        this.listener = listener;
    }
}
