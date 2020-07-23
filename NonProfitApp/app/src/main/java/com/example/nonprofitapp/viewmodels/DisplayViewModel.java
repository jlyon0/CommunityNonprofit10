package com.example.nonprofitapp.viewmodels;

import android.app.Application;
import android.util.Log;

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


public class DisplayViewModel extends AndroidViewModel {
    private DataRepository dataRepository;
    private DataWrapper dataWrapper;
    private MutableLiveData<String> toastText;

    private static final String TAG = DisplayViewModel.class.getName();

    public DisplayViewModel(@NonNull Application application) {
        super(application);
        if (dataRepository != null) {
            return;
        }
        dataRepository = DataRepository.getInstance(); // gets singleton DataRepo object
        dataWrapper = dataRepository.getDataWrapper();
        toastText = new MutableLiveData<>();
    }

    public int getColor() {
        return dataWrapper.getColor();
    }

    public String getFirstName() {
        // regex for replacing everything after the first space
        return dataWrapper.getDisplayName().substring(0, dataWrapper.getDisplayName().indexOf(" ") + 2) + ".";
    }

    public void deleteOrder() {
        String uid = dataRepository.getUser().getUid();
        dataRepository.getFoodBankOrders().document(uid).delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i(TAG, "successfully deleted");
                        toastText.setValue("Successfully cancelled order.");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception error) {
                        Log.i(TAG, "failed delete", error);
                        if ((error.getMessage() != null)
                                && error.getMessage().contains("PERMISSION_DENIED")) {
                            toastText.setValue("Sorry, you don't have permission to delete orders." +
                                    "Try advancing their progress instead!");
                        }
                    }
                });
    } // deleteOrder()

    public void markOrderCompleted() {
        final String uid = dataRepository.getUser().getUid();
        dataRepository.getFoodBankOrders().document(uid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                DataWrapper orderToAdvance = documentSnapshot.toObject(DataWrapper.class);
                if (orderToAdvance == null) {
                    Log.i(TAG, String.format("Order to advance uid: \"%s\" is null", uid));
                    return;
                }
                orderToAdvance.setProgress(DataWrapper.PROGRESS_DELIVERED);
                dataRepository.getFoodBankOrders().document(uid).set(orderToAdvance)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.i(TAG, "Advanced succesfully " + uid);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.i(TAG, "Failed to advance " + uid);
                            }
                        }); // set listeners
            }
        }); // get listeners

    }
}
