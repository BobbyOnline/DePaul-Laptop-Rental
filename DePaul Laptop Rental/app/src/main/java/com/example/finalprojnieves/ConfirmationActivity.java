package com.example.finalprojnieves;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
//Final Project Roberto Nieves
public class ConfirmationActivity extends AppCompatActivity {
    // creating TextViews for confirmation, insurance, and payment details.
    private TextView tvConfirmation, tvInsurance, tvPaymentMethod;
    //  button handler for view data
    private Button btnViewData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setting the layout for this activity view
        setContentView(R.layout.activity_confirmation);

        // intialize TextView var by pulling them from the layout resource
        tvConfirmation = findViewById(R.id.tvConfirmation);
        tvInsurance = findViewById(R.id.tvInsurance);
        tvPaymentMethod = findViewById(R.id.tvPaymentMethod);
        // intialize Button var by pulling  from the layout resource aswell
        btnViewData = findViewById(R.id.btnViewData);

        // Get the Intent that started this activity
        Intent intent = getIntent();
        // Retrieve the string extra "formInfo" from the Intent
        String formInfo = intent.getStringExtra("formInfo");
        // Set the text of tvConfirmation to the formInfo string
        tvConfirmation.setText(formInfo);

        // splitting the formInfo string into lines based on newline character to make sure that the formatting is clean and proper
        String[] formData = formInfo.split("\n");
        // create variables to keep track of ensurance and payment methods (cash credit ect)
        String insurance = "";
        String paymentMethod = "";
        // looping through each line of formData
        for (String data : formData) {
            // ensure if the line starts with "insurnace:" and extract the correct information
            if (data.startsWith("Insurance:")) {
                insurance = data.substring(data.indexOf(":") + 2);
                // double check if the line begins with "paymentmethod:" and extract the respective data
            } else if (data.startsWith("Payment Method:")) {
                paymentMethod = data.substring(data.indexOf(":") + 2);
            }
        }

        // place the text of tv insurnace and tv payment method with the extracted information
        tvInsurance.setText("Insurance: " + insurance);
        tvPaymentMethod.setText("Payment Method: " + paymentMethod);

        // set an onclick listener on the our view data button
        btnViewData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // make a new intent that begins the data displayactivity
                Intent intent = new Intent(ConfirmationActivity.this, DataDisplayActivity.class);
                // begin the datadisplayactvity
                startActivity(intent);
            }
        });
    }
}
