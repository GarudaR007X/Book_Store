package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddBookActivity extends AppCompatActivity {
    private EditText etBookName, etAuthor, etPrice, etDescription, etStatus;
    private Button btnSave, btnBack;
    private ImageView ivBookImage;
    private Spinner spnImages;
    private SQLiteDatabaseHelper dbHelper;

    // Drawable resources to show in Spinner
    private Integer[] images = {R.drawable.book_img, R.drawable.book_img, R.drawable.book_img};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        etBookName = findViewById(R.id.etBookName);
        etAuthor = findViewById(R.id.etAuthor);
        etPrice = findViewById(R.id.etPrice);
        etDescription = findViewById(R.id.etDescription);
        etStatus = findViewById(R.id.etStatus);
        btnSave = findViewById(R.id.btnSave);
        btnBack = findViewById(R.id.btnBack);
        ivBookImage = findViewById(R.id.ivBookImage);
        spnImages = findViewById(R.id.spnImages);

        dbHelper = new SQLiteDatabaseHelper(this);

        // Set up the spinner with the custom adapter
        CustomSpinnerAdapter adapter = new CustomSpinnerAdapter(this, images);
        spnImages.setAdapter(adapter);

        // Display the selected image in the ImageView when a selection is made
        spnImages.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ivBookImage.setImageResource(images[position]); // Set ImageView to the selected drawable
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No action needed
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String bookName = etBookName.getText().toString().trim();
                String author = etAuthor.getText().toString().trim();
                String priceString = etPrice.getText().toString().trim();
                String description = etDescription.getText().toString().trim();
                String status = etStatus.getText().toString().trim();

                if (bookName.isEmpty() || author.isEmpty() || priceString.isEmpty() || description.isEmpty() || status.isEmpty()) {
                    Toast.makeText(AddBookActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                double price;
                try {
                    price = Double.parseDouble(priceString);
                } catch (NumberFormatException e) {
                    Toast.makeText(AddBookActivity.this, "Invalid price format", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Get the selected image resource from the spinner
                int selectedImageResource = images[spnImages.getSelectedItemPosition()];

                if (dbHelper.addBook(bookName, author, price, description, selectedImageResource, status)) {
                    Toast.makeText(AddBookActivity.this, "Book added successfully", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(AddBookActivity.this, "Failed to add book", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddBookActivity.this, AdminDashboardActivity.class));
            }
        });
    }
}
