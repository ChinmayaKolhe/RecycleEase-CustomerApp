package com.example.recycleease_customerapp;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
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

public class RecyclingActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclingCenterAdapter adapter;
    private ArrayList<RecyclingCenter> centerList;
    private DatabaseReference databaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycling);

        recyclerView = findViewById(R.id.recyclerViewCenters);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        centerList = new ArrayList<>();
        adapter = new RecyclingCenterAdapter(centerList);
        recyclerView.setAdapter(adapter);

        // Firebase Database Reference
        databaseRef = FirebaseDatabase.getInstance().getReference("RecyclingCenters");

        // Fetch Data from Firebase
        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                centerList.clear();

                Log.d("FirebaseDebug", "Snapshot received: " + snapshot.toString());

                for (DataSnapshot data : snapshot.getChildren()) {
                    RecyclingCenter center = data.getValue(RecyclingCenter.class);

                    if (center != null) {
                        Log.d("FirebaseDebug", "Fetched Data: ");
                        Log.d("FirebaseDebug", "Name: " + center.getName());
                        Log.d("FirebaseDebug", "Location: " + center.getLocation());
                        Log.d("FirebaseDebug", "Contact: " + center.getContact());

                        centerList.add(center);
                    } else {
                        Log.e("FirebaseError", "Null data received from Firebase");
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(RecyclingActivity.this, "Failed to load data", Toast.LENGTH_SHORT).show();
                Log.e("FirebaseError", "Database error: " + error.getMessage());
            }
        });
    }
}
