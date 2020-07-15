package com.example.nonprofitapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Window_Display extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_window__display);
    }

    public void toCancelOrder(View view)
    {
        //ToDo: delete order from firebase

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    public void toExitOrder(View view)
    {
        //ToDo: mark order as completed

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}