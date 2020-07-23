package com.example.nonprofitapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProviders;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.nonprofitapp.R;
import com.example.nonprofitapp.viewmodels.DisplayViewModel;

public class Window_Display extends AppCompatActivity {

    DisplayViewModel viewModel;
    AlertDialog helpDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_window__display);
        viewModel = ViewModelProviders.of(this).get(DisplayViewModel.class);

        ConstraintLayout background = findViewById(R.id.display_bg);
        background.setBackgroundColor(viewModel.getColor());
        TextView firstName = findViewById(R.id.first_name);
        firstName.setText(viewModel.getFirstName());
        showHelpMessage();
     }

    public void toCancelOrder(View view)
    {
        viewModel.deleteOrder();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    public void toExitOrder(View view)
    {
        viewModel.markOrderCompleted();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
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
        helpDialog = new AlertDialog.Builder(this)
                .setTitle(getString(R.string.help_title))
                .setMessage(getString(R.string.help_window_display_page))
                // dialogs made with this builder automatically dismisses itself on button click.
                .setPositiveButton(R.string.ok, null)
                .create();
        helpDialog.show();
    }

    @Override
    protected void onDestroy() {
        helpDialog.dismiss();
        super.onDestroy();
    }
}