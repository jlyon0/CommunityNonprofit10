package com.example.nonprofitapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;

import com.example.nonprofitapp.R;
import com.example.nonprofitapp.viewmodels.PickupDateViewModel;

import java.util.Calendar;

public class PickupDateSelectionActivity extends AppCompatActivity {
    PickupDateViewModel viewModel;
    DatePicker datePicker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pickup_date_selection);
        viewModel = ViewModelProviders.of(this).get(PickupDateViewModel.class);

        datePicker = findViewById(R.id.date_picker);
        datePicker.setMinDate(Calendar.getInstance().getTimeInMillis());
    }

    /** Called after user logs in as a customer and selects food bank, bag, and pickup time*/
    public void toPickupTime(View view) {
        Intent intent = new Intent(this, PickupTimeSelectionActivity.class);
        viewModel.setDate(datePicker);

        //TODO send food bank selection
        Intent i = getIntent();
        intent.putExtra(MainActivity.SELECTED_BAG, i.getStringExtra(MainActivity.SELECTED_BAG));
        intent.putExtra(MainActivity.FOOD_BANK_BUTTON, i.getStringExtra(MainActivity.FOOD_BANK_BUTTON));
        intent.putExtra(MainActivity.YEAR, datePicker.getYear());
        intent.putExtra(MainActivity.MONTH, datePicker.getMonth());
        intent.putExtra(MainActivity.DAY, datePicker.getDayOfMonth());
        startActivity(intent);
    }
}