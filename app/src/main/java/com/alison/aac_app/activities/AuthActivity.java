package com.alison.aac_app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.alison.aac_app.R;

public class AuthActivity extends AppCompatActivity {

    Button loginBtn, signupBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        loginBtn = findViewById(R.id.loginBtn);
        signupBtn = findViewById(R.id.signupBtn);

        loginBtn.setOnClickListener(view -> startActivity(new Intent(AuthActivity.this, MainActivity.class)));

        signupBtn.setOnClickListener(view -> startActivity(new Intent(AuthActivity.this, MainActivity.class)));

    }
}