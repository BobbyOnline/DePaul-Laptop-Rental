package com.example.finalprojnieves;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
//Final Project Roberto Nieves
public class DatabaseHelper extends SQLiteOpenHelper {
    // constraints for database name, version, table name, column names
    private static final String DATABASE_NAME = "rental_db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "rentals";
    private static final String COL_ID = "id";
    private static final String COL_NAME = "name";
    private static final String COL_EMAIL = "email";
    private static final String COL_PHONE = "phone";
    private static final String COL_START_DATE = "start_date";
    private static final String COL_END_DATE = "end_date";
    private static final String COL_LAPTOP_MODEL = "laptop_model";
    private static final String COL_INSURANCE = "insurance";
    private static final String COL_PAYMENT_METHOD = "payment_method";
    private static final String COL_COST = "cost";

    // contsturcor to intialize database helper
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // oncrete method is called when the database is created for the first time ever.
    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL query to create the rental table
        String createTableQuery = "CREATE TABLE " + TABLE_NAME + "("
                + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COL_NAME + " TEXT,"
                + COL_EMAIL + " TEXT,"
                + COL_PHONE + " TEXT,"
                + COL_START_DATE + " INTEGER,"
                + COL_END_DATE + " INTEGER,"
                + COL_LAPTOP_MODEL + " TEXT,"
                + COL_INSURANCE + " INTEGER,"
                + COL_PAYMENT_METHOD + " TEXT,"
                + COL_COST + " REAL"
                + ")";
        // execute  query to build the table
        db.execSQL(createTableQuery);
    }

    // onUpgrade method is called when the database needs to be upgraded
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop the existing table if it exists
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        // Recreate the table
        onCreate(db);
    }

    // method to insert a rental record into the established database
    public long insertRental(String name, String email, String phone, long startDate, long endDate,
                             String laptopModel, boolean insurance, String paymentMethod, double cost) {
        // pull a writable instance of the database that we can fill in with data
        SQLiteDatabase db = this.getWritableDatabase();
        // make a contentvalues value obj to hold our rental data
        ContentValues values = new ContentValues();
        values.put(COL_NAME, name);
        values.put(COL_EMAIL, email);
        values.put(COL_PHONE, phone);
        values.put(COL_START_DATE, startDate);
        values.put(COL_END_DATE, endDate);
        values.put(COL_LAPTOP_MODEL, laptopModel);
        values.put(COL_INSURANCE, insurance ? 1 : 0); // Store insurance as 1 (true) or 0 (false)
        values.put(COL_PAYMENT_METHOD, paymentMethod);
        values.put(COL_COST, cost);
        // push the rental record into the database and get the inserted row identification
        long id = db.insert(TABLE_NAME, null, values);
        // closing the database
        db.close();
        // give us the inserted row id
        return id;
    }

    // method to pull  all rental information thats gained from the database
    public ArrayList<String> getAllRentals() {
        // make an ArrayList that has the ablity store the rental data
        ArrayList<String> rentalList = new ArrayList<>();
        // pull a human readable record of the database
        SQLiteDatabase db = this.getReadableDatabase();
        // execute query to pull all rental records via the table
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        // Check if the query returned any results
        if (cursor.moveToFirst()) {
            do {
                // pull rental information via the cursor
                String rentalInfo = "Name: " + cursor.getString(cursor.getColumnIndexOrThrow(COL_NAME)) + "\n"
                        + "Email: " + cursor.getString(cursor.getColumnIndexOrThrow(COL_EMAIL)) + "\n"
                        + "Phone: " + cursor.getString(cursor.getColumnIndexOrThrow(COL_PHONE)) + "\n"
                        + "Start Date: " + cursor.getLong(cursor.getColumnIndexOrThrow(COL_START_DATE)) + "\n"
                        + "End Date: " + cursor.getLong(cursor.getColumnIndexOrThrow(COL_END_DATE)) + "\n"
                        + "Laptop Model: " + cursor.getString(cursor.getColumnIndexOrThrow(COL_LAPTOP_MODEL)) + "\n"
                        + "Insurance: " + (cursor.getInt(cursor.getColumnIndexOrThrow(COL_INSURANCE)) == 1 ? "Yes" : "No") + "\n"
                        + "Payment Method: " + cursor.getString(cursor.getColumnIndexOrThrow(COL_PAYMENT_METHOD)) + "\n"
                        + "Cost: $" + cursor.getDouble(cursor.getColumnIndexOrThrow(COL_COST));
                // add the rental information to the list
                rentalList.add(rentalInfo);
            } while (cursor.moveToNext()); // Move to the next record
        }
        // end the cursor and the close the database
        cursor.close();
        db.close();
        // provide the list of rental information
        return rentalList;
    }
}
