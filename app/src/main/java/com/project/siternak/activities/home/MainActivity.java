package com.project.siternak.activities.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.project.siternak.R;
import com.project.siternak.activities.auth.LoginActivity;
import com.project.siternak.fragments.DashboardFragment;
import com.project.siternak.fragments.ProfileFragment;
import com.project.siternak.fragments.ScanFragment;
import com.project.siternak.utils.SharedPrefManager;


public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{
    Fragment selectedFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(this);
        bottomNav.setSelectedItemId(R.id.nav_dashboard);

//        if (savedInstanceState == null) {
            displayFragment(new DashboardFragment());
//        }
    }


    private void displayFragment(Fragment fragment){
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (!SharedPrefManager.getInstance(this).isLoggedIn()) {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        selectedFragment = null;

        switch (item.getItemId()) {
            case R.id.nav_dashboard:
                selectedFragment = new DashboardFragment();
                break;
            case R.id.nav_profile:
                selectedFragment = new ProfileFragment();
                break;
            case R.id.nav_scan:
                selectedFragment = new ScanFragment();
                break;
        }

        if(selectedFragment != null) {
            displayFragment(selectedFragment);
        }

        return true;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
//        getSupportFragmentManager()
//        .beginTransaction()
//        .detach(selectedFragment)
//        .attach(selectedFragment)
//        .commitAllowingStateLoss();
    }
}
