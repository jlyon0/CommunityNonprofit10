package com.rrhteam.nonprofitapp.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.rrhteam.nonprofitapp.DataRepository;
import com.rrhteam.nonprofitapp.DataWrapper;

/**
 * An example class of a ViewModel.
 *
 * There is no constructor here because it should use the ViewModel constructor. ViewModels should be
 * created with ViewModelProviders.of(this).get(ViewModelExample.class); in the activity where it
 * will be used. We shouldn't interact with the constructor.
 */

public class MainViewModel extends AndroidViewModel {
    private DataRepository dataRepository;
    private DataWrapper dataWrapper;
    // some live data e.g.
    private MutableLiveData<String> toastText;

    private static final String TAG = MainViewModel.class.getName();


    public MainViewModel(@NonNull Application application) {
        super(application);
        if (dataRepository != null) {
            return;
        }
        dataRepository = DataRepository.getInstance(); // gets singleton DataRepo object
        dataWrapper = dataRepository.getDataWrapper();
        toastText = new MutableLiveData<>();

    }

    public boolean isLoggedIn() {
        return dataRepository.isLoggedIn();
    }
    public boolean isVolunteer() {
        return dataRepository.isVolunteer();
    }


    public void setVolunteer(boolean isVolunteer) {
        dataRepository.setVolunteer(isVolunteer);
    }
    public void signOut() {
        dataRepository.getFirebaseAuth().signOut();

    }

    public MutableLiveData<String> getToastText() {
        return toastText;
    }
}
