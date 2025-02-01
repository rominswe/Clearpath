package com.clearpath.app;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.ComponentActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.opencv.android.OpenCVLoader;

public class UserHomePageActivity extends ComponentActivity {

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home_page);

        context = this;

        // Initialize the buttons
        @SuppressLint({"WrongViewCast", "MissingInflatedId", "LocalSuppress"})
        ImageButton btnProfileMenu = findViewById(R.id.btnProfileMenu);
        @SuppressLint({"WrongViewCast", "MissingInflatedId", "LocalSuppress"})
        ImageButton btnDropdownMenu = findViewById(R.id.btnDropdownMenu);

        // Initialize OpenCV
        if (OpenCVLoader.initLocal()) {
            Log.i("OpenCV", "OpenCV loaded successfully");
        } else {
            Log.e("OpenCV", "OpenCV loading failed");
        }

        // Initialize the views by referencing their IDs
        Button startNavigationButton = findViewById(R.id.start_navigation_button);

        // Setup the button click listeners
        startNavigationButton.setOnClickListener(v -> onStartNavigationAndObjectDetection());

        // Check Camera permission
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, 100);
        }

        // Handle Profile Menu Button click
        btnProfileMenu.setOnClickListener(v -> showProfileMenu());

        // Handle Dropdown Menu Button click
        btnDropdownMenu.setOnClickListener(v -> showDropdownMenu());
    }

    // Show Profile Menu
    private void showProfileMenu() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Profile Menu")
                .setItems(new CharSequence[]{"Notification", "About Us", "Help Center"},
                        (dialog, which) -> {
                            switch (which) {
                                case 0:
                                    navigateToMenu("notification");
                                    break;
                                case 1:
                                    navigateToMenu("aboutUs");
                                    break;
                                case 2:
                                    navigateToMenu("helpCenter");
                                    break;
                            }
                        });
        builder.show();
    }

    // Show Dropdown Menu
    private void showDropdownMenu() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Menu")
                .setItems(new CharSequence[]{"Home", "SOS Call", "Profile"},
                        (dialog, which) -> {
                            switch (which) {
                                case 0:
                                    navigateToMenu("home");
                                    break;
                                case 1:
                                    navigateToMenu("call");
                                    break;
                                case 2:
                                    navigateToMenu("profile");
                                    break;
                            }
                        });
        builder.show();
    }

    // Navigate to a different screen based on selected menu item
    private void navigateToMenu(String page) {
        Intent intent = switch (page) {
            case "home" -> new Intent(this, UserHomePageActivity.class);
            case "call" -> new Intent(this, UserSOSCallingActivity.class);
            case "profile" -> new Intent(this, UserProfileInformationActivity.class);
            case "notification" -> new Intent(this, MainActivity.class);
            case "aboutUs" ->
                    new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.companywebsite.com"));
            case "helpCenter" -> new Intent(Intent.ACTION_DIAL, Uri.parse("tel:800123456"));
            default -> throw new IllegalArgumentException("Unknown page: " + page);
        };
        startActivity(intent);
    }

    // Function to start navigation and object detection after destination is confirmed
    private void onStartNavigationAndObjectDetection() {
        // Logic to confirm the destination and start object detection
        startNavigation();
    }

    private void startNavigation() {
        // Placeholder for starting navigation logic
        // In real app, this would use the GPS or map API to navigate to the destination
        Log.i("Navigation", "Navigation started to destination.");
        Toast.makeText(this, "Navigation started", Toast.LENGTH_SHORT).show();

        // Start Object Detection here
        startObjectDetection();
    }

    private void startObjectDetection() {
        // Placeholder for object detection logic
        // In real app, this would involve OpenCV object detection code
        Log.i("Object Detection", "Object detection started.");
        Toast.makeText(this, "Object detection started", Toast.LENGTH_SHORT).show();
    }

}
