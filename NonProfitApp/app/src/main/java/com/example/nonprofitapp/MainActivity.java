package com.example.nonprofitapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/*
 * Hello Pro team! This is a basic hello world app generated by Android Studio.
 *
 * Check out /res/layout/activity_main.xml for the GUI. Double clicking the text should let you
 * edit the TextView object.
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void sendMessage(View view) {
        Intent intent = new Intent(this,Foodbank_Selection_Page.class);
        startActivity(intent);

    }
}
