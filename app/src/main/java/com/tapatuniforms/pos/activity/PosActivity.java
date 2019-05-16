package com.tapatuniforms.pos.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.tapatuniforms.pos.R;
import com.tapatuniforms.pos.fragment.DashboardFragment;
import com.tapatuniforms.pos.fragment.InventoryFragment;
import com.tapatuniforms.pos.fragment.OrderFragment;
import com.tapatuniforms.pos.fragment.POSFragment;
import com.tapatuniforms.pos.fragment.SaleReportFragment;
import com.tapatuniforms.pos.fragment.StockEntryFragment;

public class PosActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "PosActivity";

    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pos);

        mDrawerLayout = findViewById(R.id.drawerLayout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        //set home fragment on create of the activity
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, new DashboardFragment()).commit();

        navigationView.setNavigationItemSelectedListener(this);
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

}
