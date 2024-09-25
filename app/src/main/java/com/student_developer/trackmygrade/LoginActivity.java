package com.student_developer.trackmygrade;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.activity.EdgeToEdge;

public class LoginActivity extends AppCompatActivity {

    ProgressBar progressBar;
    Button btn_sendOTP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        progressBar = findViewById(R.id.otp_progress);
        btn_sendOTP = findViewById(R.id.btn_send_otp);

        btn_sendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                btn_sendOTP.setVisibility(View.INVISIBLE);

                // Corrected the context for Intent
                Intent intent = new Intent(LoginActivity.this, LoginActivity_OTP.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
