package com.example.ift2905_d4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ProgramDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_program_detail);

        FloatingActionButton backButton = findViewById(R.id.floatingActionButtonBack);
        backButton.setOnClickListener(i -> finish());

        Button selectButton = findViewById(R.id.programDetailSelectProgram);
        selectButton.setOnClickListener(i -> startActivity(new Intent(getApplicationContext(), MainActivityView.class)));
    }
}