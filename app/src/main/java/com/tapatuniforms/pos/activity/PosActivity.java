package com.tapatuniforms.pos.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.navigation.NavigationView;
import com.tapatuniforms.pos.R;
import com.tapatuniforms.pos.fragment.DashboardFragment;
import com.tapatuniforms.pos.fragment.InventoryFragment;
import com.tapatuniforms.pos.fragment.OrderFragment;
import com.tapatuniforms.pos.fragment.POSFragment;
import com.tapatuniforms.pos.fragment.SaleReportFragment;
import com.tapatuniforms.pos.fragment.StockEntryFragment;

public class PosActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    private static final String TAG = "PosActivity";

    private DrawerLayout mDrawerLayout;
    private ImageView hamburgerMenuIcon;
    private TextView closeDayView, screenNameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pos);

        mDrawerLayout = findViewById(R.id.drawerLayout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        hamburgerMenuIcon = findViewById(R.id.hamburgerMenuIcon);
        closeDayView = findViewById(R.id.closeDayView);
        screenNameView = findViewById(R.id.screenNameView);
        hamburgerMenuIcon.setOnClickListener(this);

        screenNameView.setText(getString(R.string.dashboard));
        //set home fragment on create of the activity
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, new DashboardFragment()).commit();

        navigationView.setNavigationItemSelectedListener(this);
        closeDayView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), DayClosingActivity.class));
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        // Add code here to update the UI based on the item selected
        // For example, swap UI fragments here
        Fragment fragment = null;
        FragmentManager fragmentManager = getSupportFragmentManager();

        switch (menuItem.getItemId()){
            case R.id.dashboardScreen:
                fragment = new DashboardFragment();
                screenNameView.setText(getString(R.string.dashboard));
                break;
            case R.id.posScreen:
                fragment = new POSFragment();
                screenNameView.setText(getString(R.string.billingScreen));
                break;
            case R.id.orderScreen:
                fragment = new OrderFragment();
                screenNameView.setText(getString(R.string.orderHistory));
                break;
            case R.id.saleScreen:
                fragment = new SaleReportFragment();
                screenNameView.setText(getString(R.string.saleReport));
                break;
            case R.id.stockScreen:
                fragment = new StockEntryFragment();
                screenNameView.setText(getString(R.string.stockScreen));
                break;
            case R.id.inventoryScreen:
                fragment = new InventoryFragment();
                screenNameView.setText(getString(R.string.inventory));
                break;
            case R.id.settingsScreen:
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
    public void onClick(View v) {
        mDrawerLayout.openDrawer(GravityCompat.START);
    }
}
