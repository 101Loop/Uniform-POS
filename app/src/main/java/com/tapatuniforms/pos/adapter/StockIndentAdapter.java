package com.tapatuniforms.pos.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tapatuniforms.pos.R;
import com.tapatuniforms.pos.model.Indent;

import java.util.ArrayList;

public class StockIndentAdapter extends RecyclerView.Adapter<StockIndentAdapter.ViewHolder> {
    ArrayList<Indent> indentList;

    public StockIndentAdapter(ArrayList<Indent> indentList) {
        this.indentList = indentList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.stock_indent_item_layout,
                parent, false);
        return new StockIndentAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Indent indent = indentList.get(position);

        holder.indentNameView.setText(indent.getName());
        holder.indentDateView.setText(indent.getDateTime());
        holder.dispatchPersoneNameView.setText(indent.getDispatchPerson());
        holder.indentNumberBox.setText("" + indent.getNumberOfBoxes() + " Boxes");
        holder.indentBoxValue.setText("₹" + indent.getBoxValue());
        holder.indentNumberItems.setText("" + indent.getNumberOfItems() + " Items");
    }

    @Override
    public int getItemCount() {
        return indentList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView indentNameView, indentDateView, indentNumberBox, indentBoxValue, indentNumberItems,
            dispatchPersoneNameView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            indentNameView = itemView.findViewById(R.id.indentNameView);
            indentDateView = itemView.findViewById(R.id.indentDateView);
            dispatchPersoneNameView = itemView.findViewById(R.id.indentDispatchPersonName);
            indentNumberBox = itemView.findViewById(R.id.indentNumberBox);
            indentBoxValue = itemView.findViewById(R.id.indentBoxValue);
            indentNumberItems = itemView.findViewById(R.id.indentNumberItems);
        }
    }
}
