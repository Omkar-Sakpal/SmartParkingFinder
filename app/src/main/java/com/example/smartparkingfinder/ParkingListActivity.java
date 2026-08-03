package com.example.smartparkingfinder;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ParkingListActivity
        extends AppCompatActivity {

    RecyclerView recyclerView;

    ArrayList<ParkingModel> list;

    ParkingAdapter adapter;

    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_parking_list
        );

        recyclerView =
                findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(
                new LinearLayoutManager(this)
        );

        // List

        list = new ArrayList<>();

        // Adapter

        adapter =
                new ParkingAdapter(
                        this,
                        list
                );

        recyclerView.setAdapter(adapter);

        // Firebase

        databaseReference =
                FirebaseDatabase.getInstance()
                        .getReference("parkingSpots");

        // Load parking

        loadParking();
    }

    private void loadParking() {

        databaseReference.addValueEventListener(

                new ValueEventListener() {

                    @Override
                    public void onDataChange(
                            @NonNull DataSnapshot snapshot) {

                        list.clear();

                        for(DataSnapshot data :
                                snapshot.getChildren()) {

                            ParkingModel model =
                                    data.getValue(
                                            ParkingModel.class
                                    );

                            if(model != null) {

                                list.add(model);
                            }
                        }

                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(
                            @NonNull DatabaseError error) {

                    }
                });
    }
}