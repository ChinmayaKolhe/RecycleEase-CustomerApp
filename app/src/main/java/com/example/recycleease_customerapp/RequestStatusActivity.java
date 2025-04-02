package com.example.recycleease_customerapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RequestStatusActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RequestAdapter adapter;
    private ArrayList<PickupRequest> pickupRequests;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_status);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        pickupRequests = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("PickupRequests");

        // Fetch the data from Firebase
        fetchPickupRequests();
    }

    private void fetchPickupRequests() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                pickupRequests.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    PickupRequest request = snapshot.getValue(PickupRequest.class);
                    if (request != null) {
                        pickupRequests.add(request);
                    }
                }
                adapter = new RequestAdapter(RequestStatusActivity.this, pickupRequests);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(RequestStatusActivity.this, "Failed to load data.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
