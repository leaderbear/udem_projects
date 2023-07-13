package com.example.ift2905_d4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainPrograms extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_programs);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // According to this https://developer.android.com/guide/components/activities/activity-lifecycle
        // Fixes weird app crash

        //imageButton_MainProg
        ImageButton imgButton = findViewById(R.id.imageButton_MainProg);
        imgButton.setOnClickListener(i -> startActivity(new Intent(getApplicationContext(), ActivityProgramView.class)));

        //button9 is Add a new program
        Button start = findViewById(R.id.button9);
        start.setOnClickListener(v -> startActivity(new Intent(this, SelectNewProgram.class)));


        BottomNavigationView navigation = findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(item -> {
            // Avoid if already on current page
            if(item.getItemId() == R.id.bottom_nav_button_programs)
                return false;

            startActivity(BottomNav.NavBarHandleClick(item, getApplicationContext()));
            return true;
        });

        // Fixes issue where pressing back doesn't update which button is selected
        navigation.getMenu().findItem(R.id.bottom_nav_button_programs).setChecked(true); // activity page
    }
}