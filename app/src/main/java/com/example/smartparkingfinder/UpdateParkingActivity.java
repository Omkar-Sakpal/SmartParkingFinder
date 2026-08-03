package com.example.smartparkingfinder;

// Imports

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UpdateParkingActivity
        extends AppCompatActivity {

    EditText edtName;
    EditText edtLat;
    EditText edtLon;
    EditText edtSlots;

    Button btnUpdate;

    DatabaseReference databaseReference;

    String parkingId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_update_parking
        );

        // Views

        edtName =
                findViewById(R.id.edtName);

        edtLat =
                findViewById(R.id.edtLat);

        edtLon =
                findViewById(R.id.edtLon);

        edtSlots =
                findViewById(R.id.edtSlots);

        btnUpdate =
                findViewById(R.id.btnUpdate);

        // Firebase

        databaseReference =
                FirebaseDatabase.getInstance()
                        .getReference("parkingSpots");

        // Intent data

        parkingId =
                getIntent().getStringExtra("id");

        String name =
                getIntent().getStringExtra("name");

        double lat =
                getIntent().getDoubleExtra(
                        "lat",
                        0
                );

        double lon =
                getIntent().getDoubleExtra(
                        "lon",
                        0
                );

        int available =
                getIntent().getIntExtra(
                        "available",
                        0
                );

        // Set old values

        edtName.setText(name);

        edtLat.setText(
                String.valueOf(lat)
        );

        edtLon.setText(
                String.valueOf(lon)
        );

        edtSlots.setText(
                String.valueOf(available)
        );

        // Update click

        btnUpdate.setOnClickListener(v -> {

            updateParking();
        });
    }

    // Update parking

    private void updateParking() {

        try {

            String name =
                    edtName.getText()
                            .toString()
                            .trim();

            double lat =
                    Double.parseDouble(
                            edtLat.getText()
                                    .toString()
                                    .trim()
                    );

            double lon =
                    Double.parseDouble(
                            edtLon.getText()
                                    .toString()
                                    .trim()
                    );

            int slots =
                    Integer.parseInt(
                            edtSlots.getText()
                                    .toString()
                                    .trim()
                    );

            ParkingModel model =
                    new ParkingModel(
                            parkingId,
                            name,
                            lat,
                            lon,
                            slots
                    );

            databaseReference
                    .child(parkingId)
                    .setValue(model)

                    .addOnSuccessListener(unused -> {

                        Toast.makeText(
                                this,
                                "Parking Updated",
                                Toast.LENGTH_SHORT
                        ).show();

                        finish();
                    });

        }

        catch (Exception e) {

            Toast.makeText(
                    this,
                    e.getMessage(),
                    Toast.LENGTH_LONG
            ).show();
        }
    }
}