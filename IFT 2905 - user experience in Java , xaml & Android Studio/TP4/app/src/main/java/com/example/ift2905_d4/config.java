package com.example.ift2905_d4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class config extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        FloatingActionButton returnB = findViewById(R.id.config_return);
        returnB.setOnClickListener(v -> finish());

        //button2
        Button button = findViewById(R.id.button88);
        button.setOnClickListener(v -> startActivity(new Intent(this,  register3.class)));

    }
}