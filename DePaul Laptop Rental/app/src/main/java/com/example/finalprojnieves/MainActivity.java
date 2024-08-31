package com.example.finalprojnieves;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    // Initialize UI elements
    private EditText nameEntry, emailEntry, phoneEntry;
    private DatePicker dpStartDate, dpEndDate;
    private Spinner laptopModelSpinner;
    private CheckBox insuranceCheckbox;
    private RadioButton paymentCreditCard, paymentCash;
    private Button btnSubmit, btnReset;
    private TextView tvFormCount;
    private int formCount = 0;
    private ArrayList<String> formData = new ArrayList<>();
    private MediaPlayer mediaPlayer;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // make user interface elements
        nameEntry = findViewById(R.id.etName);
        emailEntry = findViewById(R.id.etEmail);
        phoneEntry = findViewById(R.id.etPhone);
        dpStartDate = findViewById(R.id.dpStartDate);
        dpEndDate = findViewById(R.id.dpEndDate);
        laptopModelSpinner = findViewById(R.id.spLaptopModel);
        insuranceCheckbox = findViewById(R.id.cbInsurance);
        paymentCreditCard = findViewById(R.id.rbCreditCard);
        paymentCash = findViewById(R.id.rbCash);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnReset = findViewById(R.id.btnReset);
        tvFormCount = findViewById(R.id.tvFormCount);
        // build  spinner with laptop model choices via a resource array

        ArrayAdapter<CharSequence> laptopModelAdapter = ArrayAdapter.createFromResource(this,
                R.array.computer_model_array, android.R.layout.simple_spinner_item);
        laptopModelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        laptopModelSpinner.setAdapter(laptopModelAdapter);
        // make MediaPlayer for playing a sound when you click the submit fourm botton
        mediaPlayer = MediaPlayer.create(this, R.raw.tada);
        dbHelper = new DatabaseHelper(this);

        // building  the submit button click listener
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameEntry.getText().toString();
                String email = emailEntry.getText().toString();
                String phone = phoneEntry.getText().toString();
//start date and end dates
                int startYear = dpStartDate.getYear();
                int startMonth = dpStartDate.getMonth();
                int startDay = dpStartDate.getDayOfMonth();
                int endYear = dpEndDate.getYear();
                int endMonth = dpEndDate.getMonth();
                int endDay = dpEndDate.getDayOfMonth();

                Calendar startDate = Calendar.getInstance();
                startDate.set(startYear, startMonth, startDay);
                Calendar endDate = Calendar.getInstance();
                endDate.set(endYear, endMonth, endDay);

                long durationMillis = endDate.getTimeInMillis() - startDate.getTimeInMillis();
                int durationDays = (int) (durationMillis / (24 * 60 * 60 * 1000));

                String laptopModel = laptopModelSpinner.getSelectedItem().toString();
                boolean insurance = insuranceCheckbox.isChecked();
                String paymentMethod = paymentCreditCard.isChecked() ? "Credit Card" : "Cash";

                double rentalCost = calculateRentalCost(durationDays);

                // make a string with all form information

                String formInfo = "Name: " + name + "\n" +
                        "Email: " + email + "\n" +
                        "Phone: " + phone + "\n" +
                        "Start Date: " + startDay + "/" + (startMonth + 1) + "/" + startYear + "\n" +
                        "End Date: " + endDay + "/" + (endMonth + 1) + "/" + endYear + "\n" +
                        "Duration: " + durationDays + " days\n" +
                        "Laptop Model: " + laptopModel + "\n" +
                        "Insurance: " + (insurance ? "Yes" : "No") + "\n" +
                        "Payment Method: " + paymentMethod + "\n" +
                        "Rental Cost: $" + rentalCost;
                // include form data to the list and update form count

                formData.add(formInfo);
                formCount++;

                tvFormCount.setText("Computer Rental Forms Submitted: " + formCount);
                long rentalId = dbHelper.insertRental(name, email, phone, startDate.getTimeInMillis(),
                        endDate.getTimeInMillis(), laptopModel, insurance, paymentMethod, rentalCost);

                mediaPlayer.start();
                // begining the confirmation activity and pass through form data provided from the user
                Intent intent = new Intent(MainActivity.this, ConfirmationActivity.class);
                intent.putExtra("formInfo", formInfo);
                startActivity(intent);
            }
        });
        // establish up the reset fourm button click listener
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // reset form data and reset form count
                formData.clear();
                formCount = 0;
                tvFormCount.setText("Computer Rental Forms Submitted: " + formCount);
                // wipe clear the input fields
                nameEntry.setText("");
                emailEntry.setText("");
                phoneEntry.setText("");
                // start fresh date pickers to the current date
                Calendar calendar = Calendar.getInstance();
                dpStartDate.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));
                dpEndDate.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));
                // start fresh form elements to default values
                laptopModelSpinner.setSelection(0);
                insuranceCheckbox.setChecked(false);
                paymentCreditCard.setChecked(true);
            }
        });
    }


//calculate the rental cost based on how long the user chooses to rent a unit
    private double calculateRentalCost(int durationDays) {
        double costPerDay = 10.0;
        return durationDays * costPerDay;
    }
}