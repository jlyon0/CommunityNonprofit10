package com.example.nonprofitapp.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.nonprofitapp.R;
import com.example.nonprofitapp.viewmodels.FoodBankViewModel;
import com.example.nonprofitapp.viewmodels.VolunteerViewModel;

import java.time.LocalDateTime;

public class Foodbank_Selection_Page extends AppCompatActivity implements View.OnClickListener{
    Intent received;
    FoodBankViewModel viewModel;
    private static final String TAG = Foodbank_Selection_Page.class.getName();

    private Button signOut;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foodbank__selection__page);
        received = getIntent();
        viewModel = ViewModelProviders.of(this).get(FoodBankViewModel.class);
        signOut = findViewById(R.id.signout);
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.signOut();
                finish();
            }
        });
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);

    }

    @Override
    public void onClick(final View view) {
        //TODO: Set dataRepo.setFoodBank
        progressBar.setVisibility(View.VISIBLE);
        if (viewModel.isVolunteer()) {
            // if it was launched as part of a volunteer login, send to volunteer page with foodbank
            Intent launchIntent = new Intent(this, VolunteerActivity.class); //Package selection page);
            launchIntent.putExtra(MainActivity.FOOD_BANK_BUTTON, getResources().getResourceEntryName(view.getId()));
            startActivity(launchIntent);
            progressBar.setVisibility(View.INVISIBLE);
        } else {
            // set a foodBank first, then:
            try {
                // if firebase finds an uncompleted order for you at that foodbank, go to the window screen
                viewModel.hasOrder().observe(this, new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean hasOrder) {
                        if (hasOrder) {
                            // go to window display to pick up placed order
                            Intent launchWindowDisp = new Intent(Foodbank_Selection_Page.this, Window_Display.class);
                            startActivity(launchWindowDisp);
                            progressBar.setVisibility(View.INVISIBLE);

                        } else {
                            // go create an order for this food bank
                            Intent launchIntent = new Intent(Foodbank_Selection_Page.this, GroceryBagSelectionActivity.class); //Package selection page);
                            launchIntent.putExtra(MainActivity.FOOD_BANK_BUTTON, getResources().getResourceEntryName(view.getId()));
                            startActivity(launchIntent);
                            progressBar.setVisibility(View.INVISIBLE);

                        }
                    }
                });
            } catch (NullPointerException npe) {
                Log.i(TAG, "Set the foodbank first.");
                npe.printStackTrace();
            }

        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(TAG, "OnRESTART: " + viewModel.isLoggedIn());
        if (!viewModel.isLoggedIn()) {
            // if the user is not logged in, go back to login page
            Toast.makeText(getApplicationContext(), "Something went wrong when you logged in. Please try again.", Toast.LENGTH_LONG).show();
            finish();
        }
    }
}