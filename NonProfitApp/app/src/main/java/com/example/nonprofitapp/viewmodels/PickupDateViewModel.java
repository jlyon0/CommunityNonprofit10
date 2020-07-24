package com.example.nonprofitapp.viewmodels;

import android.app.Application;
import android.provider.ContactsContract;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/**
 */

public class PickupDateViewModel extends AndroidViewModel {
    private DataRepository dataRepository;
    private DataWrapper dataWrapper;

    private static final String TAG = PickupDateViewModel.class.getName();


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

    public LiveData<List<Integer>> getValidDays() {
        MutableLiveData<List<Integer>> validDays = new MutableLiveData<>();
        dataRepository.getFoodBank().get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        try {
                            ArrayList<Long> days = (ArrayList<Long>) documentSnapshot.get("days");
                            ArrayList<Integer> intDays = new ArrayList<>();
                            for (long day : days) {
                                intDays.add(Math.toIntExact(day));
                            }
                            validDays.setValue(intDays);
                        } catch (Exception e) {
                            Log.e(TAG, "failed to get valid days", e);
                            validDays.setValue(daysOnFail());
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                validDays.setValue(daysOnFail());
            }
        });
        return validDays;
    }

    private List<Integer> daysOnFail() {
        return Arrays.asList(Calendar.MONDAY, Calendar.TUESDAY, Calendar.WEDNESDAY, Calendar.THURSDAY, Calendar.FRIDAY);
    }


}
