package com.example.ift2905_d4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ActivityProgramView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_program_view);

        ImageButton imgButton = findViewById(R.id.img_button_start_activity);
        imgButton.setOnClickListener(i -> startActivity(new Intent(getApplicationContext(), TrainingView.class)));

        FloatingActionButton backButton = findViewById(R.id.program_view_back_button);
        backButton.setOnClickListener(i -> finish());

        //FloatingActionButton profileButton = findViewById(R.id.floatingProfileButtonProgramView);
        //profileButton.setOnClickListener(i -> startActivity(new Intent(getApplicationContext(), Profile.class)));
    }

    @Override
    protected void onResume() {
        super.onResume();

        // According to this https://developer.android.com/guide/components/activities/activity-lifecycle
        // Fixes weird app crash
        BottomNavigationView navigation = findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(item -> {
            // Avoid if already on current page
            if(item.getItemId() == R.id.bottom_nav_button_activity)
                return false;

            startActivity(BottomNav.NavBarHandleClick(item, getApplicationContext()));
            return true;
        });

        // Fixes issue where pressing back doesn't update which button is selected
        navigation.getMenu().findItem(R.id.bottom_nav_button_activity).setChecked(true); // activity page
    }
}