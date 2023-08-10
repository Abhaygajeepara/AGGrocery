package com.example.aggrocery;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.aggrocery.Common.CustomAlertDialog;
import com.example.aggrocery.Common.StringKeyboard;
import com.example.aggrocery.DBHelper.DBHelper;
import com.example.aggrocery.Models.Usermodel;
import com.example.aggrocery.databinding.ActivityLoginBinding;
import com.google.gson.Gson;

public class LoginActivity extends AppCompatActivity {

    private EditText etUsername;
    private EditText etPassword;

    private ActivityLoginBinding binding;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        etUsername = binding.loginEdtUsername;;
        etPassword =  binding.loginEdtPassword;


        Button btnLogin =  binding.loginBtnLogin;
        TextView tvSignUp =  binding.loginTvSignUp;

        // Initialize the DBHelper instance
        dbHelper = new DBHelper(this);

        // Set the test user credentials
        String testUsername = "test";
        String testEmail = "test@gmail.com";
        String testPassword = "test";

        // Save the test user details in shared preferences (for simplicity, you can use a database for a real app)
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", Context.MODE_PRIVATE);


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void login() {
        String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();


        if (username.isEmpty() || password.isEmpty()) {
            // Show an alert dialog indicating that fields are empty
            CustomAlertDialog customAlertDialog = new CustomAlertDialog(StringKeyboard.error_Code, StringKeyboard.error_allFieldsRequired_message);
            customAlertDialog.show(getSupportFragmentManager(), "CustomAlertDialog");
        } else {
            // Retrieve test user details from shared preferences
//

            // Verify user credentials in the database
            try {

                DBHelper userDatabaseHelper = new DBHelper(this);
               Usermodel user = userDatabaseHelper.verifyCredentials(username,password);
               if(user != null){
                   Gson gson = new Gson();
                   String userJson = gson.toJson(user);
                   SharedPreferences sharedPreferences = getSharedPreferences(StringKeyboard.SP_userSession, Context.MODE_PRIVATE);
                   SharedPreferences.Editor editor = sharedPreferences.edit();
                   editor.putString(StringKeyboard.SP_userName, userJson);


                   Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                   startActivity(intent);
                   finish();
                   editor.apply();

               }else{
                   CustomAlertDialog customAlertDialog = new CustomAlertDialog(StringKeyboard.error_Code, StringKeyboard.error_invalidCredential_message);
                   customAlertDialog.show(getSupportFragmentManager(), "CustomAlertDialog");
               }
            } catch (Exception e) {
                Log.d("LoginActivity", "Login successful for username: " + e.toString());

            }
        }
    }
}
