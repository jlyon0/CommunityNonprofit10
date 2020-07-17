package com.example.nonprofitapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

public class VolunteerActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private MyRecyclerViewAdapter recyclerViewAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ArrayList<DataWrapper> orders = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer);

        Intent received = getIntent();
        String foodBankButton = received.getStringExtra(MainActivity.FOOD_BANK_BUTTON);
        getSupportActionBar().setTitle("Food Bank of " + foodBankButton);
        // add food bank specific things here later, like fetching orders

        // TODO replace this with a method that gets the orders from Firebase
        fetchCustomers("Gleaners");
        recyclerView = findViewById(R.id.list);

        // where most of the magic happens
        recyclerViewAdapter = new MyRecyclerViewAdapter(orders);
        recyclerView.setAdapter(recyclerViewAdapter);

        // linear layout manager as opposed to grid
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }

    /*
     * Do something with firebase to fetch the list of orders and their data. Enter
     * the order data into the order arraylist. Unused right now.
     */
    private void fetchCustomers(String foodBank) {
        // TODO eventually make it orders for some selected food bank rather than a hard coded one
        db.collection("/foodbanks/" + foodBank + "/orders").document("aUA4SDE1bWUL3jZJrsAFFguJMHn1").get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                          @Override
                          public void onSuccess(DocumentSnapshot documentSnapshot) {
                              DataWrapper dataWrapper = documentSnapshot.toObject(DataWrapper.class);
                              System.out.println(dataWrapper.getDay());
                              orders.add(dataWrapper);
                          }
                      }
                );
        /*db.collection("/foodbanks/Gleaners/orders").document("aUA4SDE1bWUL3jZJrsAFFguJMHn1").get()
            .addOnCompleteListener((OnCompleteListener) new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    //for (DocumentSnapshot document : task.getResult()) {
                    Map<String, Object> map = document.getData();
                    String displayName = ""; // TODO what is this for?
                    String foodBank = ""; // TODO we should be getting this from the parent collection
                    String bankButtonID = ""; // TODO why do we have a bank button ID?
                    String bag = (String) map.get("bag");
                    long year = (long) map.get("year");
                    long month = (long) map.get("month");
                    long day = (long) map.get("day");
                    long hour = (long) map.get("hour");
                    long minute = (long) map.get("minute");
                    long color = (long) map.get("color");
                    DataWrapper data = new DataWrapper(displayName, foodBank, bankButtonID, bag, year, month, day, hour, minute, color);
                    orders.add(data);
                    //}
                } else {
                    // TODO log an error somehow, maybe something like this
                    //Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });*/
            /*.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Map<String, Object> map = document.getData();
                            String displayName = ""; // TODO what is this for?
                            String foodBank = ""; // TODO we should be getting this from the parent collection
                            String bankButtonID = ""; // TODO why do we have a bank button ID?
                            String bag = (String) map.get("Bag");
                            int year = (Integer) map.get("Year");
                            int month = (Integer) map.get("Month");
                            int day = (Integer) map.get("Day");
                            int hour = (Integer) map.get("Hour");
                            int minute = (Integer) map.get("Minute");
                            int color = (Integer) map.get("Color");
                            DataWrapper data = new DataWrapper(displayName, foodBank, bankButtonID, bag, year, month, day, hour, minute, color);
                            orders.add(data);
                        }
                    } else {
                        // TODO log an error somehow, maybe something like this
                        //Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                }
            });*/
    }
    /*
     * Generate some fake orders for testing. Add them to "orders".
     */
    private void generateFakeOrders(int howMany) {
        Random random = new Random();

        for (int i = 0; i < howMany; i++) {
            int color = Color.argb(255, random.nextInt(255 + 1),random.nextInt(255 + 1),random.nextInt(255 + 1));
            int foodBankInt = i % 4 + 1;
            orders.add(new DataWrapper("namenumber" + i,
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
    } /* generateFakeOrders() */
}