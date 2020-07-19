package com.example.nonprofitapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TimePicker;

import com.example.nonprofitapp.R;
import com.example.nonprofitapp.viewmodels.PickupTimeViewModel;

public class PickupTimeSelectionActivity extends AppCompatActivity {
    PickupTimeViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pickup_time_selection);
        viewModel = ViewModelProviders.of(this).get(PickupTimeViewModel.class);
    }

    /** Called after user logs in as a customer and selects food bank, bag, and pickup date/time */
    public void toOrderConfirmation(View view) {
        Intent intent = new Intent(this, ConfirmationActivity.class);
        TimePicker timePicker = (TimePicker) findViewById(R.id.time_picker);

        viewModel.setDate(timePicker);

        Intent i = getIntent();
        // TODO send food bank selection
        intent.putExtra(MainActivity.SELECTED_BAG, i.getStringExtra(MainActivity.SELECTED_BAG));
        intent.putExtra(MainActivity.FOOD_BANK_BUTTON, i.getStringExtra(MainActivity.FOOD_BANK_BUTTON));
        intent.putExtra(MainActivity.YEAR, i.getStringExtra(MainActivity.YEAR));
        intent.putExtra(MainActivity.MONTH, i.getStringExtra(MainActivity.MONTH));
        intent.putExtra(MainActivity.DAY, i.getStringExtra(MainActivity.DAY));
        intent.putExtra(MainActivity.HOUR, timePicker.getCurrentHour());
        intent.putExtra(MainActivity.MINUTE, timePicker.getCurrentMinute());
        startActivity(intent);
    }
}