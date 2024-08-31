package com.example.finalprojnieves;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.ArrayList;
//Final Project Roberto Nieves
public class DataDisplayActivity extends AppCompatActivity {
    // create a DatabaseHelper instance
    private ListView lvRentals;
    //create a reference to a DatabaseHelper object for database access
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // load the activity's layout
        setContentView(R.layout.activity_data_display);

        // pull a reference to the ListView from the activity's layout
        lvRentals = findViewById(R.id.lvRentals);
        // make a new DatabaseHelper instance to handle database interactions.
        dbHelper = new DatabaseHelper(this);

        // get all rental information as a list of strings.
        ArrayList<String> rentalList = dbHelper.getAllRentals();
        // set up an ArrayAdapter to display the rental list in the ListView.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, rentalList);
        // setting the adapter for the list view that will show us the rental records
        lvRentals.setAdapter(adapter);
    }
}
