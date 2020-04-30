package com.project.siternak.activities.data;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.project.siternak.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class DataActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);

        getSupportActionBar().hide();
        ButterKnife.bind(this);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @OnClick(R.id.tv_data_kematian)
    public void moveToKematian(){
        Intent intent=new Intent(DataActivity.this, DataKematianActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.tv_data_pemilik)
    public void moveToPemilik(){
//        Intent intent=new Intent(DataActivity.this, DataPemilikActivity.class);
//        startActivity(intent);
    }

    @OnClick(R.id.tv_data_penyakit)
    public void moveToPenyakit(){
//        Intent intent=new Intent(DataActivity.this, DataPenyakitActivity.class);
//        startActivity(intent);
    }

    @OnClick(R.id.tv_data_perkawinan)
    public void moveToPerkawinan(){
//        Intent intent=new Intent(DataActivity.this, DataPerkawinanActivity.class);
//        startActivity(intent);
    }

    @OnClick(R.id.tv_data_ras)
    public void moveToRas(){
//        Intent intent=new Intent(DataActivity.this, DataRasActivity.class);
//        startActivity(intent);
    }

    @OnClick(R.id.tv_data_riwayat)
    public void moveToRiwayat(){
//        Intent intent=new Intent(DataActivity.this, DataRiwayatPenyakitActivity.class);
//        startActivity(intent);
    }

    @OnClick(R.id.tv_data_ternak)
    public void moveToTernak(){
//        Intent intent=new Intent(DataActivity.this, TernakActivity.class);
//        startActivity(intent);
    }

    @OnClick(R.id.tv_data_peternakan)
    public void moveToPeternakan(){
//        Intent intent=new Intent(DataActivity.this, PeternakanActivity.class);
//        startActivity(intent);
    }
}
