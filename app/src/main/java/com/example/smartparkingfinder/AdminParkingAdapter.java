package com.example.smartparkingfinder;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
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

public class AdminParkingAdapter extends RecyclerView.Adapter<AdminParkingAdapter.ViewHolder> {

    Context context;
    ArrayList<ParkingModel> list;

    public AdminParkingAdapter(Context context, ArrayList<ParkingModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_parking, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ParkingModel model = list.get(position);

        holder.txtName.setText(model.getName());
        holder.txtSlots.setText("Available Slots: " + model.getAvailable());

        // Change button texts for Admin
        holder.btnBook.setText("Update");
        holder.btnNavigate.setText("Delete");

        // UPDATE BUTTON
        holder.btnBook.setOnClickListener(v -> {
            Intent intent = new Intent(context, UpdateParkingActivity.class);
            intent.putExtra("id", model.getId());
            intent.putExtra("name", model.getName());
            intent.putExtra("lat", model.getLat());
            intent.putExtra("lon", model.getLon());
            intent.putExtra("available", model.getAvailable());
            context.startActivity(intent);
        });

        // DELETE BUTTON
        holder.btnNavigate.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Delete Parking")
                    .setMessage("Are you sure you want to delete this parking spot?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        FirebaseDatabase.getInstance()
                                .getReference("parkingSpots")
                                .child(model.getId())
                                .removeValue()
                                .addOnSuccessListener(unused -> {
                                    Toast.makeText(context, "Parking Deleted", Toast.LENGTH_SHORT).show();
                                });
                    })
                    .setNegativeButton("No", null)
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtName;
        TextView txtSlots;
        Button btnBook;
        Button btnNavigate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            txtSlots = itemView.findViewById(R.id.txtSlots);
            btnBook = itemView.findViewById(R.id.btnBook);
            btnNavigate = itemView.findViewById(R.id.btnNavigate);
        }
    }
}
