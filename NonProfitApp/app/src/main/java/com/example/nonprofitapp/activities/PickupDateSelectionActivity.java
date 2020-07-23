package com.example.nonprofitapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.example.nonprofitapp.R;
import com.example.nonprofitapp.viewmodels.PickupDateViewModel;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class PickupDateSelectionActivity extends AppCompatActivity {
    PickupDateViewModel viewModel;
    DatePicker datePicker;
    TextView selectADay;
    Button next;

    // TODO: set this var with something retrieved from firebase.
    List<Integer> selectableDays = Arrays.asList(Calendar.TUESDAY, Calendar.WEDNESDAY, Calendar.THURSDAY);

    private static final String TAG = PickupDateSelectionActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pickup_date_selection);
        viewModel = ViewModelProviders.of(this).get(PickupDateViewModel.class);

        selectADay = findViewById(R.id.pickupDate);
        StringBuilder selectADayMessage = new StringBuilder(getString(R.string.please_choose_date));
        for (int i = 0; i < selectableDays.size(); i++) {
            int dayOfWeek = selectableDays.get(i);
            String weekDay = "";
            if (Calendar.MONDAY == dayOfWeek) {
                weekDay = "Monday";
            } else if (Calendar.TUESDAY == dayOfWeek) {
                weekDay = "Tuesday";
            } else if (Calendar.WEDNESDAY == dayOfWeek) {
                weekDay = "Wednesday";
            } else if (Calendar.THURSDAY == dayOfWeek) {
                weekDay = "Thursday";
            } else if (Calendar.FRIDAY == dayOfWeek) {
                weekDay = "Friday";
            } else if (Calendar.SATURDAY == dayOfWeek) {
                weekDay = "Saturday";
            } else if (Calendar.SUNDAY == dayOfWeek) {
                weekDay = "Sunday";
            }
            selectADayMessage.append(weekDay) ;
            if (i != selectableDays.size() - 1) {
                selectADayMessage.append(", ");
            }
        }
        selectADay.setText(selectADayMessage.toString());
        next = findViewById(R.id.selectTime);

        datePicker = findViewById(R.id.date_picker);
        datePicker.setMinDate(Calendar.getInstance().getTimeInMillis());
        minimalDateRestrictor();
        //ignorantDatePicker();
    }

    public void minimalDateRestrictor() {
        Calendar pickerCurrentDate = Calendar.getInstance();
        pickerCurrentDate.setTimeInMillis(System.currentTimeMillis());
        if (pickerCurrentDate.get(Calendar.DAY_OF_WEEK) > 4) {
            // if it's near the end of the week, get the last day it can be.
            pickerCurrentDate.set(Calendar.DAY_OF_WEEK, selectableDays.get(selectableDays.size() - 1));
        } else {
            // otherwise get the earliest day it can be.
            pickerCurrentDate.set(Calendar.DAY_OF_WEEK, selectableDays.get(0));
        }
        datePicker.init(pickerCurrentDate.get(Calendar.YEAR),
                pickerCurrentDate.get(Calendar.MONTH),
                pickerCurrentDate.get(Calendar.DAY_OF_MONTH),
                new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar requestedDate = Calendar.getInstance();
                        Log.i(TAG, "Calendar clicked");
                        requestedDate.set(year, monthOfYear, dayOfMonth);
                        if (!selectableDays.contains(requestedDate.get(Calendar.DAY_OF_WEEK))) {
                            Log.i(TAG, "Cannot select this day.");
                            // shake stuff and send an error code.
                            selectADay.setTextColor(getResources().getColor(R.color.design_default_color_error));
                            if (requestedDate.get(Calendar.DAY_OF_WEEK) > 4) {
                                // if it's near the end of the week, get the last day it can be.
                                requestedDate.set(Calendar.DAY_OF_WEEK, selectableDays.get(selectableDays.size() - 1));
                            } else {
                                // otherwise get the earliest day it can be.
                                requestedDate.set(Calendar.DAY_OF_WEEK, selectableDays.get(0));
                            }
                            datePicker.updateDate(requestedDate.get(Calendar.YEAR),
                                    requestedDate.get(Calendar.MONTH),
                                    requestedDate.get(Calendar.DAY_OF_MONTH));
                            // shake the textview laterally
                            selectADay.startAnimation(AnimationUtils.loadAnimation(PickupDateSelectionActivity.this, R.anim.shake));
                        } // not in selectable days
                    } // onDateChanged
                });
    }
    public void ignorantDatePicker() {

        MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker();
        builder.setTitleText("Choose a date");
        CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder();
        constraintsBuilder.setValidator(new CalendarConstraints.DateValidator() {
            private Calendar utc = Calendar.getInstance(TimeZone.getTimeZone("UTC"));

            @Override
            public boolean isValid(long date) {
                utc.setTimeInMillis(date);
                int dayOfWeek = utc.get(Calendar.DAY_OF_WEEK);
                return selectableDays.contains(dayOfWeek);
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
            }
        });
        builder.setCalendarConstraints(constraintsBuilder.build());
        builder.setTheme(R.style.ThemeOverlay_MaterialComponents_MaterialCalendar_Fullscreen);
        MaterialDatePicker<Long> picker = builder.build();
        picker.show(getSupportFragmentManager(), picker.toString());

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
                .setMessage(getString(R.string.help_date_picker))
                // dialogs made with this builder automatically dismisses itself on button click.
                .setPositiveButton(R.string.ok, null)
                .create();
        dialog.show();
    }
}