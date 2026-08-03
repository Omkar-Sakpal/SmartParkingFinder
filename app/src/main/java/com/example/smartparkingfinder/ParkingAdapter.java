package com.example.smartparkingfinder;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ParkingAdapter
        extends RecyclerView.Adapter<ParkingAdapter.ViewHolder> {

    Context context;

    ArrayList<ParkingModel> list;

    public ParkingAdapter(
            Context context,
            ArrayList<ParkingModel> list) {

        this.context = context;

        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        View view =
                LayoutInflater.from(context)

                        .inflate(
                                R.layout.item_parking,
                                parent,
                                false
                        );

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull ViewHolder holder,
            int position) {

        ParkingModel model =
                list.get(position);

        holder.txtName.setText(
                model.getName()
        );

        holder.txtSlots.setText(
                "Available Slots: "
                        + model.getAvailable()
        );

        // BOOK BUTTON

        holder.btnBook.setOnClickListener(v -> {

            if(model.getAvailable() <= 0) {

                Toast.makeText(
                        context,
                        "Parking Full",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            String currentDevice =

                    Build.MANUFACTURER
                            + " "
                            + Build.MODEL;

            FirebaseDatabase
                    .getInstance()
                    .getReference("bookings")

                    .get()

                    .addOnSuccessListener(snapshot -> {

                        boolean alreadyBooked = false;

                        // CHECK DUPLICATE BOOKING

                        for(DataSnapshot data :
                                snapshot.getChildren()) {

                            BookingModel booking =
                                    data.getValue(
                                            BookingModel.class
                                    );

                            if(booking != null

                                    &&

                                    booking.getParkingId()
                                            != null

                                    &&

                                    booking.getParkingId()
                                            .equals(model.getId())

                                    &&

                                    booking.getDeviceName()
                                            .equals(currentDevice)

                                    &&

                                    booking.getStatus()
                                            .equals("Booked")) {

                                alreadyBooked = true;

                                break;
                            }
                        }

                        // ALREADY BOOKED

                        if(alreadyBooked) {

                            Toast.makeText(
                                    context,
                                    "Already Booked",
                                    Toast.LENGTH_SHORT
                            ).show();

                            return;
                        }

                        // =========================
                        // SMART HYBRID LOGIC
                        // =========================

                        double userLat = 19.0760;

                        double userLon = 72.8777;

                        double parkingLat =
                                model.getLat();

                        double parkingLon =
                                model.getLon();

                        float[] results =
                                new float[1];

                        android.location.Location
                                .distanceBetween(

                                        userLat,
                                        userLon,

                                        parkingLat,
                                        parkingLon,

                                        results
                                );

                        // Distance in KM

                        double distanceKm =
                                results[0] / 1000;

                        // Average speed

                        double averageSpeed = 30;

                        // Travel minutes

                        long travelMinutes =

                                (long)((distanceKm
                                        / averageSpeed)
                                        * 60);

                        // Minimum 5 mins

                        if(travelMinutes < 5) {

                            travelMinutes = 5;
                        }

                        // Grace time

                        long graceMinutes = 5;

                        // Expiry time

                        long expiryTime =

                                System.currentTimeMillis()

                                        +

                                        ((travelMinutes
                                                + graceMinutes)

                                                * 60 * 1000);

                        // =========================
                        // REDUCE SLOT
                        // =========================

                        int updatedSlots =
                                model.getAvailable() - 1;

                        FirebaseDatabase
                                .getInstance()
                                .getReference("parkingSpots")

                                .child(model.getId())

                                .child("available")

                                .setValue(updatedSlots);

                        // =========================
                        // SAVE BOOKING
                        // =========================

                        DatabaseReference bookingRef =

                                FirebaseDatabase
                                        .getInstance()
                                        .getReference("bookings");

                        // Booking ID

                        String bookingId =
                                bookingRef.push().getKey();

                        // Current time

                        String time =

                                new SimpleDateFormat(
                                        "dd MMM yyyy, hh:mm a",
                                        Locale.getDefault()
                                )

                                        .format(new Date());

                        // Booking model

                        BookingModel booking =

                                new BookingModel(

                                        bookingId,

                                        model.getId(),

                                        model.getName(),

                                        time,

                                        currentDevice,

                                        "Booked",

                                        parkingLat,

                                        parkingLon,

                                        expiryTime,

                                        false
                                );

                        // Save booking

                        bookingRef
                                .child(bookingId)
                                .setValue(booking);

                        Toast.makeText(
                                context,
                                "Parking Booked",
                                Toast.LENGTH_SHORT
                        ).show();
                    });
        });

        // NAVIGATION BUTTON

        holder.btnNavigate.setOnClickListener(v -> {

            String uri =

                    "google.navigation:q="
                            + model.getLat()
                            + ","
                            + model.getLon();

            Intent intent =
                    new Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse(uri)
                    );

            intent.setPackage(
                    "com.google.android.apps.maps"
            );

            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {

        return list.size();
    }

    // VIEW HOLDER

    public static class ViewHolder
            extends RecyclerView.ViewHolder {

        TextView txtName;

        TextView txtSlots;

        Button btnBook;

        Button btnNavigate;

        public ViewHolder(
                @NonNull View itemView) {

            super(itemView);

            txtName =
                    itemView.findViewById(
                            R.id.txtName
                    );

            txtSlots =
                    itemView.findViewById(
                            R.id.txtSlots
                    );

            btnBook =
                    itemView.findViewById(
                            R.id.btnBook
                    );

            btnNavigate =
                    itemView.findViewById(
                            R.id.btnNavigate
                    );
        }
    }
}