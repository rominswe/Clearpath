package com.clearpath.app;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AssistantLoginActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private EditText emailInput, passwordInput;
    private ProgressBar progressBar;
    private TextView errorText;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assistant_login);

        auth = FirebaseAuth.getInstance();
        emailInput = findViewById(R.id.email_input);
        passwordInput = findViewById(R.id.password_input);
        progressBar = findViewById(R.id.progress_bar);
        errorText = findViewById(R.id.error_text);
        Button loginButton = findViewById(R.id.login_button);
        Button signUpButton = findViewById(R.id.sign_up_button);

        loginButton.setOnClickListener(v -> loginAssistantUser());
        signUpButton.setOnClickListener(v -> navigateToAssistantSignUp());
    }

    @SuppressLint("SetTextI18n")
    private void loginAssistantUser() {
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            errorText.setText("Please enter a valid email address");
            return;
        }

        if (password.length() < 6) {
            errorText.setText("Password must be at least 6 characters long");
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        Task<AuthResult> loginSuccessful = auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    progressBar.setVisibility(View.GONE);
                    if (task.isSuccessful()) {
                        FirebaseUser user = auth.getCurrentUser();
                        Toast.makeText(AssistantLoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(AssistantLoginActivity.this, AssistantHomePageActivity.class));
                        finish();
                    } else {
                        errorText.setText(task.getException() != null ? task.getException().getMessage() : "Authentication failed");
                    }
                });
    }

    private void navigateToAssistantSignUp() {
        Intent intent = new Intent(this, AssistantSignUpActivity.class);
        startActivity(intent);
    }
}