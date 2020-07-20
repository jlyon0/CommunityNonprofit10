package com.example.nonprofitapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.MyViewHolder> {
    private ArrayList<DataWrapper> orders;
    private ArrayList<String> checked;
    private Context context;

    private static final String TAG = MyRecyclerViewAdapter.class.getName();

    /*
     * Data comes in here.
     *
     * If you want to send intents from this class, add context to this constructor and pass this
     * in when it's called, see here: https://github.com/mitchtabian/Recyclerview
     */
    public MyRecyclerViewAdapter(Context context, ArrayList<DataWrapper> fetchedOrders) {
        this.context = context;
        orders = fetchedOrders;
        checked = new ArrayList<>();
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
        final DataWrapper order = orders.get(position);
        final String uid = order.getUid();
        String progress;
        switch (order.getProgress()) {
            default:
            case DataWrapper.PROGRESS_NOT_STARTED:
                progress = "Not started.";
                break;
            case DataWrapper.PROGRESS_STARTED:
                progress = "Started.";
                break;
            case DataWrapper.PROGRESS_UNDELIVERED:
                progress = "Made, but not delivered.";
                break;
            case DataWrapper.PROGRESS_DELIVERED:
                progress = "Delivered.";
                break;
        }

        StringBuilder timeString = new StringBuilder();
        String AM = " a.m.";
        String PM = " p.m.";
        // same as confirmation:
        String amOrPm = "";
        if (order.getHour() > 12) {
            timeString.append(order.getHour() - 12);
            amOrPm = PM;
        } else {
            if (order.getHour() == 0) {
                timeString.append("12");
            } else {
                timeString.append(order.getHour());
                amOrPm = AM;
            }
        }
        timeString.append(":");
        if (order.getMinute() < 10) timeString.append(0);
        timeString.append(order.getMinute());
        timeString.append(amOrPm);
        // end

        @SuppressLint("DefaultLocale") final String ordersum = String.format("Order from %s for a %s arriving at %s on %d/%d/%d. %s",
                order.getDisplayName(),
                order.getBag(),
                timeString.toString(),
                order.getMonth(),
                order.getDay(),
                order.getYear(),
                progress);

        holder.orderDetails.setText(ordersum);
        holder.colorPanel.setBackgroundColor(order.getColor());
        holder.checkBox.setChecked(checked.contains(uid));

        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isChecked = ((CheckBox) view).isChecked();
                Log.i(TAG, "checked:" + isChecked + " uid: " + uid);

                if (checked.contains(uid) && !isChecked) {
                    checked.remove(uid);
                } else if (isChecked && !checked.contains(uid)) {
                    checked.add(uid);
                }
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: add an order viewer
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
        CheckBox checkBox;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            orderDetails = itemView.findViewById(R.id.order_details);
            colorPanel = itemView.findViewById(R.id.color_panel);
            checkBox = itemView.findViewById(R.id.checkBox);
        }

    }

    public ArrayList<String> getChecked() {
        return checked;
    }
}
