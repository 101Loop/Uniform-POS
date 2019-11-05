package com.tapatuniforms.pos.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.tapatuniforms.pos.R;
import com.tapatuniforms.pos.adapter.BagItemsAdapter;
import com.tapatuniforms.pos.adapter.NotificationsAdapter;
import com.tapatuniforms.pos.fragment.DashboardFragment;
import com.tapatuniforms.pos.fragment.DayClosingFragment;
import com.tapatuniforms.pos.fragment.InventoryFragment;
import com.tapatuniforms.pos.fragment.OrderFragment;
import com.tapatuniforms.pos.fragment.POSFragment;
import com.tapatuniforms.pos.fragment.SaleReportFragment;
import com.tapatuniforms.pos.fragment.StockEntryFragment;
import com.tapatuniforms.pos.helper.DatabaseHelper;
import com.tapatuniforms.pos.helper.DatabaseSingleton;
import com.tapatuniforms.pos.helper.NetworkChangeReceiver;
import com.tapatuniforms.pos.helper.NotifyListener;
import com.tapatuniforms.pos.helper.RecyclerDivider;
import com.tapatuniforms.pos.model.CartItem;
import com.tapatuniforms.pos.model.NotificationItem;
import com.tapatuniforms.pos.model.Outlet;
import com.tapatuniforms.pos.model.ProductHeader;
import com.tapatuniforms.pos.model.School;
import com.tapatuniforms.pos.network.ProductAPI;
import com.tapatuniforms.pos.network.SchoolAPI;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class PosActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, NotifyListener {
    private static final String TAG = "PosActivity";
    private DrawerLayout mDrawerLayout;
    private ImageView hamburgerMenuIcon;
    private ImageView detailIcon;
    private ImageView calendarIcon;
    private ImageView homeIcon;
    private ImageView bagNavIcon;
    private ImageView notificationIcon;
    private NetworkChangeReceiver receiver;
    long lastBackPress;
    private RecyclerView notificationsRecycler;
    private RecyclerView bagItemsRecycler;
    private RecyclerView itemsRecycler;
    private CardView itemsCountLayout;
    private TextView itemsCountText;
    private TextView noNotificationText;
    private TextView noBagItemsText;
    private TextView noItemsText;
    private TextView schoolNameText;
    private BagItemsAdapter bagItemsAdapter;
    private List<CartItem> cartItemList;
    private NotificationsAdapter notificationsAdapter;
    private ArrayList<NotificationItem> notificationItems;
    private ConstraintLayout rootLayout;
    private ArrayList<School> schoolList;
    private ArrayList<Outlet> outletList;
    private DatabaseSingleton db;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pos);

        rootLayout = findViewById(R.id.rootLayout);
        mDrawerLayout = findViewById(R.id.drawerLayout);
        schoolNameText = findViewById(R.id.schoolNameText);
        NavigationView navigationView = findViewById(R.id.nav_view);
        hamburgerMenuIcon = findViewById(R.id.hamburgerMenuIcon);
        detailIcon = findViewById(R.id.detailNavIcon);
        calendarIcon = findViewById(R.id.calenderNavIcon);
        homeIcon = findViewById(R.id.homeNavIcon);
        bagNavIcon = findViewById(R.id.bagNavIcon);
        notificationIcon = findViewById(R.id.notificationNavIcon);
        receiver = new NetworkChangeReceiver();

        hamburgerMenuIcon.setOnClickListener(this);

        //set home fragment on create of the activity
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, new DashboardFragment()).commit();

        navigationView.setNavigationItemSelectedListener(this);
        calendarIcon.setOnClickListener(v -> {
            if (!(getSupportFragmentManager().findFragmentById(R.id.fragmentContainer) instanceof DayClosingFragment)) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new DayClosingFragment()).commit();
            }
        });

        homeIcon.setOnClickListener(v -> {
            if (!(getSupportFragmentManager().findFragmentById(R.id.fragmentContainer) instanceof DashboardFragment)) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new DashboardFragment()).commit();
            }
        });

        bagNavIcon.setOnClickListener(view -> showBagItemsDialog());

        notificationIcon.setOnClickListener(v -> showNotificationDialog());

        detailIcon.setOnClickListener(view -> showInfoDialog());

        db = DatabaseHelper.getDatabase(this);
        schoolList = new ArrayList<>();
        SchoolAPI.getInstance(this).getSchool(schoolList, db);
        SchoolAPI.getInstance(this).setNotifyListener(this);

        outletList = new ArrayList<>();
        ProductAPI.getInstance(this).getOutletList(outletList, db);

        registerBroadcastReceiver();
    }

    private void showBagItemsDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        AlertDialog dialog = alertDialog.create();

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
            params.gravity = Gravity.TOP | Gravity.END;
            params.y = 70;
        }

        View view = LayoutInflater.from(this).inflate(R.layout.bag_items_layout, null);
        dialog.setView(view);

        itemsCountLayout = view.findViewById(R.id.itemsCountLayout);
        itemsCountText = view.findViewById(R.id.itemsCountText);
        noBagItemsText = view.findViewById(R.id.noBagItemsText);
        bagItemsRecycler = view.findViewById(R.id.bagItemsRecycler);

        bagItemsRecycler.addItemDecoration(new RecyclerDivider(this, RecyclerDivider.VERTICAL));

        cartItemList = new ArrayList<>();
        bagItemsAdapter = new BagItemsAdapter(this, cartItemList);
        bagItemsRecycler.setLayoutManager(new LinearLayoutManager(this));
        bagItemsRecycler.setAdapter(bagItemsAdapter);

        getCartItems();
        updateItemsCount(cartItemList.size(), bagItemsRecycler, noBagItemsText, itemsCountLayout, itemsCountText);

        dialog.show();
    }

    private void getCartItems() {
        ProductHeader product = db.productHeaderDao().getAllProductHeader().get(0);
        cartItemList.clear();
        for (int i = 0; i < 2; i++) {
            CartItem cartItem = new CartItem(i, 10 + i, product, String.valueOf(20 + (i * i)), 1000 * (i + 1));
            cartItemList.add(cartItem);
        }
        bagItemsAdapter.notifyDataSetChanged();
    }

    private void showNotificationDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        AlertDialog dialog = alertDialog.create();

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
            params.gravity = Gravity.TOP | Gravity.END;
            params.y = 70;
        }

        View view = LayoutInflater.from(this).inflate(R.layout.notifications_layout, null);
        dialog.setView(view);

        noNotificationText = view.findViewById(R.id.noNotificationText);
        notificationsRecycler = view.findViewById(R.id.notificationsRecycler);

        notificationsRecycler.addItemDecoration(new RecyclerDivider(this, RecyclerDivider.VERTICAL));

        notificationItems = new ArrayList<>();
        notificationsAdapter = new NotificationsAdapter(this, notificationItems);
        notificationsRecycler.setLayoutManager(new LinearLayoutManager(this));
        notificationsRecycler.setAdapter(notificationsAdapter);

        getNotifications();
        updateItemsCount(notificationItems.size(), bagItemsRecycler, noNotificationText, null, null);

        dialog.show();
    }

    private void showInfoDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        AlertDialog dialog = alertDialog.create();

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
            params.gravity = Gravity.TOP | Gravity.END;
            params.y = 70;
        }

        View view = LayoutInflater.from(this).inflate(R.layout.details_item_layout, null);
        dialog.setView(view);

        noItemsText = view.findViewById(R.id.noItemsText);
        itemsRecycler = view.findViewById(R.id.itemsRecycler);

        itemsRecycler.addItemDecoration(new RecyclerDivider(this, RecyclerDivider.VERTICAL));

        notificationItems = new ArrayList<>();
        notificationsAdapter = new NotificationsAdapter(this, notificationItems);
        itemsRecycler.setLayoutManager(new LinearLayoutManager(this));
        itemsRecycler.setAdapter(notificationsAdapter);

        getNotifications();
        updateItemsCount(notificationItems.size(), bagItemsRecycler, noItemsText, null, null);

        dialog.show();
    }

    private void updateItemsCount(int size, RecyclerView recyclerView, TextView noItemsText, CardView itemsCountLayout, TextView itemsCountText) {
        if (recyclerView != null && noItemsText != null) {
            if (size < 1) {
                recyclerView.setVisibility(View.GONE);
                noItemsText.setVisibility(View.VISIBLE);
            } else {
                noItemsText.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            }
            if (itemsCountLayout != null && itemsCountText != null) {
                itemsCountText.setText(size + " Items");

                if (size < 1) {
                    itemsCountLayout.setVisibility(View.GONE);
                } else {
                    itemsCountLayout.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    private void getNotifications() {
        //TODO: fetch notifications from server

        notificationItems.clear();
        for (int i = 0; i < 3; i++) {
            NotificationItem notificationItem = new NotificationItem("Heading", "Some content of the notification");
            notificationItems.add(notificationItem);
        }
        notificationsAdapter.notifyDataSetChanged();
    }

    /**
     * registering a broadcast receiver to listen any connectivity change
     */
    private void registerBroadcastReceiver() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            registerReceiver(receiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
    }

    /**
     * replacing fragment on navigation selection
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        // Add code here to update the UI based on the item selected
        // For example, swap UI fragments here
        Fragment fragment = null;
        FragmentManager fragmentManager = getSupportFragmentManager();

        switch (menuItem.getItemId()) {
            case R.id.dashboardScreen:
                fragment = new DashboardFragment();
                break;
            case R.id.posScreen:
                fragment = new POSFragment();
                break;
            case R.id.orderScreen:
                fragment = new OrderFragment();
                break;
            case R.id.saleScreen:
                fragment = new SaleReportFragment();
                break;
            case R.id.stockScreen:
                fragment = new StockEntryFragment();
                break;
            case R.id.inventoryScreen:
                fragment = new InventoryFragment();
                break;
        }

        if (fragment != null) fragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, fragment).commit();

        // set item as selected to persist highlight
        menuItem.setChecked(true);
        mDrawerLayout.closeDrawers();
        return true;
    }

    @Override
    public void onBackPressed() {
        Date cTime = Calendar.getInstance().getTime();
        long currentTime = cTime.getTime();

        if (currentTime - lastBackPress <= 2000) {
            finish();
            return;
        } else {
            if (!(getSupportFragmentManager().findFragmentById(R.id.fragmentContainer) instanceof DashboardFragment)) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new DashboardFragment()).commit();
            }

            Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT).show();
        }
        lastBackPress = currentTime;
    }

    /**
     * opens navigation drawer on clicking hamburger icon
     */
    @Override
    public void onClick(View v) {
        mDrawerLayout.openDrawer(GravityCompat.START);
    }

    /**
     * unregistering broadcast receiver
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();

        unregisterReceiver(receiver);
    }

    @Override
    public void onNotify() {
        if (schoolList.size() > 0) {
            School school = schoolList.get(0);
            String schoolName = school.getName() + " - " + school.getCity() + ", " + school.getState();
            schoolNameText.setText(schoolName);
        }
    }
}
