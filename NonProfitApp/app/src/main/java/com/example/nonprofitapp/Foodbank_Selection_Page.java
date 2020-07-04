package com.example.nonprofitapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.HashMap;

public class Foodbank_Selection_Page extends AppCompatActivity implements View.OnClickListener{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foodbank__selection__page);
    }

    @Override
    public void onClick(View view) {
        launchNext(view.getId());
    }

    public void launchNext(final int buttonID) {
        Intent launchIntent = new Intent(this, GroceryBagSelectionActivity.class); //Package selection page);

        launchIntent.putExtra(MainActivity.FOOD_BANK_BUTTON, buttonID);
        startActivity(launchIntent);
    }
}