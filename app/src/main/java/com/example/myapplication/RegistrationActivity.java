// RegistrationActivity.java
package com.example.myapplication;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class RegistrationActivity extends AppCompatActivity {
    private EditText usernameInput, passwordInput;
    private RadioGroup roleGroup;
    private Button registerButton;
    private SQLiteDatabaseHelper databaseHelper;
    private TextView loginLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.app_name);  // Set app title
        }

        usernameInput = findViewById(R.id.username);
        passwordInput = findViewById(R.id.password);
        roleGroup = findViewById(R.id.role_group);
        registerButton = findViewById(R.id.register_button);
        loginLink = findViewById(R.id.login_link);
        databaseHelper = new SQLiteDatabaseHelper(this);

        // Handle register button click
        registerButton.setOnClickListener(view -> {
            String username = usernameInput.getText().toString();
            String password = passwordInput.getText().toString();
            String role = getSelectedRole();

            if (username.isEmpty() || password.isEmpty() || role == null) {
                Toast.makeText(RegistrationActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
                return;
            }

            // Register user and check if the registration is successful
            if (databaseHelper.registerUser(username, password, role)) {
                Toast.makeText(RegistrationActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                startActivity(intent);  // Go to login page after registration
                finish();
            } else {
                Toast.makeText(RegistrationActivity.this, "Username already exists", Toast.LENGTH_SHORT).show();
            }
        });

        // Handle login link click to go back to login page
        loginLink.setOnClickListener(view -> {
            Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();  // Close the registration activity
        });
    }

    // Method to get the selected role from the RadioGroup
    private String getSelectedRole() {
        int selectedId = roleGroup.getCheckedRadioButtonId();
        if (selectedId == R.id.admin_radio) {
            return "admin";
        } else if (selectedId == R.id.user_radio) {
            return "user";
        }
        return null;  // No role selected
    }
}

