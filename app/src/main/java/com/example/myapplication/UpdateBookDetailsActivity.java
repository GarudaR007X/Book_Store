package com.example.myapplication;

import android.content.Intent;
import android.util.Log;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class UpdateBookDetailsActivity extends AppCompatActivity {
    private int bookId;
    private SQLiteDatabaseHelper dbHelper;
    private EditText editTextBookName, editTextAuthor, editTextPrice, editTextDescription, editTextImage, editTextStatus;
    private Button buttonUpdate,btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_book_details);

        dbHelper = new SQLiteDatabaseHelper(this);
        bookId = getIntent().getIntExtra("bookid", -1); // Correct key "BOOK_ID"
        Log.d("UpdateBookDetailsActivity", "Received book ID: " + bookId);

        // Check if the bookId is valid
        if (bookId == -1) {
            Toast.makeText(this, "Invalid Book ID", Toast.LENGTH_SHORT).show();
            finish(); // Close the activity if the book ID is invalid
            return;
        }

        // Initialize UI elements
        editTextBookName = findViewById(R.id.etBookName);
        editTextAuthor = findViewById(R.id.etAuthor);
        editTextPrice = findViewById(R.id.etPrice);
        editTextDescription = findViewById(R.id.etDescription);
        editTextImage = findViewById(R.id.etImage);
        editTextStatus = findViewById(R.id.etStatus);
        buttonUpdate = findViewById(R.id.btnUpdate);
        btnBack = findViewById(R.id.btnBack);
        loadBookDetails(bookId); // Load the book details using the bookId

        // Set up the button click listener for updating book details
        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateBookDetails();
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UpdateBookDetailsActivity.this, AdminDashboardActivity.class));
            }
        });
    }

    private void loadBookDetails(int bookId) {
        Cursor cursor = dbHelper.getBookById(bookId);
        if (cursor != null && cursor.moveToFirst()) {
            // Populate your fields with the retrieved data
            String bookName = cursor.getString(cursor.getColumnIndex("book_name"));
            String author = cursor.getString(cursor.getColumnIndex("book_author"));
            double price = cursor.getDouble(cursor.getColumnIndex("book_price"));
            String description = cursor.getString(cursor.getColumnIndex("book_description"));
            String image = cursor.getString(cursor.getColumnIndex("book_image"));
            String status = cursor.getString(cursor.getColumnIndex("status"));

            // Set the values to the EditText fields
            editTextBookName.setText(bookName);
            editTextAuthor.setText(author);
            editTextPrice.setText(String.valueOf(price));
            editTextDescription.setText(description);
            editTextImage.setText(image);
            editTextStatus.setText(status);

            cursor.close(); // Close the cursor
        } else {
            Toast.makeText(this, "Book not found.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void updateBookDetails() {
        // Get updated values from the EditText fields
        String bookName = editTextBookName.getText().toString().trim();
        String author = editTextAuthor.getText().toString().trim();
        String priceString = editTextPrice.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();
        String image = editTextImage.getText().toString().trim();
        String status = editTextStatus.getText().toString().trim();

        // Validate that all fields are filled
        if (bookName.isEmpty() || author.isEmpty() || priceString.isEmpty() || description.isEmpty() || image.isEmpty() || status.isEmpty()) {
            Toast.makeText(this, "Please fill all fields.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validate price input
        double price;
        try {
            price = Double.parseDouble(priceString);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid price value.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Update the book in the database
        boolean isUpdated = dbHelper.updateBook(bookId, bookName, author, price, description, image, status);
        if (isUpdated) {
            Toast.makeText(this, "Book details updated successfully.", Toast.LENGTH_SHORT).show();
            finish(); // Close the activity after successful update
        } else {
            Toast.makeText(this, "Failed to update book details.", Toast.LENGTH_SHORT).show();
        }
    }
}
