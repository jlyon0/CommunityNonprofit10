package com.example.nonprofitapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.nonprofitapp.DataRepository;
import com.example.nonprofitapp.R;
import com.example.nonprofitapp.viewmodels.BagSelectViewModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class GroceryBagSelectionActivity extends AppCompatActivity {
    public static final String SELECTED_BAG = "com.example.nonprofitapp.BAG";
    private String bag = "custom"; // default to custom bag
    private final int DEFAULT_FOOD_BANK = -1;
    BagSelectViewModel viewModel;
    private static final String TAG = GroceryBagSelectionActivity.class.getName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grocery_bag_selection);
        viewModel = ViewModelProviders.of(this).get(BagSelectViewModel.class);
       // int foodBankID = receivedLauncher.getIntExtra(MainActivity.FOOD_BANK_BUTTON, DEFAULT_FOOD_BANK);


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
        viewModel.setBag(bag);
        Intent intent = new Intent(this, PickupDateSelectionActivity.class);
        intent.putExtra(SELECTED_BAG, bag);
        intent.putExtra(MainActivity.FOOD_BANK_BUTTON, getIntent().getStringExtra(MainActivity.FOOD_BANK_BUTTON));
        startActivity(intent);
    }
}