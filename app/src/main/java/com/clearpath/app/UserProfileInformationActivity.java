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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserProfileInformationActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseFirestore database;
    private EditText firstNameEditText, lastNameEditText, emailEditText, phoneEditText;
    private TextView uniqueCodeTextView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_information);

        auth = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();

        firstNameEditText = findViewById(R.id.firstNameEditText);
        lastNameEditText = findViewById(R.id.lastNameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        uniqueCodeTextView = findViewById(R.id.uniqueCodeTextView);
        progressBar = findViewById(R.id.progressBar);
        ImageView menuButton = findViewById(R.id.menuButton);
        ImageView profileButton = findViewById(R.id.profileButton);

        fetchUserDetails();

        menuButton.setOnClickListener(this::showNavigationMenu);
        profileButton.setOnClickListener(this::showProfileMenu);
    }

    @SuppressLint("SetTextI18n")
    private void fetchUserDetails() {
        String userId = auth.getCurrentUser() != null ? auth.getCurrentUser().getUid() : null;
        if (userId == null) {
            // User is not logged in, redirect to login screen
            navigateToPage(MainActivity.class);
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        database.collection("users").document(userId).get()
                .addOnSuccessListener(document -> {
                    progressBar.setVisibility(View.GONE);
                    if (document.exists()) {
                        // Fetching fields and checking if they exist
                        String firstName = document.getString("firstName");
                        String lastName = document.getString("lastName");
                        String email = document.getString("email");
                        String phoneNumber = document.getString("phoneNumber");
                        String uniqueCode = document.getString("uniqueCode");

                        // Set default text if the field is null
                        firstNameEditText.setText(firstName != null ? firstName : "N/A");
                        lastNameEditText.setText(lastName != null ? lastName : "N/A");
                        emailEditText.setText(email != null ? email : "N/A");
                        phoneEditText.setText(phoneNumber != null ? phoneNumber : "N/A");

                        // For unique code, format the string if it exists
                        uniqueCodeTextView.setText(getString(R.string.unique_code, uniqueCode != null ? uniqueCode : "N/A"));
                    } else {
                        // Handle case where the document does not exist
                        Toast.makeText(this, "User details not found", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    progressBar.setVisibility(View.GONE);
                    Log.e("UserProfile", "Failed to fetch user details", e);
                    // Better error handling with a user-friendly message
                    Toast.makeText(this, "Failed to fetch user details. Please try again later.", Toast.LENGTH_SHORT).show();
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
                    navigateToPage(UserProfileInformationActivity.class);
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
