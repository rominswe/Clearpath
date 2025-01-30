package com.clearpath.app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class UserSOSCallingActivity extends AppCompatActivity {

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_sos_calling);

        // Find views by ID
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView titleText = findViewById(R.id.titleText);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button policeButton = findViewById(R.id.policeButton);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button hospitalButton = findViewById(R.id.hospitalButton);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button assistantButton = findViewById(R.id.assistantButton);

        titleText.setText("Emergency Contacts");

        // Set onClickListeners for the buttons
        policeButton.setOnClickListener(v -> makeEmergencyCall(this, "tel:911"));
        hospitalButton.setOnClickListener(v -> makeEmergencyCall(this, "tel:123456789")); // Replace with actual number
        assistantButton.setOnClickListener(v -> makeEmergencyCall(this, "tel:987654321")); // Replace with actual number
    }

    // Method to initiate the emergency call
    private void makeEmergencyCall(Context context, String uri) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(uri));
        context.startActivity(intent);
    }
}