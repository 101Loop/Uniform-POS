package com.tapatuniforms.pos.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.tapatuniforms.pos.R;
import com.tapatuniforms.pos.adapter.ActivitiesAdapter;
import com.tapatuniforms.pos.adapter.DashboardAdapter;
import com.tapatuniforms.pos.adapter.IndexAdapter;
import com.tapatuniforms.pos.helper.DatabaseHelper;
import com.tapatuniforms.pos.helper.DatabaseSingleton;
import com.tapatuniforms.pos.helper.GridItemDecoration;
import com.tapatuniforms.pos.model.Activities;
import com.tapatuniforms.pos.model.DashboardItem;
import com.tapatuniforms.pos.model.PieChartItem;
import com.tapatuniforms.pos.model.ProductHeader;
import com.tapatuniforms.pos.network.ProductAPI;

import java.util.ArrayList;
import java.util.Random;

public class DashboardFragment extends Fragment implements DashboardAdapter.OnClickListener, OnChartValueSelectedListener {
    private RecyclerView dashRecycler;
    private DashboardAdapter dashAdapter;
    private DatabaseSingleton db;
    private ArrayList<ProductHeader> productList;
    private ArrayList<ProductHeader> allProductList;
    private PieChart pieChart;
    private BarChart barChart;
    private ArrayList<PieEntry> entries;
    private ArrayList<PieChartItem> pieChartItems;
    private RecyclerView activitiesRecycler;
    private ActivitiesAdapter activitiesAdapter;
    private ArrayList<Activities> activitiesList;
    private IndexAdapter indexAdapter;
    private ArrayList<Integer> indexList;
    private RecyclerView indexRecycler;

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
        dashRecycler.setLayoutManager(new GridLayoutManager(getContext(), 5));
        dashRecycler.addItemDecoration(new GridItemDecoration(8, 8));
        dashAdapter = new DashboardAdapter(getDashboardItems());
        dashRecycler.setAdapter(dashAdapter);
        dashAdapter.setOnClickListener(this);

        activitiesList = new ArrayList<>();
        activitiesRecycler = v.findViewById(R.id.activitiesRecycler);
        activitiesRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        activitiesAdapter = new ActivitiesAdapter(getContext(), activitiesList);
        activitiesRecycler.setAdapter(activitiesAdapter);
        getActivities();

        indexList = new ArrayList<>();
        indexRecycler = v.findViewById(R.id.indexRecycler);
        indexRecycler.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        indexAdapter = new IndexAdapter(getContext(), indexList);
        indexRecycler.setAdapter(indexAdapter);
        getIndexes();

        entries = new ArrayList<>();
        pieChartItems = new ArrayList<>();
        getEntries();

        pieChart = v.findViewById(R.id.pieChart);
        barChart = v.findViewById(R.id.barChart);

        initPieChart();
        initBarChart();

        db = DatabaseHelper.getDatabase(getContext());
        productList = new ArrayList<>();
        allProductList = new ArrayList<>();

