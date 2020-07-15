package com.example.nonprofitapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.nonprofitapp.R;

public class Foodbank_Selection_Page extends AppCompatActivity implements View.OnClickListener{
    Intent received;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foodbank__selection__page);
        received = getIntent();
    }

    @Override
    public void onClick(View view) {
        if (received.getBooleanExtra(MainActivity.VOLUNTEER_LOGIN, false)) {
            // if it was launched as part of a volunteer login, send to volunteer page with foodbank
            Intent launchIntent = new Intent(this, VolunteerActivity.class); //Package selection page);
            launchIntent.putExtra(MainActivity.FOOD_BANK_BUTTON, getResources().getResourceEntryName(view.getId()));
            startActivity(launchIntent);
        } else {
            Intent launchIntent = new Intent(this, GroceryBagSelectionActivity.class); //Package selection page);
            launchIntent.putExtra(MainActivity.FOOD_BANK_BUTTON, getResources().getResourceEntryName(view.getId()));
            startActivity(launchIntent);
        }
    }
}