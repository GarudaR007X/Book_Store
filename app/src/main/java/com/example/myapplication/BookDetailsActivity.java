package com.example.myapplication;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView; // Import ImageView
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.HashSet;
import java.util.Set;

public class BookDetailsActivity extends AppCompatActivity {

    private TextView bookName, bookAuthor, bookPrice, bookDescription;
    private Button addToCartButton, backButton;
    private ImageView bookImage; // Declare ImageView for book image
    private String bookNameStr, bookPriceStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);

        // Initialize views
        bookName = findViewById(R.id.book_name);
        bookAuthor = findViewById(R.id.book_author);
        bookPrice = findViewById(R.id.book_price);
        bookDescription = findViewById(R.id.book_description);
        bookImage = findViewById(R.id.book_image); // Initialize ImageView
        addToCartButton = findViewById(R.id.add_to_cart_button);
        backButton = findViewById(R.id.back_button);

        // Get book data passed from UserActivity
        Intent intent = getIntent();
        bookNameStr = intent.getStringExtra("book_name");
        String bookAuthorStr = intent.getStringExtra("book_author");
        bookPriceStr = String.valueOf(intent.getDoubleExtra("book_price", 0.0));
        String bookDescriptionStr = intent.getStringExtra("book_description");
        String bookImageResourceName = intent.getStringExtra("book_image"); // New line for image resource

        // Set book details
        bookName.setText(bookNameStr);
        bookAuthor.setText("Author: " + bookAuthorStr);
        bookPrice.setText("Price: Rs:" + bookPriceStr);
        bookDescription.setText(bookDescriptionStr);

        // Load the book image (You can use Glide or any image loading library)
        int imageResource = getResources().getIdentifier(bookImageResourceName, "drawable", getPackageName());
        bookImage.setImageResource(imageResource);

        // Handle Add to Cart button click
        addToCartButton.setOnClickListener(view -> {
            addToCart(bookNameStr, bookPriceStr);

            // Redirect to CartActivity
            Intent cartIntent = new Intent(BookDetailsActivity.this, CartActivity.class);
            startActivity(cartIntent);
        });

        // Handle Back button click
        backButton.setOnClickListener(view -> finish());
    }

    private void addToCart(String bookName, String bookPrice) {
        SharedPreferences sharedPreferences = getSharedPreferences("CartPreferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Set<String> cartItems = sharedPreferences.getStringSet("cart", new HashSet<>());
        cartItems.add(bookName + " - Rs" + bookPrice); // Add book details to the cart
        editor.putStringSet("cart", cartItems);
        editor.apply();

        Toast.makeText(this, "Book added to cart", Toast.LENGTH_SHORT).show();
    }
}
