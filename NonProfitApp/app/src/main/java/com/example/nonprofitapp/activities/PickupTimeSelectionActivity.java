package com.example.nonprofitapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

    /*
     * The next 3 methods control the help icon/option in the ActionBar.
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.top_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.help_header) {
            // help's onclicklistener basically
            showHelpMessage();
        }
        return super.onOptionsItemSelected(item);
    }

    public void showHelpMessage() {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(getString(R.string.help_title))
                .setMessage(getString(R.string.help_time_picker))
                // dialogs made with this builder automatically dismisses itself on button click.
                .setPositiveButton(R.string.ok, null)
                .create();
        dialog.show();
    }

}