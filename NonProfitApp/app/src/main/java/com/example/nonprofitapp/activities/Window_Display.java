package com.example.nonprofitapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.View;

import com.example.nonprofitapp.R;
import com.example.nonprofitapp.viewmodels.DisplayViewModel;

public class Window_Display extends AppCompatActivity {

    DisplayViewModel viewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_window__display);
        viewModel = ViewModelProviders.of(this).get(DisplayViewModel.class);
    }

    public void toCancelOrder(View view)
    {
        //ToDo: delete order from firebase
        viewModel.deleteOrder();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    public void toExitOrder(View view)
    {
        //ToDo: mark order as completed
        viewModel.markOrderCompleted();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}