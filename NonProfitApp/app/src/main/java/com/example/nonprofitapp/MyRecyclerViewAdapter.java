package com.example.nonprofitapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.MyViewHolder> {
    private ArrayList<DataWrapper> orders;
    private Context context;


    /*
     * Data comes in here.
     *
     * If you want to send intents from this class, add context to this constructor and pass this
     * in when it's called, see here: https://github.com/mitchtabian/Recyclerview
     */
    public MyRecyclerViewAdapter(Context context, ArrayList<DataWrapper> fetchedOrders) {
        this.context = context;
        orders = fetchedOrders;
    }

    /*
     * Called when the row item is created, but does not populate text etc.
     */
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LinearLayout layout = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.text_row_item, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(layout);
        return viewHolder;
    }

    /*
     * Called after row item is created, populates text and color.
     */
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        DataWrapper order = orders.get(position);
        StringBuilder orderSummary = new StringBuilder();
        orderSummary.append("Order from ");
        orderSummary.append(order.getDisplayName());
        orderSummary.append(" for a ");
        @SuppressLint("DefaultLocale")
        String ordersum = String.format("Order from %s for a %s arriving at %d:%02d on %d/%d/%d",
                order.getDisplayName(),
                order.getBag(),
                order.getHour(),
                order.getMinute(),
                order.getMonth(),
                order.getDay(),
                order.getYear());

        holder.orderDetails.setText(ordersum);
        holder.colorPanel.setBackgroundColor(order.getColor());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView orderDetails;
        View colorPanel;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            orderDetails = itemView.findViewById(R.id.order_details);
            colorPanel = itemView.findViewById(R.id.color_panel);
        }

    }
}
