package com.tapatuniforms.pos.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tapatuniforms.pos.R;
import com.tapatuniforms.pos.helper.APIStatic;
import com.tapatuniforms.pos.helper.DatabaseHelper;
import com.tapatuniforms.pos.helper.DatabaseSingleton;
import com.tapatuniforms.pos.helper.NotifyListener;
import com.tapatuniforms.pos.model.BoxItem;
import com.tapatuniforms.pos.model.Outlet;
import com.tapatuniforms.pos.model.ProductHeader;
import com.tapatuniforms.pos.model.ProductVariant;
import com.tapatuniforms.pos.model.Stock;
import com.tapatuniforms.pos.network.ProductAPI;
import com.tapatuniforms.pos.network.StockOrderAPI;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class StockBoxItemAdapter extends RecyclerView.Adapter<StockBoxItemAdapter.ViewHolder> implements NotifyListener {
    private Context context;
    private ArrayList<BoxItem> boxItemList;
    private DatabaseSingleton db;
    private BoxItem currentItem;

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
        currentItem = boxItemList.get(position);
        ProductHeader product = db.productHeaderDao().getProductHeaderById(currentItem.getProductId());
        ProductVariant productVariant = db.productVariantDao().getProductVariantsById(product.getId()).get(0);
        Stock stock = db.stockDao().getStocksById(productVariant.getId()).get(0);

        String name = product.getName();
        holder.itemNameView.setText(name);
        holder.itemSentView.setText(String.valueOf(currentItem.getNumberOfItems()));
        holder.itemScannedView.setText(String.valueOf(currentItem.getNumberOfScannedItems()));
        holder.itemShelfView.setText(String.valueOf(stock.getDisplay()));

        int warehouseStock = currentItem.getWarehouseStock();
        int numberOfItems = currentItem.getNumberOfItems();

        if (warehouseStock != numberOfItems) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put(APIStatic.Key.warehouseStock, numberOfItems);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            StockOrderAPI.getInstance(context).updateBoxItem(currentItem.getBoxId(), currentItem.getId(), jsonObject, db, this);
        }

        holder.moveButton.setOnClickListener(view -> {
            int moveCount = Integer.parseInt(holder.itemsToMoveText.getText().toString().trim());
            int scannedCount = Integer.parseInt(holder.itemScannedView.getText().toString().trim());

            if (moveCount == 0) {
                Toast.makeText(context, "Items to be moved should be greater than 0", Toast.LENGTH_SHORT).show();
                return;
            }

            if (moveCount > scannedCount) {
                Toast.makeText(context, "Items can't be greater than scanned items", Toast.LENGTH_SHORT).show();
                return;
            }

            int displayCount = stock.getDisplay();
            int warehouseCount = stock.getWarehouse();
            int shelfCount = displayCount + moveCount;
            int finalScannedItems = scannedCount - moveCount;

            if (moveCount > warehouseCount) {
                Toast.makeText(context, "Not enough items in warehouse", Toast.LENGTH_SHORT).show();
                return;
            }

            holder.itemsToMoveText.setText(null);
            holder.itemShelfView.setText(String.valueOf(shelfCount));
            holder.itemScannedView.setText(String.valueOf(finalScannedItems));

            List<Outlet> outletList = db.outletDao().getAll();
            long outletId = -1;
            if (outletList.size() > 0)
                outletId = outletList.get(0).getId();

            stock.setWarehouse(warehouseCount - moveCount);
            stock.setDisplay(displayCount + moveCount);

            JSONObject json = stock.toJson();
            try {
                json.put(APIStatic.Key.numberOfScannedItems, finalScannedItems);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            StockOrderAPI.getInstance(context).updateBoxItem(currentItem.getBoxId(), currentItem.getId(), json, db, this);

            if (outletId != -1)
                ProductAPI.getInstance(context).updateStock(outletId, productVariant.getId(), stock.toJson(), db, null);

            db.boxItemDao().updateScannedItems(finalScannedItems, currentItem.getId());

            Toast.makeText(context, "Items transferred successfully", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return boxItemList.size();
    }

    @Override
    public void onNotify() {
        new Handler().postDelayed(this::notifyDataSetChanged, 1000);
    }

    @Override
    public void onNotifyResponse(Object data) {
        new Handler().postDelayed(() -> {
            currentItem = (BoxItem) data;

            ProductHeader product = db.productHeaderDao().getProductHeaderById(currentItem.getProductId());
            ProductVariant productVariant = db.productVariantDao().getProductVariantsById(product.getId()).get(0);

            List<Outlet> outletList = db.outletDao().getAll();
            Outlet outlet = null;
            if (outletList.size() > 0)
                outlet = outletList.get(0);

            List<Stock> stockList = db.stockDao().getStocksById(productVariant.getId());
            Stock stock = null;

            if (stockList.size() > 0) {
                stock = stockList.get(0);
            }

            if (stock != null && outlet != null) {
                stock.setWarehouse(currentItem.getWarehouseStock() + productVariant.getWarehouseStock());
                ProductAPI.getInstance(context).updateStock(outlet.getId(), productVariant.getId(), stock.toJson(), db, null);
            }
        }, 1000);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView itemNameView, itemSentView, itemScannedView, itemShelfView;
        EditText itemsToMoveText;
        ImageView itemCheckedStatus;
        Button moveButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemNameView = itemView.findViewById(R.id.itemNameView);
            itemSentView = itemView.findViewById(R.id.itemSentView);
            itemScannedView = itemView.findViewById(R.id.itemScannedView);
            itemShelfView = itemView.findViewById(R.id.itemShelfView);
            itemsToMoveText = itemView.findViewById(R.id.moveText);
            itemCheckedStatus = itemView.findViewById(R.id.itemCheckedStatus);
            moveButton = itemView.findViewById(R.id.moveButton);
        }
    }
}
