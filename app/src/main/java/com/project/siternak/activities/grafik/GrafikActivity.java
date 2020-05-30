package com.project.siternak.activities.grafik;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.project.siternak.R;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GrafikActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_grafik);
        getSupportActionBar().hide();

        ButterKnife.bind(this);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @OnClick(R.id.tv_umur)
    public void grafikUmur(){
        Intent intent = new Intent(GrafikActivity.this, GrafikFragmentActivity.class);
        intent.putExtra("grafik", "umur");
        startActivity(intent);
    }

    @OnClick(R.id.tv_ras)
    public void grafikRas(){
        Intent intent = new Intent(GrafikActivity.this, GrafikFragmentActivity.class);
        intent.putExtra("grafik", "ras");
        startActivity(intent);
    }

    @OnClick(R.id.tv_kelahiran)
    public void grafikLahir(){
        Intent intent = new Intent(GrafikActivity.this, GrafikFragmentActivity.class);
        intent.putExtra("grafik", "lahir");
        startActivity(intent);
    }

    @OnClick(R.id.tv_kematian)
    public void grafikMati(){
        Intent intent = new Intent(GrafikActivity.this, GrafikFragmentActivity.class);
        intent.putExtra("grafik", "mati");
        startActivity(intent);
    }
}
