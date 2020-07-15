package com.example.nonprofitapp.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.nonprofitapp.DataRepository;
import com.google.firebase.auth.FirebaseUser;

/**
 * An example class of a ViewModel.
 *
 * There is no constructor here because it should use the ViewModel constructor. ViewModels should be
 * created with ViewModelProviders.of(this).get(ViewModelExample.class); in the activity where it
 * will be used. We shouldn't interact with the constructor.
 */

public class ViewModelExample extends ViewModel {
    private DataRepository dataRepository;
    // some live data e.g.
    private LiveData<FirebaseUser> liveUser;

    public void init() {
        if (dataRepository != null) {
            return;
        }
        dataRepository = DataRepository.getInstance(); // gets singleton DataRepo object
        // set the live data
        liveUser = dataRepository.getUser();
    }

    public void createOrder() {

    }



}
