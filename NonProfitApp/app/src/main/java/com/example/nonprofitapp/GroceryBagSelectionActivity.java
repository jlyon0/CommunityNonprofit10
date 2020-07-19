package com.example.nonprofitapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;

public class GroceryBagSelectionActivity extends AppCompatActivity {
    public static final String SELECTED_BAG = "com.example.nonprofitapp.BAG";
    private int selected = -1; // TODO pick a valid default value later
    private final int DEFAULT_FOOD_BANK = -1;
    private ArrayList<String> buttonNames; // get these from Firebase

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grocery_bag_selection);

        Intent receivedLauncher = getIntent();
        String foodBankID = receivedLauncher.getStringExtra(MainActivity.FOOD_BANK_BUTTON);

        RadioGroup rg = findViewById(R.id.radioGroup);
        buttonNames = new ArrayList<>();
        // some random test buttons for now
        buttonNames.add("Kosher");
        buttonNames.add("Halal");
        buttonNames.add("Vegan");
        buttonNames.add("Nut Free");
        rg.setWeightSum(Float.parseFloat(buttonNames.size() + ""));

        for (int i = 0; i < buttonNames.size(); i++) {
            // create the radio button; add constraints
            RadioButton radioButton = new RadioButton(this);
            radioButton.setId(i);
            //RadioGroup.LayoutParams childParam1 = new RadioGroup.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1f);
            //childParam1.setMarginEnd(2);
            //radioButton.setGravity(Gravity.CENTER);
            //radioButton.setLayoutParams(childParam1);
            //radioButton.setBackground(null);
            radioButton.setText(buttonNames.get(i));

            // TODO check out this line
            radioButton.setButtonDrawable(null);

            radioButton.setVisibility(View.VISIBLE);

            // set the default value
            if (buttonNames.get(i).equals("Vegan")) {
                radioButton.setChecked(true);
                radioButton.setTextColor(Color.BLUE);
            }

            // TODO check out this line
            //rg.addView(radioButton, childParam1);
            rg.addView(radioButton);
        }

        // redraw the radio group
        rg.invalidate();

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO eventually get the bag name somehow
                selected = checkedId;
            }
        });
    }

    /** Called after user logs in as a customer and selects a food bank and grocery bag */
    public void toSelectPickupTime(View view) {
        Intent intent = new Intent(this, PickupDateSelectionActivity.class);
        // TODO put the info for the selected bag here
        //intent.putExtra(SELECTED_BAG, bag);
        intent.putExtra(MainActivity.FOOD_BANK_BUTTON, getIntent().getStringExtra(MainActivity.FOOD_BANK_BUTTON));
        startActivity(intent);
    }
}