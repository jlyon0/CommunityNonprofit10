package com.example.nonprofitapp.viewmodels;

import android.app.Application;
import android.graphics.Color;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.nonprofitapp.DataRepository;
import com.example.nonprofitapp.DataWrapper;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;

import java.util.Random;

public class ConfirmationViewModel extends AndroidViewModel {
    public static final String COLORS = "colors";
    private DataRepository dataRepository;
    private DataWrapper dataWrapper;
    // 26, shades of pretty colors, hard to distinguish.
//    private int[][] RGBS = {{2,63,165},{125,135,185},{190,193,212},{214,188,192},{187,119,132},
//            {142,6,59},{74,111,227},{133,149,225},{181,187,227},{230,175,185},{224,123,145},
//            {211,63,106},{17,198,56},{141,213,147},{198,222,199},{234,211,198},{240,185,141},
//            {239,151,8},{15,207,192},{156,222,214},{213,234,231},{243,225,235},{246,196,225},
//            {247,156,212}};
    // 18, accessible to 99% of population. Thinking if we use two, theres 324 options
//    public static final int[][] RGBS = {{230, 25, 75}, {60, 180, 75}, {255, 225, 25}, {0, 130, 200},
//        {245, 130, 48}, {70, 240, 240}, {240, 50, 230}, {250, 190, 212}, {0, 128, 128},
//        {220, 190, 255}, {170, 110, 40}, {255, 250, 200}, {128, 0, 0}, {170, 255, 195},
//        {0, 0, 128}, {128, 128, 128}, {255, 255, 255}, {0, 0, 0}};
    // 22, accessible to 95% of population
    public static final int[][] RGBS = {{230, 25, 75}, {60, 180, 75}, {255, 225, 25}, {0, 130, 200},
            {245, 130, 48}, {145, 30, 180}, {70, 240, 240}, {240, 50, 230}, {210, 245, 60},
            {250, 190, 212}, {0, 128, 128}, {220, 190, 255}, {170, 110, 40}, {255, 250, 200},
            {128, 0, 0}, {170, 255, 195}, {128, 128, 0}, {255, 215, 180}, {0, 0, 128},
            {128, 128, 128}, {255, 255, 255}, {0, 0, 0}};


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
        setWrapper();
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
        if (dataWrapper.getHour() >= 12) {
            if (dataWrapper.getHour() != 12) {
                timeString.append(dataWrapper.getHour() - 12);
            }
            amOrPm = PM;
        } else {
            if (dataWrapper.getHour() == 0) {
                timeString.append("12");
            } else {
                timeString.append(dataWrapper.getHour());
            }
            amOrPm = AM;
        }
        timeString.append(":");
        if (dataWrapper.getMinute() < 10) timeString.append(0);
        timeString.append(dataWrapper.getMinute());
        timeString.append(amOrPm);
        return timeString.toString();
    } /* getTimeString() */

    public void setWrapper(){
        dataWrapper.setUid(dataRepository.getUser().getUid());
        dataWrapper.setDisplayName(dataRepository.getUser().getDisplayName());
        dataWrapper.setCompleted(false);
        dataWrapper.setProgress(0);
    }

    /**
     * Gets and sets the order's color in a non-random way.
     *
     * Increments or sets a value corresponding to the number of orders placed at this time of day
     * at this foodbank. Then gets that value, uses it as an index in an array of rgb color values.
     * Creates a fully opaque color with that rgb value, and sets the order color to that color.
     * The idea behind this was to make it less likely for people showing up in the same hour to
     * have the same colors, but it's unclear if this will help.
     * @return
     */
    public MutableLiveData<Boolean> getAndSetColor() {
        final MutableLiveData<Boolean> done = new MutableLiveData<>(false);
        Log.i(TAG, "Getting and setting color");

        String hour = String.valueOf(dataWrapper.getHour());
        dataRepository.getFoodBankOrders().document(COLORS)
                .update(hour, FieldValue.increment(1))
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Failed to update color for " + dataWrapper.getHour(), e);
                        setColorOnFail();
                    }
                })
                .addOnSuccessListener((aVoid) -> {
                            // if incrementing was successful, get color.
                            dataRepository.getFoodBankOrders().document(COLORS)
                                    .get()
                                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            if (!documentSnapshot.contains(hour)) {
                                                setColorOnFail();
                                            }
                                            long colorIndex = (long) documentSnapshot.get(hour);
                                            dataWrapper.setColor(colorFromIndex(colorIndex));
                                            done.setValue(true);
                                        }
                                    })
                                    .addOnFailureListener((error) -> {
                                        Log.e(TAG, "Failed to retrieve color for " + dataWrapper.getHour(), error);
                                        setColorOnFail();
                                        done.setValue(true);
                                    });
                        }
                );
        return done;
    }
    public int colorFromIndex(long colorIndex) {
        int intColorIndex = Math.toIntExact(colorIndex % RGBS.length); // required in java 8
        int[] rgb = RGBS[intColorIndex];
        return Color.argb(255,rgb[0], rgb[1], rgb[2]);
    }
    public void setColorOnFail() {
        Log.i(TAG, "Setting random color");
        dataWrapper.setColor(new Random().nextInt(RGBS.length));
    }

    public void sendDataToFireBase() {



        Log.i(TAG, "SendData was triggered");

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
