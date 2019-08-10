package com.tapatuniforms.pos.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tapatuniforms.pos.R;
import com.tapatuniforms.pos.helper.DatabaseHelper;
import com.tapatuniforms.pos.helper.DatabaseSingleton;
import com.tapatuniforms.pos.model.BoxItem;
import com.tapatuniforms.pos.model.ProductHeader;

import java.util.ArrayList;

public class StockBoxItemAdapter extends RecyclerView.Adapter<StockBoxItemAdapter.ViewHolder> {
    private Context context;
    private ArrayList<BoxItem> boxItemList;
    private DatabaseSingleton db;

    public StockBoxItemAdapter(Context context, ArrayList<BoxItem> boxItemList) {
        this.context = context;
        this.boxItemList = boxItemList;

        db = DatabaseHelper.getDatabase(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.stock_list_item_layout, parent, false);
        return new StockBoxItemAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BoxItem currentItem = boxItemList.get(position);
        ProductHeader product = db.productHeaderDao().getProductHeaderById(currentItem.getProductId());

        String name = product.getName();
        holder.itemNameView.setText(name);
        holder.itemSentView.setText(String.valueOf(currentItem.getNumberOfItems()));
        holder.itemScannedView.setText(String.valueOf(currentItem.getNumberOfScannedItems()));

//        int itemsInShelf =
//        holder.itemShelfView.setText(String.valueOf(currentItem.getNumberOfShelfItems()));
    }

    @Override
    public int getItemCount() {
        return boxItemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView itemNameView, itemSentView, itemScannedView, itemShelfView;
        EditText itemReceivedView;
        ImageView itemCheckedStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemNameView = itemView.findViewById(R.id.itemNameView);
            itemSentView = itemView.findViewById(R.id.itemSentView);
            itemScannedView = itemView.findViewById(R.id.itemScannedView);
            itemShelfView = itemView.findViewById(R.id.itemShelfView);
            itemReceivedView = itemView.findViewById(R.id.itemReceivedView);
            itemCheckedStatus = itemView.findViewById(R.id.itemCheckedStatus);
        }
    }
}
