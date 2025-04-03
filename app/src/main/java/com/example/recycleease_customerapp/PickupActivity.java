package com.example.recycleease_customerapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.osmdroid.util.GeoPoint;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;

public class PickupActivity extends AppCompatActivity {
    private static final int CAMERA_REQUEST_CODE = 101;
    private static final int PERMISSION_REQUEST_CODE = 200;

    private EditText etId, etName, etPhone, etPickupDate, etStatus;
    private ImageView ivCapturedImage;
    private TextView etAddress;
    private Spinner spinnerWasteType;
    private Button btnSubmitPickup, btnGetLocation, btnCapturePhoto;
    private String selectedWasteType = "";
    private DatabaseReference databaseReference;
    private GeoPoint currentLocation;
    private Bitmap capturedImageBitmap;

    private static final String AES_SECRET_KEY = "1234567890123456"; // Change this

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pickup);

        ivCapturedImage = findViewById(R.id.ivCapturedImage);
        databaseReference = FirebaseDatabase.getInstance().getReference("PickupRequests");

        etId = findViewById(R.id.etId);
        etName = findViewById(R.id.etName);
        etPhone = findViewById(R.id.etPhone);
        etPickupDate = findViewById(R.id.etPickupDate);
        etStatus = findViewById(R.id.etStatus);
        etAddress = findViewById(R.id.tvLocation);
        spinnerWasteType = findViewById(R.id.spinnerWasteType);
        btnSubmitPickup = findViewById(R.id.btnSubmitPickup);
        btnGetLocation = findViewById(R.id.btnGetLocation);
        btnCapturePhoto = findViewById(R.id.btnCapturePhoto);

        spinnerWasteType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedWasteType = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        etPickupDate.setOnClickListener(v -> showDatePicker());
        btnSubmitPickup.setOnClickListener(v -> validateAndSubmit());
        btnGetLocation.setOnClickListener(v -> fetchLocation());

        btnCapturePhoto.setOnClickListener(v -> {
            if (checkCameraPermission()) {
                capturePhoto();
            } else {
                requestCameraPermission();
            }
        });
    }

    private boolean checkCameraPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                capturePhoto();
            } else {
                Toast.makeText(this, "Camera permission is required.", Toast.LENGTH_SHORT).show();
            }
        }
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
        if (selectedWasteType.isEmpty() || selectedWasteType.equals("Select Waste Type")) {
            Toast.makeText(this, "Select a waste type", Toast.LENGTH_SHORT).show();
            return;
        }
        if (pickupDate.isEmpty()) {
            etPickupDate.setError("Select a pickup date");
            return;
        }

        String requestId = databaseReference.push().getKey();
        etId.setText(requestId);
        etStatus.setText("Pending");

        String base64Image = capturedImageBitmap != null ? processAndEncryptImage(capturedImageBitmap) : null;

        PickupRequest request = new PickupRequest(requestId, name, phone, selectedWasteType, pickupDate, address, "Pending", currentLocation.toString(), base64Image);

        databaseReference.child(requestId).setValue(request).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(PickupActivity.this, "Pickup Request Submitted!", Toast.LENGTH_LONG).show();
                startActivity(new Intent(PickupActivity.this, RequestStatusActivity.class).putExtra("REQUEST_STATUS", "Pending"));
                finish();
            } else {
                Toast.makeText(PickupActivity.this, "Submission Failed! Try Again", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void fetchLocation() {
        etAddress.setText("Dr. D Y Patil College of Engineering & Innovation, Varale, Talegaon, Pune");}

    private void capturePhoto() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
        } else {
            Toast.makeText(this, "No camera app found", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            capturedImageBitmap = (Bitmap) extras.get("data");

            if (capturedImageBitmap != null) {
                ivCapturedImage.setImageBitmap(capturedImageBitmap);
            }
        }
    }

    private String processAndEncryptImage(Bitmap bitmap) {
        Bitmap compressedBitmap = ImageUtils.compressImage(bitmap, 80);
        String base64Image = encodeImageToBase64(compressedBitmap);
        return AESHelper.encrypt(base64Image);
    }

    private String encodeImageToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    public String decryptImage(String encryptedImage) {
        return AESHelper.decrypt(encryptedImage);
    }
}
