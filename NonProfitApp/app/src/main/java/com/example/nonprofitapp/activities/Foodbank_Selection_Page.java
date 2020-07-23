package com.example.nonprofitapp.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nonprofitapp.R;
import com.example.nonprofitapp.viewmodels.FoodBankViewModel;
import com.example.nonprofitapp.viewmodels.VolunteerViewModel;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Foodbank_Selection_Page extends AppCompatActivity implements View.OnClickListener{
    Intent received;
    FoodBankViewModel viewModel;
    private static final String TAG = Foodbank_Selection_Page.class.getName();

    private final int DEFAULT_FOOD_BANK = 0;
    private int selected = DEFAULT_FOOD_BANK;
    private ArrayList<String> buttonNames; // get these from Firebase
    private ArrayList<RadioButton> buttons;
    private ArrayList<String> bankAddresses;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foodbank__selection__page);
        received = getIntent();
        viewModel = ViewModelProviders.of(this).get(FoodBankViewModel.class);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        viewModel.getToastText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String string) {
                Log.i(TAG, "textchanged " + string);
                Toast.makeText(Foodbank_Selection_Page.this, string, Toast.LENGTH_SHORT).show();
            }
        });

        RadioGroup rg = findViewById(R.id.radioGroup);
        buttonNames = new ArrayList<>();
        buttons = new ArrayList<>();
        bankAddresses = new ArrayList<>();
        // some random test buttons for now
        buttonNames.add("Gleaners");
        bankAddresses.add("3737 Waldemere Ave, Indianapolis, IN 46241");
        buttonNames.add("Midwest Food Bank");
        bankAddresses.add("6450 S Belmont Ave, Indianapolis, IN 46217");
        buttonNames.add("Marion County: CARE Mobile Pantries");
        bankAddresses.add("Mondays – Marion County Election Board 3737 E. Washington St. Indianapolis 2-6P\n" +
                "Fridays – Ivy Tech Community College 2535 N. Capitol St. Indianapolis  2-6P\n" +
                "Saturdays – John Marshall High School 10101 E. 38th St. Indianapolis 10A-2P\n" +
                "7/27 – Moorhead Elementary School  8400 E. 10th Street, Indianapolis 4-6P\n" +
                "7/30 – Lawrence Community Park  5301 N Franklin Rd Indianapolis  4-6P\n" +
                "8/11 – Franklin Cove Apartments  8505 Faywood Dr. Indianapolis  9A (drop and go)");
        rg.setWeightSum(Float.parseFloat(buttonNames.size() + ""));

        for (int i = 0; i < buttonNames.size(); i++) {
            // create the radio button; add constraints
            RadioButton radioButton = new RadioButton(this);
            radioButton.setId(i);
            radioButton.setText(buttonNames.get(i));


            // set the default value
            if (buttonNames.get(i).equals(selected)) {
                radioButton.setChecked(true);
                radioButton.setTextColor(Color.BLUE);
            }

            rg.addView(radioButton);
            buttons.add(radioButton);
        }

        // redraw the radio group
        rg.invalidate();

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                buttons.get(selected).setTextColor(Color.BLACK);
                selected = checkedId;
                buttons.get(selected).setTextColor(Color.BLUE);

                //Changes the description of the food bank
                TextView header = (TextView) findViewById(R.id.header);
                header.setVisibility(View.VISIBLE);
                TextView description = (TextView) findViewById(R.id.description);
                description.setText(bankAddresses.get(selected));

            }
        });
    }

    @Override
    public void onClick(final View view) {
        if (!viewModel.setFoodBank(buttonNames.get(selected))) {
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        if (viewModel.isVolunteer()) {
            Log.i(TAG, "isvolunteer");
            // if it was launched as part of a volunteer login, send to volunteer page with foodbank
            if(viewModel.checkIfVolValid()) {
                Intent launchIntent = new Intent(this, VolunteerActivity.class); //Package selection page);
                launchIntent.putExtra(MainActivity.FOOD_BANK_BUTTON, getResources().getResourceEntryName(view.getId()));
                startActivity(launchIntent);
            }
            progressBar.setVisibility(View.INVISIBLE);

        } else {
            // set a foodBank first, then:
            try {
                // if firebase finds an uncompleted order for you at that foodbank, go to the window screen
                viewModel.hasOrder().observe(this, new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean hasOrder) {
                        if (hasOrder) {
                            // go to window display to pick up placed order
                            Intent launchWindowDisp = new Intent(Foodbank_Selection_Page.this, Window_Display.class);
                            startActivity(launchWindowDisp);
                            progressBar.setVisibility(View.INVISIBLE);

                        } else {
                            // go create an order for this food bank
                            Intent launchIntent = new Intent(Foodbank_Selection_Page.this, GroceryBagSelectionActivity.class); //Package selection page);
                            launchIntent.putExtra(MainActivity.FOOD_BANK_BUTTON, getResources().getResourceEntryName(view.getId()));
                            startActivity(launchIntent);
                            progressBar.setVisibility(View.INVISIBLE);

                        }
                    }
                });
            } catch (NullPointerException npe) {
                Log.i(TAG, "Set the foodbank first.");
                npe.printStackTrace();
            }

        }
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(TAG, "OnRESTART: " + viewModel.isLoggedIn());
        if (!viewModel.isLoggedIn()) {
            // if the user is not logged in, go back to login page
            finish();
        } else {
            if (viewModel.isVolunteer()) {
                viewModel.checkIfVolValid();
            }
        }
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
        AlertDialog dialog;
        if (viewModel.isVolunteer()) {
             dialog = new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.help_title))
                    .setMessage(getString(R.string.help_vol_foodbank))
                    // dialogs made with this builder automatically dismisses itself on button click
                    .setPositiveButton(R.string.ok, null)
                    .create();
        } else {
             dialog = new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.help_title))
                    .setMessage(getString(R.string.help_foodbank_sel))
                    .setPositiveButton(R.string.ok, null)
                    .create();
        }
        dialog.show();
    }


}