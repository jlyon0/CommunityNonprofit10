package com.example.nonprofitapp;

import android.provider.ContactsContract;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

public class DataRepository {
    private static DataRepository sInstance;
    private DataWrapper dataWrapper;

    private FirebaseFirestore db;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;

    private CollectionReference foodBankOrders;
    private DocumentReference foodBank;

    boolean isVolunteer;

    private static final String TAG = DataRepository.class.getName();

    /**
     * Should be accessed through getInstance().
     */
    private DataRepository() {
        dataWrapper = new DataWrapper();
        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    public static DataRepository getInstance() {
        if (sInstance == null) {
            synchronized (DataRepository.class) {
                if (sInstance == null) {
                    sInstance = new DataRepository();
                }
            }
        }
        return sInstance;
    }

    public void setDb(FirebaseFirestore db) {
        this.db = db;
    }

    public void setFirebaseAuth(FirebaseAuth firebaseAuth) {
        this.firebaseAuth = firebaseAuth;
    }

    public DataWrapper getDataWrapper() {
        return dataWrapper;
    }

    /**
     * Call when you need to reset user.
     */
    public void initUser() {
        if (user != null) {
            user.reload();
        }
        user = firebaseAuth.getCurrentUser();
    }

    /**
     * Normal getter
     * @return
     */
    public FirebaseUser getUser() {
        return user;
    }


    public boolean isLoggedIn() {
        initUser();
        return (getUser() != null);
    }

    public void setFoodBank(String foodBankName) {
        foodBank = db.collection("/foodbanks/").document(foodBankName);
        foodBankOrders = foodBank.collection("orders");
    }


    /**
     * Messing with getting orders and returning a normal object. This is going to eat time on the
     * main thread, so it is not ideal. Uses class wide object orders.
     */
    //private Task<QuerySnapshot> orders = new ArrayList<>();
    synchronized public Task<QuerySnapshot> getOrdersNonLive() {
        Log.i(TAG, "Launched task");
        return foodBankOrders.get();
    }

    public void validateEmail() {
        firebaseAuth.getCurrentUser().sendEmailVerification();
    }

    public FirebaseFirestore getDb() {
        return db;
    }

    public FirebaseAuth getFirebaseAuth() {
        return firebaseAuth;
    }

    public CollectionReference getFoodBankOrders() {
        return foodBankOrders;
    }

    public DocumentReference getFoodBank() {
        return foodBank;
    }

    public void setDataWrapper(DataWrapper dataWrapper) {
        this.dataWrapper = dataWrapper;
    }

    public boolean isVolunteer() {
        return isVolunteer;
    }

    public void setVolunteer(boolean volunteer) {
        isVolunteer = volunteer;
    }
}
