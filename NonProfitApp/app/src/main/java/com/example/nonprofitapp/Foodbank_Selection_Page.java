package com.example.nonprofitapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;

import java.util.HashMap;

public class Foodbank_Selection_Page extends AppCompatActivity implements View.OnClickListener{
    Intent received;
    private String foodbank = "Gleaners"; // default to Gleaners

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foodbank__selection__page);
        received = getIntent();

        RadioGroup rg = findViewById(R.id.radioGroup);

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                System.out.println("checked!");
                /*switch(checkedId){
                    case R.id.kidBag:
                        bag = "Kid Bag";
                        break;
                    case R.id.glutenFree:
                        bag = "Gluten Free Bag";
                        break;
                    case R.id.halalBag:
                        bag = "Halal Bag";
                        break;
                    case R.id.kosherBag:
                        bag = "Kosher Bag";
                        break;
                    case R.id.nutFreeBag:
                        bag = "Nut Free Bag";
                        break;
                    case R.id.vegetarianBag:
                        bag = "Vegetarian Bag";
                        break;
                    case R.id.customBag:
                        bag = "Custom Bag";
                        break;
                }*/
            }
        });
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