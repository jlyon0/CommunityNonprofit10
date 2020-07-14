package com.example.nonprofitapp;

import androidx.lifecycle.LiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class DataRepository {
    private static DataRepository sInstance;
    private DataWrapper dataWrapper;

    private FirebaseFirestore firestore;
    private FirebaseAuth firebaseAuth;

    public DataRepository() {
        dataWrapper = new DataWrapper();
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
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

    public LiveData<FirebaseUser> getUser() {

    }

    public void createOrder() {
        //TODO: takes data from dataWrapper and input to firebase
    }

}
