package com.example.nonprofitapp.viewmodels;

import android.app.Application;
import android.provider.ContactsContract;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.nonprofitapp.DataRepository;
import com.example.nonprofitapp.DataWrapper;
import com.google.firebase.auth.FirebaseUser;

/**
 */

public class PickupDateViewModel extends AndroidViewModel {
    private DataRepository dataRepository;
    private DataWrapper dataWrapper;

    public PickupDateViewModel(@NonNull Application application) {
        super(application);
        if (dataRepository != null) {
            return;
        }
        dataRepository = DataRepository.getInstance(); // gets singleton DataRepo object
        dataWrapper = dataRepository.getDataWrapper();
    }
    public void setDate(DatePicker datePicker) {
        dataWrapper.setDay(datePicker.getDayOfMonth());
        dataWrapper.setMonth(datePicker.getMonth() + 1); // january is 0 for some reason
        dataWrapper.setYear(datePicker.getYear());
    }
}
