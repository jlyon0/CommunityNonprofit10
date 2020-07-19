package com.example.nonprofitapp.viewmodels;

import android.app.Application;
import android.graphics.Color;
import android.util.Log;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.nonprofitapp.DataRepository;
import com.example.nonprofitapp.DataWrapper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * An example class of a ViewModel.
 *
 * There is no constructor here because it should use the ViewModel constructor. ViewModels should be
 * created with ViewModelProviders.of(this).get(ViewModelExample.class); in the activity where it
 * will be used. We shouldn't interact with the constructor.
 */

public class VolunteerViewModel extends AndroidViewModel {
    private DataRepository dataRepository;

    private MutableLiveData<ArrayList<DataWrapper>> liveOrders;
    private MutableLiveData<String> toastText;

    // for the recyclerview:
    //private ArrayList<DataWrapper> orders;

    private static final String TAG = VolunteerViewModel.class.getName();


    public VolunteerViewModel(@NonNull Application application) {
        super(application);
        Log.i(TAG, "init");
        if (dataRepository != null) {
            return;
        }
        dataRepository = DataRepository.getInstance(); // gets singleton DataRepo object
        // init toastText
        toastText = new MutableLiveData<>();
        // set the live data
        liveOrders = new MutableLiveData<>();
        liveOrders.setValue(new ArrayList<DataWrapper>());
        //orders = new ArrayList<>();


        // remember that initial get is called through activity so it can show progress

        listenInRealtime(); // Aaay it works!!
//        ArrayList<DataWrapper> temp = liveOrders.getValue();
//        temp.addAll(generateFakeOrders(50));
//        liveOrders.setValue(temp);

//        for (DataWrapper order : generateFakeOrders(10)) {
//            Log.i(TAG, "Added a fake order, uid: \"" + order.getUid() + "\"");
//            dataRepository.getFoodBankOrders().document(order.getUid()).set(order);
//        }
    }

