package com.example.ift2905_d4;

import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;

import androidx.annotation.NonNull;

public class BottomNav  {

    public static Intent NavBarHandleClick(@NonNull MenuItem item, Context c){

        switch (item.getItemId()) {
            case R.id.bottom_nav_button_social:
                return new Intent(c,  Social.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            case R.id.bottom_nav_button_programs:
                return new Intent(c,  MainPrograms.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            case R.id.bottom_nav_button_activity:
                return new Intent(c,  MainActivityView.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            case R.id.bottom_nav_button_progress:
                return new Intent(c,  progress.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            case R.id.bottom_nav_button_profile:
                return new Intent(c,  Profile.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            default: return new Intent();
        }
    }
}
