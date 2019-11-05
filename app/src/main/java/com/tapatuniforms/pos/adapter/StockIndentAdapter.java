package com.tapatuniforms.pos.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.tapatuniforms.pos.R;
import com.tapatuniforms.pos.model.Indent;

import java.util.ArrayList;

public class StockIndentAdapter extends RecyclerView.Adapter<StockIndentAdapter.ViewHolder> {
    private static final String TAG = "StockIndentAdapter";
    private ArrayList<Indent> indentList;
    private Context context;

    private Indent selectedIndent;
    private Indent lastIndent;
    private ViewHolder lastViewHolder;
    private OnIndentClickListener listener;

    public StockIndentAdapter(Context context, ArrayList<Indent> indentList) {
        this.context = context;
        this.indentList = indentList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.stock_indent_item_layout,
                parent, false);
        return new StockIndentAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Indent indent = indentList.get(position);

        if (selectedIndent != null && selectedIndent.getId() == indent.getId()) {
            lastViewHolder = holder;
            setBackgroundBlue(holder);
        } else {
            setBackgroundWhite(holder);
        }

        holder.indentNameView.setText(indent.getIndent());
        holder.indentBoxNumber.setText(indent.getNumberOfBoxes() + " Boxes");
        holder.price.setText("₹" + indent.getPrice());
        holder.itemCount.setText(indent.getNumberOfItems() + " Items");
//        holder.senderName.setText(indent.getDispatchPerson());
//        holder.receivedDate.setText(indent.getReceivedOn());
//        holder.shippingLocation.setText(indent.getShippingFrom());

        //do nothing if the indent is already selected
        //change bg accordingly for other cases
        holder.rootLayout.setOnClickListener(view -> {

            selectedIndent = indentList.get(position);

            if (lastIndent != null && lastIndent == selectedIndent) {
                return;
            }

            if (listener != null) {
                listener.onClickListener(selectedIndent.getId(), selectedIndent.getIndent());
            }

            if (selectedIndent != lastIndent) {
                setBackgroundBlue(holder);
            } else {
                selectedIndent = null;
                setBackgroundWhite(holder);
            }

            if (lastViewHolder != null) {
                setBackgroundWhite(lastViewHolder);
            }

            lastViewHolder = holder;
            lastIndent = selectedIndent;
        });
    }

    /**
     * Method to set blue bg and other colors as well
     *
     * @param holder reference of ViewHolder to update changes
     */
    private void setBackgroundBlue(ViewHolder holder) {
        holder.rootLayout.setCardBackgroundColor(ContextCompat.getColor(context, R.color.blueButton));
        holder.indentNameView.setTextColor(context.getResources().getColor(R.color.white1));
        holder.indentBoxNumber.setTextColor(context.getResources().getColor(R.color.white1));
        holder.price.setTextColor(context.getResources().getColor(R.color.success));
        holder.itemCount.setTextColor(context.getResources().getColor(R.color.success));
        holder.senderName.setTextColor(context.getResources().getColor(R.color.white1));
        holder.shippingOn.setTextColor(context.getResources().getColor(R.color.white1));
        holder.shippingDate.setTextColor(context.getResources().getColor(R.color.white1));
        holder.receivedOn.setTextColor(context.getResources().getColor(R.color.white1));
        holder.receivedDate.setTextColor(context.getResources().getColor(R.color.white1));
        holder.shippingFrom.setTextColor(context.getResources().getColor(R.color.white1));
        holder.shippingLocation.setTextColor(context.getResources().getColor(R.color.white1));
    }

    /**
     * Method to set white bg and other colors as well
     *
     * @param holder reference of ViewHolder to update changes
     */
    private void setBackgroundWhite(ViewHolder holder) {
        holder.rootLayout.setCardBackgroundColor(ContextCompat.getColor(context, R.color.white1));
        holder.indentNameView.setTextColor(context.getResources().getColor(R.color.black));
        holder.indentBoxNumber.setTextColor(context.getResources().getColor(R.color.black3));
        holder.price.setTextColor(context.getResources().getColor(R.color.colorPrimary));
        holder.itemCount.setTextColor(context.getResources().getColor(R.color.colorPrimary));
        holder.senderName.setTextColor(context.getResources().getColor(R.color.black3));
        holder.shippingOn.setTextColor(context.getResources().getColor(R.color.black));
        holder.shippingDate.setTextColor(context.getResources().getColor(R.color.black3));
        holder.receivedOn.setTextColor(context.getResources().getColor(R.color.black));
        holder.receivedDate.setTextColor(context.getResources().getColor(R.color.black3));
        holder.shippingFrom.setTextColor(context.getResources().getColor(R.color.black));
        holder.shippingLocation.setTextColor(context.getResources().getColor(R.color.black3));
    }

    @Override
    public int getItemCount() {
        return indentList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        CardView rootLayout;
        TextView indentNameView, indentBoxNumber, price, senderName, shippingOn, shippingDate, receivedOn, receivedDate, shippingFrom, shippingLocation, itemCount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rootLayout = itemView.findViewById(R.id.rootLayout);
            indentNameView = itemView.findViewById(R.id.indentName);
            indentBoxNumber = itemView.findViewById(R.id.indentNumberBoxes);
            price = itemView.findViewById(R.id.priceItemView);
            senderName = itemView.findViewById(R.id.senderNameView);
            shippingOn = itemView.findViewById(R.id.shippingOnTextView);
            shippingDate = itemView.findViewById(R.id.shippingDateTextView);
            receivedOn = itemView.findViewById(R.id.receivedTextView);
            receivedDate = itemView.findViewById(R.id.receivedDateTextView);
            shippingFrom = itemView.findViewById(R.id.shippingFromTextView);
            shippingLocation = itemView.findViewById(R.id.shippingLocationTextView);
            itemCount = itemView.findViewById(R.id.itemCount);
        }
    }

    /**
     * interface for Indent click
     */
    public interface OnIndentClickListener {
        void onClickListener(long indentId, String indentName);
    }

    /**
     * Method to set click listener
     *
     * @param listener OnIndentClickListener interface's reference
     */
    public void setOnIndentClickListener(OnIndentClickListener listener) {
        this.listener = listener;
    }

    public void clearSelectedIndent() {
        selectedIndent = null;
        lastIndent = null;
        lastViewHolder = null;
        notifyDataSetChanged();
    }
}
