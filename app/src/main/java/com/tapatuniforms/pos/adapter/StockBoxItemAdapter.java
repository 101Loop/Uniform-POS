package com.tapatuniforms.pos.adapter;

import android.content.Context;
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
import com.tapatuniforms.pos.helper.SharedPrefs;
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
    private Outlet outlet;

    public StockBoxItemAdapter(Context context, ArrayList<BoxItem> boxItemList) {
        this.context = context;
        this.boxItemList = boxItemList;

        SharedPrefs.init(context);
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
            holder.incrementScannedItem.setClickable(false);

            if (numberOfItems <= numberOfScannedItems) {
                holder.incrementScannedItem.setClickable(true);
                Toast.makeText(context, "You cannot scan items more than the total number of items", Toast.LENGTH_LONG).show();
                return;
            }

            int scannedItems = ++numberOfScannedItems;
            int warehouseItems = ++warehouseStock;

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put(APIStatic.Key.numberOfScannedItems, scannedItems);
                jsonObject.put(APIStatic.Key.warehouseStock, warehouseItems);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            StockOrderAPI.getInstance(context).updateBoxItem(currentItem.getBoxId(), currentItem.getId(), jsonObject, db, this);
        });
    }

    private void checkStatus() {
        int scannedItems = currentItem.getNumberOfScannedItems();
        int numberOfItems = currentItem.getNumberOfItems();

        if (scannedItems == numberOfItems) {
            holder.statusText.setText(context.getString(R.string.items_matched));
            holder.statusText.setTextColor(ContextCompat.getColor(context, R.color.green));
        } else {
            int remainingItems = numberOfItems - scannedItems;
            String status = remainingItems + " item(s)";
            if (remainingItems == 1) {
                status += " is ";
            }else{
                status += " are ";
            }
            holder.statusText.setText(status + "not matched");
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

        ArrayList<Outlet> outletList = new ArrayList<>(db.outletDao().getAll());
        if (outletList.size() > 0)
            outlet = outletList.get(0);

        if (outlet == null) {
            ProductAPI.getInstance(context).getOutletList(outletList, db, new NotifyListener() {
                @Override
                public void onNotify() {
                    if (outletList.size() > 0)
                        outlet = outletList.get(0);
                }

                @Override
                public void onNotifyResponse(Object data) {

                }
            });
        }
        int outletId = SharedPrefs.readInt(APIStatic.Outlet.OUTLET_ID, -1);

        List<Stock> stockList = db.stockDao().getStocksById(productVariant.getId());
        Stock stock = null;

        if (stockList.size() > 0) {
            stock = stockList.get(0);
        }

        if (stock != null && outletId != -1) {
            productVariant.setWarehouseStock(productVariant.getWarehouseStock() + 1);
            stock.setWarehouse(productVariant.getWarehouseStock());
            ProductAPI.getInstance(context).updateStock(outletId, productVariant.getId(), stock.toJson(), db, new NotifyListener() {
                @Override
                public void onNotify() {
                    holder.incrementScannedItem.setClickable(true);
                }

                @Override
                public void onNotifyResponse(Object data) {

                }
            });
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
