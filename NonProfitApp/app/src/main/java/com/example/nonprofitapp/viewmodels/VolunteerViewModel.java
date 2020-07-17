package com.example.nonprofitapp.viewmodels;

import android.app.Application;
import android.graphics.Color;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.nonprofitapp.DataRepository;
import com.example.nonprofitapp.DataWrapper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
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
    // for the recyclerview:
    private ArrayList<DataWrapper> orders;

    private static final String TAG = VolunteerViewModel.class.getName();


    public VolunteerViewModel(@NonNull Application application) {
        super(application);
        Log.i(TAG, "init");
        if (dataRepository != null) {
            return;
        }
        dataRepository = DataRepository.getInstance(); // gets singleton DataRepo object
        // set the live data
        liveOrders = new MutableLiveData<>();
        orders = new ArrayList<>();
        dataRepository.setFoodBank("Gleaners");

    }

//    public void init() {
//        Log.i(TAG, "init");
//        if (dataRepository != null) {
//            return;
//        }
//        dataRepository = DataRepository.getInstance(); // gets singleton DataRepo object
//        // set the live data
//        orders = new MutableLiveData<>();
//    }

    public MutableLiveData<ArrayList<DataWrapper>> fetchOrdersLive() {
        // updates orders usingk setvalue
        //getOrdersLive(orders);
        dataRepository.getOrdersNonLive();
//        Random random = new Random();
//        ArrayList<DataWrapper> fakeorders = generateFakeOrders(random.nextInt(20));
//        orders.setValue(fakeorders);
        return liveOrders;
    }

    /**
     * Gets orders from firebase, assuming setFoodBank has been called already. If there's a
     * Nullpointer it's because the foodBankOrders hasn't been set. This method uses LiveData in
     * such a way that it should not block the main thread. It sets the input mOrders object to the
     * list of orders retrieved from firebase.
     *
     * @andrea should this be in dataRepo?
     *
     * For more than anyone wanted to know about tasks, check out this blog series:
     * https://firebase.googleblog.com/2016/09/become-a-firebase-taskmaster-part-1.html
     * @return
     */
    public Task<QuerySnapshot> getOrdersLive(@NonNull final MutableLiveData<ArrayList<DataWrapper>> liveOrders) {
        // final value is still able to be changed with setValue
        Log.i(TAG, "Entered GetOrders");
        return dataRepository.getFoodBankOrders().get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                // this happens on the main thread, but way after the func finishes.

                if (task.isSuccessful()) {
                    Log.i(TAG, "Successful!");
                    ArrayList<DataWrapper> orders = new ArrayList<>();
                    for (DocumentSnapshot document : task.getResult().getDocuments()) {
                        orders.add(document.toObject(DataWrapper.class));
                    }
                    liveOrders.setValue(orders);
                } else {
                    Log.e(TAG, "Could not retrieve data.");
                    task.getException().printStackTrace();

                }
            }
        });
    }


    public Task<QuerySnapshot> fetchOrdersNonLive() {
        return dataRepository.getOrdersNonLive().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                Log.i(TAG, "entered viewModel oncomplete");
                if (task.isSuccessful()) {
                    orders.clear();
                    for (DocumentSnapshot document : task.getResult().getDocuments()) {
                        orders.add(document.toObject(DataWrapper.class));
                    }
                    Log.i(TAG, "Success, now notifying.");
                }
            }
        });
    }
    private ArrayList<DataWrapper> generateFakeOrders(int howMany) {
        Log.i(TAG, "generateFakeOrders: " + howMany);
        Random random = new Random();
        ArrayList<DataWrapper> fakeOrders = new ArrayList<>();
        for (int i = 0; i < howMany; i++) {
            int color = Color.argb(255, random.nextInt(255 + 1),random.nextInt(255 + 1),random.nextInt(255 + 1));
            int foodBankInt = i % 4 + 1;
            fakeOrders.add(new DataWrapper("namenumber" + i,
                    "At food bank " + foodBankInt,
                    "button" + foodBankInt,
                    "bag " + foodBankInt,
                    2020,
                    1,
                    1,
                    1,
                    1,
                    color));
        }
        return fakeOrders;
    } /* generateFakeOrders() */

    public ArrayList<DataWrapper> getOrders() {
        return orders;
    }

    public void setOrders(ArrayList<DataWrapper> orders) {
        this.orders = orders;
    }
}
