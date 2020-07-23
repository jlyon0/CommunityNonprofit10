package com.example.nonprofitapp.viewmodels;

import android.app.Application;
import android.util.Log;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.nonprofitapp.DataRepository;
import com.example.nonprofitapp.DataWrapper;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;

/**
 */

public class BagSelectViewModel extends AndroidViewModel {
    private DataRepository dataRepository;
    private DataWrapper dataWrapper;

    private static final String TAG = BagSelectViewModel.class.getName();


    public BagSelectViewModel(@NonNull Application application) {
        super(application);
        if (dataRepository != null) {
            return;
        }
        dataRepository = DataRepository.getInstance(); // gets singleton DataRepo object
        dataWrapper = dataRepository.getDataWrapper();
    }
    public void setBag(String bag) {
        dataWrapper.setBag(bag);
    }

    /**
     * This method is to attempt to decrement the amount of a bag in a foodbanks inventory. Because
     * of the manual firebase data entry required for it not to crash, I'm suggesting that we
     * do not *use* this function in the stable branch.
     *
     *
     * @param bag
     * @return
     */
    public LiveData<Boolean> tryToSelectBag(String bag) {
        MutableLiveData<Boolean> selectIsPossible = new MutableLiveData<>();
        final String amountName = "amount"; // the fieldname of the amount left in firebase
        Log.i(TAG, "Entered inv tryToSelectBag: " + bag);
        dataRepository.setBag(bag);
        dataRepository.getBag().get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                int amount = Math.toIntExact((long) documentSnapshot.get(amountName));
                if (amount <= 0) { // I don't know how it would get less than, but I'm allergic to equals signs
                    selectIsPossible.setValue(false);
                } else {
                    dataRepository.getBag().update(amountName, FieldValue.increment(-1))
                            .addOnSuccessListener((aVoid -> {
                                selectIsPossible.setValue(true);
                            }))
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // don't let them choose a bag
                                    Log.i(TAG, "Failed to decrement: " + bag);
                                    selectIsPossible.setValue(false);
                                }
                            });
                }

            }
        });
        return selectIsPossible;
    }
}
