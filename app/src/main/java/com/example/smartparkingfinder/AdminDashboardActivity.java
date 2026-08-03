package com.example.smartparkingfinder;

// Imports

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class AdminDashboardActivity
        extends AppCompatActivity {

    Button btnAddParking;

    Button btnManageParking;

    Button btnViewBookings;

    Button btnLogout;

    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_admin_dashboard
        );

        // Session

        sessionManager =
                new SessionManager(this);

        // Initialize buttons

        btnAddParking =
                findViewById(
                        R.id.btnAddParking
                );

        btnManageParking =
                findViewById(
                        R.id.btnManageParking
                );

        btnViewBookings =
                findViewById(
                        R.id.btnViewBookings
                );

        btnLogout =
                findViewById(
                        R.id.btnLogout
                );

        // Add Parking

        btnAddParking.setOnClickListener(v -> {

            startActivity(

                    new Intent(
                            this,
                            AddParkingActivity.class
                    )
            );
        });

        // Manage Parking

        btnManageParking.setOnClickListener(v -> {

            startActivity(

                    new Intent(
                            this,
                            ManageParkingActivity.class
                    )
            );
        });

        // View Bookings

        btnViewBookings.setOnClickListener(v -> {

            startActivity(

                    new Intent(
                            this,
                            AdminBookingsActivity.class
                    )
            );
        });

        // Logout

        btnLogout.setOnClickListener(v -> {

            sessionManager.setLogin(false);

            startActivity(

                    new Intent(
                            this,
                            MainActivity.class
                    )
            );

            finish();
        });
    }
}