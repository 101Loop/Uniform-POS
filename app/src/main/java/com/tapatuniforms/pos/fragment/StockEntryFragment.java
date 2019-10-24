package com.tapatuniforms.pos.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import com.tapatuniforms.pos.helper.Validator;
import com.tapatuniforms.pos.model.Box;
import com.tapatuniforms.pos.model.Indent;
import com.tapatuniforms.pos.model.School;
import com.tapatuniforms.pos.network.StockOrderAPI;

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
    private ArrayList<School> schoolList;
    private ArrayList<Indent> allIndentList = new ArrayList<>();

    private EditText searchEditText, barcodeEditText;
    private Button searchButton;

    private DatabaseSingleton db;

    private TextView boxText;

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
        barcodeEditText = v.findViewById(R.id.barcodeEditText);
        searchButton = v.findViewById(R.id.searchButton);
        boxText = v.findViewById(R.id.boxesText);

        db = DatabaseHelper.getDatabase(getContext());

        indentList = new ArrayList<>();
        schoolList = new ArrayList<>();
        stockIndentAdapter = new StockIndentAdapter(getContext(), indentList);
        boxList = new ArrayList<>();
        allBoxList = new ArrayList<>();
        getIndentList();
        indentRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        indentRecyclerView.addItemDecoration(new GridItemDecoration(8, 8));
        indentRecyclerView.setAdapter(stockIndentAdapter);
        stockIndentAdapter.setOnIndentClickListener(this);

        stockBoxAdapter = new StockBoxAdapter(boxList);
        requestRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        requestRecyclerView.setAdapter(stockBoxAdapter);
        stockBoxAdapter.setOnBoxClickListener(this);

        Validator.hideKeyBoardEditText(getActivity(), barcodeEditText);

        searchEditText.setOnEditorActionListener((textView, actionId, keyEvent) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                onSearchClicked();
                return true;
            }

            return false;
        });

        searchButton.setOnClickListener(view -> onSearchClicked());

        barcodeEditText.setOnEditorActionListener((textView, actionId, keyEvent) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                onBarcodeAddClicked();
                return true;
            }

            return false;
        });
    }

    private void onBarcodeAddClicked() {
        String barcodeText = barcodeEditText.getText().toString();

        barcodeEditText.clearFocus();
        Validator.hideKeyboard(Objects.requireNonNull(getActivity()));
    }

    private void onSearchClicked() {
        String searchText = searchEditText.getText().toString();

        searchEditText.clearFocus();
        Validator.hideKeyboard(Objects.requireNonNull(getActivity()));

        if (!searchText.isEmpty()) {
            indentList.clear();

            for (Indent indent : allIndentList) {
                if (searchText.equals(String.valueOf(indent.getId())) || searchText.equals(indent.getName())) {
                    indentList.add(indent);
                }
            }

            stockIndentAdapter.clearSelectedIndent();
            boxList.clear();
            stockBoxAdapter.notifyDataSetChanged();

            boxText.setText(Objects.requireNonNull(getContext()).getString(R.string.select_indent_to_get_boxes));
        } else {
            indentList.clear();
            indentList.addAll(allIndentList);
        }

        checkBoxAvailability();
        checkIndentsAvailability();
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
     */
    private void getIndentList() {
        StockOrderAPI.getInstance(getContext()).getIndentList(indentList, allIndentList, boxList, allBoxList, schoolList, stockIndentAdapter, this, db);
    }

    /**
     * called when an indent is clicked
     * sets boxes, their names and checks if there are any boxes or not
     */
    @Override
    public void onClickListener(long indentId, String indentName) {

        StockOrderAPI.getInstance(getContext()).getBoxList(indentId, boxList, allBoxList, stockBoxAdapter, this, db);

        boxText.setText(Objects.requireNonNull(getContext()).getString(R.string.no_boxes_available));
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
