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

import org.w3c.dom.Text;

import java.util.List;

public class IndexAdapter extends RecyclerView.Adapter<IndexAdapter.ViewHolder> {
    private Context context;
    private List<Integer> indexList;

    public IndexAdapter(Context context, List<Integer> indexList){
        this.context = context;
        this.indexList = indexList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.index_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.indexText.setText(String.valueOf(indexList.get(position)));
    }

    @Override
    public int getItemCount() {
        return indexList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View rootLayout;
        TextView indexText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            rootLayout = itemView;
            indexText = itemView.findViewById(R.id.indexText);
        }
    }
}
