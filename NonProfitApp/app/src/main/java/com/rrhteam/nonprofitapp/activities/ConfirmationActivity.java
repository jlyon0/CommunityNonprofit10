package com.rrhteam.nonprofitapp.activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.rrhteam.nonprofitapp.R;
import com.rrhteam.nonprofitapp.viewmodels.ConfirmationViewModel;

public class ConfirmationActivity extends AppCompatActivity {

    ConfirmationViewModel viewModel;
    private ProgressBar progressBar;

    private String bag;
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;
    private String foodBankId;

    private static final String TAG = ConfirmationActivity.class.getName();



    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);
        // TODO display all info here
        viewModel = ViewModelProviders.of(this).get(ConfirmationViewModel.class);
        progressBar = findViewById(R.id.confirmProgressBar);
        progressBar.setVisibility(View.INVISIBLE);

        viewModel.getToastText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String somethingOtherThanS) {
                Toast.makeText(getApplicationContext(), somethingOtherThanS, Toast.LENGTH_LONG).show();

            }
        });

        // Get the Intent that started this activity and extract the info
        Intent intent = getIntent();
        bag = intent.getStringExtra(MainActivity.SELECTED_BAG);
        year = intent.getIntExtra(MainActivity.YEAR, 2020);
        month = intent.getIntExtra(MainActivity.MONTH, 0);
        day = intent.getIntExtra(MainActivity.DAY, 1);
        hour = intent.getIntExtra(MainActivity.HOUR, 0);
        minute = intent.getIntExtra(MainActivity.MINUTE, 0);
        foodBankId = intent.getStringExtra(MainActivity.FOOD_BANK_BUTTON);

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
        // method call to send to firebase with wrapper
        progressBar.setVisibility(View.VISIBLE);
        // these observers are quite ugly, they're here because there are async things happening.
        viewModel.getAndSetColor().observe(this, (readyToSend) -> {
            Log.i(TAG, "observer");
            viewModel.sendDataToFireBase().observe(ConfirmationActivity.this, new Observer<Boolean>() {
                @Override
                public void onChanged(Boolean isDone) {
                    if (isDone) {
                        progressBar.setVisibility(View.INVISIBLE);
                        Intent launchWindow = new Intent(ConfirmationActivity.this, Window_Display.class);
                        startActivity(launchWindow);
                    }
                }
            });
        });
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
                .setMessage(getString(R.string.help_confirmation_page))
                // dialogs made with this builder automatically dismisses itself on button click.
                .setPositiveButton(R.string.ok, null)
                .create();
        dialog.show();
    }

}