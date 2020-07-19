package com.example.nonprofitapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;
import android.widget.TextView;

import com.example.nonprofitapp.DataRepository;
import com.example.nonprofitapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class GroceryBagSelectionActivity extends AppCompatActivity {
    public static final String SELECTED_BAG = "com.example.nonprofitapp.BAG";
    private final int DEFAULT_BAG = 0; // whatever is in the first spot in the array is the default
    private int selected = DEFAULT_BAG;

    private ArrayList<String> buttonNames; // get these from Firebase
    private ArrayList<RadioButton> buttons;
    private static final String TAG = GroceryBagSelectionActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grocery_bag_selection);
        FirebaseAuth.getInstance().getCurrentUser().reload();
        Log.i(TAG, "" + FirebaseAuth.getInstance().getCurrentUser().isEmailVerified());
        Intent receivedLauncher = getIntent();
        String foodBankID = receivedLauncher.getStringExtra(MainActivity.FOOD_BANK_BUTTON);
       // int foodBankID = receivedLauncher.getIntExtra(MainActivity.FOOD_BANK_BUTTON, DEFAULT_FOOD_BANK);

        RadioGroup rg = findViewById(R.id.radioGroup);
        buttonNames = new ArrayList<>();
        buttons = new ArrayList<>();
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

    /** Called after user logs in as a customer and selects a food bank and grocery bag */
    public void toSelectPickupTime(View view) {
        Intent intent = new Intent(this, PickupDateSelectionActivity.class);
        intent.putExtra(SELECTED_BAG, buttonNames.get(selected));
        intent.putExtra(MainActivity.FOOD_BANK_BUTTON, getIntent().getStringExtra(MainActivity.FOOD_BANK_BUTTON));
        startActivity(intent);
    }
}