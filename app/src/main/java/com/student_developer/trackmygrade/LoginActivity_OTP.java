package com.student_developer.trackmygrade;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class LoginActivity_OTP extends AppCompatActivity {

    FirebaseAuth mAuth;  // Firebase Auth instance
    ProgressBar progressBar;
    Button btn_validateOTP, btn_editPhoneNumber, btn_resendOTP; // Add resend button
    EditText et_otp;
    TextView tv_otpStatus;
    String phoneNumber;
    String verificationCode;  // Fixed typo
    PhoneAuthProvider.ForceResendingToken resendingToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_otp);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Get phone number passed from previous activity
        phoneNumber = getIntent().getStringExtra("phoneNumber");
        phoneNumber = "+91" + phoneNumber;

        // Initialize views
        progressBar = findViewById(R.id.validate_progress);
        btn_validateOTP = findViewById(R.id.btn_validate_otp);
        btn_editPhoneNumber = findViewById(R.id.btn_edit_phone);
        btn_resendOTP = findViewById(R.id.btn_resend_otp); // Resend button
        et_otp = findViewById(R.id.et_login_otp);
        tv_otpStatus = findViewById(R.id.tv_login_otp);

        String message = "OTP sent to: " + phoneNumber;
        tv_otpStatus.setText(message);

        // Send OTP for the first time
        sendOTP(phoneNumber, false);

        // Handle OTP validation
        btn_validateOTP.setOnClickListener(view -> {
            String otp = et_otp.getText().toString().trim();
            if (otp.isEmpty()) {
                et_otp.setError("Please enter the OTP");
                et_otp.requestFocus();
                return;
            }
            if (otp.length() != 6) {
                et_otp.setError("OTP must be 6 digits");
                et_otp.requestFocus();
                return;
            }

            // Validate OTP with the verification code
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCode, otp);
            signIn(credential);  // Call sign-in method
        });

        // Handle phone number editing
        btn_editPhoneNumber.setOnClickListener(view -> {
            Intent i = new Intent(LoginActivity_OTP.this, LoginActivity.class);
            startActivity(i);
            finish();
        });

        // Handle OTP resend
        btn_resendOTP.setOnClickListener(view -> {
            sendOTP(phoneNumber, true);  // Call resend logic
        });
    }

    // Updated sendOTP method to handle both sending and resending OTP
    private void sendOTP(String phoneNumber, boolean isResend) {
        setInProgress(true);

        PhoneAuthOptions.Builder builder = PhoneAuthOptions.newBuilder(mAuth)
                .setPhoneNumber(phoneNumber)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                        signIn(phoneAuthCredential);  // Auto verification
                        setInProgress(false);
                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {
                        Toast.makeText(LoginActivity_OTP.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        setInProgress(false);
                    }

                    @Override
                    public void onCodeSent(String verifyID, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        verificationCode = verifyID;
                        resendingToken = forceResendingToken; // Save for future resend
                        setInProgress(false);

                        // Show resend button after first code is sent
                        btn_resendOTP.setVisibility(View.VISIBLE);
                    }
                });

        // If resending OTP, pass the existing resending token
        if (isResend) {
            builder.setForceResendingToken(resendingToken);
        }

        PhoneAuthOptions options = builder.build();
        PhoneAuthProvider.verifyPhoneNumber(options);  // Initiate the OTP process
    }

    // Method to sign in user after OTP verification
    private void signIn(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Intent intent = new Intent(LoginActivity_OTP.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(LoginActivity_OTP.this, "OTP Verification Failed", Toast.LENGTH_SHORT).show();
                setInProgress(false);
            }
        });
    }

    // Method to control progress bar visibility
    private void setInProgress(boolean inProgress) {
        if (inProgress) {
            progressBar.setVisibility(View.VISIBLE);
            btn_validateOTP.setVisibility(View.INVISIBLE);
            btn_resendOTP.setVisibility(View.INVISIBLE); // Hide resend button while in progress
        } else {
            progressBar.setVisibility(View.INVISIBLE);
            btn_validateOTP.setVisibility(View.VISIBLE);
        }
    }
}
