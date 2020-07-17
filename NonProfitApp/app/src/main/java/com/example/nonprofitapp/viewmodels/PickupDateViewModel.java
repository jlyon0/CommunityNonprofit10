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
 * An example class of a ViewModel.
 *
 * There is no constructor here because it should use the ViewModel constructor. ViewModels should be
 * created with ViewModelProviders.of(this).get(ViewModelExample.class); in the activity where it
 * will be used. We shouldn't interact with the constructor.
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
        dataWrapper.setMonth(datePicker.getMonth());
        dataWrapper.setYear(datePicker.getYear());
    }
}
