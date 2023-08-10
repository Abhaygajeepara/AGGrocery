package com.example.aggrocery;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.aggrocery.Common.StringKeyboard;
import com.example.aggrocery.DBHelper.DBHelper;
import com.example.aggrocery.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
private DBHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        dbHelper = new DBHelper(this);

        SharedPreferences sharedPreferences = getSharedPreferences(StringKeyboard.SP_userSession, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        boolean containsKey = sharedPreferences.contains(StringKeyboard.SP_userName);
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);

        if (containsKey) {
            intent =    new Intent(MainActivity.this, HomeActivity.class);
        }
        startActivity(intent);
        finish();
    }
}