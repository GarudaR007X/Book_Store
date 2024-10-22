package com.example.myapplication;// AdminDashboardActivity.java
import android.content.Intent;
import android.widget.*;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class AdminDashboardActivity extends AppCompatActivity {
    private TextView welcomeTextView;
    private Button addBookButton;
    private Button updateBookButton;
    private Button deleteBookButton;
    private Button viewBooksButton;
    private Button logoutButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.app_name);
        }
        welcomeTextView = findViewById(R.id.welcome_text);
        addBookButton = findViewById(R.id.add_book_button);
        updateBookButton = findViewById(R.id.update_book_button);
        deleteBookButton = findViewById(R.id.delete_book_button);
        viewBooksButton = findViewById(R.id.view_books_button);
        logoutButton = findViewById(R.id.logout_button);

        String username = getIntent().getStringExtra("username");



        welcomeTextView.setText("Welcome, " + username + "!");
        addBookButton.setOnClickListener(view -> {
            Intent intent = new Intent(AdminDashboardActivity.this, AddBookActivity.class);
            startActivity(intent);
        });

        updateBookButton.setOnClickListener(view -> {
            Intent intent = new Intent(AdminDashboardActivity.this, UpdateBookActivity.class);
            startActivity(intent);
        });

        deleteBookButton.setOnClickListener(view -> {
            Intent intent = new Intent(AdminDashboardActivity.this, DeleteBookActivity.class);
            startActivity(intent);
        });

        viewBooksButton.setOnClickListener(view -> {
            Intent intent = new Intent(AdminDashboardActivity.this, ViewBooksActivity.class);
            startActivity(intent);
        });

        logoutButton.setOnClickListener(view -> {
            // Redirect to login page
            Intent intent = new Intent(AdminDashboardActivity.this, LoginActivity.class);
            startActivity(intent);
            finish(); // Close the dashboard
        });
    }
}

