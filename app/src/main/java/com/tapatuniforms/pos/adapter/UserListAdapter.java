package com.tapatuniforms.pos.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tapatuniforms.pos.R;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ViewHolder> {
    private OnUserSelectedListener listener;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.login_list_layout,
                parent, false);
        return new UserListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.rootView.setOnClickListener((v) -> {
            if (listener != null) {
                listener.onUserSelected();
            }
        });
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public void setOnUserSelectedListener(OnUserSelectedListener listener) {
        this.listener = listener;
    }

    public interface OnUserSelectedListener {
        void onUserSelected();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View rootView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rootView = itemView;
        }
    }
}
