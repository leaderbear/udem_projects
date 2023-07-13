package com.example.ift2905_h21_devoir3;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private Menu menu;
    private NavController navController;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navController = Navigation.findNavController(findViewById(R.id.nav_host_fragment));
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigateUp();
            }
        });

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.search_link:
                    navController.navigate(R.id.researchFragment);
                    break;
                case R.id.news_link:
                    navController.navigate(R.id.newsListFragment);
                    break;
                case R.id.social_link:
                    navController.navigate(R.id.socialFragment);
                    break;
                default:
                    return false;
            }
            menu.setGroupVisible(R.id.toolbar_items_to_hide, true);
            return true;
        });

    }

    public NavController getNavController() {
        return this.navController;
    }

    public Toolbar getToolbar() {
        return this.toolbar;
    }

    public BottomNavigationView getBottomNav() {
        return bottomNavigationView;
    }

    public void showBack(boolean show) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(show);
        getSupportActionBar().setDisplayShowHomeEnabled(show);
    }

    public void showToolbarMenu(boolean show) {
        if (menu != null) {
            menu.setGroupVisible(R.id.toolbar_items_to_hide, show);
        }
    }

    public void showBottomNav(boolean show) {
        if (show) {
            bottomNavigationView.setVisibility(View.VISIBLE);
        } else {
            bottomNavigationView.setVisibility(View.GONE);
        }
    }

    public void setBottomNavChecked(int index) {
        bottomNavigationView.getMenu().getItem(index).setChecked(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        this.menu = menu;
        getMenuInflater().inflate(R.menu.menu_main, menu);
        showToolbarMenu(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.profile_link) {
            navController.navigate(R.id.profileFragment);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}