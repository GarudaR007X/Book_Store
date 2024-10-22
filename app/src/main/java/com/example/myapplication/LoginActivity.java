package com.example.myapplication;



import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    private EditText usernameInput, passwordInput;
    private RadioGroup roleGroup;
    private Button loginButton, registerButton;
    private SQLiteDatabaseHelper databaseHelper;
    private TextView registerLink;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.app_name);
        }
        usernameInput = findViewById(R.id.username);
        passwordInput = findViewById(R.id.password);
        roleGroup = findViewById(R.id.role_group);
        loginButton = findViewById(R.id.login_button);
        registerLink = findViewById(R.id.register_link);

        databaseHelper = new SQLiteDatabaseHelper(this);

        loginButton.setOnClickListener(view -> {
            String username = usernameInput.getText().toString();
            String password = passwordInput.getText().toString();
            String role = getSelectedRole();

            if (username.isEmpty() || password.isEmpty() || role == null) {
                Toast.makeText(LoginActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
                return;
            }

            if (databaseHelper.loginUser(username, password, role)) {
                if (role.equals("admin")) {
                    // Redirect to Admin Dashboard
                    Intent intent = new Intent(LoginActivity.this, AdminDashboardActivity.class);
                    intent.putExtra("username", username);
                    startActivity(intent);
                } else {
                    // Redirect to User Activity
                    Intent intent = new Intent(LoginActivity.this, UserActivity.class);
                    intent.putExtra("username", username);
                    startActivity(intent);
                }
                finish(); // Close login activity
            } else {
                Toast.makeText(LoginActivity.this, "Login failed. Please register first.", Toast.LENGTH_SHORT).show();
            }
        });

        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Redirect to the RegistrationActivity
                Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);

                startActivity(intent);
            }
        });
    }

    // Method to get selected role from RadioGroup
    private String getSelectedRole() {
        int selectedId = roleGroup.getCheckedRadioButtonId();
        if (selectedId == R.id.admin_radio) {
            return "admin";
        } else if (selectedId == R.id.user_radio) {
            return "user";
        }
        return null; // No role selected


    }

    }
