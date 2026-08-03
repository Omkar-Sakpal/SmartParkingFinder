package com.example.smartparkingfinder;

import android.content.Context;
import android.location.Location;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class BookingVerificationService {

    Context context;

    public BookingVerificationService(Context context) {

        this.context = context;
    }

    // START VERIFICATION

    public void verifyBookings() {

        DatabaseReference bookingRef =

                FirebaseDatabase
                        .getInstance()
                        .getReference("bookings");

        bookingRef.addListenerForSingleValueEvent(

                new ValueEventListener() {

                    @Override
                    public void onDataChange(
                            @NonNull DataSnapshot snapshot) {

                        // CURRENT TIME

                        long currentTime =
                                System.currentTimeMillis();

                        // LOOP BOOKINGS

                        for(DataSnapshot data :
                                snapshot.getChildren()) {

                            BookingModel booking =
                                    data.getValue(
                                            BookingModel.class
                                    );

                            if(booking == null)
                                continue;

                            // ONLY ACTIVE BOOKINGS

                            if(!booking.getStatus()
                                    .equals("Booked"))
                                continue;

                            // SKIP ARRIVED USERS

                            if(booking.isArrived())
                                continue;

                            // CHECK EXPIRY

                            if(currentTime
                                    > booking.getExpiryTime()) {

                                // CHECK USER LOCATION

                                checkArrivalAndCancel(
                                        booking
                                );
                            }
                        }
                    }

                    @Override
                    public void onCancelled(
                            @NonNull DatabaseError error) {

                    }
                });
    }

    // CHECK ARRIVAL

    private void checkArrivalAndCancel(
            BookingModel booking) {

        LocationHelper helper =
                new LocationHelper(context);

        helper.getCurrentLocation(

                (userLat, userLon) -> {

                    float[] results =
                            new float[1];

                    Location.distanceBetween(

                            userLat,
                            userLon,

                            booking.getParkingLat(),
                            booking.getParkingLon(),

                            results
                    );

                    // DISTANCE IN METERS

                    float distance =
                            results[0];

                    // ARRIVAL RADIUS

                    float arrivalRadius = 100;

                    // USER ARRIVED

                    if(distance <= arrivalRadius) {

                        FirebaseDatabase
                                .getInstance()
                                .getReference("bookings")

                                .child(booking.getBookingId())

                                .child("arrived")

                                .setValue(true);

                        return;
                    }

                    // AUTO CANCEL

                    autoCancelBooking(
                            booking
                    );
                });
    }

    // AUTO CANCEL BOOKING

    private void autoCancelBooking(
            BookingModel booking) {

        DatabaseReference bookingRef =

                FirebaseDatabase
                        .getInstance()
                        .getReference("bookings");

        DatabaseReference parkingRef =

                FirebaseDatabase
                        .getInstance()
                        .getReference("parkingSpots");

        // UPDATE STATUS

        bookingRef
                .child(booking.getBookingId())
                .child("status")
                .setValue("Auto Cancelled");

        // RESTORE SLOT

        parkingRef
                .child(booking.getParkingId())

                .get()

                .addOnSuccessListener(snapshot -> {

                    ParkingModel parking =
                            snapshot.getValue(
                                    ParkingModel.class
                            );

                    if(parking != null) {

                        int updatedSlots =
                                parking.getAvailable() + 1;

                        parkingRef
                                .child(booking.getParkingId())
                                .child("available")
                                .setValue(updatedSlots);
                    }
                });

        // TOAST MESSAGE

        Toast.makeText(

                context,

                "Expired booking auto cancelled",

                Toast.LENGTH_SHORT

        ).show();
    }
}