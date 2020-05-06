package com.project.siternak.activities.data;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.project.siternak.R;
import com.project.siternak.models.data.PemilikModel;
import com.project.siternak.responses.DataDeleteResponse;
import com.project.siternak.responses.PemilikResponse;
import com.project.siternak.rest.RetrofitClient;
import com.project.siternak.utils.SharedPrefManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PemilikDetailActivity extends AppCompatActivity {
    @BindView(R.id.tv_id) TextView tvId;
    @BindView(R.id.tv_ktp) TextView tvKtp;
    @BindView(R.id.tv_nama_pemilik) TextView tvNamaPemilik;
    @BindView(R.id.tv_created_at) TextView tvCreatedAt;
    @BindView(R.id.tv_updated_at) TextView tvUpdatedAt;
    @BindView(R.id.tl_detail_pemilik) TableLayout tlDetailPemilik;

    private PemilikModel pemilikData;
    private String userToken;
    private int backFinish;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_pemilik);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.actionbar_primary_arrow);

        TextView tv_actionbar_title = getSupportActionBar().getCustomView().findViewById(R.id.tv_actionbar_title);

        ButterKnife.bind(this);

        pemilikData = (PemilikModel) getIntent().getSerializableExtra("pemilik");
        tv_actionbar_title.setText("Pemilik - " + String.valueOf(pemilikData.getId()));

        backFinish = (int) getIntent().getIntExtra("finish", 0); //data from after edit pemilik

        userToken = SharedPrefManager.getInstance(this).getAccessToken();
        setDataPemilik();
    }

    @Override
    public void onBackPressed() {
        if(backFinish == 1){
            Intent intent = new Intent(PemilikDetailActivity.this, PemilikActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        finish();
    }

    @OnClick(R.id.ib_actionbar_close)
    public void close(){
        finish();
    }

    @OnClick(R.id.ib_delete_data)
    public void deleteData(){
        SweetAlertDialog wDialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
        wDialog.setTitleText("Apakah anda yakin untuk menghapus data ini?");
        wDialog.setContentText("Data kematian id " + pemilikData.getId());
        wDialog.setConfirmText("Ya");
        wDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sDialog) {
                sDialog.dismissWithAnimation();

                Call<DataDeleteResponse> calls = RetrofitClient
                        .getInstance()
                        .getApi()
                        .delPemilik("Bearer " + userToken, pemilikData.getId());

                calls.enqueue(new Callback<DataDeleteResponse>() {
                    @Override
                    public void onResponse(Call<DataDeleteResponse> call, Response<DataDeleteResponse> response) {
                        DataDeleteResponse resp = response.body();

                        if(response.isSuccessful()){
                            Toast.makeText(PemilikDetailActivity.this, resp.getMessage(), Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(PemilikDetailActivity.this, PemilikActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);

                            PemilikDetailActivity.this.finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<DataDeleteResponse> call, Throwable t) {
                        Toast.makeText(PemilikDetailActivity.this, "Gagal", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        wDialog.setCancelButton("Batal", new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sDialog) {
                sDialog.dismissWithAnimation();
            }
        });
        wDialog.show();
    }

    @OnClick(R.id.tl_detail_pemilik)
    public void editData(){
        Intent intent = new Intent(PemilikDetailActivity.this, PemilikEditActivity.class);
        intent.putExtra("data", pemilikData);
        startActivity(intent);
    }

    private void setDataPemilik() {
        SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Mohon Tunggu");
        pDialog.setCancelable(false);
        pDialog.show();

        Call<PemilikResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .getPemilikDetail("Bearer " + this.userToken, pemilikData.getId());

        call.enqueue(new Callback<PemilikResponse>() {
            @Override
            public void onResponse(Call<PemilikResponse> call, Response<PemilikResponse> response) {
                PemilikResponse resp = response.body();

                if(response.isSuccessful()){
                    pDialog.cancel();
                    PemilikModel data = resp.getPemiliks();

                    tvId.setText(String.valueOf(data.getId()));
                    tvKtp.setText(data.getKtp());
                    tvNamaPemilik.setText(data.getNama_pemilik());
                    tvCreatedAt.setText(data.getCreated_at());
                    tvUpdatedAt.setText(data.getUpdated_at());
                }
                else {
                    pDialog.cancel();
                    Toast.makeText(PemilikDetailActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PemilikResponse> call, Throwable t) {
                pDialog.cancel();
                Toast.makeText(PemilikDetailActivity.this, "Gagal", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
