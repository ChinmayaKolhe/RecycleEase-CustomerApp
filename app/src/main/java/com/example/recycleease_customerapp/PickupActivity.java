package com.example.recycleease_customerapp;
import android.Manifest;
import android.content.pm.PackageManager;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import org.osmdroid.api.IGeoPoint;
import org.osmdroid.util.GeoPoint;

import java.io.ByteArrayOutputStream;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class PickupActivity extends AppCompatActivity {
    private static final int CAMERA_REQUEST_CODE = 101;  // Unique request code for camera intent
    private static final int PERMISSION_REQUEST_CODE = 200;

    private EditText etId, etName, etPhone, etPickupDate, etStatus;
    private ImageView  ivCapturedImage ;  // Initialize the ImageView

    private TextView etAddress;
    private Spinner spinnerWasteType;
    private Button btnSubmitPickup, btnGetLocation, btnCapturePhoto;
    private String selectedWasteType = "";
    private DatabaseReference databaseReference;
    private GeoPoint currentLocation;
    private Bitmap capturedImageBitmap;  // Variable to hold the captured image

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pickup);
        ivCapturedImage = findViewById(R.id.ivCapturedImage);  // Initialize the ImageView

        // Firebase Database Reference
        databaseReference = FirebaseDatabase.getInstance().getReference("PickupRequests");

        // Initializing UI elements
        etId = findViewById(R.id.etId);  // Request ID (Auto-generated)
        etName = findViewById(R.id.etName);
        etPhone = findViewById(R.id.etPhone);
        etPickupDate = findViewById(R.id.etPickupDate);
        etStatus = findViewById(R.id.etStatus);  // Status (Pending)
        etAddress = findViewById(R.id.tvLocation);
        spinnerWasteType = findViewById(R.id.spinnerWasteType);
        btnSubmitPickup = findViewById(R.id.btnSubmitPickup);
        btnGetLocation = findViewById(R.id.btnGetLocation);
        btnCapturePhoto = findViewById(R.id.btnCapturePhoto);

        // Waste Type Spinner Listener
        spinnerWasteType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedWasteType = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // Pickup Date Picker
        etPickupDate.setOnClickListener(v -> showDatePicker());

        // Submit Button Click
        btnSubmitPickup.setOnClickListener(v -> validateAndSubmit());

        // Get Location Button Click
        btnGetLocation.setOnClickListener(v -> fetchLocation());

        // Capture Photo Button (This should be integrated with actual photo capturing functionality)
        btnCapturePhoto.setOnClickListener(v -> capturePhoto());
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
                Toast.makeText(this, "Camera permission is required to capture photo.", Toast.LENGTH_SHORT).show();
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

        // Generate Firebase Request ID
        String requestId = databaseReference.push().getKey();
        etId.setText(requestId);  // Display the ID in the UI
        etStatus.setText("Pending");  // Default status

        // Encrypt photo before uploading (Assuming photo has been captured as a Bitmap)
        String encryptedPhoto = encryptImage(capturedImageBitmap);

        // Create PickupRequest object with encrypted photo and location
        PickupRequest request = new PickupRequest(requestId, name, phone, selectedWasteType, pickupDate, address, "Pending", currentLocation.toString(), encryptedPhoto);

        // Save data to Firebase
        databaseReference.child(requestId).setValue(request).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(PickupActivity.this, "Pickup Request Submitted!", Toast.LENGTH_LONG).show();

                // After successful submission, start RequestStatusActivity
                Intent intent = new Intent(PickupActivity.this, RequestStatusActivity.class);
                intent.putExtra("REQUEST_STATUS", "Pending");
                startActivity(intent);
                finish();  // Close the current activity
            } else {
                Toast.makeText(PickupActivity.this, "Failed! Try Again", Toast.LENGTH_LONG).show();
            }
        });
    }


    // Fetch Location using osmdroid (or other location provider)
    private void fetchLocation() {
        // Assuming you are getting location using osmdroid's MapView or some other method
        // Example: Setting currentLocation to a random geo-point (you can use real GPS data here)
        currentLocation = new GeoPoint(37.7749, -122.4194); // Sample coordinates
        etAddress.setText("Lat: " + currentLocation.getLatitude() + ", Lon: " + currentLocation.getLongitude());
    }

    // Encrypt Image (Bitmap) using AES
    private String encryptImage(Bitmap bitmap) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();

            // AES encryption
            SecretKey secretKey = generateSecretKey();
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encrypted = cipher.doFinal(byteArray);

            return Base64.encodeToString(encrypted, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Generate Secret Key for AES encryption
    private SecretKey generateSecretKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(256); // 256-bit encryption
        return keyGenerator.generateKey();
    }

    // Clear input fields after submission
    private void clearFields() {
        etId.setText("");  // Clear ID
        etName.setText("");
        etPhone.setText("");
        etPickupDate.setText("");
        etStatus.setText("Pending");  // Reset status
        spinnerWasteType.setSelection(0);
        etAddress.setText("");
    }

    // Capture Photo (Dummy Function - To be implemented)
    // Method to capture photo using the camera
    private void capturePhoto() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            // Start camera intent
            startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
        } else {
            Toast.makeText(this, "No camera app found", Toast.LENGTH_SHORT).show();
        }
    }

    // Handle the result of the camera intent
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            // Get the captured image from the camera intent
            Bundle extras = data.getExtras();
            capturedImageBitmap = (Bitmap) extras.get("data");  // Get the thumbnail image

            // Optionally, set the captured image to an ImageView
            ivCapturedImage.setImageBitmap(capturedImageBitmap);
            Toast.makeText(this, "Photo captured successfully.", Toast.LENGTH_SHORT).show();
        }
    }

    // Method to get the encrypted form of the image for uploading to Firebase (if needed)
    private String encodeImageToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);  // Compress image to PNG
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return android.util.Base64.encodeToString(byteArray, android.util.Base64.DEFAULT);
    }

    // If you need to upload the image to Firebase, you can call this method after capturing the image
    private void uploadImageToFirebase() {
        if (capturedImageBitmap != null) {
            String base64Image = encodeImageToBase64(capturedImageBitmap);
            // Save the base64 image string to Firebase (or your preferred storage solution)
        } else {
            Toast.makeText(this, "No image to upload.", Toast.LENGTH_SHORT).show();
        }
    }
}

