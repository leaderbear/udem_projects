package com.example.ift2905_d4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class SelectNewProgram extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_new_program);

    }

    @Override
    protected void onResume() {
        super.onResume();


        //imageButtonNewProgramsPick
        ImageButton imgButton = findViewById(R.id.imageButtonNewProgramsPick);
        imgButton.setOnClickListener(i -> startActivity(new Intent(getApplicationContext(), MainProgDetail.class)));

        FloatingActionButton backButton = findViewById(R.id.pick_prevNew);
        backButton.setOnClickListener(i -> finish());

        //button2
        Button button = findViewById(R.id.button2);
        button.setOnClickListener(v -> startActivity(new Intent(this,  register3.class)));

        // According to this https://developer.android.com/guide/components/activities/activity-lifecycle
        // Fixes weird app crash
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