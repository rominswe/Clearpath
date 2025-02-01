package com.clearpath.app;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class AssistantHomePageActivity extends AppCompatActivity {

    private LinearLayout userListContainer;
    private final ArrayList<String> addedUsers = new ArrayList<>();
    private Context context;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assistant_home_page);

        context = this;

        // Initialize UI elements
        @SuppressLint("WrongViewCast") ImageButton btnProfileMenu = findViewById(R.id.btnProfileMenu);
        @SuppressLint("WrongViewCast") ImageButton btnDropdownMenu = findViewById(R.id.btnDropdownMenu);
        Button btnAddUser = findViewById(R.id.btnAddUser);
        userListContainer = findViewById(R.id.userListContainer);

        // Click Listeners
        btnProfileMenu.setOnClickListener(v -> showProfileMenu());
        btnDropdownMenu.setOnClickListener(v -> showDropdownMenu());
        btnAddUser.setOnClickListener(v -> showAddUserDialog());
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

    // Show Input Dialog for Adding a User
    private void showAddUserDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Enter the ID Code");

        // Set up input field
        EditText input = new EditText(context);
        builder.setView(input);

        builder.setPositiveButton("OK", (dialog, which) -> {
            String userId = input.getText().toString().trim();
            if (!userId.isEmpty() && addedUsers.size() < 10) {
                addedUsers.add(userId);
                updateUserList();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    // Update User List
    private void updateUserList() {
        userListContainer.removeAllViews();
        for (String user : addedUsers) {
            Button userButton = new Button(context);
            userButton.setText(user);
            userButton.setOnClickListener(v -> navigateToMonitoring(user));
            userListContainer.addView(userButton);
        }
    }

    // Navigate based on menu selection
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

    // Navigate to Monitoring Activity with User ID
    private void navigateToMonitoring(String userId) {
        Intent intent = new Intent(this, AssistantMonitoringActivity.class);
        intent.putExtra("USER_ID", userId);
        startActivity(intent);
    }
}
