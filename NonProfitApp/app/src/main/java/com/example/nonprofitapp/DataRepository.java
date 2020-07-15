package com.example.nonprofitapp;

import android.provider.ContactsContract;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

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

    public void initUser() {
        user = firebaseAuth.getCurrentUser();
    }
    public FirebaseUser getUser() {
        return user;
    }
    public void setFoodBank(String foodBankName) {
        foodBank = db.collection("/foodbanks/").document(foodBankName);
        foodBankOrders = foodBank.collection("orders");
    }
    public ArrayList<DataWrapper> getOrders() {
        final MutableLiveData<ArrayList<DataWrapper>> mOrders = new MutableLiveData<>();
        mOrders.setValue(new ArrayList<DataWrapper>());
        ArrayList<DataWrapper> orders = new ArrayList<>();
        foodBankOrders.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    Log.i(TAG, "Successful!");
                    for (DocumentSnapshot document : task.getResult().getDocuments()) {
                        ArrayList<DataWrapper> orders = mOrders.getValue();
                        orders.add(document.toObject(DataWrapper.class));

                    }
                }
            }
        });
        return null;
    }

    public void validateEmail() {
        firebaseAuth.getCurrentUser().sendEmailVerification();
    }

    public void setDocument() {
        //TODO: takes data from dataWrapper and input to firebase
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
}
