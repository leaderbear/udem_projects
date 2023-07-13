package com.example.ift2905_d4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ProgramPick extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_program_pick);

        FloatingActionButton returnB = findViewById(R.id.pick_prev);
        returnB.setOnClickListener(v -> finish());

        Button skipButton = findViewById(R.id.programPickSkipButton);
        skipButton.setOnClickListener(v -> startActivity(new Intent(this,  MainActivityView.class)));

        ImageButton imgButton = findViewById(R.id.imageButtonProgramsPick);
        imgButton.setOnClickListener(i -> startActivity(new Intent(this,  ProgramDetail.class)));
    }
}