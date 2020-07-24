package com.example.nonprofitapp.viewmodels;

import android.app.Application;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.nonprofitapp.DataRepository;
import com.example.nonprofitapp.DataWrapper;
import com.example.nonprofitapp.activities.PickupDateSelectionActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;

/**
 */

public class PickupTimeViewModel extends AndroidViewModel {
    private DataRepository dataRepository;
    private DataWrapper dataWrapper;

    private static final String TAG = PickupTimeViewModel.class.getName();

    public PickupTimeViewModel(@NonNull Application application) {
        super(application);
        if (dataRepository != null) {
            return;
        }
        dataRepository = DataRepository.getInstance(); // gets singleton DataRepo object
        dataWrapper = dataRepository.getDataWrapper();
    }
    public void setTime(TimePicker timePicker) {
        dataWrapper.setHour(timePicker.getCurrentHour()); // 0 indexed 24hr time
        dataWrapper.setMinute(timePicker.getCurrentMinute());
    }

    public LiveData<Integer[]> getValidHours() {
        MutableLiveData<Integer[]> validHours = new MutableLiveData<>();
        dataRepository.getFoodBank().get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        try {
                            ArrayList<Long> hours = (ArrayList<Long>) documentSnapshot.get("hours");
                            Integer[] intHours = new Integer[2];
                            intHours[0] = Integer.valueOf(Math.toIntExact(hours.get(0)));
                            intHours[1] = Integer.valueOf(Math.toIntExact(hours.get(1)));
                            validHours.setValue(intHours);
                        } catch (Exception e) {
                            Log.e(TAG, "failed to get valid hours", e);
                            validHours.setValue(hoursOnFail());
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        validHours.setValue(hoursOnFail());
                    }
                });
        return validHours;
    }

    private Integer[] hoursOnFail() {
        Integer[] intHours = new Integer[2];
        // sets to 8a - 5p
        intHours[0] = Integer.valueOf(8);
        intHours[1] = Integer.valueOf(17);
        return intHours;
    }
}
