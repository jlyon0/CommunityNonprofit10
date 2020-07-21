package com.example.nonprofitapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.nonprofitapp.DataWrapper;
import com.example.nonprofitapp.MyRecyclerViewAdapter;
import com.example.nonprofitapp.R;
import com.example.nonprofitapp.viewmodels.VolunteerViewModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class VolunteerActivity extends AppCompatActivity {
    private SwipeRefreshLayout pullToRefresh;
    private RecyclerView recyclerView;
    private MyRecyclerViewAdapter recyclerViewAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private BottomAppBar bottomAppBar;
    private FloatingActionButton advanceButton;


    private VolunteerViewModel viewModel;
    //private ArrayList<DataWrapper> orders = new ArrayList<>();

    private static final String TAG = VolunteerActivity.class.getName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer);

        viewModel = ViewModelProviders.of(this).get(VolunteerViewModel.class);
        //viewModel.init();
        pullToRefresh = findViewById(R.id.pullToRefresh);
        pullToRefresh.setRefreshing(true);

        final Intent received = getIntent();
        String foodBankButton = received.getStringExtra(MainActivity.FOOD_BANK_BUTTON);
        getSupportActionBar().setTitle("Food Bank of " + viewModel.getFoodBank());
        // add food bank specific things here later, like fetching orders

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

        pullToRefresh.setColorSchemeColors(getResources().getColor(R.color.secondaryColor),
//                getResources().getColor(R.color.colorPrimaryLight),
                getResources().getColor(R.color.secondaryDarkColor),
                getResources().getColor(R.color.secondaryLightColor));

        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.i(TAG, "refresh pull happened, fetching Orders");
                fetchOrders();
            }
        });


        pullToRefresh.setRefreshing(true);
        fetchOrders();

        viewModel.getToastText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String string) {
                Log.i(TAG, "textchanged " + string);
                Toast.makeText(VolunteerActivity.this, string, Toast.LENGTH_SHORT).show();
            }
        });

        advanceButton = findViewById(R.id.advanceButton);
        advanceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.advanceProgress(recyclerViewAdapter.getChecked());
            }
        });

        bottomAppBar = findViewById(R.id.bottomAppBar);
        bottomAppBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.back :
                        viewModel.decrementProgress(recyclerViewAdapter.getChecked());
                        return true;
                    case R.id.trash:
                        viewModel.deleteOrder(recyclerViewAdapter.getChecked());
                        return true;
                    case R.id.by_bag:
                        viewModel.setSortStyle(VolunteerViewModel.BAG_SORT);
                        return true;
                    case R.id.by_date:
                        viewModel.setSortStyle(VolunteerViewModel.DATE_SORT);
                        return true;
                    case R.id.by_name:
                        viewModel.setSortStyle(VolunteerViewModel.NAME_SORT);
                        return true;
                    case R.id.by_progress:
                        viewModel.setSortStyle(VolunteerViewModel.PROGRESS_SORT);
                        return true;
                    default:
                        return false;
                }
            }
        });


    }

    /**
     * Fetch orders. Mostly exists to make a toast if the refresh fails.
     */
    private void fetchOrders() {
        pullToRefresh.setRefreshing(true);
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
     * The next 3 methods control the help icon/option in the ActionBar.
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.top_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.help_header) {
            // help's onclicklistener basically
            showHelpMessage();
        }
        return super.onOptionsItemSelected(item);
    }

    public void showHelpMessage() {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(getString(R.string.help_title))
                .setMessage(getString(R.string.help_vol_orders))
                // dialogs made with this builder automatically dismisses itself on button click.
                .setPositiveButton(R.string.ok, null)
                .create();
        dialog.show();
    }
}