    /**
     * Sets up the listeners that allow real time updates. Note that this will eat data, so it
     * may not be a good idea to set it as a default.
     */
    public void listenInRealtime() {
        dataRepository.getFoodBankOrders()
                //.whereEqualTo("isComplete",false) // this only returns things which match
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        Log.i(TAG, "Realtime update!");
                        if (error != null) {
                            Log.w(TAG, "Listen failed.", error);
                            return;
                        }

                        addDocuments(value.getDocuments()); // change if using whereEqualTo
                    }
                });
    }

    /**
     * Gets orders from firebase, assuming setFoodBank has been called already. If there's a
     * Nullpointer it's because the foodBankOrders hasn't been set. This method uses LiveData in
     * such a way that it should not block the main thread. It sets the input mOrders object to the
     * list of orders retrieved from firebase.
     *
     * For more than anyone wanted to know about tasks, check out this blog series:
     * https://firebase.googleblog.com/2016/09/become-a-firebase-taskmaster-part-1.html
     * @return
     */
    public Task<QuerySnapshot> fetchOrdersLive() {
        // final value is still able to be changed with setValue
        Log.i(TAG, "Entered GetOrders");
        return dataRepository.getFoodBankOrders().get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                // this happens on the main thread, but way after the func finishes.

                if (task.isSuccessful()) {
                    Log.i(TAG, "Successful!");
                    addDocuments(task.getResult().getDocuments());
                } else {
                    Log.e(TAG, "Could not retrieve data.");
                    if (task.getException() != null) {
                        task.getException().printStackTrace();
                    }
                } /* task.isSuccessful */
            } /* onComplete()*/
        });
    } /* fetchOrdersLive() */

    /**
     * Method to add a list of {@code DocumentSnapshot} objects to the livedata list.
     * Used to keep live and manual fetching consistent.
     * @param documentSnapshots
     */
    public void addDocuments(List<DocumentSnapshot> documentSnapshots) {
        ArrayList<DataWrapper> orders = liveOrders.getValue();
        orders.clear();
        for (DocumentSnapshot document : documentSnapshots) {
            DataWrapper thisOrder = document.toObject(DataWrapper.class);
            if (thisOrder != null) {
                orders.add(thisOrder);
            }
        }
        liveOrders.setValue(orders);
    }

    private ArrayList<DataWrapper> generateFakeOrders(int howMany) {
        Log.i(TAG, "generateFakeOrders: " + howMany);
        Random random = new Random();
        ArrayList<DataWrapper> fakeOrders = new ArrayList<>();
        for (int i = 0; i < howMany; i++) {
            // nextInt bound is exclusive
            int color = Color.argb(255, random.nextInt(255 + 1),random.nextInt(255 + 1),random.nextInt(255 + 1));
            int foodBankInt = i % 4 + 1;
            DataWrapper temp = new DataWrapper("namenumber" + i,
                    "At food bank " + foodBankInt,
                    "button" + foodBankInt,
                    "bag " + foodBankInt,
                    2020,
                    random.nextInt(13),
                    random.nextInt(32),
                    random.nextInt(24),
                    random.nextInt(60),
                    color);
            temp.setUid(""+i);
            fakeOrders.add(temp);
        }
        return fakeOrders;
    } /* generateFakeOrders() */

    public MutableLiveData<ArrayList<DataWrapper>> getLiveOrders() {
        return liveOrders;
    }

    public void deleteOrder(ArrayList<String> uids) {
        final MutableLiveData<Boolean> saidNo = new MutableLiveData<>(false);
        if (uids.size() < 1) {
            toastText.setValue("No orders selected.");
            return;
        }
        for (String uid : uids) {
            dataRepository.getFoodBankOrders().document(uid).delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.i(TAG, "successfully deleted");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception error) {
                            Log.i(TAG, "failed delete", error);
                            if ((error.getMessage() != null)
                                    && error.getMessage().contains("PERMISSION_DENIED")
                                    && !saidNo.getValue()) {
                                toastText.setValue("Sorry, you don't have permission to delete orders." +
                                        "Try advancing their progress instead!");
                                saidNo.setValue(true);
                            }
                        }
                    });
        }
    }

    public void advanceProgress(ArrayList<String> uids) {
        if (uids.size() < 1) {
            toastText.setValue("No orders selected.");
            return;
        }
        for (final String uid : uids) {
            dataRepository.getFoodBankOrders().document(uid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    DataWrapper orderToAdvance = documentSnapshot.toObject(DataWrapper.class);
                    if (orderToAdvance == null) {
                        Log.i(TAG, String.format("Order to advance uid: \"%s\" is null"));
                        return;
                    }
                    int progress = orderToAdvance.getProgress();
                    if (progress > 2) {
                        toastText.setValue("Already completed.");
                        return;
                    }
                    progress++;
                    orderToAdvance.setProgress(progress);
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
        } // uid loop
    } // advanceProgress()

    public void decrementProgress(ArrayList<String> uids) {
        if (uids.size() < 1) {
            toastText.setValue("No orders selected.");
            return;
        }
        for (final String uid : uids) {
            dataRepository.getFoodBankOrders().document(uid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    DataWrapper orderToAdvance = documentSnapshot.toObject(DataWrapper.class);
                    if (orderToAdvance == null) {
                        Log.i(TAG, String.format("Order to advance uid: \"%s\" is null"));
                        return;
                    }
                    int progress = orderToAdvance.getProgress();
                    if (progress < 1) {
                        toastText.setValue("This order wasn't started.");
                        return;
                    }
                    progress--;
                    orderToAdvance.setProgress(progress);
                    dataRepository.getFoodBankOrders().document(uid).set(orderToAdvance)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.i(TAG, "Decremented succesfully " + uid);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.i(TAG, "Failed to decrement " + uid);
                                }
                            }); // set listeners
                }
            }); // get listeners
        } // uid loop
    } // decrementProgress()


    public LiveData<String> getToastText() {
        return toastText;
    }
/*
    public ArrayList<DataWrapper> getOrders() {
        return orders;
    }

    public void setOrders(ArrayList<DataWrapper> orders) {
        this.orders = orders;
    }
*/
}
