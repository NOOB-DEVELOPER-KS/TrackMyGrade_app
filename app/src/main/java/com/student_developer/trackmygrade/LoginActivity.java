package com.student_developer.trackmygrade;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.activity.EdgeToEdge;

public class LoginActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private Button btnSendOTP;
    private EditText etPhoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this); // Optional for edge-to-edge display
        setContentView(R.layout.activity_login);

        // Initialize views
        progressBar = findViewById(R.id.otp_progress);
        btnSendOTP = findViewById(R.id.btn_send_otp);
        etPhoneNumber = findViewById(R.id.et_login_mobilenumber);

        // Set up button click listener
        btnSendOTP.setOnClickListener(view -> sendOTP());
    }

    // Method to handle OTP sending process
    private void sendOTP() {
        String phoneNumber = etPhoneNumber.getText().toString().trim(); // Get phone number input

        if (validatePhoneNumber(phoneNumber)) {
            // Show progress bar and hide send OTP button
            btnSendOTP.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);


            // Navigate to OTP validation screen
            Intent intent = new Intent(LoginActivity.this, LoginActivity_OTP.class);
            intent.putExtra("phoneNumber", phoneNumber); // Pass phone number to next screen
            startActivity(intent);
            finish(); // Close the current activity
        }
    }

    // Method to validate phone number (10 digits, starts with 6-9)
    private boolean validatePhoneNumber(String phoneNumber) {
        if (phoneNumber.isEmpty()) {
            etPhoneNumber.setError("Phone number is required");
            etPhoneNumber.requestFocus();
            Toast.makeText(this, "Please enter a phone number.", Toast.LENGTH_SHORT).show();
            etPhoneNumber.setBackgroundResource(R.drawable.edit_text_round_corner_red);
            return false;
        } else if (!isValidPhoneNumber(phoneNumber)) {
            etPhoneNumber.setError("Invalid phone number");
            etPhoneNumber.requestFocus();
            Toast.makeText(this, "Please enter a valid phone number.", Toast.LENGTH_SHORT).show();
            etPhoneNumber.setBackgroundResource(R.drawable.edit_text_round_corner_red);
            return false;
        }
        etPhoneNumber.setBackgroundResource(R.drawable.edit_text_round_corner);
        return true;
    }

    // Method to check if the phone number matches the regex pattern
    private boolean isValidPhoneNumber(String phoneNumber) {
        String regex = "^[6-9]\\d{9}$"; // Validates a 10-digit phone number (Indian format)
        return phoneNumber.matches(regex);
    }
}
