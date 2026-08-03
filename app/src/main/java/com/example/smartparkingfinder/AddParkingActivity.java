package com.example.smartparkingfinder;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddParkingActivity
        extends AppCompatActivity {

    EditText edtName;
    EditText edtLat;
    EditText edtLon;
    EditText edtSlots;

    Button btnAddParking;

    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_add_parking
        );

        // Initialize views

        edtName =
                findViewById(R.id.edtName);

        edtLat =
                findViewById(R.id.edtLat);

        edtLon =
                findViewById(R.id.edtLon);

        edtSlots =
                findViewById(R.id.edtSlots);

        btnAddParking =
                findViewById(
                        R.id.btnAddParking
                );

        // Firebase

        databaseReference =
                FirebaseDatabase.getInstance()
                        .getReference("parkingSpots");

        // Button click

        btnAddParking.setOnClickListener(v -> {

            addParking();
        });
    }

    // Add parking

    private void addParking() {

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

            // Firebase ID

            String id =
                    databaseReference.push().getKey();

            // Model

            ParkingModel model =
                    new ParkingModel(
                            id,
                            name,
                            lat,
                            lon,
                            slots
                    );

            // Save

            databaseReference
                    .child(id)
                    .setValue(model)

                    .addOnSuccessListener(unused -> {

                        Toast.makeText(
                                this,
                                "Parking Added",
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