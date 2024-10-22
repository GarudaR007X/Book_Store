package com.example.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class UserActivity extends AppCompatActivity {
    private TextView welcomeTextView;
    private ListView listViewBooks;
    private SQLiteDatabaseHelper dbHelper;
    private Button logoutButton;
    private static final String COLUMN_BOOKNAME = "book_name";
    private static final String COLUMN_AUTHOR = "book_author";
    private static final String COLUMN_PRICE = "book_price";
    private static final String COLUMN_DESCRIPTION = "book_description";
    private static final String COLUMN_STATUS = "status";
    private static final String COLUMN_IMAGE = "book_image";
    private static final String USER_PREFS = "UserPrefs"; // Name of SharedPreferences
    private static final String USERNAME_KEY = "username"; // Key for username in SharedPreferences

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.app_name);
        }

        welcomeTextView = findViewById(R.id.welcome_text);
        listViewBooks = findViewById(R.id.listViewBooks);
        dbHelper = new SQLiteDatabaseHelper(this);
        logoutButton = findViewById(R.id.logout_button);
        // Retrieve username from SharedPreferences
        String username = getIntent().getStringExtra("username");// Default to "Guest"
        welcomeTextView.setText("Welcome, " + username + "!");


        loadBooks();

        // Add item click listener to redirect to the BookDetailsActivity when a book is clicked
        listViewBooks.setOnItemClickListener((parent, view, position, id) -> {
            Book selectedBook = (Book) parent.getItemAtPosition(position);

            // Redirect to BookDetailsActivity with book details
            Intent intent = new Intent(UserActivity.this, BookDetailsActivity.class);
            intent.putExtra("book_author", selectedBook.getAuthor());
            intent.putExtra("book_price", selectedBook.getPrice());
            intent.putExtra("book_description", selectedBook.getDescription());
            intent.putExtra("book_image", selectedBook.getImageResourceName());
            startActivity(intent);
        });
        logoutButton.setOnClickListener(view -> {
            // Redirect to LoginActivity
            Intent intent = new Intent(UserActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK); // Clear the activity stack
            startActivity(intent);
            finish(); // Finish UserActivity
        });
    }

    private void loadBooks() {
        Cursor cursor = dbHelper.getAvailableBooks();
        List<Book> booksList = new ArrayList<>();

        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String bookName = cursor.getString(cursor.getColumnIndex(COLUMN_BOOKNAME));
                String author = cursor.getString(cursor.getColumnIndex(COLUMN_AUTHOR));
                double price = cursor.getDouble(cursor.getColumnIndex(COLUMN_PRICE));
                String description = cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION));
                String status = cursor.getString(cursor.getColumnIndex(COLUMN_STATUS));
                String imageResourceName = cursor.getString(cursor.getColumnIndex(COLUMN_IMAGE));

                Book book = new Book(bookName, author, price, description, status, imageResourceName);
                booksList.add(book);
            }

            BookAdapter adapter = new BookAdapter(this, booksList);
            listViewBooks.setAdapter(adapter);
        } else {
            ArrayList<Book> noBooksList = new ArrayList<>();
            noBooksList.add(new Book("No books available.", "", 0, "", "", ""));
            BookAdapter adapter = new BookAdapter(this, noBooksList);
            listViewBooks.setAdapter(adapter);
        }
        cursor.close();
    }
}
