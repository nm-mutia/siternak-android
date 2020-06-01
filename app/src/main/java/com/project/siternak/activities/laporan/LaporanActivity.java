package com.project.siternak.activities.laporan;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.project.siternak.R;

public class LaporanActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_laporan);
        getSupportActionBar().hide();

//        ButterKnife.bind(this);
    }
}
