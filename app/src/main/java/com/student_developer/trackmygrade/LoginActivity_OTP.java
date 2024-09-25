package com.student_developer.trackmygrade;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity_OTP extends AppCompatActivity {

    ProgressBar progressBar;
    Button btn_validateOTP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login_otp);

        progressBar = findViewById(R.id.validate_progress);
        btn_validateOTP = findViewById(R.id.btn_validate_otp);

        btn_validateOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                btn_validateOTP.setVisibility(View.INVISIBLE);

                // Assuming OTP validation is successful here
                Intent intent = new Intent(LoginActivity_OTP.this, MainActivity.class);
                startActivity(intent);
                finish(); // Close the OTP validation activity
            }
        });
    }
}
