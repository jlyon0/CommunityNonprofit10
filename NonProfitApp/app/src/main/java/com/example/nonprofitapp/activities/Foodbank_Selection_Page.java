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
        // some random test buttons for now
        buttonNames.add("Gleaners");
        buttonNames.add("Midwest Food Bank");
        rg.setWeightSum(Float.parseFloat(buttonNames.size() + ""));

        for (int i = 0; i < buttonNames.size(); i++) {
            // create the radio button; add constraints
            RadioButton radioButton = new RadioButton(this);
            radioButton.setId(i);
            radioButton.setText(buttonNames.get(i));
            radioButton.setButtonDrawable(null);

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