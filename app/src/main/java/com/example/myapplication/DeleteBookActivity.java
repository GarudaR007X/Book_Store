package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class DeleteBookActivity extends AppCompatActivity {
    private ListView listViewBooks;
    private Button btnDelete, btnBack;
    private SQLiteDatabaseHelper dbHelper;
    private ArrayList<String> bookList;
    private String username;
    private ArrayAdapter<String> adapter;
    private int selectedBookIndex = -1;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_book);

        listViewBooks = findViewById(R.id.listViewBooks);
        btnDelete = findViewById(R.id.btnDelete);
        btnBack = findViewById(R.id.btnBack);
        dbHelper = new SQLiteDatabaseHelper(this);
        bookList = new ArrayList<>();

        // Retrieve username and handle null case
        username = getIntent().getStringExtra("username");
        if (username == null || username.isEmpty()) {
            username = "Admin"; // Default value if username is not passed
        }

        loadBooks(); // Load the list of books

        // Handle book selection
        listViewBooks.setOnItemClickListener((parent, view, position, id) -> {
            selectedBookIndex = position;
            Toast.makeText(DeleteBookActivity.this, "Selected: " + bookList.get(position), Toast.LENGTH_SHORT).show();
        });

        // Handle back button click
        btnBack.setOnClickListener(v -> navigateToAdminDashboard());

        // Handle delete button click
        btnDelete.setOnClickListener(v -> {
            if (selectedBookIndex != -1) {
                confirmDeleteBook(bookList.get(selectedBookIndex));
            } else {
                showToast("Please select a book to delete.");
            }
        });
    }

    // Load books from the database
    private void loadBooks() {
        bookList.clear(); // Clear the list before loading
        Cursor cursor = dbHelper.getAllBooks();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String bookName = cursor.getString(cursor.getColumnIndex("book_name"));
                bookList.add(bookName);
            } while (cursor.moveToNext());

            cursor.close(); // Always close the cursor when done
        }

        if (bookList.isEmpty()) {
            showToast("No books available.");
        }

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_single_choice, bookList);
        listViewBooks.setAdapter(adapter);
    }

    // Delete the selected book after confirmation
    private void confirmDeleteBook(String bookName) {
        new AlertDialog.Builder(this)
                .setTitle("Confirm Deletion")
                .setMessage("Are you sure you want to delete " + bookName + "?")
                .setPositiveButton("Yes", (dialog, which) -> deleteBook(bookName))
                .setNegativeButton("No", null)
                .show();
    }

    // Perform the deletion of the book
    private void deleteBook(String bookName) {
        boolean isDeleted = dbHelper.deleteBook(bookName);
        if (isDeleted) {
            bookList.remove(selectedBookIndex);
            adapter.notifyDataSetChanged(); // Update the list view
            selectedBookIndex = -1; // Reset selection
            showToast(bookName + " has been deleted.");
        } else {
            showToast("Failed to delete " + bookName);
        }
    }

    // Navigate back to the Admin Dashboard with the username
    private void navigateToAdminDashboard() {
        Intent intent = new Intent(DeleteBookActivity.this, AdminDashboardActivity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    // Show a toast message for feedback
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
