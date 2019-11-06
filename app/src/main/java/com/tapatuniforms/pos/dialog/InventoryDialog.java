package com.tapatuniforms.pos.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tapatuniforms.pos.R;
import com.tapatuniforms.pos.adapter.InventoryPopupListAdapter;
import com.tapatuniforms.pos.helper.DatabaseHelper;
import com.tapatuniforms.pos.helper.DatabaseSingleton;
import com.tapatuniforms.pos.helper.NotifyListener;
import com.tapatuniforms.pos.helper.RoundedCornerLayout;
import com.tapatuniforms.pos.model.Outlet;
import com.tapatuniforms.pos.model.ProductHeader;
import com.tapatuniforms.pos.model.ProductVariant;
import com.tapatuniforms.pos.model.Stock;
import com.tapatuniforms.pos.network.ProductAPI;
import com.tapatuniforms.pos.network.StockOrderAPI;

import org.json.JSONObject;

import java.util.List;

public class InventoryDialog extends AlertDialog implements InventoryPopupListAdapter.ItemCountChangeListener, NotifyListener {
    private final static String TAG = "InventoryDialog";
    private RecyclerView itemRecyclerView;
    private Button closeButton;
    private Button transferButton;
    private String title;
    private TextView titleText, itemName, itemType, itemColorView, warehouseQuantityView, displayQuantityView, stockItemsView, totalTransferView, countTitle;
    private RoundedCornerLayout colorImage;
    private ProductHeader item;
    private LinearLayout stockWarehouseLayout;
    private DatabaseSingleton db;
    private List<ProductVariant> productVariantList;
    private int totalCount = 0;
    private DialogDismissedListener listener;
    private Activity activity;
    private long outletId;

    @Override
    public void onNotify() {
        Toast.makeText(activity, "Items transferred successfully", Toast.LENGTH_SHORT).show();
    }

    public interface DialogDismissedListener {
        void onDialogDismissListener();
    }

    public InventoryDialog(Context context, FragmentActivity activity, ProductHeader item, String title) {
        super(context);

        this.activity = activity;
        this.item = item;
        this.title = title;
        db = DatabaseHelper.getDatabase(context);

        List<Outlet> outletList = db.outletDao().getAll();

        if (outletList.size() > 0)
            outletId = outletList.get(0).getId();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inventory_dialog);

        //setting views
        titleText = findViewById(R.id.titleText);
        itemName = findViewById(R.id.itemName);
        itemType = findViewById(R.id.itemTypeView);
        colorImage = findViewById(R.id.colorImage);
        itemColorView = findViewById(R.id.itemColorView);
        warehouseQuantityView = findViewById(R.id.warehouseQuantityView);
        displayQuantityView = findViewById(R.id.displayQuantityView);
        stockWarehouseLayout = findViewById(R.id.stockWarehouseLayout);
        stockItemsView = findViewById(R.id.stockItemsView);
        totalTransferView = findViewById(R.id.totalTransferView);
        transferButton = findViewById(R.id.transferButton);
        closeButton = findViewById(R.id.closeButton);
        itemRecyclerView = findViewById(R.id.itemListRecyclerView);
        countTitle = findViewById(R.id.countTitle);

        //initializing entities
        InventoryPopupListAdapter inventoryPopupListAdapter = new InventoryPopupListAdapter(getContext(), activity, item, title);
        productVariantList = db.productVariantDao().getProductVariantsById(item.getId());
        itemRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        itemRecyclerView.setAdapter(inventoryPopupListAdapter);
        inventoryPopupListAdapter.setOnItemCountChangeListener(this);

        titleText.setText(title);
        transferButton.setText(title);
        countTitle.setText(title);

        //calculating total warehouse and display stock
        int totalStock = 0;
        int totalDisplayStock = 0;

