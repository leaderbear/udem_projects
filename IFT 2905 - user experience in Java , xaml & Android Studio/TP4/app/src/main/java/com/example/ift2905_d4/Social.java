package com.example.ift2905_d4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Social extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social);
    }

    @Override
    protected void onResume() {
        super.onResume();

        //floatingActionButton4
        FloatingActionButton returnB = findViewById(R.id.floatingActionButton4);
        returnB.setOnClickListener(v -> startActivity(new Intent(this,  Profile.class)));

        // According to this https://developer.android.com/guide/components/activities/activity-lifecycle
        // Fixes weird app crash
        BottomNavigationView navigation = findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(item -> {
            // Avoid if already on current page
            if(item.getItemId() == R.id.bottom_nav_button_social)
                return false;

            startActivity(BottomNav.NavBarHandleClick(item, getApplicationContext()));
            return true;
        });

        // Fixes issue where pressing back doesn't update which button is selected
        navigation.getMenu().findItem(R.id.bottom_nav_button_social).setChecked(true); // activity page
    }
}