package com.example.aggrocery;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.aggrocery.Common.CustomAlertDialog;
import com.example.aggrocery.Common.StringKeyboard;
import com.example.aggrocery.DBHelper.DBHelper;

import com.example.aggrocery.databinding.ActivitySignupBinding;


public class SignupActivity extends AppCompatActivity {

    private ActivitySignupBinding binding; // ViewBinding instance

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize ViewBinding
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        Button btnSignup = binding.btnSignup;
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        binding.signUpLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void signup() {
        String username = binding.etUsername.getText().toString();
        String email = binding.etEmail.getText().toString();
        String password = binding.etPassword.getText().toString();

        // Validate the input fields
        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            // Show an alert dialog indicating that fields are empty
            CustomAlertDialog customAlertDialog = new CustomAlertDialog(StringKeyboard.error_Code, StringKeyboard.error_allFieldsRequired_message);
            customAlertDialog.show(getSupportFragmentManager(), "CustomAlertDialog");
        } else {
            // Save the new user details in the User table
            DBHelper dbHelper = new DBHelper(this);
            long result = dbHelper.insertUser(username, email, password);

            if (result != -1) {
                // Signup successful, show a toast message
                Toast.makeText(this, "Signup successful.", Toast.LENGTH_SHORT).show();

                // Optionally, navigate the user to the login screen after successful signup
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
                finish(); // Optional: finish this signup activity so that pressing back won't take the user back to the signup screen
            } else {
                // Signup failed, show an alert dialog
                CustomAlertDialog customAlertDialog = new CustomAlertDialog(StringKeyboard.error_Code, StringKeyboard.error_signUpFailed_message);
                customAlertDialog.show(getSupportFragmentManager(), "CustomAlertDialog");
            }
        }
    }
}
