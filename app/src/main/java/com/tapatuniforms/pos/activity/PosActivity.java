package com.tapatuniforms.pos.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
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
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.tapatuniforms.pos.R;
import com.tapatuniforms.pos.adapter.NotificationsAdapter;
import com.tapatuniforms.pos.fragment.DashboardFragment;
import com.tapatuniforms.pos.fragment.DayClosingFragment;
import com.tapatuniforms.pos.fragment.InventoryFragment;
import com.tapatuniforms.pos.fragment.POSFragment;
import com.tapatuniforms.pos.fragment.StockEntryFragment;
import com.tapatuniforms.pos.helper.NetworkChangeReceiver;
import com.tapatuniforms.pos.helper.RecyclerDivider;
import com.tapatuniforms.pos.model.NotificationItem;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class PosActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    private static final String TAG = "PosActivity";
    private DrawerLayout mDrawerLayout;
    private ImageView hamburgerMenuIcon;
    private ImageView detailIcon;
    private ImageView calendarIcon;
    private ImageView homeIcon;
    private ImageView bagNavIcon;
    private NetworkChangeReceiver receiver;
    long lastBackPress;
    private RecyclerView notificationsRecycler;
    private CardView notificationsCardLayout;
    private TextView notificationCountText;
    private TextView noNotificationText;
    private NotificationsAdapter notificationsAdapter;
    private ArrayList<NotificationItem> notificationItems;
    private ConstraintLayout rootLayout;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pos);

        rootLayout = findViewById(R.id.rootLayout);
        mDrawerLayout = findViewById(R.id.drawerLayout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        hamburgerMenuIcon = findViewById(R.id.hamburgerMenuIcon);
        detailIcon = findViewById(R.id.detailNavIcon);
        calendarIcon = findViewById(R.id.calenderNavIcon);
        homeIcon = findViewById(R.id.homeNavIcon);
        bagNavIcon = findViewById(R.id.bagNavIcon);
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

        bagNavIcon.setOnClickListener(v -> showNotificationDialog());
        registerBroadcastReceiver();
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

        notificationsCardLayout = view.findViewById(R.id.notificationsCardLayout);
        notificationCountText = view.findViewById(R.id.notificationCountText);
        noNotificationText = view.findViewById(R.id.noNotificationText);
        notificationsRecycler = view.findViewById(R.id.notificationsRecycler);

        notificationsRecycler.addItemDecoration(new RecyclerDivider(this, RecyclerDivider.VERTICAL));

        notificationItems = new ArrayList<>();
        notificationsAdapter = new NotificationsAdapter(this, notificationItems);
        notificationsRecycler.setLayoutManager(new LinearLayoutManager(this));
        notificationsRecycler.setAdapter(notificationsAdapter);

        getNotifications();
        updateNotificationsCount();

        dialog.show();
    }

    private void updateNotificationsCount() {
        if (notificationItems != null && notificationCountText != null && notificationsCardLayout != null && notificationsRecycler != null && noNotificationText != null) {
            if (notificationItems.size() < 1) {
                notificationsRecycler.setVisibility(View.GONE);
                notificationsCardLayout.setVisibility(View.GONE);
                noNotificationText.setVisibility(View.VISIBLE);
            } else {
                noNotificationText.setVisibility(View.GONE);
                notificationsRecycler.setVisibility(View.VISIBLE);
                notificationsCardLayout.setVisibility(View.VISIBLE);

                notificationCountText.setText(notificationItems.size() + " Items");
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
            /*case R.id.orderScreen:
                fragment = new OrderFragment();
                break;*/
            /*case R.id.saleScreen:
                fragment = new SaleReportFragment();
                break;*/
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
}
