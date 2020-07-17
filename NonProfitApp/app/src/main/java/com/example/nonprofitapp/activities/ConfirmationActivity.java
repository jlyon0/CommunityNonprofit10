package com.example.nonprofitapp.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.nonprofitapp.R;
import com.example.nonprofitapp.viewmodels.ConfirmationViewModel;
import com.example.nonprofitapp.viewmodels.VolunteerViewModel;

public class ConfirmationActivity extends AppCompatActivity {
    ConfirmationViewModel viewModel;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);
        // TODO display all info here
        viewModel = ViewModelProviders.of(this).get(ConfirmationViewModel.class);

        // Get the Intent that started this activity and extract the info
        Intent intent = getIntent();
        String bag = intent.getStringExtra(MainActivity.SELECTED_BAG);
        int year = intent.getIntExtra(MainActivity.YEAR, 2020);
        int month = intent.getIntExtra(MainActivity.MONTH, 0);
        int day = intent.getIntExtra(MainActivity.DAY, 1);
        int hour = intent.getIntExtra(MainActivity.HOUR, 0);
        int minute = intent.getIntExtra(MainActivity.MINUTE, 0);
        String foodBankId = intent.getStringExtra(MainActivity.FOOD_BANK_BUTTON);

        TextView foodBank = findViewById(R.id.foodBank);
        //TextView bagType = findViewById(R.id.bagType);
        //TextView pickup = findViewById(R.id.pickupTime);

        foodBank.setText(viewModel.getConfirmationString());//"Food Bank " + foodBankId);
        //bagType.setText(bag);
        //pickup.setText(dateTime);


        // Capture the layout's TextView and set the string as its text
        //TextView textView = findViewById(R.id.textView);
        //textView.setText(message);
    }

    /** Called after user logs in as a customer and selects food bank, bag, and pickup time */
    public void confirmOrder(View view) {
        // TODO do something when confirm order button is clicked
        Intent launchWindow = new Intent(this, Window_Display.class);
        startActivity(launchWindow);
    }
}