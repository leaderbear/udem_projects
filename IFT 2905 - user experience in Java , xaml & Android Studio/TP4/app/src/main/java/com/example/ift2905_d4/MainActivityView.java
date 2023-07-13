package com.example.ift2905_d4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.progressindicator.LinearProgressIndicator;

public class MainActivityView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_view);

        LinearProgressIndicator pBar = findViewById(R.id.general_progress_bar);
        pBar.setMin(0); pBar.setMax(100);
        pBar.setProgress(25);

        ImageButton buttonNewProgram = findViewById(R.id.image_button_start_new_program);
        buttonNewProgram.setOnClickListener(i -> startActivity(new Intent(getApplicationContext(), MainPrograms.class)));

        ImageButton profileButton = findViewById(R.id.main_view_profile_button);
        profileButton.setOnClickListener(i -> startActivity(new Intent(getApplicationContext(), Profile.class)));

        ImageButton resumeButton = findViewById(R.id.img_button_resume_activity);
        resumeButton.setOnClickListener(i -> startActivity(new Intent(getApplicationContext(), ActivityProgramView.class)));

        //img_button_resume_activity
        ImageButton imgButton = findViewById(R.id.img_button_resume_activity);
        imgButton.setOnClickListener(i -> startActivity(new Intent(getApplicationContext(), ActivityProgramView.class)));

        //obs_start
        Button button = findViewById(R.id.obs_start);
        button.setOnClickListener(v -> startActivity(new Intent(this,  ActivityProgramView.class)));

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