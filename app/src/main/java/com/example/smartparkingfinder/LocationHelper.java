package com.example.smartparkingfinder;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;

import androidx.annotation.NonNull;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class LocationHelper {

    private final FusedLocationProviderClient fusedLocationClient;

    public LocationHelper(Context context) {

        fusedLocationClient =
                LocationServices
                        .getFusedLocationProviderClient(context);
    }

    // Callback interface

    public interface LocationCallback {

        void onLocationReceived(
                double latitude,
                double longitude
        );
    }

    // Get current location

    @SuppressLint("MissingPermission")
    public void getCurrentLocation(
            LocationCallback callback) {

        fusedLocationClient
                .getLastLocation()

                .addOnSuccessListener(

                        new OnSuccessListener<Location>() {

                            @Override
                            public void onSuccess(Location location) {

                                if(location != null) {

                                    callback.onLocationReceived(

                                            location.getLatitude(),

                                            location.getLongitude()
                                    );
                                }

                                else {

                                    // Default Mumbai fallback

                                    callback.onLocationReceived(

                                            19.0760,

                                            72.8777
                                    );
                                }
                            }
                        });
    }
}