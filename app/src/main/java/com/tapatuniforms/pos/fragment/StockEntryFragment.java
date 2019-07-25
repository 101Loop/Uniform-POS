package com.tapatuniforms.pos.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tapatuniforms.pos.R;
import com.tapatuniforms.pos.adapter.StockBoxAdapter;
import com.tapatuniforms.pos.adapter.StockIndentAdapter;
import com.tapatuniforms.pos.dialog.StockItemDialog;
import com.tapatuniforms.pos.helper.GridItemDecoration;
import com.tapatuniforms.pos.model.Box;
import com.tapatuniforms.pos.model.CartItem;
import com.tapatuniforms.pos.model.Indent;
import com.tapatuniforms.pos.model.Product;

import java.util.ArrayList;
import java.util.Objects;

public class StockEntryFragment extends Fragment implements StockBoxAdapter.OnBoxClickListener, StockIndentAdapter.OnIndentClickListener {
    private static final String TAG = "StockEntryFragment";
    private RecyclerView indentRecyclerView, requestRecyclerView;
    private Button requestButton;
    private ArrayList<CartItem> requestList;
    private ArrayList<Product> productList;
    private StockBoxAdapter stockAdapter;
    private ArrayList<Box> boxList;
    private LinearLayout noIndentsLayout, noBoxLayout;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_stock_entry, container, false);

        init(v);

        return v;
    }

    private void init(View v) {
        indentRecyclerView = v.findViewById(R.id.indentRecyclerView);
        requestRecyclerView = v.findViewById(R.id.itemRequestRecyclerView);
        noIndentsLayout = v.findViewById(R.id.noIndentsLayout);
        noBoxLayout = v.findViewById(R.id.noBoxLayout);
//        requestButton = v.findViewById(R.id.raiseRequestButton);

        StockIndentAdapter adapter = new StockIndentAdapter(getContext(), getIndentList());
        checkIndentsAvailability();
        indentRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        indentRecyclerView.addItemDecoration(new GridItemDecoration(8, 8));
        indentRecyclerView.setAdapter(adapter);
        adapter.setOnIndentClickListener(this);

        boxList = new ArrayList<>();
        boxList.add(getBoxList().get(0));
        checkBoxAvailability();
        requestRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        stockAdapter = new StockBoxAdapter(boxList);
        requestRecyclerView.setAdapter(stockAdapter);
        stockAdapter.setOnBoxClickListener(this);
    }

    @Override
    public void onBoxSelected() {
        StockItemDialog dialog = new StockItemDialog(getContext());
        dialog.show();

        Objects.requireNonNull(dialog.getWindow()).clearFlags(
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    private ArrayList<Indent> getIndentList() {
        ArrayList<Indent> list = new ArrayList<>();

        list.add(new Indent(1, "Indent 1", "20/May/2019, 06:44 PM", "Vivek Kumar",
                5, 2000, 260, getBoxList().get(0), "Delhi"));
        list.add(new Indent(2, "Indent 2", "20/May/2019, 06:44 PM", "Vivek Kumar",
                9, 5200, 350, getBoxList().get(1), "Faridabad"));
        list.add(new Indent(3, "Indent 3", "20/May/2019, 06:44 PM", "Vivek Kumar",
                8, 4700, 300, getBoxList().get(2), "kanpur"));
        list.add(new Indent(4, "Indent 4", "20/May/2019, 06:44 PM", "Vivek Kumar",
                5, 2400, 400, getBoxList().get(3), "New York"));
        list.add(new Indent(5, "Indent 5", "20/May/2019, 06:44 PM", "Vivek Kumar",
                9, 5200, 350, getBoxList().get(4), "Noida"));
        list.add(new Indent(6, "Indent 6", "20/May/2019, 06:44 PM", "Vivek Kumar",
                8, 4700, 300, getBoxList().get(5), "Haryana"));
        list.add(new Indent(7, "Indent 7", "20/May/2019, 06:44 PM", "Vivek Kumar",
                5, 2400, 400, getBoxList().get(6), "Kurukshetra"));
        list.add(new Indent(8, "Indent 8", "20/May/2019, 06:44 PM", "Vivek Kumar",
                5, 2000, 260, getBoxList().get(7), "Panipat"));
        list.add(new Indent(9, "Indent 9", "20/May/2019, 06:44 PM", "Vivek Kumar",
                9, 5200, 350, getBoxList().get(8), "Shamli"));
        list.add(new Indent(10, "Indent 10", "20/May/2019, 06:44 PM", "Vivek Kumar",
                8, 4700, 300, getBoxList().get(9), "Meerut"));
        list.add(new Indent(11, "Indent 11", "20/May/2019, 06:44 PM", "Vivek Kumar",
                5, 2400, 400, getBoxList().get(10), "Dehradun"));
        list.add(new Indent(12, "Indent 12", "20/May/2019, 06:44 PM", "Vivek Kumar",
                9, 5200, 350, getBoxList().get(11), "Shimla"));
        list.add(new Indent(13, "Indent 13", "20/May/2019, 06:44 PM", "Vivek Kumar",
                8, 4700, 300, getBoxList().get(12), "Mumbai"));
        list.add(new Indent(14, "Indent 14", "20/May/2019, 06:44 PM", "Vivek Kumar",
                5, 2400, 400, getBoxList().get(13), "Goa"));
        list.add(new Indent(15, "Indent 15", "20/May/2019, 06:44 PM", "Vivek Kumar",
                5, 2000, 260, getBoxList().get(14), "Pune"));
        list.add(new Indent(16, "Indent 16", "20/May/2019, 06:44 PM", "Vivek Kumar",
                9, 5200, 350, getBoxList().get(15), "Maharashtra"));
        list.add(new Indent(17, "Indent 17", "20/May/2019, 06:44 PM", "Vivek Kumar",
                8, 4700, 300, getBoxList().get(16), "Gujrat"));
        list.add(new Indent(18, "Indent 18", "20/May/2019, 06:44 PM", "Vivek Kumar",
                5, 2400, 400, getBoxList().get(17), "Lucknow"));
        list.add(new Indent(19, "Indent 19", "20/May/2019, 06:44 PM", "Vivek Kumar",
                9, 5200, 350, getBoxList().get(18), "Delhi"));
        list.add(new Indent(20, "Indent 20", "20/May/2019, 06:44 PM", "Vivek Kumar",
                8, 4700, 300, getBoxList().get(19), "Faridabad"));
        return list;
    }

    private ArrayList<Box> getBoxList() {
        ArrayList<Box> list = new ArrayList<>();

        list.add(new Box(1, "Box 1", "1234456677", "10/May/2019 6:55 PM",
                300, 213));
        list.add(new Box(2, "Box 2", "1234456677", "10/May/2019 6:55 PM",
                300, 120));
        list.add(new Box(3, "Box 3", "1234456677", "10/May/2019 6:55 PM",
                300, 120));
        list.add(new Box(4, "Box 4", "1234456677", "10/May/2019 6:55 PM",
                300, 120));
        list.add(new Box(5, "Box 5", "1234456677", "10/May/2019 6:55 PM",
                300, 120));
        list.add(new Box(6, "Box 6", "1234456677", "10/May/2019 6:55 PM",
                300, 120));
        list.add(new Box(7, "Box 7", "1234456677", "10/May/2019 6:55 PM",
                300, 120));
        list.add(new Box(8, "Box 8", "1234456677", "10/May/2019 6:55 PM",
                300, 120));
        list.add(new Box(9, "Box 9", "1234456677", "10/May/2019 6:55 PM",
                300, 120));
        list.add(new Box(10, "Box 10", "1234456677", "10/May/2019 6:55 PM",
                300, 120));
        list.add(new Box(11, "Box 11", "1234456677", "10/May/2019 6:55 PM",
                300, 120));
        list.add(new Box(12, "Box 12", "1234456677", "10/May/2019 6:55 PM",
                300, 120));
        list.add(new Box(13, "Box 13", "1234456677", "10/May/2019 6:55 PM",
                300, 120));
        list.add(new Box(14, "Box 14", "1234456677", "10/May/2019 6:55 PM",
                300, 120));
        list.add(new Box(15, "Box 15", "1234456677", "10/May/2019 6:55 PM",
                300, 120));
        list.add(new Box(16, "Box 16", "1234456677", "10/May/2019 6:55 PM",
                300, 120));
        list.add(new Box(17, "Box 17", "1234456677", "10/May/2019 6:55 PM",
                300, 120));
        list.add(new Box(18, "Box 18", "1234456677", "10/May/2019 6:55 PM",
                300, 120));
        list.add(new Box(19, "Box 19", "1234456677", "10/May/2019 6:55 PM",
                300, 120));
        list.add(new Box(20, "Box 20", "1234456677", "10/May/2019 6:55 PM",
                300, 120));

        return list;
    }

    @Override
    public void onClickListener(int position) {
        boxList.clear();

        boxList.add(getBoxList().get(position));
        checkBoxAvailability();
        stockAdapter.notifyDataSetChanged();
    }

    private void checkIndentsAvailability() {
        if (getIndentList().size() > 0) {
            noIndentsLayout.setVisibility(View.GONE);
            indentRecyclerView.setVisibility(View.VISIBLE);
        } else {
            noIndentsLayout.setVisibility(View.VISIBLE);
            indentRecyclerView.setVisibility(View.GONE);
        }
    }

    private void checkBoxAvailability() {
        if (boxList.size() > 0) {
            noBoxLayout.setVisibility(View.GONE);
            requestRecyclerView.setVisibility(View.VISIBLE);
        } else {
            noBoxLayout.setVisibility(View.VISIBLE);
            requestRecyclerView.setVisibility(View.GONE);
        }
    }
}
