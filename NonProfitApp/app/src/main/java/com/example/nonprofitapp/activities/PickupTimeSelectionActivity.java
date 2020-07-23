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
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.nonprofitapp.R;
import com.example.nonprofitapp.viewmodels.PickupTimeViewModel;

public class PickupTimeSelectionActivity extends AppCompatActivity {
    PickupTimeViewModel viewModel;
    private TimePicker timePicker;
    private TextView selectTime;

    // TODO: set this var with something retrieved from firebase.
    int[] startAndEndHour = {10,15}; // in 0-23 hour time

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pickup_time_selection);
        viewModel = ViewModelProviders.of(this).get(PickupTimeViewModel.class);

        selectTime = findViewById(R.id.please_choose_time);
        StringBuilder timeMessage = new StringBuilder(getString(R.string.please_choose_a_pickup_time));
        timeMessage.append(getHumanReadableHour(startAndEndHour[0]));
        timeMessage.append(" and ");
        timeMessage.append(getHumanReadableHour(startAndEndHour[1]));
        selectTime.setText(timeMessage.toString());

        timePicker = (TimePicker) findViewById(R.id.time_picker);
        timePicker.setCurrentHour(startAndEndHour[0]);
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                if (hourOfDay < startAndEndHour[0]) {
                    // the hour is too early
                    timePicker.setCurrentHour(startAndEndHour[0]);
                    selectTime.setTextColor(getResources().getColor(R.color.design_default_color_error));
                    selectTime.startAnimation(AnimationUtils.loadAnimation(PickupTimeSelectionActivity.this, R.anim.shake));
                } else if (hourOfDay >= startAndEndHour[1]) {
                    // hour is too late, set it to an hour before the closing hour (closing hour is probably exclusive)
                    timePicker.setCurrentHour(startAndEndHour[1] - 1);
                    selectTime.setTextColor(getResources().getColor(R.color.design_default_color_error));
                    selectTime.startAnimation(AnimationUtils.loadAnimation(PickupTimeSelectionActivity.this, R.anim.shake));
                }
            } // onTimeChanged
        });
    }

    public String getHumanReadableHour(int hour) {
        StringBuilder readable = new StringBuilder();
        // add an am or pm
        String amOrPm = "";
        if (hour >= 12) {
            if (hour != 12) {
                readable.append(hour - 12);
            }
            amOrPm = " p.m.";
        } else {
            if (hour == 0) {
                readable.append("12");
            } else {
                readable.append(hour);
            }
            amOrPm = " a.m.";
        }
        readable.append(amOrPm);
        return readable.toString();
    }

    /** Called after user logs in as a customer and selects food bank, bag, and pickup date/time */
    public void toOrderConfirmation(View view) {
        Intent intent = new Intent(this, ConfirmationActivity.class);

        viewModel.setTime(timePicker);

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