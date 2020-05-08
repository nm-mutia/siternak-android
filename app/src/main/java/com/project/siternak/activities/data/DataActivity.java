package com.project.siternak.activities.data;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.project.siternak.R;
import com.project.siternak.activities.auth.LoginActivity;
import com.project.siternak.models.auth.UserModel;
import com.project.siternak.utils.SharedPrefManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DataActivity extends AppCompatActivity {
    @BindView(R.id.tv_data_peternakan) TextView tvDataPeternakan;

    private UserModel mUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);

        getSupportActionBar().hide();
        ButterKnife.bind(this);

        mUser = SharedPrefManager.getInstance(this).getUser();

        if(mUser.getRole() != null && mUser.getRole().equals("peternak")){
            tvDataPeternakan.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @OnClick(R.id.tv_data_kematian)
    public void moveToKematian(){
        Intent intent = new Intent(DataActivity.this, KematianActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.tv_data_pemilik)
    public void moveToPemilik(){
        Intent intent = new Intent(DataActivity.this, PemilikActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.tv_data_penyakit)
    public void moveToPenyakit(){
        Intent intent = new Intent(DataActivity.this, PenyakitActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.tv_data_perkawinan)
    public void moveToPerkawinan(){
        Intent intent = new Intent(DataActivity.this, PerkawinanActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.tv_data_ras)
    public void moveToRas(){
        Intent intent = new Intent(DataActivity.this, RasActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.tv_data_riwayat)
    public void moveToRiwayat(){
        Intent intent = new Intent(DataActivity.this, RiwayatPenyakitActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.tv_data_ternak)
    public void moveToTernak(){
        Intent intent = new Intent(DataActivity.this, TernakActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.tv_data_peternakan)
    public void moveToPeternakan(){
        Intent intent = new Intent(DataActivity.this, PeternakanActivity.class);
        startActivity(intent);
    }
}
