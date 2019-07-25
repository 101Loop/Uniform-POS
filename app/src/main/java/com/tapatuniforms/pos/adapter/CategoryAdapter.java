package com.tapatuniforms.pos.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.tapatuniforms.pos.R;
import com.tapatuniforms.pos.model.Category;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    private static final String TAG = "CategoryAdapter";

    private ArrayList<Category> categoryList;
    private Category selectedCategory;
    private Category lastCategory;
    private CategoryClickListener listener;
    private Context context;
    private ViewHolder lastViewHolder;
    private ViewHolder holder;

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
        Category category = categoryList.get(position);

        this.holder = holder;

        if (selectedCategory != null && selectedCategory.getName().equals(category.getName())) {
            lastViewHolder = holder;
            holder.rootLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.rectangle_light_blue));
        } else {
            holder.rootLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.rounded_white));
        }

        holder.categoryImage.setOnClickListener(v -> {

            if (listener != null) {
                listener.onCategorySelected(category);
            }

            selectedCategory = categoryList.get(position);
            holder.rootLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.rectangle_light_blue));

            if (lastViewHolder != null) {
                holder.rootLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.rounded_white));
            }

            lastViewHolder = holder;
            lastCategory = selectedCategory;
            notifyDataSetChanged();
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
        LinearLayout rootLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rootView = itemView;

            categoryImage = itemView.findViewById(R.id.categoryImage);
            rootLayout = itemView.findViewById(R.id.rootLayout);
        }
    }

    public void clearBackground(){
        selectedCategory = null;
        notifyDataSetChanged();
    }
}
