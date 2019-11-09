package com.tapatuniforms.pos.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
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
    private ProductVariant productVariant;
    private ViewHolder holder;
    private int numberOfItems;
    private int numberOfScannedItems;
    private int warehouseStock;

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
        this.holder = holder;
        currentItem = boxItemList.get(position);
        ProductHeader product = db.productHeaderDao().getProductHeaderById(currentItem.getProductId());

        List<ProductVariant> productVariants = db.productVariantDao().getProductVariantsById(product.getId());
        ProductVariant productVariant;
        Stock stock = null;
        if (productVariants.size() > 0) {
            productVariant = productVariants.get(0);

            List<Stock> stockList = db.stockDao().getStocksById(productVariant.getId());
            if (stockList.size() > 0)
                stock = stockList.get(0);
        }

        String name = product.getName();
        numberOfItems = currentItem.getNumberOfItems();
        numberOfScannedItems = currentItem.getNumberOfScannedItems();

        holder.itemNameView.setText(name);
        holder.itemSentView.setText(String.valueOf(numberOfItems));
        holder.itemScannedView.setText(String.valueOf(numberOfScannedItems));

        checkStatus();

        if (stock != null)
            holder.itemShelfView.setText(String.valueOf(stock.getDisplay()));

        warehouseStock = currentItem.getWarehouseStock();

        holder.incrementScannedItem.setOnClickListener(view -> {
            if (numberOfItems < numberOfScannedItems) {
                Toast.makeText(context, "You cannot scan items more than the total number of items", Toast.LENGTH_LONG).show();
                return;
            }

            int scannedItems = numberOfScannedItems++;
            int warehouseItems = warehouseStock++;

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put(APIStatic.Key.numberOfScannedItems, scannedItems);
                jsonObject.put(APIStatic.Key.warehouseStock, warehouseItems);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            StockOrderAPI.getInstance(context).updateBoxItem(currentItem.getBoxId(), currentItem.getId(), jsonObject, db, this);
        });

        //TODO: the move logic might be removed completely
        /*ProductVariant finalProductVariant = productVariant;
        Stock finalStock = stock;
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

            assert finalStock != null;
            int displayCount = finalStock.getDisplay();
            int warehouseCount = finalStock.getWarehouse();
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

            finalStock.setWarehouse(warehouseCount - moveCount);
            finalStock.setDisplay(displayCount + moveCount);

            JSONObject json = finalStock.toJson();
            try {
                json.put(APIStatic.Key.numberOfScannedItems, finalScannedItems);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            StockOrderAPI.getInstance(context).updateBoxItem(currentItem.getBoxId(), currentItem.getId(), json, db, this);

            if (outletId != -1)
                ProductAPI.getInstance(context).updateStock(outletId, Objects.requireNonNull(finalProductVariant).getId(), finalStock.toJson(), db, null);

            db.boxItemDao().updateScannedItems(finalScannedItems, currentItem.getId());

            Toast.makeText(context, "Items transferred successfully", Toast.LENGTH_SHORT).show();
        });*/
    }

    private void checkStatus() {
        int scannedItems = currentItem.getNumberOfScannedItems();
        int numberOfItems = currentItem.getNumberOfItems();

        if (scannedItems == numberOfItems) {
            holder.statusText.setText(context.getString(R.string.items_matched));
            holder.statusText.setTextColor(ContextCompat.getColor(context, R.color.green));
        } else {
            int remainingItems = numberOfItems - scannedItems;
            holder.statusText.setText(remainingItems + " item(s) are not matched");
            holder.statusText.setTextColor(ContextCompat.getColor(context, R.color.black1));
        }
    }

    @Override
    public int getItemCount() {
        return boxItemList.size();
    }

    @Override
    public void onNotify() {
        notifyDataSetChanged();
    }

    @Override
    public void onNotifyResponse(Object data) {
        currentItem = (BoxItem) data;
        updateViews();

        ProductHeader product = db.productHeaderDao().getProductHeaderById(currentItem.getProductId());
        List<ProductVariant> productVariants = db.productVariantDao().getProductVariantsById(product.getId());

        if (productVariants.size() > 0)
            productVariant = productVariants.get(0);

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
    }

    private void updateViews() {
        holder.itemScannedView.setText(String.valueOf(currentItem.getNumberOfScannedItems()));

        checkStatus();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView itemNameView, itemSentView, itemScannedView, itemShelfView, statusText;
        CardView incrementScannedItem;
        TextView itemsToMoveText;
        ImageView itemCheckedStatus;
        Button moveButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemNameView = itemView.findViewById(R.id.itemNameView);
            itemSentView = itemView.findViewById(R.id.itemSentView);
            itemScannedView = itemView.findViewById(R.id.itemScannedView);
            itemShelfView = itemView.findViewById(R.id.itemShelfView);
            statusText = itemView.findViewById(R.id.statusText);
            itemsToMoveText = itemView.findViewById(R.id.moveText);
            itemCheckedStatus = itemView.findViewById(R.id.itemCheckedStatus);
            moveButton = itemView.findViewById(R.id.moveButton);
            incrementScannedItem = itemView.findViewById(R.id.incrementScannedItems);
        }
    }
}
