package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteDatabaseHelper extends SQLiteOpenHelper {


    private static final String DATABASE_NAME = "BookStore.db";
    private static final int DATABASE_VERSION = 2;

    // Table Name and Columns for Users
    private static final String TABLE_USERS = "users";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_ROLE = "role";

    // Table Name and Columns for Books
    private static final String TABLE_BOOKS = "books";
    private static final String COLUMN_BOOKID = "bookid";
    private static final String COLUMN_BOOKNAME = "book_name";
    private static final String COLUMN_AUTHOR = "book_author";
    private static final String COLUMN_PRICE = "book_price";
    private static final String COLUMN_DESCRIPTION = "book_description";
    private static final String COLUMN_IMAGE = "book_image";
    private static final String COLUMN_STATUS = "status"; // e.g., available or not

    public SQLiteDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create Users Table
        String createUsersTable = "CREATE TABLE " + TABLE_USERS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_USERNAME + " TEXT UNIQUE, " + // Ensure usernames are unique
                COLUMN_PASSWORD + " TEXT, " +
                COLUMN_ROLE + " TEXT)";
        db.execSQL(createUsersTable);

        // Create Books Table
        String createBooksTable = "CREATE TABLE " + TABLE_BOOKS + " (" +
                COLUMN_BOOKID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_BOOKNAME + " TEXT, " +
                COLUMN_AUTHOR + " TEXT, " +
                COLUMN_PRICE + " REAL, " +
                COLUMN_DESCRIPTION + " TEXT, " +
                COLUMN_IMAGE + " TEXT, " + // Path or URI to the book image
                COLUMN_STATUS + " TEXT)"; // available or not
        db.execSQL(createBooksTable);
        Log.d("Database", "Books table created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop the existing tables and create new ones
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOKS);
        onCreate(db);
    }

    // Method to check if the username already exists
    public boolean isUsernameExists(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_ID};
        String selection = COLUMN_USERNAME + " = ?";
        String[] selectionArgs = {username};

        Cursor cursor = db.query(TABLE_USERS, columns, selection, selectionArgs, null, null, null);
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    // Method to register a new user
    public boolean registerUser(String username, String password, String role) {
        if (isUsernameExists(username)) {
            return false; // User already exists, return false
        }

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_PASSWORD, password);
        values.put(COLUMN_ROLE, role);

        // Insert the new user and check for success
        long result = db.insert(TABLE_USERS, null, values);
        return result != -1; // Return true if registration is successful
    }

    // Method to validate user login
    public boolean loginUser(String username, String password, String role) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = COLUMN_USERNAME + " = ? AND " + COLUMN_PASSWORD + " = ? AND " + COLUMN_ROLE + " = ?";
        String[] selectionArgs = { username, password, role };
        Cursor cursor = db.query(TABLE_USERS, null, selection, selectionArgs, null, null, null);

        boolean exists = cursor.getCount() > 0; // Check if the cursor has any results
        cursor.close(); // Close cursor
        return exists; // Return true if the user exists
    }

    // Method to add a new book
    public boolean addBook(String bookName, String author, double price, String description, int imageResource, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_BOOKNAME, bookName);
        values.put(COLUMN_AUTHOR, author);
        values.put(COLUMN_PRICE, price);
        values.put(COLUMN_DESCRIPTION, description);
        values.put(COLUMN_IMAGE, imageResource);
        values.put(COLUMN_STATUS, status);

        long result = db.insert(TABLE_BOOKS, null, values);
        Log.d("AddBook", "Inserting book: " + bookName + " result: " + result);
        return result != -1; // Return true if addition is successful
    }

    // Method to update a book
    public boolean updateBook(int bookId, String bookName, String author, double price, String description, String image, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_BOOKNAME, bookName);
        values.put(COLUMN_AUTHOR, author);
        values.put(COLUMN_PRICE, price);
        values.put(COLUMN_DESCRIPTION, description);
        values.put(COLUMN_IMAGE, image);
        values.put(COLUMN_STATUS, status);

        String selection = COLUMN_BOOKID + " = ?";
        String[] selectionArgs = { String.valueOf(bookId) };

        int count = db.update(TABLE_BOOKS, values, selection, selectionArgs);
        return count > 0; // Return true if update is successful
    }

    // Method to delete a book
    public boolean deleteBook(String bookName) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("books", "book_name = ?", new String[]{bookName}) > 0; // Adjust table name and column name as necessary
    }

    // Method to get all books
    public Cursor getAllBooks() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_BOOKS, null);
    }

    // Method to get a specific book by ID
    public Cursor getBookById(int bookId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query("books", null, "bookid=?", new String[]{String.valueOf(bookId)}, null, null, null);
    }
    public Cursor getAvailableBooks() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query("books",   // The table name
                null,              // Columns (null for all)
                "status = ?",      // Where clause
                new String[]{"available"}, // Where arguments
                null,              // Group by
                null,              // Having
                null);             // Order by
    }
    public static String getColumnImage() {
        return COLUMN_IMAGE;
    }

    public static String getTableBooks() {
        return TABLE_BOOKS;
    }
    public static String getColumnBookName() {
        return COLUMN_BOOKNAME;
    }
    public Cursor getUserByUsernameAndPassword(String username, String password, String role) {
        SQLiteDatabase db = this.getReadableDatabase();

        // Query to select the user based on username, password, and role
        String selection = COLUMN_USERNAME + " = ? AND " + COLUMN_PASSWORD + " = ? AND " + COLUMN_ROLE + " = ?";
        String[] selectionArgs = {username, password, role};

        // Execute the query
        return db.query(TABLE_USERS, null, selection, selectionArgs, null, null, null);
    }
}

