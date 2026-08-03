package com.example.smartparkingfinder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class BookingAdapter
        extends RecyclerView.Adapter<BookingAdapter.ViewHolder> {

    ArrayList<BookingModel> list;

    public BookingAdapter(
            ArrayList<BookingModel> list) {

        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        View view =
                LayoutInflater.from(parent.getContext())

                        .inflate(
                                R.layout.item_booking,
                                parent,
                                false
                        );

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull ViewHolder holder,
            int position) {

        BookingModel model =
                list.get(position);

        holder.txtParkingName.setText(
                model.getParkingName()
        );

        holder.txtTime.setText(
                "Time: " + model.getBookingTime()
        );

        holder.txtDevice.setText(
                "Device: " + model.getDeviceName()
        );

        holder.txtStatus.setText(
                "Status: " + model.getStatus()
        );

        // Already cancelled

        if (model.getStatus()
                .equals("Cancelled")) {

            holder.btnCancel.setEnabled(false);

            holder.btnCancel.setText(
                    "Cancelled"
            );
        }

        // Cancel booking

        holder.btnCancel.setOnClickListener(v -> {

            // Update booking status

            FirebaseDatabase
                    .getInstance()
                    .getReference("bookings")

                    .child(model.getBookingId())

                    .child("status")

                    .setValue("Cancelled")

                    .addOnSuccessListener(unused -> {

                        // Restore parking slot

                        FirebaseDatabase
                                .getInstance()
                                .getReference("parkingSpots")

                                .child(model.getParkingId())

                                .child("available")

                                .get()

                                .addOnSuccessListener(snapshot -> {

                                    Integer slots =
                                            snapshot.getValue(
                                                    Integer.class
                                            );

                                    if (slots != null) {

                                        FirebaseDatabase
                                                .getInstance()
                                                .getReference("parkingSpots")

                                                .child(model.getParkingId())

                                                .child("available")

                                                .setValue(slots + 1);
                                    }
                                });

                        Toast.makeText(

                                holder.itemView.getContext(),

                                "Booking Cancelled",

                                Toast.LENGTH_SHORT
                        ).show();
                        // Remove local booking

                        holder.itemView.getContext()

                                .getSharedPreferences(
                                        "booking_pref",
                                        android.content.Context.MODE_PRIVATE
                                )

                                .edit()

                                .remove("booked_" + model.getParkingId())

                                .apply();
                    });
        });
    }
    @Override
    public int getItemCount() {

        return list.size();
    }

    // ViewHolder

    public static class ViewHolder
            extends RecyclerView.ViewHolder {

        TextView txtParkingName;

        TextView txtTime;

        TextView txtDevice;

        TextView txtStatus;

        Button btnCancel;

        public ViewHolder(
                @NonNull View itemView) {

            super(itemView);

            txtParkingName =
                    itemView.findViewById(
                            R.id.txtParkingName
                    );

            txtTime =
                    itemView.findViewById(
                            R.id.txtTime
                    );

            txtDevice =
                    itemView.findViewById(
                            R.id.txtDevice
                    );

            txtStatus =
                    itemView.findViewById(
                            R.id.txtStatus
                    );

            btnCancel =
                    itemView.findViewById(
                            R.id.btnCancel
                    );
        }
    }
}