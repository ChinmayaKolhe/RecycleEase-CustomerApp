package com.example.recycleease_customerapp;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.Calendar;

public class DonationActivity extends AppCompatActivity {

    private EditText etName, etPhone, etPickupDate, etAddress;
    private Spinner spinnerItemType;
    private Button btnSubmitDonation;
    private String selectedItemType = "";
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation);

        // Firebase Database Reference
        databaseReference = FirebaseDatabase.getInstance().getReference("DonationRequests");

        // Initializing UI elements
        etName = findViewById(R.id.etName);
        etPhone = findViewById(R.id.etPhone);
        etPickupDate = findViewById(R.id.etPickupDate);
        etAddress = findViewById(R.id.etAddress);
        spinnerItemType = findViewById(R.id.spinnerItemType);
        btnSubmitDonation = findViewById(R.id.btnSubmitDonation);

        // Set Spinner Data
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.donation_items, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerItemType.setAdapter(adapter);

        // Spinner Listener
        spinnerItemType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedItemType = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // Pickup Date Picker
        etPickupDate.setOnClickListener(v -> showDatePicker());

        // Submit Button Click
        btnSubmitDonation.setOnClickListener(v -> validateAndSubmit());
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year1, month1, dayOfMonth) -> etPickupDate.setText(year1 + "-" + (month1 + 1) + "-" + dayOfMonth),
                year, month, day);
        datePickerDialog.show();
    }

    private void validateAndSubmit() {
        String name = etName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String pickupDate = etPickupDate.getText().toString().trim();
        String address = etAddress.getText().toString().trim();

        if (name.isEmpty()) {
            etName.setError("Enter your name");
            return;
        }
        if (phone.isEmpty() || phone.length() != 10) {
            etPhone.setError("Enter a valid 10-digit phone number");
            return;
        }
        if (selectedItemType.isEmpty() || selectedItemType.equals("Select Item Type")) {
            Toast.makeText(this, "Select an item type", Toast.LENGTH_SHORT).show();
            return;
        }
        if (pickupDate.isEmpty()) {
            etPickupDate.setError("Select a pickup date");
            return;
        }
        if (address.isEmpty()) {
            etAddress.setError("Enter pickup address");
            return;
        }

        // Generate Firebase Request ID
        String requestId = databaseReference.push().getKey();

        // Create a DonationRequest object (CORRECT ORDER!)
        DonationRequest request = new DonationRequest(requestId, name, phone, selectedItemType, address, pickupDate, "Pending");

        // Save data to Firebase
        databaseReference.child(requestId).setValue(request).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(DonationActivity.this, "Donation Request Submitted!", Toast.LENGTH_LONG).show();
                clearFields();
            } else {
                Toast.makeText(DonationActivity.this, "Failed! Try Again", Toast.LENGTH_LONG).show();
            }
        });
    }

    // Clear input fields after submission
    private void clearFields() {
        etName.setText("");
        etPhone.setText("");
        etPickupDate.setText("");
        etAddress.setText("");
        spinnerItemType.setSelection(0);
    }
}
