// ViewBooksActivity.java
package com.example.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class ViewBooksActivity extends AppCompatActivity {
    private TextView tvBooks;
    private ListView listViewBooks; // Changed from TextView to ListView
    private Button btnBack;
    private SQLiteDatabaseHelper dbHelper;

    // Define constants for column names
    private static final String COLUMN_BOOKNAME = "book_name"; // Ensure this matches your database
    private static final String COLUMN_AUTHOR = "book_author";
    private static final String COLUMN_PRICE = "book_price";
    private static final String COLUMN_DESCRIPTION = "book_description";
    private static final String COLUMN_STATUS = "status";
    private static final String COLUMN_IMAGE = "book_image"; // Image column

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_books);

        listViewBooks = findViewById(R.id.listViewBooks); // Initialize ListView
        btnBack = findViewById(R.id.btnBack);

        dbHelper = new SQLiteDatabaseHelper(this);

        // Load books when the activity is created
        loadBooks();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ViewBooksActivity.this, AdminDashboardActivity.class));
            }
        });
    }

    private void loadBooks() {
        Cursor cursor = dbHelper.getAllBooks(); // Fetch all books from the database
        List<Book> booksList = new ArrayList<>(); // Create a list to hold book objects

        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String bookName = cursor.getString(cursor.getColumnIndex(COLUMN_BOOKNAME));
                String author = cursor.getString(cursor.getColumnIndex(COLUMN_AUTHOR));
                double price = cursor.getDouble(cursor.getColumnIndex(COLUMN_PRICE));
                String description = cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION));
                String status = cursor.getString(cursor.getColumnIndex(COLUMN_STATUS));
                String imageResourceName = cursor.getString(cursor.getColumnIndex(COLUMN_IMAGE));

                // Create a Book object and add it to the list
                Book book = new Book(bookName, author, price, description, status, imageResourceName);
                booksList.add(book);
            }

            // Set the custom adapter to display the book details in the ListView
            BookAdapter adapter = new BookAdapter(this, booksList);
            listViewBooks.setAdapter(adapter);
        } else {
            // Handle no books found scenario
            ArrayList<Book> noBooksList = new ArrayList<>();
            noBooksList.add(new Book("No books available.", "", 0, "", "", ""));
            BookAdapter adapter = new BookAdapter(this, noBooksList);
            listViewBooks.setAdapter(adapter);
        }
        cursor.close(); // Close the cursor to avoid memory leaks
    }
}
