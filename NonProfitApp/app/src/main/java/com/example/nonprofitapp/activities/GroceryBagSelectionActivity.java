package com.example.nonprofitapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;

import com.example.nonprofitapp.R;

public class GroceryBagSelectionActivity extends AppCompatActivity {
    public static final String SELECTED_BAG = "com.example.nonprofitapp.BAG";
    private String bag = "custom"; // default to custom bag
    private final int DEFAULT_FOOD_BANK = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grocery_bag_selection);

        Intent receivedLauncher = getIntent();
        int foodBankID = receivedLauncher.getIntExtra(MainActivity.FOOD_BANK_BUTTON, DEFAULT_FOOD_BANK);

        RadioGroup rg = findViewById(R.id.radioGroup);

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
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
                }
            }
        });
    }

    /** Called after user logs in as a customer and selects a food bank and grocery bag */
    public void toSelectPickupTime(View view) {
        Intent intent = new Intent(this, PickupDateSelectionActivity.class);
        intent.putExtra(SELECTED_BAG, bag);
        intent.putExtra(MainActivity.FOOD_BANK_BUTTON, getIntent().getStringExtra(MainActivity.FOOD_BANK_BUTTON));
        startActivity(intent);
    }
}