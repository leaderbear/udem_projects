package com.example.ift2905_d4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class stats extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        Button buttonYes = findViewById(R.id.stats_yes);
        buttonYes.setOnClickListener(i -> startActivity(new Intent(getApplicationContext(), MainActivityView.class)));

        Button buttonNo = findViewById(R.id.stats_no);
        buttonNo.setOnClickListener(i -> startActivity(new Intent(getApplicationContext(), MainActivityView.class)));
    }
}