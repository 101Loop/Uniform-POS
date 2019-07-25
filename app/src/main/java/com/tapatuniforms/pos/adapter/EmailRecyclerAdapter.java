package com.tapatuniforms.pos.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.tapatuniforms.pos.R;

import java.util.ArrayList;

public class EmailRecyclerAdapter extends RecyclerView.Adapter<EmailRecyclerAdapter.ViewHolder> {
    private Context context;
    private ArrayList<String> emailList;

    public EmailRecyclerAdapter(Context context, ArrayList<String> emailList){
        this.context = context;
        this.emailList = emailList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.share_email_recycler_item,
                parent, false);
        return new EmailRecyclerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.emailText.setText(emailList.get(position));

        holder.removeButton.setOnClickListener(view -> {
            emailList.remove(position);
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return emailList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView emailText;
        ImageView removeButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            emailText = itemView.findViewById(R.id.emailEditText);
            removeButton = itemView.findViewById(R.id.removeButton);
        }
    }
}
