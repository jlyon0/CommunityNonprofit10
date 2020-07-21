package com.example.nonprofitapp.viewmodels;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.nonprofitapp.DataRepository;
import com.example.nonprofitapp.DataWrapper;
import com.example.nonprofitapp.activities.Foodbank_Selection_Page;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;

import java.util.ArrayList;

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
    private MutableLiveData<String> toastText;
    private boolean hasEmailed = false;

    private static final String TAG = FoodBankViewModel.class.getName();


    public FoodBankViewModel(@NonNull Application application) {
        super(application);
        if (dataRepository != null) {
            return;
        }
        dataRepository = DataRepository.getInstance(); // gets singleton DataRepo object
        dataWrapper = dataRepository.getDataWrapper();
        hasOrder = new MutableLiveData<>();
        toastText = new MutableLiveData<>();
    }

    /**
     * Checks if the volunteer is email validated. If they are, add them to the list of volunteers.
     * @return
     */
    public boolean checkIfVolValid() {
        Log.i(TAG, "checking if valid");
        dataRepository.initUser();
        if (!FirebaseAuth.getInstance().getCurrentUser().isEmailVerified()) {
            Log.i(TAG, "Not email verified");
            // if they aren't verified, verify em
            if (!hasEmailed) {
                dataRepository.getUser().sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        toastText.setValue("Check " + dataRepository.getUser().getEmail()
                                + " for a verification email to continue.");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        toastText.setValue("Failed to send a verification email to " +
                                dataRepository.getUser().getEmail());
                    }
                });
                hasEmailed = true;
            } else {
                toastText.setValue("Check " + dataRepository.getUser().getEmail()
                        + " for a verification email, it may take a second.");
            }

            return false;
        } else {
            Log.i(TAG, "is email verified");
            // update the vol email_list to include this persons email.
            String uid = dataRepository.getUser().getUid();
            //dataRepository.getVolList().document(dataRepository.getUser().getUid());
            return true;
        }
    }

    public void setFoodBank(String fb) {
        dataRepository.setFoodBank(fb);
        dataWrapper.setFoodBank(fb);
    }

    public ArrayList<String> getFoodBankNames() {
        // TODO fix this
        return null;
    }

//    public checkIfVol() {
//        dataRepository.getVolList().
//                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//            @Override
//            public void onSuccess(DocumentSnapshot documentSnapshot) {
//                ArrayList<String> vol_emails = documentSnapshot.getData().get("email_list")
//            }
//        })
//    }

    public boolean isLoggedIn() {
        dataRepository.initUser();
        return (dataRepository.getUser() != null);
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

    public MutableLiveData<String> getToastText() {
        return toastText;
    }

}
