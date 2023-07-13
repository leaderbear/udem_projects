package com.example.ift2905_d4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class register3 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register3);

        Button buttonNext = findViewById(R.id.register3_next);
        buttonNext.setOnClickListener(v -> startActivity(new Intent(this,  register_equip.class)));

        Button buttonSkip = findViewById(R.id.register3_skip);
        buttonSkip.setOnClickListener(v -> startActivity(new Intent(this,  register_equip.class)));
    }
}