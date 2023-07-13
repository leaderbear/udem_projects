package com.example.ift2905_d4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainLogin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_login);

        Button signUpButton = findViewById(R.id.signup);
        signUpButton.setOnClickListener(v -> startActivity(new Intent(this,  MainActivity.class)));

        Button logInButton = findViewById(R.id.login);
        logInButton.setOnClickListener(v -> startActivity(new Intent(this,  MainActivityView.class)));
    }
}