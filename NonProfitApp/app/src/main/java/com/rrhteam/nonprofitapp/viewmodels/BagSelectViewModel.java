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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 */

public class BagSelectViewModel extends AndroidViewModel {
    private DataRepository dataRepository;
    private DataWrapper dataWrapper;

    public static final int BAGS = 0;
    public static final int DESCRIPTIONS = 1;

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

    public LiveData<ArrayList<ArrayList<String>>> getBags() {
        MutableLiveData<ArrayList<ArrayList<String>>> data = new MutableLiveData<>();
        dataRepository.getBags().get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        Log.i(TAG, "Success");
                        if (queryDocumentSnapshots.getDocuments().size() == 0) {
                            data.setValue(getBagsOnFailure());
                            return;
                        }
                        ArrayList<String> bags = new ArrayList<>();
                        ArrayList<String> descriptions = new ArrayList<>();
                        for (DocumentSnapshot bag : queryDocumentSnapshots) {
                            bags.add(bag.getId());
                            if (bag.get("description") != null) {
                                descriptions.add((String)bag.get("description"));
                            } else {
                                descriptions.add("No description for this place, sorry.");
                            }
                        }
                        ArrayList<ArrayList<String>> thisData = new ArrayList<>();
                        thisData.add(bags);
                        thisData.add(descriptions);
                        data.setValue(thisData);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i(TAG, "Failure");
                        data.setValue(getBagsOnFailure());
                    }
                });
        return data;
    }

    private ArrayList<ArrayList<String>> getBagsOnFailure() {
        ArrayList<String> buttonNames;
        ArrayList<String> bagDescriptions;
        buttonNames = new ArrayList<>();
        bagDescriptions = new ArrayList<>();

        // some random test buttons for now
        buttonNames.add("Kids");
        bagDescriptions.add("2 boxes of Craft Mac and Cheese, 2 cases of caprisun, 1 box of apple sauce, and 2 cans of Spaghettio's.");
        buttonNames.add("Vegan");
        bagDescriptions.add("Probably some vegetables and dirt.");
        buttonNames.add("Nut Free");
        bagDescriptions.add("2 boxes of spaghetti, 2 cans of tomato sauce, 2 cans of black beans, 1 can of corn.");
        buttonNames.add("Dairy Free");
        bagDescriptions.add("2 cans of black beans, 2 boxes of spaghetti, 1 can of tomato sauce, 1 can of corn.");
        ArrayList<ArrayList<String>> thisData = new ArrayList<>();
        thisData.add(buttonNames);
        thisData.add(bagDescriptions);

        return thisData;
    }

}
