package com.example.ift2905_d4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class register5 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register5);

        Button buttonNext = findViewById(R.id.register5_next);
        buttonNext.setOnClickListener( v -> startActivity(new Intent(this,  ProgramPick.class)));

        Button buttonSkip = findViewById(R.id.register5_skip);
        buttonSkip.setOnClickListener( v -> startActivity(new Intent(this,  ProgramPick.class)));

        FloatingActionButton returnB = findViewById(R.id.register5_return);
        returnB.setOnClickListener(v -> finish());
    }
}