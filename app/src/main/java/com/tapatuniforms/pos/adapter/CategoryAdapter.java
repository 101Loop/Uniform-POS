package com.tapatuniforms.pos.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tapatuniforms.pos.R;
import com.tapatuniforms.pos.model.Category;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    private static final String TAG = "CategoryAdapter";

    private ArrayList<Category> categoryList;
    private CategoryClickListener listener;

    public interface CategoryClickListener {
        void onCategorySelected(String category);
    }

    public CategoryAdapter(ArrayList<Category> categoryList) {
        this.categoryList = categoryList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item_layout,
                parent, false);
        return new CategoryAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Category category = categoryList.get(position);

        holder.categoryButton.setText(category.getName());
        holder.categoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onCategorySelected(category.getName());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public void setOnCategorySelectedListener(CategoryClickListener listener) {
        this.listener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView categoryButton;
        View rootView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rootView = itemView;
            categoryButton = itemView.findViewById(R.id.categoryButton);

        }
    }
}
