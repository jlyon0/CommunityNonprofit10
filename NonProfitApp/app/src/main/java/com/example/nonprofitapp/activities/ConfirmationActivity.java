package com.example.nonprofitapp.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.nonprofitapp.DataRepository;
import com.example.nonprofitapp.DataWrapper;
import com.example.nonprofitapp.R;

public class ConfirmationActivity extends AppCompatActivity {

    private String bag;
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;
    private String foodBankId;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);
        // TODO display all info here

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
        TextView bagType = findViewById(R.id.bagType);
        TextView pickup = findViewById(R.id.pickupTime);

        foodBank.setText("Food Bank " + foodBankId);
        bagType.setText(bag);

//        if (hour > 12) {
//            hour -= 12;
//        }

        String dateTime = String.format("Arriving at %d:%02d on %d/%d/%d",
                hour,
                minute,
                month,
                day,
                year);
        pickup.setText(dateTime);

//        SimpleDateFormat format = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
//        Calendar calendar = new GregorianCalendar(year, month, day, hour, minute);
//        String date = format.format(calendar.getTime());
//        pickup.setText(date);

//        android.text.format.DateFormat df = new android.text.format.DateFormat();
//        df.format("yyyy-MM-dd hh:mm:ss a", new java.util.Date(year, month, day, hour, minute));
//        pickup.setText(df.toString());

        // Capture the layout's TextView and set the string as its text
        //TextView textView = findViewById(R.id.textView);
        //textView.setText(message);
    }

    /** Called after user logs in as a customer and selects food bank, bag, and pickup time */
    public void confirmOrder(View view) {
        // TODO do something when confirm order button is clicked
        Intent launchWindow = new Intent(this, Window_Display.class);
        startActivity(launchWindow);
    }

    public DataWrapper setWrapper(){

        DataWrapper wrapper = DataRepository.getInstance().getDataWrapper();
        wrapper.setFoodBank(foodBankId);
        wrapper.setBag(bag);
        wrapper.setYear(year);
        wrapper.setMonth(month);
        wrapper.setDay(day);
        wrapper.setMinute(minute);
        wrapper.setIsCompleted(false);
        wrapper.setProgress(0);

        return wrapper;
    }
}