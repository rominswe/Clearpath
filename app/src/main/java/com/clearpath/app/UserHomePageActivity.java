package com.clearpath.app;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;
import androidx.activity.ComponentActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import org.opencv.android.OpenCVLoader;

public class UserHomePageActivity extends ComponentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home_page); // Set layout from XML

        // Initialize OpenCV
        if (OpenCVLoader.initDebug()) {
            Log.i("OpenCV", "OpenCV loaded successfully");
        } else {
            Log.e("OpenCV", "OpenCV loading failed");
        }

        // Initialize the views by referencing their IDs
        // Declare the views
        Button startNavigationButton = findViewById(R.id.start_navigation_button);

        // Setup the button click listeners
        startNavigationButton.setOnClickListener(v -> onStartNavigationAndObjectDetection());

        // Check Camera permission
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, 100);
        }
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