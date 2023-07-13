package com.example.ift2905_d4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

public class TrainEnd extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train_end);

        ImageButton likeButton = findViewById(R.id.like);
        likeButton.setOnClickListener(i -> startActivity(new Intent(getApplicationContext(), stats.class)));

        Button skipButton = findViewById(R.id.train_end_skip);
        skipButton.setOnClickListener(i -> startActivity(new Intent(getApplicationContext(), stats.class)));
    }
}