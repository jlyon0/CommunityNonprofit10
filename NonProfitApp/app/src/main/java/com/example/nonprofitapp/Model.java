package com.example.nonprofitapp;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Hold the data for the order we eventually give to firebase, and interact with Firebase. Note
 * that this model would persist throughout the activation of multiple activities because it's
 * a property of MyApplication.
 *
 * I'm rapidly being convinced that even a halfway done implementation of MVVM will be better than
 * this, so I've stopped working on it.
 */
public class Model {
    String foodBank;
    DataWrapper data;
    boolean isVolunteer;
    FirebaseUser user;

    public Model() {
        data = new DataWrapper();
        isVolunteer = false;
        // if this is null, setUser after the firebase login screen.
        user = FirebaseAuth.getInstance().getCurrentUser();
    }

    public void setUser() {
        user = FirebaseAuth.getInstance().getCurrentUser();
    }

    public String getFoodBank() {
        return foodBank;
    }

    public void setFoodBank(String foodBank) {
        this.foodBank = foodBank;
    }

    public DataWrapper getData() {
        return data;
    }

    public boolean isVolunteer() {
        return isVolunteer;
    }

    public void setVolunteer(boolean volunteer) {
        isVolunteer = volunteer;
    }

    public void fetchFirebaseData() {
    }
    public void placeOrModifyOrder() {

    }
}
