package com.project.siternak.activities.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.project.siternak.R;
import com.project.siternak.activities.auth.LoginActivity;
import com.project.siternak.fragments.DashboardFragment;
import com.project.siternak.fragments.ProfileFragment;
import com.project.siternak.fragments.ScanFragment;
import com.project.siternak.utils.FirebaseHelper;
import com.project.siternak.utils.NetworkManager;
import com.project.siternak.utils.SharedPrefManager;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{
    Fragment selectedFragment;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(this);
        bottomNav.setSelectedItemId(R.id.nav_dashboard);

        if (savedInstanceState == null) {
            displayFragment(new DashboardFragment());
        }

        mAuth = FirebaseAuth.getInstance();
    }


    private void displayFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (!SharedPrefManager.getInstance(this).isLoggedIn()) {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

        FirebaseAuth.AuthStateListener authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = mAuth.getCurrentUser();
                if (user == null) {
                    SharedPrefManager.getInstance(MainActivity.this).logout();
                }
            }
        };

        if(NetworkManager.isNetworkAvailable(MainActivity.this)){
            FirebaseHelper firebaseHelper = new FirebaseHelper(MainActivity.this);
            firebaseHelper.syncData();
        }
        else{
            Toast.makeText(this, "Gagal sync: tidak ada koneksi", Toast.LENGTH_SHORT).show();
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
    }
}
