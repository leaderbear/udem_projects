package com.example.ift2905_d4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class register_equip extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_equip);

        Button buttonNext = findViewById(R.id.register_equip_next);
        buttonNext.setOnClickListener(v -> startActivity(new Intent(this,  register5.class)));

        Button buttonSkip = findViewById(R.id.register_equip_skip);
        buttonSkip.setOnClickListener(v -> startActivity(new Intent(this,  register5.class)));

        FloatingActionButton returnB = findViewById(R.id.equip_prev);
        returnB.setOnClickListener(v -> finish());
    }
}