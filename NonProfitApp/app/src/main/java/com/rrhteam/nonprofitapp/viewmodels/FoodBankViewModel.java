package com.rrhteam.nonprofitapp.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.rrhteam.nonprofitapp.DataRepository;
import com.rrhteam.nonprofitapp.DataWrapper;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/**
 * An example class of a ViewModel.
 *
 * There is no constructor here because it should use the ViewModel constructor. ViewModels should be
 * created with ViewModelProviders.of(this).get(ViewModelExample.class); in the activity where it
 * will be used. We shouldn't interact with the constructor.
 */

public class FoodBankViewModel extends AndroidViewModel {
    public static final int FOODBANKS = 0;
    public static final int DESCRIPTIONS = 1;

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

    /**
     * Returns a boolean describing whether or not the food bank could be set.
     * @param fb
     * @return
     */
    public boolean setFoodBank(String fb) {
        if ((fb == null ) || (fb.length() == 0)) {
            toastText.setValue("Choose a food bank or pantry first.");
            return false;
        }
        dataRepository.setFoodBank(fb);
        dataWrapper.setFoodBank(fb);
        return true;
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
                        if ((fromFirebase != null) && (fromFirebase.getProgress() <= 2)) {
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

    public LiveData<ArrayList<ArrayList<String>>> getFoodBanks() {
        MutableLiveData<ArrayList<ArrayList<String>>> data = new MutableLiveData<>();
        dataRepository.getFoodBanks().get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots.getDocuments().size() == 0) {
                            data.setValue(getBanksOnFailure());
                            return;
                        }
                        ArrayList<String> foodbanks = new ArrayList<>();
                        ArrayList<String> descriptions = new ArrayList<>();
                        for (DocumentSnapshot foodBank : queryDocumentSnapshots) {
                            foodbanks.add(foodBank.getId());
                            if (foodBank.get("description") != null) {
                                descriptions.add((String)foodBank.get("description"));
                            } else {
                                descriptions.add("No description for this place, sorry.");
                            }
                        }
                        ArrayList<ArrayList<String>> thisData = new ArrayList<>();
                        thisData.add(foodbanks);
                        thisData.add(descriptions);
                        data.setValue(thisData);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        data.setValue(getBanksOnFailure());
                    }
                });
        return data;
    }

    private ArrayList<ArrayList<String>> getBanksOnFailure() {
        ArrayList<String> buttonNames;
        ArrayList<String> bankAddresses;
        buttonNames = new ArrayList<>();
        bankAddresses = new ArrayList<>();
        // some random test buttons for now
        buttonNames.add("Gleaners");
        bankAddresses.add("3737 Waldemere Ave, Indianapolis, IN 46241");
        //buttonNames.add("Midwest Food Bank");
        //bankAddresses.add("6450 S Belmont Ave, Indianapolis, IN 46217");
        buttonNames.add("Marion County: CARE Mobile Pantries");
        bankAddresses.add("Mondays – Marion County Election Board 3737 E. Washington St. Indianapolis 2-6P\n" +
                "Fridays – Ivy Tech Community College 2535 N. Capitol St. Indianapolis  2-6P\n" +
                "Saturdays – John Marshall High School 10101 E. 38th St. Indianapolis 10A-2P\n" +
                "7/27 – Moorhead Elementary School  8400 E. 10th Street, Indianapolis 4-6P\n" +
                "7/30 – Lawrence Community Park  5301 N Franklin Rd Indianapolis  4-6P\n" +
                "8/11 – Franklin Cove Apartments  8505 Faywood Dr. Indianapolis  9A (drop and go)");
        ArrayList<ArrayList<String>> thisData = new ArrayList<>();
        thisData.add(buttonNames);
        thisData.add(bankAddresses);

        return thisData;
    }


    private List<Integer> daysOnFail() {
        return Arrays.asList(Calendar.MONDAY, Calendar.TUESDAY, Calendar.WEDNESDAY, Calendar.THURSDAY, Calendar.FRIDAY);
    }


    public MutableLiveData<String> getToastText() {
        return toastText;
    }
}
