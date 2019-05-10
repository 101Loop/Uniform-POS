package com.tapatuniforms.pos.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tapatuniforms.pos.R;
import com.tapatuniforms.pos.model.DashboardItem;

import java.util.ArrayList;

public class DashboardAdapter extends RecyclerView.Adapter<DashboardAdapter.ViewHolder> {
    private ArrayList<DashboardItem> dashItemList;
    private OnClickListener listener;

    public interface OnClickListener {
        void onItemClicked(DashboardItem dashItem);
    }

    public DashboardAdapter(ArrayList<DashboardItem> dashItemList) {
        this.dashItemList = dashItemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dashboard_list_item_layout,
                parent, false);
        return new DashboardAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final DashboardItem dashItem = dashItemList.get(position);

        holder.dashImage.setImageResource(dashItem.getImage());
        holder.dashName.setText(dashItem.getName());

        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClicked(dashItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return dashItemList.size();
    }

    public void setOnClickListener(OnClickListener listener) {
        this.listener = listener;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        View rootView;
        ImageView dashImage;
        TextView dashName;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            rootView = itemView;
            dashImage = itemView.findViewById(R.id.dashImageView);
            dashName = itemView.findViewById(R.id.dashName);
        }
    }
}
