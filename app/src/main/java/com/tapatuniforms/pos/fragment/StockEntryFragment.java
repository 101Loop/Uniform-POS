package com.tapatuniforms.pos.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
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
import com.tapatuniforms.pos.helper.DatabaseHelper;
import com.tapatuniforms.pos.helper.DatabaseSingleton;
import com.tapatuniforms.pos.helper.GridItemDecoration;
import com.tapatuniforms.pos.model.Box;
import com.tapatuniforms.pos.model.Indent;
import com.tapatuniforms.pos.network.StockOrder;

import java.util.ArrayList;
import java.util.Objects;

public class StockEntryFragment extends Fragment implements StockBoxAdapter.OnBoxClickListener, StockIndentAdapter.OnIndentClickListener {
    private static final String TAG = "StockEntryFragment";
    private RecyclerView indentRecyclerView, requestRecyclerView;
    private LinearLayout noIndentsLayout, noBoxLayout;

    private StockBoxAdapter stockBoxAdapter;
    private ArrayList<Box> boxList;
    private ArrayList<Box> allBoxList;

    private StockIndentAdapter stockIndentAdapter;
    private ArrayList<Indent> indentList;
    private ArrayList<Indent> allIndentList = new ArrayList<>();

    private EditText searchEditText;
    private Button searchButton;

    private DatabaseSingleton db;

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
        searchEditText = v.findViewById(R.id.searchEditText);
        searchButton = v.findViewById(R.id.searchButton);

        db = DatabaseHelper.getDatabase(getContext());

        indentList = new ArrayList<>();
        stockIndentAdapter = new StockIndentAdapter(getContext(), indentList);
        getIndentList();
        indentRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        indentRecyclerView.addItemDecoration(new GridItemDecoration(8, 8));
        indentRecyclerView.setAdapter(stockIndentAdapter);
        stockIndentAdapter.setOnIndentClickListener(this);

        boxList = new ArrayList<>();
        stockBoxAdapter = new StockBoxAdapter(boxList);
        getBoxList();
        requestRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        requestRecyclerView.setAdapter(stockBoxAdapter);
        stockBoxAdapter.setOnBoxClickListener(this);

        searchButton.setOnClickListener(view -> {
            String searchText = searchEditText.getText().toString();

            allIndentList.addAll(indentList);
            indentList.clear();

            for (Indent indent : allIndentList) {
                if (searchText.equals(String.valueOf(indent.getId()))) {
                    indentList.add(indent);
                }
            }

            stockIndentAdapter.selectFirstIndent();
            stockIndentAdapter.notifyDataSetChanged();
        });
    }

    /**
     * called when a box is selected
     * shows the stock item dialog
     */
    @Override
    public void onBoxSelected(Box box) {
        StockItemDialog dialog = new StockItemDialog(getContext(), box);
        dialog.show();

        Objects.requireNonNull(dialog.getWindow()).clearFlags(
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    /**
     * Method to get list of indents
     * */
    private void getIndentList() {
        StockOrder.getInstance(getContext()).getIndentList(indentList, stockIndentAdapter, this, db);
    }

    /**
     * Method to list of Box
     * */
    private void getBoxList() {
        if (allBoxList == null) {
            allBoxList = new ArrayList<>();
        } else {
            allBoxList.clear();
        }

        StockOrder.getInstance(getContext()).getBoxList(boxList, allBoxList, stockBoxAdapter, this, db);
    }

    /**
     * called when an indent is clicked
     * sets boxes, their names and checks if there are any boxes or not
     */
    @Override
    public void onClickListener(long indentId, String indentName) {
        boxList.clear();

        for (Box box : allBoxList) {
            if (box.getIndentId().equals(String.valueOf(indentId))) {
                box.setIndentName(indentName);
                boxList.add(box);
            }
        }

        stockBoxAdapter.notifyDataSetChanged();
    }

    /**
     * shows empty state if indent list is empty, hides otherwise
     */
    public void checkIndentsAvailability() {
        if (indentList.size() > 0) {
            noIndentsLayout.setVisibility(View.GONE);
            indentRecyclerView.setVisibility(View.VISIBLE);
        } else {
            noIndentsLayout.setVisibility(View.VISIBLE);
            indentRecyclerView.setVisibility(View.GONE);
        }
    }

    /**
     * shows empty state if box list is empty, hides otherwise
     */
    public void checkBoxAvailability() {
        if (boxList.size() > 0) {
            noBoxLayout.setVisibility(View.GONE);
            requestRecyclerView.setVisibility(View.VISIBLE);
        } else {
            noBoxLayout.setVisibility(View.VISIBLE);
            requestRecyclerView.setVisibility(View.GONE);
        }
    }
}
