package com.clearpath.app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Locale;
import java.util.UUID;

public class UserSignUpActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseFirestore database;
    private EditText firstNameEditText, lastNameEditText, emailEditText, phoneEditText, passwordEditText, confirmPasswordEditText;
    private ProgressBar progressBar;
    private TextView errorTextView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_signup);

        auth = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();

        firstNameEditText = findViewById(R.id.firstNameEditText);
        lastNameEditText = findViewById(R.id.lastNameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        Button signUpButton = findViewById(R.id.signUpButton);
        progressBar = findViewById(R.id.progressBar);
        errorTextView = findViewById(R.id.errorTextView);

        signUpButton.setOnClickListener(v -> {
            String firstName = firstNameEditText.getText().toString().trim();
            String lastName = lastNameEditText.getText().toString().trim();
            String email = emailEditText.getText().toString().trim();
            String phone = phoneEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();
            String confirmPassword = confirmPasswordEditText.getText().toString().trim();

            userSignUp(firstName, lastName, email, phone, password, confirmPassword);
        });
    }

    @SuppressLint("SetTextI18n")
    private void userSignUp(String firstName, String lastName, String email, String phone, String password, String confirmPassword) {
        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            errorTextView.setText("All fields must be filled");
            return;
        }

        if (!password.equals(confirmPassword)) {
            errorTextView.setText("Passwords do not match");
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    progressBar.setVisibility(View.GONE);
                    if (task.isSuccessful()) {
                        String userId = auth.getCurrentUser() != null ? auth.getCurrentUser().getUid() : null;
                        if (userId != null) {
                            String uniqueCode = UUID.randomUUID().toString().substring(0, 8).toUpperCase(Locale.getDefault());

                            HashMap<String, Object> userMap = new HashMap<>();
                            userMap.put("firstName", firstName);
                            userMap.put("lastName", lastName);
                            userMap.put("email", email);
                            userMap.put("phoneNumber", phone);
                            userMap.put("role", "U");
                            userMap.put("uniqueCode", uniqueCode);

                            database.collection("users").document(userId)
                                    .set(userMap)
                                    .addOnSuccessListener(aVoid -> {
                                        Toast.makeText(this, "User registered successfully", Toast.LENGTH_SHORT).show();
                                        navigateToUserHomePage();
                                    })
                                    .addOnFailureListener(e -> {
                                        errorTextView.setText("Failed to save user data: " + e.getMessage());
                                    });
                        }
                    } else {
                        errorTextView.setText("Authentication failed: " + (task.getException() != null ? task.getException().getMessage() : "Unknown error"));
                    }
                });
    }

    private void navigateToUserHomePage() {
        startActivity(new Intent(this, UserHomePageActivity.class));
    }
}