        ProductAPI.fetchProducts(getContext(), allProductList, productList, null, null, db, null, null);
    }

    private void getIndexes() {
        //TODO: generate indexes on the basis of activities count

        indexList.clear();
        for (int i = 1; i < 11; i++) {
            indexList.add(i);
        }
        indexAdapter.notifyDataSetChanged();
    }

    private void getActivities() {
        Activities activities = new Activities("Sold Out", "Dipanshu", "13251", "14/10/2019", "Faridabad", "XYZ", 10);

        activitiesList.clear();
        for (int i = 0; i < 3; i++) {
            activitiesList.add(activities);
        }
        activitiesAdapter.notifyDataSetChanged();
    }

    private void initBarChart() {
        barChart.setDrawBarShadow(false);
        barChart.setDrawValueAboveBar(true);
        barChart.getDescription().setEnabled(false);

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        barChart.setMaxVisibleValueCount(65);

        // scaling can now only be done on x- and y-axis separately
        barChart.setPinchZoom(true);

        barChart.setDrawGridBackground(false);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(5f);
        xAxis.setLabelCount(5);

        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setLabelCount(4, false);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        YAxis rightAxis = barChart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setLabelCount(0, true);
        rightAxis.setSpaceTop(15f);
        rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        Legend l1 = barChart.getLegend();
        l1.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l1.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l1.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l1.setDrawInside(false);
        l1.setForm(Legend.LegendForm.SQUARE);
        l1.setFormSize(9f);
        l1.setTextSize(11f);
        l1.setXEntrySpace(4f);

        setBarChartData(20, 100);
    }

    private void initPieChart() {
        pieChart.setUsePercentValues(true);
        pieChart.setCenterText("Total Sale");
        pieChart.animateY(1400, Easing.EaseInOutQuad);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5, 10, 5, 5);

        pieChart.setDragDecelerationFrictionCoef(0.95f);

        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.WHITE);

        pieChart.setTransparentCircleColor(Color.WHITE);
        pieChart.setTransparentCircleAlpha(110);

        pieChart.setHoleRadius(58f);
        pieChart.setTransparentCircleRadius(61f);

        pieChart.setDrawCenterText(true);

        pieChart.setRotationAngle(0);
        pieChart.setRotationEnabled(true);
        pieChart.setHighlightPerTapEnabled(true);

        pieChart.setOnChartValueSelectedListener(this);

        Legend l = pieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);

        // entry label styling
        pieChart.setEntryLabelColor(Color.WHITE);
        pieChart.setEntryLabelTextSize(12f);

        setData();
    }

    private ArrayList<DashboardItem> getDashboardItems() {
        ArrayList<DashboardItem> itemList = new ArrayList<>();

        itemList.add(new DashboardItem(R.drawable.ic_bill, "Billing", R.id.posScreen));
        itemList.add(new DashboardItem(R.drawable.ic_order_history, "Order History", R.id.orderScreen));
        itemList.add(new DashboardItem(R.drawable.ic_diagram, "Reports", R.id.saleScreen));
        itemList.add(new DashboardItem(R.drawable.ic_inventory, "Stock Entry", R.id.stockScreen));
        itemList.add(new DashboardItem(R.drawable.ic_storage, "Inventory", R.id.inventoryScreen));

        return itemList;
    }

    @Override
    public void onItemClicked(DashboardItem dashItem) {
        Fragment fragment = null;
        FragmentManager fragmentManager = getFragmentManager();

        switch (dashItem.getItemId()) {
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

        if (fragment != null) {
            assert fragmentManager != null;
            fragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, fragment).commit();
        }
    }

    private void setData() {
        PieDataSet dataSet = new PieDataSet(entries, "Total Sale");

        dataSet.setSelectionShift(10f);
        dataSet.setSliceSpace(5f);

        ArrayList<Integer> colors = new ArrayList<>();

        for (PieChartItem pieChartItem : pieChartItems) {
            colors.add(Color.parseColor(pieChartItem.getColor()));
        }

        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        data.setDrawValues(false);
        pieChart.setData(data);

        // undo all highlights
        pieChart.highlightValues(null);

        pieChart.invalidate();
    }

    private void setBarChartData(int count, int range) {
        float groupSpace = 0.08f;
        float barSpace = 0.03f; // x4 DataSet
        float barWidth = 0.2f; // x4 DataSet
        // (0.2 + 0.03) * 4 + 0.08 = 1.00 -> interval per "group"

        int groupCount = 2;
        int startYear = 1980;
        int endYear = startYear + groupCount;

        float start = 1f;

        ArrayList<BarEntry> values1 = new ArrayList<>();
        ArrayList<BarEntry> values2 = new ArrayList<>();
        ArrayList<BarEntry> values3 = new ArrayList<>();

        for (int i = (int) start; i < start + count; i++) {
            float val1 = (float) (Math.random() * (range + 1));
            float val2 = (float) (Math.random() * (range + 1));

            values1.add(new BarEntry(i, val1));
            values2.add(new BarEntry(i, val2));
            values3.add(new BarEntry(i, val2));
        }

        BarDataSet set1, set2, set3;

        if (barChart.getData() != null &&
                barChart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) barChart.getData().getDataSetByIndex(0);
            set2 = (BarDataSet) barChart.getData().getDataSetByIndex(1);
            set3 = (BarDataSet) barChart.getData().getDataSetByIndex(1);
            set1.setValues(values1);
            set2.setValues(values2);
            set3.setValues(values3);

            barChart.getData().notifyDataChanged();
            barChart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(values1, "Ordered");
            set1.setDrawIcons(true);

            set2 = new BarDataSet(values2, "Received");
            set2.setDrawIcons(false);

            set3 = new BarDataSet(values3, "Not Received");
            set3.setDrawIcons(false);

            ArrayList<Integer> colors1 = new ArrayList<>();
            ArrayList<Integer> colors2 = new ArrayList<>();
            ArrayList<Integer> colors3 = new ArrayList<>();

            Context context = getContext();
            if (context != null) {
                colors1.add(Color.parseColor("#ffb997"));
                colors2.add(Color.parseColor("#53d397"));
                colors3.add(Color.parseColor("#e26241"));
            }

            set1.setColors(colors1);
            set2.setColors(colors2);
            set3.setColors(colors3);

            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);
            dataSets.add(set2);
            dataSets.add(set3);

            BarData data = new BarData(dataSets);
            data.setValueFormatter(new LargeValueFormatter());
            data.setDrawValues(false);
            data.setValueTextSize(10f);
            data.setBarWidth(0.5f);

            Legend l = barChart.getLegend();
            l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
            l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
            l.setOrientation(Legend.LegendOrientation.VERTICAL);
            l.setDrawInside(false);
            l.setXEntrySpace(7f);
            l.setYEntrySpace(0f);
            l.setYOffset(0f);

            barChart.setHighlightFullBarEnabled(false);
            barChart.setData(data);
        }
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        pieChart.setCenterTextSize(18f);

        pieChart.setCenterText(e.getY() + "%");
    }

    @Override
    public void onNothingSelected() {
    }

    private void getEntries() {
        //TODO: fetch entries from the server

        PieChartItem pieChartItem1 = new PieChartItem(25, "Stock left", "#49B5E8");
        PieChartItem pieChartItem2 = new PieChartItem(20, "Sold Out", "#32AD59");
        PieChartItem pieChartItem3 = new PieChartItem(40, "Not Sold", "#f35352");
        PieChartItem pieChartItem4 = new PieChartItem(15, "Return", "#F7C21C");

        pieChartItems.add(pieChartItem1);
        pieChartItems.add(pieChartItem2);
        pieChartItems.add(pieChartItem3);
        pieChartItems.add(pieChartItem4);

        for (PieChartItem pieChartItem : pieChartItems) {
            entries.add(pieChartItem.getPieEntry());
        }
    }
}
