package com.example.nonprofitapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.example.nonprofitapp.DataWrapper;
import com.example.nonprofitapp.MyRecyclerViewAdapter;
import com.example.nonprofitapp.R;

import java.util.ArrayList;
import java.util.Random;

public class VolunteerActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private MyRecyclerViewAdapter recyclerViewAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private ArrayList<DataWrapper> orders = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer);

        Intent received = getIntent();
        String foodBankButton = received.getStringExtra(MainActivity.FOOD_BANK_BUTTON);
        getSupportActionBar().setTitle("Food Bank of " + foodBankButton);
        // add food bank specific things here later, like fetching orders

        generateFakeOrders(12);
        recyclerView = findViewById(R.id.list);

        // where most of the magic happens
        recyclerViewAdapter = new MyRecyclerViewAdapter(orders);
        recyclerView.setAdapter(recyclerViewAdapter);

        // linear layout manager as opposed to grid
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }

    /*
     * Do something with cognito or firebase to fetch the list of orders and their data. Enter
     * the order data into the order arraylist. Unused right now.
     */
    private void fetchCustomers(String foodBank) {
        // retrieval likely looks like this, with an AsyncTask subclass or separate thread doing the
        // networking
        // Networker asyncTaskNetworker = new Networker();
        // asyncTaskNetworker.execute();
    }
    /*
     * Generate some fake orders for testing. Add them to "orders".
     */
    private void generateFakeOrders(int howMany) {
        Random random = new Random();

        for (int i = 0; i < howMany; i++) {
            int color = Color.argb(255, random.nextInt(255 + 1),random.nextInt(255 + 1),random.nextInt(255 + 1));
            int foodBankInt = i % 4 + 1;
            orders.add(new DataWrapper("namenumber" + i,
                    "At food bank " + foodBankInt,
                    "button" + foodBankInt,
                    "bag " + foodBankInt,
                    2020,
                    1,
                    1,
                    1,
                    1,
                    color));
        }
    } /* generateFakeOrders() */
}