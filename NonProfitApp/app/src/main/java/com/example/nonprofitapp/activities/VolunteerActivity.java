package com.example.nonprofitapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.nonprofitapp.DataRepository;
import com.example.nonprofitapp.DataWrapper;
import com.example.nonprofitapp.MyRecyclerViewAdapter;
import com.example.nonprofitapp.R;
import com.example.nonprofitapp.viewmodels.VolunteerViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Random;

public class VolunteerActivity extends AppCompatActivity {
    private SwipeRefreshLayout pullToRefresh;
    private FloatingActionButton fab;
    private RecyclerView recyclerView;
    private MyRecyclerViewAdapter recyclerViewAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private VolunteerViewModel viewModel;
    //private ArrayList<DataWrapper> orders = new ArrayList<>();

    private static final String TAG = VolunteerActivity.class.getName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer);

        viewModel = ViewModelProviders.of(this).get(VolunteerViewModel.class);
        //viewModel.init();

        Intent received = getIntent();
        String foodBankButton = received.getStringExtra(MainActivity.FOOD_BANK_BUTTON);
        getSupportActionBar().setTitle("Food Bank of " + foodBankButton);
        // add food bank specific things here later, like fetching orders

        //generateFakeOrders(12);
        pullToRefresh = findViewById(R.id.pullToRefresh);

        recyclerView = findViewById(R.id.list);

        // where most of the magic happens
        recyclerViewAdapter = new MyRecyclerViewAdapter(this, viewModel.getLiveOrders().getValue());
        recyclerView.setAdapter(recyclerViewAdapter);

        // linear layout manager as opposed to grid
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);



        // update data if it changes
        viewModel.getLiveOrders().observe(this, new Observer<ArrayList<DataWrapper>>() {
            @Override
            public void onChanged(ArrayList<DataWrapper> dataWrappers) {
                // this will work if we move to realtime updates too.
                Log.i(TAG, "orders changed.");
                recyclerViewAdapter.notifyDataSetChanged();
                pullToRefresh.setRefreshing(false);
            }
        });

        pullToRefresh.setColorSchemeColors(getResources().getColor(R.color.colorAccent),
                getResources().getColor(R.color.colorPrimaryLight),
                getResources().getColor(R.color.colorPrimary),
                getResources().getColor(R.color.colorPrimaryDark));
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.i(TAG, "refresh pull happened, fetching Orders");
                fetchOrders();
            }
        });

        pullToRefresh.setRefreshing(true);
        //TODO: readd below:
        //fetchOrders();

    }

    /**
     * Fetch orders. Mostly exists to make a toast if the refresh fails.
     */
    private void fetchOrders() {
        viewModel.fetchOrdersLive().addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(VolunteerActivity.this,
                        "Failed to refresh, try again",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
    /*
     * Generate some fake orders for testing. Add them to "orders".
     */
    /*private void generateFakeOrders(int howMany) {
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
    }
     */
    /* generateFakeOrders() */
}