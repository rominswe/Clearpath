package com.clearpath.app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.opencv.android.OpenCVLoader;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        if (OpenCVLoader.initLocal()) {
            Log.i("opencv", "OpenCV has been successfully integrated");
        }

        Button userButton = findViewById(R.id.user_button);
        Button assistantButton = findViewById(R.id.assistant_button);

        userButton.setOnClickListener(v -> navigateToLogin("user"));
        assistantButton.setOnClickListener(v -> navigateToLogin("assistant"));
    }

    private void navigateToLogin(String userRole) {
        Intent intent;
        switch (userRole) {
            case "assistant":
                intent = new Intent(this, AssistantLoginActivity.class);
                break;
            case "user":
                intent = new Intent(this, UserLoginActivity.class);
                break;
            default:
                throw new IllegalArgumentException("Unknown role: " + userRole);
        }
        Log.d("MainActivity", "Navigating to: " + Objects.requireNonNull(intent.getComponent()).getClassName());
        startActivity(intent);
    }
}