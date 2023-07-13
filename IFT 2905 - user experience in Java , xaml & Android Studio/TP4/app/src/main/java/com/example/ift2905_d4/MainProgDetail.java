package com.example.ift2905_d4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainProgDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_prog_detail);
    }



    @Override
    protected void onResume() {

        Button add = findViewById(R.id.main_prog_detail_add);
        add.setOnClickListener(v -> startActivity(new Intent(this, MainPrograms.class)));



        Button start = findViewById(R.id.main_prog_detail_start);
        start.setOnClickListener(v -> startActivity(new Intent(this, ActivityProgramView.class)));

        FloatingActionButton returnB = findViewById(R.id.floatingActionButton15);
        returnB.setOnClickListener(v -> finish());



        super.onResume();

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