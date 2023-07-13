package com.example.ift2905_d4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register1);

        Button button = findViewById(R.id.create);
        button.setOnClickListener(v -> startActivity(new Intent(this,  register2.class)));

        FloatingActionButton returnB = findViewById(R.id.register1_return);
        returnB.setOnClickListener(v -> finish());
    }
}