package com.example.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class UpdateBookActivity extends AppCompatActivity {
    private ListView listViewBooks;
    private SQLiteDatabaseHelper dbHelper;
    private ArrayList<Book> bookList;
    private Button btnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_book);

        listViewBooks = findViewById(R.id.listViewBooks);
        dbHelper = new SQLiteDatabaseHelper(this);
        bookList = new ArrayList<>();
        btnBack = findViewById(R.id.btnBack);
        loadBooks();

        // Set up item click listener
        listViewBooks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position < bookList.size()) {
                    Book selectedBook = bookList.get(position);
                    Intent intent = new Intent(UpdateBookActivity.this, UpdateBookDetailsActivity.class);
                    intent.putExtra("bookid", selectedBook.getBookId());
                    Log.d("UpdateBookActivity", "Selected book ID: " + selectedBook.getBookId());
                    startActivity(intent);
                } else {
                    Toast.makeText(UpdateBookActivity.this, "Invalid selection.", Toast.LENGTH_SHORT).show();
                }
            }

        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UpdateBookActivity.this, AdminDashboardActivity.class));
            }
        });
    }

    private void loadBooks() {
        Cursor cursor = null;
        try {
            cursor = dbHelper.getAllBooks();

            if (cursor == null) {
                Toast.makeText(this, "Cursor is null. Unable to fetch books.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (cursor.getCount() > 0) {
                bookList.clear();
                while (cursor.moveToNext()) {
                    int bookId = cursor.getInt(cursor.getColumnIndex("bookid"));
                    String bookName = cursor.getString(cursor.getColumnIndex("book_name"));
                    String author = cursor.getString(cursor.getColumnIndex("book_author"));
                    double price = cursor.getDouble(cursor.getColumnIndex("book_price"));
                    String description = cursor.getString(cursor.getColumnIndex("book_description"));
                    String image = cursor.getString(cursor.getColumnIndex("book_image"));
                    String status = cursor.getString(cursor.getColumnIndex("status"));

                    bookList.add(new Book(bookId, bookName, author, price, description, image, status));
                }

                ArrayAdapter<Book> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, bookList);
                listViewBooks.setAdapter(adapter);
            } else {
                Toast.makeText(this, "No books available.", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            Log.e("UpdateBookActivity", "Error loading books", e);
            Toast.makeText(this, "An error occurred while loading books.", Toast.LENGTH_SHORT).show();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}

