package com.example.nonprofitapp.viewmodels;

import android.app.Application;
import android.app.DownloadManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.nonprofitapp.DataRepository;
import com.example.nonprofitapp.DataWrapper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;

import java.util.Map;

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
