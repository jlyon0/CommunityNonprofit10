package com.example.nonprofitapp.viewmodels;

import android.app.Application;
import android.graphics.Color;
import android.provider.ContactsContract;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.example.nonprofitapp.DataRepository;
import com.example.nonprofitapp.DataWrapper;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.type.Date;

import java.time.LocalDateTime;
import java.util.HashMap;

/**
 * An example class of a ViewModel.
 *
 * There is no constructor here because it should use the ViewModel constructor. ViewModels should be
 * created with ViewModelProviders.of(this).get(ViewModelExample.class); in the activity where it
 * will be used. We shouldn't interact with the constructor.
 */

public class ConfirmationViewModel extends AndroidViewModel {
    private DataRepository dataRepository;
    private DataWrapper dataWrapper;
    // some live data e.g.
    private LiveData<FirebaseUser> liveUser;


    private MutableLiveData<String> toastText;


    // am/pm consts
    private final String AM = " a.m.";
    private final String PM = " p.m.";

    private static final String TAG = ConfirmationViewModel.class.getName();


    public ConfirmationViewModel(@NonNull Application application) {
        super(application);
        if (dataRepository != null) {
            return;
        }

        toastText = new MutableLiveData<>();

        dataRepository = DataRepository.getInstance(); // gets singleton DataRepo object
        dataWrapper = dataRepository.getDataWrapper();
        /*new DataWrapper("joe",
                "Gleaners",
                "",
                "Nut-Free",
                2020,
                7,
                30,
                22,
                12,
                Color.RED);*/
        dataWrapper.setUid(dataRepository.getUser().getUid());
        dataWrapper.setDisplayName(dataRepository.getUser().getDisplayName());

    }

    public String getConfirmationString() {
        StringBuilder confirmationString = new StringBuilder();
        confirmationString.append("Food Bank: ");
        confirmationString.append(dataWrapper.getFoodBank());
        confirmationString.append("\n");
        confirmationString.append("Bag: ");
        confirmationString.append(dataWrapper.getBag());
        confirmationString.append("\n");
        confirmationString.append("Arriving at ");
        confirmationString.append(getTimeString());
        confirmationString.append(" on ");
        confirmationString.append(getDateString());
        return confirmationString.toString();
    }

    public String getDateString() {
        StringBuilder dateString = new StringBuilder();
        dateString.append(dataWrapper.getMonth());
        dateString.append("/");
        dateString.append(dataWrapper.getDay());
        dateString.append("/");
        dateString.append(dataWrapper.getYear());
        return dateString.toString();
    }

    public String getTimeString() {
        StringBuilder timeString = new StringBuilder();
        // add an am or pm
        String amOrPm = "";
        if (dataWrapper.getHour() > 12) {
            timeString.append(dataWrapper.getHour() - 12);
            amOrPm = PM;
        } else {
            if (dataWrapper.getHour() == 0) {
                timeString.append("12");
            } else {
                timeString.append(dataWrapper.getHour());
                amOrPm = AM;
            }
        }
        timeString.append(":");
        if (dataWrapper.getMinute() < 10) timeString.append(0);
        timeString.append(dataWrapper.getMinute());
        timeString.append(amOrPm);
        return timeString.toString();
    } /* getTimeString() */

    public DataWrapper setWrapper(){

        DataWrapper wrapper = dataRepository.getDataWrapper();
//        wrapper.setFoodBank(foodBankId);
//        wrapper.setBag(bag);
//        wrapper.setYear(year);
//        wrapper.setMonth(month);
//        wrapper.setDay(day);
//        wrapper.setMinute(minute);
        wrapper.setCompleted(false);
        wrapper.setProgress(0);

        return wrapper;
    }

    public void sendDataToFireBase() {
        HashMap<String, Object> data = new HashMap<>();


        Log.i("TAG", "SendData was triggered");
        dataRepository.getFoodBankOrders().document(dataWrapper.getUid())
                .set(dataWrapper)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        toastText.setValue("Order Received!");

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        toastText.setValue("Order Failed");
                    }
                });



    }
    public MutableLiveData<String> getToastText() {
        return toastText;
    }

}
