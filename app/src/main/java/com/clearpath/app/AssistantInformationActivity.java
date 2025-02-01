package com.clearpath.app;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class AssistantInformationActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseFirestore database;
    private EditText firstNameEditText, lastNameEditText, emailEditText, phoneEditText;
    private ProgressBar progressBar;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assistant_information);

        auth = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();

        firstNameEditText = findViewById(R.id.firstNameEditText);
        lastNameEditText = findViewById(R.id.lastNameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        progressBar = findViewById(R.id.progressBar);
        ImageView menuButton = findViewById(R.id.menuButton);
        ImageView profileButton = findViewById(R.id.profileButton);

        fetchAssistantDetails();

        menuButton.setOnClickListener(this::showNavigationMenu);
        profileButton.setOnClickListener(this::showProfileMenu);
    }

    @SuppressLint("SetTextI18n")
    private void fetchAssistantDetails() {
        String userId = auth.getCurrentUser() != null ? auth.getCurrentUser().getUid() : null;
        if (userId == null) {
            // User is not logged in, redirect to login screen
            navigateToPage(MainActivity.class);
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        // Fetch the assistant details using the userId as the document ID
        database.collection("assistants").document(userId).get()
                .addOnSuccessListener(document -> {
                    progressBar.setVisibility(View.GONE);
                    if (document.exists()) {
                        // Set values to UI components if document exists
                        firstNameEditText.setText(document.getString("firstName"));
                        lastNameEditText.setText(document.getString("lastName"));
                        emailEditText.setText(document.getString("email"));
                        phoneEditText.setText(document.getString("phoneNumber"));
                    } else {
                        // Document does not exist
                        Toast.makeText(this, "Assistant details not found", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    progressBar.setVisibility(View.GONE);
                    Log.e("AssistantProfile", "Failed to fetch assistant details", e);
                    Toast.makeText(this, "Failed to fetch assistant details", Toast.LENGTH_SHORT).show();
                });
    }
    private void showNavigationMenu(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.getMenuInflater().inflate(R.menu.navigation_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.menu_home) {
                    navigateToPage(UserHomePageActivity.class);
                    return true;
                } else if (item.getItemId() == R.id.menu_sos) {
                    navigateToPage(UserSOSCallingActivity.class);
                    return true;
                } else if (item.getItemId() == R.id.menu_profile) {
                    navigateToPage(AssistantInformationActivity.class);
                    return true;
                } else {
                    return false;
                }
            }
        });
        popup.show();
    }

    private void showProfileMenu(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.getMenuInflater().inflate(R.menu.profile_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.menu_bell_pin_light) {
                    navigateToPage(MainActivity.class);
                    return true;
                } else if (item.getItemId() == R.id.menu_about_us) {
                    openWebPage();
                    return true;
                } else if (item.getItemId() == R.id.menu_help_center) {
                    dialNumber();
                    return true;
                } else if (item.getItemId() == R.id.menu_log_out) {
                    logOut();
                    return true;
                } else {
                    return false;
                }
            }
        });
        popup.show();
    }

    private void navigateToPage(Class<?> destination) {
        startActivity(new Intent(this, destination));
    }

    private void openWebPage() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.companywebsite.com"));
        startActivity(intent);
    }

    private void dialNumber() {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "800123456"));
        startActivity(intent);
    }

    private void logOut() {
        auth.signOut();
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}