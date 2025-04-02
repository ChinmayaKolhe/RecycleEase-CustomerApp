package com.example.recycleease_customerapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ViewFlipper;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class EwasteHomePage extends AppCompatActivity {
    private Button logoutButton;
    private ImageButton chatbotButton;
    private Button logoutbtn;
    private ViewFlipper imageFlipper;
    private GestureDetector gestureDetector;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private CardView pickupService, recyclingCenterService, donationService, buyBackService, guidelinesService;
    private CardView bulkCollectionService, certifiedDisposalService, corporateService;
    private FirebaseAuth mAuth;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ewaste_home_page);
        chatbotButton=findViewById(R.id.chatbotButton);
        mAuth = FirebaseAuth.getInstance();
        sharedPreferences = getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE);

        logoutbtn = findViewById(R.id.logoutButton);
        imageFlipper = findViewById(R.id.imageFlipper);
        pickupService = findViewById(R.id.pickupService);
        recyclingCenterService = findViewById(R.id.recyclingCenterService);
        donationService = findViewById(R.id.donationService);
        buyBackService = findViewById(R.id.buyBackService);
        guidelinesService = findViewById(R.id.guidelinesService);
        bulkCollectionService = findViewById(R.id.bulkCollectionService);
        certifiedDisposalService = findViewById(R.id.certifiedDisposalService);
        corporateService = findViewById(R.id.corporateService);

        // Start automatic image flipping
        imageFlipper.setFlipInterval(3000); // 3 seconds
        imageFlipper.setAutoStart(true);
        imageFlipper.setInAnimation(this, android.R.anim.slide_in_left);
        imageFlipper.setOutAnimation(this, android.R.anim.slide_out_right);

        // Gesture detector for manual swipe
        gestureDetector = new GestureDetector(this, new GestureListener());
        imageFlipper.setOnTouchListener((v, event) -> gestureDetector.onTouchEvent(event));

        // Logout button click event
        logoutbtn.setOnClickListener(view -> logoutUser());
        chatbotButton.setOnClickListener(view -> {
            Intent intent = new Intent(EwasteHomePage.this, ChatActivity.class);
            startActivity(intent);
        });
        certifiedDisposalService.setOnClickListener(view -> openServicePage(RequestStatusActivity.class));
        pickupService.setOnClickListener(view -> openServicePage(PickupActivity.class));
        recyclingCenterService.setOnClickListener(view -> openServicePage(RecyclingActivity.class));
        donationService.setOnClickListener(view -> openServicePage(DonationActivity.class));
        buyBackService.setOnClickListener(view -> openServicePage(BuyBackActivity.class));
        guidelinesService.setOnClickListener(view -> openServicePage(GuidelinesActivity.class));
    }

    private void logoutUser() {
        // Firebase logout
        mAuth.signOut();

        // Clear login status from SharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLoggedIn", false);
        editor.apply();

        Toast.makeText(EwasteHomePage.this, "Logged Out Successfully!", Toast.LENGTH_SHORT).show();

        // Redirect to LoginActivity
        startActivity(new Intent(EwasteHomePage.this, LoginActivity.class));
        finish();
    }

    private void openServicePage(Class<?> activityClass) {
        Intent intent = new Intent(EwasteHomePage.this, activityClass);
        startActivity(intent);
    }

    // Gesture detection for manual swipe in ViewFlipper
    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            float diffX = e2.getX() - e1.getX();
            if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                if (diffX > 0) {
                    imageFlipper.showPrevious(); // Swipe right
                } else {
                    imageFlipper.showNext(); // Swipe left
                }
                return true;
            }
            return false;
        }
    }
}
