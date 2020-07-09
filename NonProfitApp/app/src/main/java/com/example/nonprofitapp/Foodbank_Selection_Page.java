package com.example.nonprofitapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.HashMap;

public class Foodbank_Selection_Page extends AppCompatActivity implements View.OnClickListener{
    Model model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foodbank__selection__page);
        model = ((MyApplication) getApplication()).getModel();
    }

    @Override
    public void onClick(View view) {
        if (model.isVolunteer()) {
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