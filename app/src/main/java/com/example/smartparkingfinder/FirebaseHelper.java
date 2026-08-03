package com.example.smartparkingfinder;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseHelper {

    public static DatabaseReference getParkingReference() {
        return FirebaseDatabase.getInstance()
                .getReference("parkingSpots");
    }
}