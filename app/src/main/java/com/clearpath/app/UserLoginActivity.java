package com.clearpath.app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class UserLoginActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private EditText emailEditText, passwordEditText;
    private ProgressBar progressBar;
    private TextView errorTextView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);

        auth = FirebaseAuth.getInstance();

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        Button loginButton = findViewById(R.id.loginButton);
        Button signUpButton = findViewById(R.id.signUpButton);
        progressBar = findViewById(R.id.progressBar);
        errorTextView = findViewById(R.id.errorTextView);

        loginButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();
            loginUser(email, password, this);
        });

        signUpButton.setOnClickListener(v -> navigateToUserSignUp(this));
    }

    @SuppressLint("SetTextI18n")
    public void loginUser(String email, String password, Context context) {
        if (!validateEmail(email)) {
            errorTextView.setText("Please enter a valid email address.");
            return;
        }

        if (!validatePassword(password)) {
            errorTextView.setText("Password must be at least 6 characters long.");
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    progressBar.setVisibility(View.GONE);
                    if (task.isSuccessful()) {
                        showToast(context);
                        navigateToUserHomePage(context);
                    } else {
                        String errorMsg = task.getException() != null ? task.getException().getMessage() : "Authentication failed";
                        errorTextView.setText(errorMsg);
                    }
                });
    }

    private boolean validateEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean validatePassword(String password) {
        return password.length() >= 6;
    }

    private void navigateToUserHomePage(Context context) {
        Intent intent = new Intent(context, UserHomePageActivity.class);
        context.startActivity(intent);
    }

    public void navigateToUserSignUp(Context context) {
        Intent intent = new Intent(context, UserSignUpActivity.class);
        context.startActivity(intent);
    }

    private void showToast(Context context) {
        Toast.makeText(context, "Login successful", Toast.LENGTH_SHORT).show();
    }
}