        for (ProductVariant currentVariant : productVariantList) {
            List<Stock> stockList = db.stockDao().getStocksById(currentVariant.getId());

            Stock stock = null;
            if (stockList.size() > 0)
                stock = stockList.get(0);

            if (stock != null) {
                totalStock += stock.getWarehouse();
                totalDisplayStock += stock.getDisplay();
            }
        }

        stockItemsView.setText(totalStock + " in Stock");
        warehouseQuantityView.setText(String.valueOf(totalStock));
        displayQuantityView.setText(String.valueOf(totalDisplayStock));

        if (!title.equalsIgnoreCase("transfer")) {
            stockWarehouseLayout.setVisibility(View.GONE);
        } else {
            stockWarehouseLayout.setVisibility(View.VISIBLE);
        }

        //product type
        String type = item.getProductType();
        if (type == null || type.isEmpty() || type.equalsIgnoreCase("null")) {
            type = "";
        }
        itemType.setText(type);

        //name
        itemName.setText(item.getName());

        //color image and code
        String hexColor = item.getColorCode();

        if (hexColor.length() == 4) {
            String[] arrHexColor = hexColor.split("#");
            hexColor = "#" + arrHexColor[1] + arrHexColor[1];
        }
        colorImage.setBackgroundColor(Color.parseColor(hexColor));
        itemColorView.setText(item.getColor());

        transferButton.setOnClickListener(view -> {
            int quantity = Integer.parseInt(totalTransferView.getText().toString());

            if (quantity > 0) {
                if (!title.equalsIgnoreCase("transfer")) {

                    int productId = item.getId();
                    int outletId = db.outletDao().getAll().get(0).getId();
                    StockOrderAPI.getInstance(getContext()).indentRequestDetails(productId, quantity, outletId, this);
                } else {
                    List<ProductVariant> productVariantList = db.productVariantDao().getProductVariantsById(item.getId());

                    for (ProductVariant currentVariant : productVariantList) {
                        Stock stock = db.stockDao().getStocksById(currentVariant.getId()).get(0);
                        int transferOrderCount = currentVariant.getTransferOrderCount();
                        int warehouseStock = stock.getWarehouse();
                        int displayStock = stock.getDisplay();

                        stock.setDisplay(displayStock + transferOrderCount);
                        stock.setWarehouse(warehouseStock - transferOrderCount);

                        if (transferOrderCount > warehouseStock) {
                            Toast.makeText(getContext(), "Not enough items in warehouse", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        JSONObject stockJson = stock.toJson();
                        ProductAPI.getInstance(getContext()).updateStock(outletId, currentVariant.getId(), stockJson, db, this);

                        db.productVariantDao().updateTransferOrderCount(0, currentVariant.getId());
                        dismiss();
                    }
                }
            } else {
                String message = "No items ";

                if (title.equalsIgnoreCase("transfer")) {
                    message += "to transfer";
                } else {
                    message += "for order";
                }

                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            }
        });

        closeButton.setOnClickListener(view -> {
            clearTransferOrderCount();
            dismiss();
        });
    }

    /**
     * Overridden method to notify changes on dialog dismiss
     */
    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        clearTransferOrderCount();

        if (listener != null) {
            listener.onDialogDismissListener();
        }
    }

    /**
     * Method to update total count
     *
     * @param count Count to be added (+1 for adding, -1 for subtracting)
     */
    @Override
    public void onItemChangeListener(int count, int isDone) {
        if (count < 0 && totalCount == 0)
            return;

        totalCount += count;
        totalTransferView.setText(String.valueOf(totalCount));
    }

    /**
     * Method to make transfer order count 0
     */
    private void clearTransferOrderCount() {
        List<ProductVariant> productVariantList = db.productVariantDao().getProductVariantsById(item.getId());

        for (ProductVariant currentVariant : productVariantList) {
            db.productVariantDao().updateTransferOrderCount(0, currentVariant.getId());
        }
    }

    /**
     * Method to set dialog dismiss listener
     */
    public void setOnDialogDismissListener(DialogDismissedListener listener) {
        this.listener = listener;
    }
}
