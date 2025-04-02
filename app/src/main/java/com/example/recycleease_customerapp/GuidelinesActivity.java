package com.example.recycleease_customerapp;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class GuidelinesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private GuidelinesAdapter adapter;
    private List<EWasteGuideline> guidelineList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guidelines);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        guidelineList = new ArrayList<>();
        guidelineList.add(new EWasteGuideline("Proper Disposal", "Always dispose of e-waste in authorized centers."));
        guidelineList.add(new EWasteGuideline("Avoid Burning", "Burning e-waste releases toxic chemicals into the air."));
        guidelineList.add(new EWasteGuideline("Recycling", "Recycle e-waste at certified centers."));
        guidelineList.add(new EWasteGuideline("Donate if Possible", "Donate old but working electronics to schools or NGOs."));

        adapter = new GuidelinesAdapter(guidelineList);
        recyclerView.setAdapter(adapter);
    }
}
