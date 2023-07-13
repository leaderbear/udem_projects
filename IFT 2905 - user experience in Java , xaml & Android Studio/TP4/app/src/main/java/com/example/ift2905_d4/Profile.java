package com.example.ift2905_d4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Profile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
    }

    @Override
    protected void onResume() {
        super.onResume();

        FloatingActionButton notif = findViewById(R.id.addfriends);
        notif.setOnClickListener(v -> startActivity(new Intent(this,  Friendsadd.class)));

        FloatingActionButton config = findViewById(R.id.config);
        config.setOnClickListener(v -> startActivity(new Intent(this,  config.class)));

        FloatingActionButton addfriends = findViewById(R.id.notif);
        addfriends.setOnClickListener(v -> startActivity(new Intent(this,  FriendsFind.class)));


        // According to this https://developer.android.com/guide/components/activities/activity-lifecycle
        // Fixes weird app crash
        BottomNavigationView navigation = findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(item -> {
            // Avoid if already on current page
            if(item.getItemId() == R.id.bottom_nav_button_profile)
                return false;

            startActivity(BottomNav.NavBarHandleClick(item, getApplicationContext()));
            return true;
        });

        // Fixes issue where pressing back doesn't update which button is selected
        navigation.getMenu().findItem(R.id.bottom_nav_button_profile).setChecked(true); // activity page
    }
}