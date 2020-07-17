package com.example.nonprofitapp.viewmodels;

import android.app.Application;
import android.graphics.Color;
import android.provider.ContactsContract;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.nonprofitapp.DataRepository;
import com.example.nonprofitapp.DataWrapper;
import com.google.firebase.auth.FirebaseUser;
import com.google.type.Date;

import java.time.LocalDateTime;

/**
 * An example class of a ViewModel.
 *
 * There is no constructor here because it should use the ViewModel constructor. ViewModels should be
 * created with ViewModelProviders.of(this).get(ViewModelExample.class); in the activity where it
 * will be used. We shouldn't interact with the constructor.
 */

public class ConfirmationViewModel extends AndroidViewModel {
    private DataRepository dataRepository;
    private DataWrapper dataWrapper;
    // some live data e.g.
    private LiveData<FirebaseUser> liveUser;
    // am/pm consts
    private final String AM = " a.m.";
    private final String PM = " p.m.";

    public ConfirmationViewModel(@NonNull Application application) {
        super(application);
        if (dataRepository != null) {
            return;
        }
        dataRepository = DataRepository.getInstance(); // gets singleton DataRepo object
        dataWrapper = dataRepository.getDataWrapper();
        /*new DataWrapper("joe",
                "Gleaners",
                "",
                "Nut-Free",
                2020,
                7,
                30,
                22,
                12,
                Color.RED);*/
    }

    public String getConfirmationString() {
        StringBuilder confirmationString = new StringBuilder();
        confirmationString.append("Food Bank: ");
        confirmationString.append(dataWrapper.getFoodBank());
        confirmationString.append("\n");
        confirmationString.append("Bag: ");
        confirmationString.append(dataWrapper.getBag());
        confirmationString.append("\n");
        confirmationString.append("Arriving at ");
        confirmationString.append(getTimeString());
        confirmationString.append(" on ");
        confirmationString.append(getDateString());
        return confirmationString.toString();
    }

    public String getDateString() {
        StringBuilder dateString = new StringBuilder();
        dateString.append(dataWrapper.getMonth());
        dateString.append("/");
        dateString.append(dataWrapper.getDay());
        dateString.append("/");
        dateString.append(dataWrapper.getYear());
        return dateString.toString();
    }

    public String getTimeString() {
        StringBuilder timeString = new StringBuilder();
        // add an am or pm
        String amOrPm;
        if (dataWrapper.getHour() > 12) {
            timeString.append(dataWrapper.getHour() - 12);
            amOrPm = AM;
        } else {
            timeString.append(dataWrapper.getHour());
            amOrPm = PM;
        }
        timeString.append(":");
        timeString.append(dataWrapper.getMinute());
        timeString.append(amOrPm);
        return timeString.toString();
    } /* getTimeString() */
}
