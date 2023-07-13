package com.example.ift2905_d4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Timer;
import java.util.TimerTask;

public class TrainingView extends AppCompatActivity {

    private Timer timer = new Timer();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training_view);

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Intent timerIntent = new Intent(TrainingView.this, TrainEnd.class);
                startActivity(timerIntent);
            }
        }, 6000);


        //ImageButton imgButton = findViewById(R.id.img_button_pause_activity);
        //imgButton.setOnClickListener(i -> startActivity(new Intent(getApplicationContext(), TrainEnd.class)));
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

    @Override
    protected void onPause(){
        super.onPause();
        timer.cancel();
    }
}