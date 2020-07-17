package com.example.nonprofitapp.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.nonprofitapp.DataRepository;
import com.example.nonprofitapp.DataWrapper;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;

/**
 * An example class of a ViewModel.
 *
 * There is no constructor here because it should use the ViewModel constructor. ViewModels should be
 * created with ViewModelProviders.of(this).get(ViewModelExample.class); in the activity where it
 * will be used. We shouldn't interact with the constructor.
 */

public class FoodBankViewModel extends AndroidViewModel {
    private DataRepository dataRepository;
    private DataWrapper dataWrapper;
    // some live data e.g.
    private MutableLiveData<Boolean> hasOrder;


    public FoodBankViewModel(@NonNull Application application) {
        super(application);
        if (dataRepository != null) {
            return;
        }
        dataRepository = DataRepository.getInstance(); // gets singleton DataRepo object
        dataWrapper = dataRepository.getDataWrapper();
    }

    public void volunteerValidation() {
        if (dataRepository.isVolunteer()) {

        }
    }

    public void validateEmail() {
        dataRepository.getFirebaseAuth().getCurrentUser().sendEmailVerification();
        dataRepository.initUser();
    }

    public boolean isLoggedIn() {
        dataRepository.initUser();
        return (dataRepository.getUser() != null);
    }

    public void signOut() {
        dataRepository.getFirebaseAuth().signOut();
    }

    public boolean isVolunteer() {
        return dataRepository.isVolunteer();
    }

    /**
     * Returns whether or not firebase has an uncompleted order for this user.
     * @return
     */
    public LiveData<Boolean> hasOrder() {
        dataRepository.getFoodBankOrders().document(dataRepository.getUser().getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        DataWrapper fromFirebase = documentSnapshot.toObject(DataWrapper.class);
                        if ((fromFirebase != null) && !fromFirebase.isCompleted()) {
                            dataRepository.setDataWrapper(fromFirebase);
                            hasOrder.setValue(true);
                        } else {
                            hasOrder.setValue(false);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        hasOrder.setValue(false);
                    }
                });
        return hasOrder;
    }

}
