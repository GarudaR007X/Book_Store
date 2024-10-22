// BookAdapter.java
package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;



import java.util.List;

public class BookAdapter extends ArrayAdapter<Book> {
    private Context context;
    private List<Book> books;

    public BookAdapter(Context context, List<Book> books) {
        super(context, 0, books);
        this.context = context;
        this.books = books;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_book, parent, false);
        }

        // Get the data item for this position
        Book book = books.get(position);

        // Lookup view for data population
        TextView tvBookName = convertView.findViewById(R.id.tvBookName);
        TextView tvAuthor = convertView.findViewById(R.id.tvAuthor);
        TextView tvPrice = convertView.findViewById(R.id.tvPrice);
        TextView tvDescription = convertView.findViewById(R.id.tvDescription);
        TextView tvStatus = convertView.findViewById(R.id.tvStatus);
        ImageView ivBookImage = convertView.findViewById(R.id.ivBookImage);

        // Populate the data into the template view using the data object
        tvBookName.setText(book.getName());
        tvAuthor.setText(book.getAuthor());
        tvPrice.setText(String.valueOf(book.getPrice()));
        tvDescription.setText(book.getDescription());
        tvStatus.setText(book.getStatus());

        // Set the ImageView with the correct image resource
        int imageResId = context.getResources().getIdentifier(book.getImageResourceName(), "drawable", context.getPackageName());
        if (imageResId != 0) {
            ivBookImage.setImageResource(imageResId);
        } else {
            ivBookImage.setImageResource(R.drawable.book_img); // Set a default image if not found
        }

        // Return the completed view to render on screen
        return convertView;
    }
}
