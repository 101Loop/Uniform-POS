package com.tapatuniforms.pos.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tapatuniforms.pos.R;
import com.tapatuniforms.pos.model.Activities;

import java.util.ArrayList;

public class ActivitiesAdapter extends RecyclerView.Adapter<ActivitiesAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Activities> activities;

    public ActivitiesAdapter(Context context, ArrayList<Activities> activities) {
        this.context = context;
        this.activities = activities;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activities_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Activities currentActivity = activities.get(position);

        holder.statusText.setText(currentActivity.getStatus());
        holder.nameText.setText(currentActivity.getName());
        holder.studentIdText.setText(currentActivity.getStudentId());
        holder.dateText.setText(currentActivity.getDate());
        holder.addressText.setText(currentActivity.getAddress());
        holder.motherNameText.setText(currentActivity.getMotherName());
        holder.itemsText.setText(String.valueOf(currentActivity.getItems()));
    }

    @Override
    public int getItemCount() {
        return activities.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        View rootView;
        LinearLayout statusLayout;
        TextView statusText, nameText, studentIdText, dateText, addressText, motherNameText, itemsText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            rootView = itemView;
            statusLayout = itemView.findViewById(R.id.statusLayout);
            statusText = itemView.findViewById(R.id.statusText);
            nameText = itemView.findViewById(R.id.nameText);
            studentIdText = itemView.findViewById(R.id.studentIdText);
            dateText = itemView.findViewById(R.id.dateText);
            addressText = itemView.findViewById(R.id.addressText);
            motherNameText = itemView.findViewById(R.id.motherNameText);
            itemsText = itemView.findViewById(R.id.itemsText);
        }
    }
}
