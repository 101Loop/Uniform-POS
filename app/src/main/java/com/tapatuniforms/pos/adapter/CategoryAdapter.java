package com.tapatuniforms.pos.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.tapatuniforms.pos.R;
import com.tapatuniforms.pos.model.Category;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    private static final String TAG = "CategoryAdapter";

    private ArrayList<Category> categoryList;
    private CategoryClickListener listener;
    private Context context;

    public interface CategoryClickListener {
        void onCategorySelected(Category category);
    }

    public CategoryAdapter(Context context, ArrayList<Category> categoryList) {
        this.context = context;
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

        holder.categoryImage.setOnClickListener(v -> {
            if (listener != null) {
                listener.onCategorySelected(category);
            }
        });

        Glide.with(context)
                .load(category.getImage())
                .centerCrop()
                .placeholder(R.drawable.ic_shirt)
                .into(holder.categoryImage);

    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public void setOnCategorySelectedListener(CategoryClickListener listener) {
        this.listener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View rootView;
        ImageView categoryImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rootView = itemView;

            categoryImage = itemView.findViewById(R.id.categoryImage);
        }
    }
}
