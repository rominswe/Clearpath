package com.clearpath.app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class AssistantSOSCallingActivity extends AppCompatActivity {

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assistant_sos_calling);

        // Find views by ID
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView titleText = findViewById(R.id.titleText);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button personAButton = findViewById(R.id.personAButton);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button personBButton = findViewById(R.id.personBButton);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button personCButton = findViewById(R.id.personCButton);

        titleText.setText("Emergency Contacts");

        // Set onClickListeners for the buttons
        personAButton.setOnClickListener(v -> makeEmergencyCall(this, "tel:25255"));
        personBButton.setOnClickListener(v -> makeEmergencyCall(this, "tel:123456789")); // Replace with actual number
        personCButton.setOnClickListener(v -> makeEmergencyCall(this, "tel:987654321")); // Replace with actual number
    }

    // Method to initiate the emergency call
    private void makeEmergencyCall(Context context, String uri) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(uri));
        context.startActivity(intent);
    }
}