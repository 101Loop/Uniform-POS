package com.tapatuniforms.pos.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tapatuniforms.pos.R;
import com.tapatuniforms.pos.activity.PosActivity;
import com.tapatuniforms.pos.adapter.DashboardAdapter;
import com.tapatuniforms.pos.helper.GridItemDecoration;
import com.tapatuniforms.pos.model.DashboardItem;

import java.util.ArrayList;

public class DashboardFragment extends Fragment implements DashboardAdapter.OnClickListener {
    private RecyclerView dashRecycler;
    private DashboardAdapter dashAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        init(view);

        return view;
    }

    private void init(View v) {
        dashRecycler = v.findViewById(R.id.dashRecycler);
        dashRecycler.setLayoutManager(new GridLayoutManager(getContext(), 3));
        dashRecycler.addItemDecoration(new GridItemDecoration(8, 8));
        dashAdapter = new DashboardAdapter(getDashboardItems());
        dashRecycler.setAdapter(dashAdapter);
        dashAdapter.setOnClickListener(this);
    }

    private ArrayList<DashboardItem> getDashboardItems() {
        ArrayList<DashboardItem> itemList = new ArrayList<>();

        itemList.add(new DashboardItem(R.drawable.ic_bill, "Billing Screen", R.id.posScreen));
        itemList.add(new DashboardItem(R.drawable.ic_box, "Order History", R.id.orderScreen));
        itemList.add(new DashboardItem(R.drawable.ic_inventory, "Stock Input", 0));
        itemList.add(new DashboardItem(R.drawable.ic_diagram, "Order History", R.id.saleScreen));
        itemList.add(new DashboardItem(R.drawable.ic_storage, "Inventory Management",0));

        return itemList;
    }

    @Override
    public void onItemClicked(DashboardItem dashItem) {
        Fragment fragment = null;
        FragmentManager fragmentManager = getFragmentManager();

        switch (dashItem.getItemId()){
            case R.id.posScreen:
                fragment = new POSFragment();
                break;
            case R.id.orderScreen:
                fragment = new OrderFragment();
                break;
            case R.id.saleScreen:
                fragment = new SaleReportFragment();
                break;
            case R.id.settingsScreen:
                break;
        }

        if (fragment != null) {
            assert fragmentManager != null;
            fragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, fragment).commit();
        }
    }
}
