package com.example.nonprofitapp.activities;

import androidx.annotation.NonNull;
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

import java.util.ArrayList;
import android.widget.TextView;

import com.example.nonprofitapp.R;
import com.example.nonprofitapp.viewmodels.BagSelectViewModel;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.firebase.auth.FirebaseAuth;

public class GroceryBagSelectionActivity extends AppCompatActivity {
    public static final String SELECTED_BAG = "com.example.nonprofitapp.BAG";
    private final int DEFAULT_BAG = 0; // whatever is in the first spot in the array is the default
    private int selected = DEFAULT_BAG;
    BagSelectViewModel viewModel;
    private static final String TAG = GroceryBagSelectionActivity.class.getName();

    private ArrayList<String> buttonNames; // get these from Firebase
    private ArrayList<RadioButton> buttons;
    private ArrayList<String> bagDescriptions;
    private ProgressBar progressBar;

    private Button nextButton;

    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grocery_bag_selection);
        Intent receivedLauncher = getIntent();
        viewModel = ViewModelProviders.of(this).get(BagSelectViewModel.class);
       // int foodBankID = receivedLauncher.getIntExtra(MainActivity.FOOD_BANK_BUTTON, DEFAULT_FOOD_BANK);

        progressBar = findViewById(R.id.bagProgressBar);
        progressBar.setVisibility(View.VISIBLE);
        RadioGroup rg = findViewById(R.id.radioGroup);
        nextButton = findViewById(R.id.continueToPickup);
        buttonNames = new ArrayList<>();
        buttons = new ArrayList<>();
        bagDescriptions = new ArrayList<>();

        viewModel.getBags().observe(this, new Observer<ArrayList<ArrayList<String>>>() {
            @Override
            public void onChanged(ArrayList<ArrayList<String>> arrayLists) {
                buttonNames = arrayLists.get(BagSelectViewModel.BAGS);
                bagDescriptions = arrayLists.get(BagSelectViewModel.DESCRIPTIONS);

                rg.setWeightSum(Float.parseFloat(buttonNames.size() + ""));

                for (int i = 0; i < buttonNames.size(); i++) {
                    // create the radio button; add constraints
                    RadioButton radioButton = new RadioButton(GroceryBagSelectionActivity.this);
                    radioButton.setId(i);
                    radioButton.setText(buttonNames.get(i));
                    //radioButton.setButtonDrawable(null);

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
                progressBar.setVisibility(View.INVISIBLE);
                nextButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //advanceWithInventory(); // uncomment and comment the rest to enable inventory.
                        continueWithBag();
                    }
                });
            }
        });


        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                buttons.get(selected).setTextColor(Color.BLACK);
                selected = checkedId;
                buttons.get(selected).setTextColor(Color.BLUE);

                //This is where the bag descriptions might go
                   TextView description = (TextView) findViewById(R.id.description);
                   description.setText(bagDescriptions.get(selected));

                   TextView heading = (TextView) findViewById(R.id.heading);
                   heading.setVisibility(View.VISIBLE);
            }
        });
    }

    /**
     * Called after user logs in as a customer and selects a food bank and grocery bag. Normal
     * continuation method that sets the bag and advances screen.
     */
    public void continueWithBag() {
        viewModel.setBag(buttonNames.get(selected));
        Intent intent = new Intent(this, PickupDateSelectionActivity.class);
        intent.putExtra(SELECTED_BAG, buttonNames.get(selected));
        intent.putExtra(MainActivity.FOOD_BANK_BUTTON, getIntent().getStringExtra(MainActivity.FOOD_BANK_BUTTON));
        startActivity(intent);

    }

    /**
     * To implement Inventory stuff, add this when continue clicked.
     */
    public void advanceWithInventory() {
        viewModel.tryToSelectBag(buttonNames.get(selected)).observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean selectIsPossible) {
                Log.i(TAG, "onchanged");
                if (selectIsPossible) {
                    continueWithBag();
                } else {
                    Log.i(TAG, "Failed, toasting");

                    Toast.makeText(GroceryBagSelectionActivity.this,
                            "This bag is probably no longer in stock. Sorry!",
                            Toast.LENGTH_SHORT).show();
                }
            }
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
                .setMessage(getString(R.string.help_bag_sel))
                // dialogs made with this builder automatically dismisses itself on button click.
                .setPositiveButton(R.string.ok, null)
                .create();
        dialog.show();
    }

}