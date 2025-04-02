package com.example.recycleease_customerapp;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.Calendar;

public class BuyBackActivity extends AppCompatActivity {

    private EditText etName, etPhone, etPickupDate, etAddress;
    private Spinner spinnerDeviceType, spinnerDeviceCondition;
    private Button btnSubmitBuyback;
    private String selectedDeviceType = "", selectedDeviceCondition = "";
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_back);

        // Firebase Database Reference
        databaseReference = FirebaseDatabase.getInstance().getReference("BuybackRequests");

        // Initializing UI elements
        etName = findViewById(R.id.etName);
        etPhone = findViewById(R.id.etPhone);
        etPickupDate = findViewById(R.id.etPickupDate);
        etAddress = findViewById(R.id.etAddress);
        spinnerDeviceType = findViewById(R.id.spinnerDeviceType);
        spinnerDeviceCondition = findViewById(R.id.spinnerDeviceCondition);
        btnSubmitBuyback = findViewById(R.id.btnSubmitBuyback);

        // Set Spinner Data for Device Type
        ArrayAdapter<CharSequence> adapterDeviceType = ArrayAdapter.createFromResource(this,
                R.array.device_types, android.R.layout.simple_spinner_item);
        adapterDeviceType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDeviceType.setAdapter(adapterDeviceType);

        // Set Spinner Data for Device Condition
        ArrayAdapter<CharSequence> adapterDeviceCondition = ArrayAdapter.createFromResource(this,
                R.array.device_conditions, android.R.layout.simple_spinner_item);
        adapterDeviceCondition.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDeviceCondition.setAdapter(adapterDeviceCondition);

        // Spinner Listeners
        spinnerDeviceType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedDeviceType = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        spinnerDeviceCondition.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedDeviceCondition = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // Pickup Date Picker
        etPickupDate.setOnClickListener(v -> showDatePicker());

        // Submit Button Click
        btnSubmitBuyback.setOnClickListener(v -> validateAndSubmit());
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
        if (selectedDeviceType.isEmpty() || selectedDeviceType.equals("Select Device Type")) {
            Toast.makeText(this, "Select a device type", Toast.LENGTH_SHORT).show();
            return;
        }
        if (selectedDeviceCondition.isEmpty() || selectedDeviceCondition.equals("Select Condition")) {
            Toast.makeText(this, "Select a device condition", Toast.LENGTH_SHORT).show();
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

        // Create a BuybackRequest object
        BuybackRequest request = new BuybackRequest(requestId, name, phone, selectedDeviceType, selectedDeviceCondition, pickupDate, address, "Pending");

        // Save data to Firebase
        databaseReference.child(requestId).setValue(request).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(BuyBackActivity.this, "Buyback Request Submitted!", Toast.LENGTH_LONG).show();
                clearFields();
            } else {
                Toast.makeText(BuyBackActivity.this, "Failed! Try Again", Toast.LENGTH_LONG).show();
            }
        });
    }

    // Clear input fields after submission
    private void clearFields() {
        etName.setText("");
        etPhone.setText("");
        etPickupDate.setText("");
        etAddress.setText("");
        spinnerDeviceType.setSelection(0);
        spinnerDeviceCondition.setSelection(0);
    }
}
