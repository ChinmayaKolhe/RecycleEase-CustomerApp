package com.example.recycleease_customerapp;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.*;
import java.util.ArrayList;
import java.util.List;

public class RecyclingActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclingCenterAdapter adapter;
    private List<RecyclingCenter> centerList;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycling);

        recyclerView = findViewById(R.id.recyclerViewCenters);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        centerList = new ArrayList<>();
        adapter = new RecyclingCenterAdapter(centerList);
        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();
        fetchRecyclingCenters();
    }

    private void fetchRecyclingCenters() {
        db.collection("RecyclingCenters")
                .addSnapshotListener((queryDocumentSnapshots, error) -> {
                    if (error != null) {
                        Log.e("FirestoreError", "Error: " + error.getMessage());
                        Toast.makeText(this, "Error loading data!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    centerList.clear();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        RecyclingCenter center = document.toObject(RecyclingCenter.class);
                        centerList.add(center);
                        Log.d("FirestoreData", "Fetched: " + center.getName());
                    }
                    adapter.notifyDataSetChanged();
                });
    }
}
