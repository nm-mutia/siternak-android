package com.project.siternak.activities.auth;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.project.siternak.R;
import com.project.siternak.activities.home.MainActivity;
import com.project.siternak.utils.SharedPrefManager;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class VerifyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_verify);
        getSupportActionBar().hide();

        ButterKnife.bind(this);
    }

    @OnClick(R.id.b_sent_link)
    public void sentVerifyLink(){

    }

    @OnClick(R.id.b_verified)
    public void verified(){
//        if(){
//
//        }
//        else{
//            Intent intent = new Intent(VerifyActivity.this, MainActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            startActivity(intent);
//        }
    }

    @OnClick(R.id.b_logout)
    public void logout(){
        SharedPrefManager.getInstance(this).logout();
        FirebaseAuth.getInstance().signOut();

        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
