package com.tapatuniforms.pos.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tapatuniforms.pos.R;
import com.tapatuniforms.pos.model.User;

import java.util.ArrayList;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ViewHolder> {
    private ArrayList<User> userList;
    private OnUserSelectedListener listener;

    public UserListAdapter(ArrayList<User> userList) {
        this.userList = userList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.login_list_layout,
                parent, false);
        return new UserListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = userList.get(position);

        holder.nameView.setText(user.getName());
        holder.rootView.setOnClickListener((v) -> {
            if (listener != null) {
                listener.onUserSelected(user);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public void setOnUserSelectedListener(OnUserSelectedListener listener) {
        this.listener = listener;
    }

    public interface OnUserSelectedListener {
        void onUserSelected(User user);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View rootView;
        TextView nameView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rootView = itemView;
            nameView = itemView.findViewById(R.id.nameView);
        }
    }
}
