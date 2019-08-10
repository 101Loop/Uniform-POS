package com.tapatuniforms.pos.activity;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.navigation.NavigationView;
import com.tapatuniforms.pos.R;
import com.tapatuniforms.pos.fragment.DashboardFragment;
import com.tapatuniforms.pos.fragment.DayClosingFragment;
import com.tapatuniforms.pos.fragment.InventoryFragment;
import com.tapatuniforms.pos.fragment.POSFragment;
import com.tapatuniforms.pos.fragment.StockEntryFragment;
import com.tapatuniforms.pos.helper.NetworkChangeReceiver;

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
    private NetworkChangeReceiver receiver;

    long lastBackPress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pos);

        mDrawerLayout = findViewById(R.id.drawerLayout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        hamburgerMenuIcon = findViewById(R.id.hamburgerMenuIcon);
        detailIcon = findViewById(R.id.detailNavIcon);
        calendarIcon = findViewById(R.id.calenderNavIcon);
        homeIcon = findViewById(R.id.homeNavIcon);
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

        registerBroadcastReceiver();
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
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, new DashboardFragment()).commit();
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
