package com.project.siternak.activities;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class SiternakApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
