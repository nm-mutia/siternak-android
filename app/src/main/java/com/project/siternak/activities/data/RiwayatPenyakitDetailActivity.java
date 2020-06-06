package com.project.siternak.activities.data;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.project.siternak.R;
import com.project.siternak.models.data.RiwayatPenyakitModel;
import com.project.siternak.responses.DataResponse;
import com.project.siternak.responses.RiwayatPenyakitResponse;
import com.project.siternak.rest.RetrofitClient;
import com.project.siternak.utils.DialogUtils;
import com.project.siternak.utils.SharedPrefManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RiwayatPenyakitDetailActivity extends AppCompatActivity {
    @BindView(R.id.tv_id) TextView tvId;
    @BindView(R.id.tv_penyakit) TextView tvPenyakit;
    @BindView(R.id.tv_necktag) TextView tvNecktag;
    @BindView(R.id.tv_tgl) TextView tvTgl;
    @BindView(R.id.tv_obat) TextView tvObat;
    @BindView(R.id.tv_lama_sakit) TextView tvLamaSakit;
    @BindView(R.id.tv_keterangan) TextView tvKeterangan;
    @BindView(R.id.tv_created_at) TextView tvCreatedAt;
    @BindView(R.id.tv_updated_at) TextView tvUpdatedAt;
    @BindView(R.id.tl_detail_riwayat) TableLayout tlDetailRiwayat;

    private RiwayatPenyakitModel riwayatData;
    private String userToken;
    private int backFinish;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_riwayat);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.actionbar_primary_arrow);

        TextView tv_actionbar_title = getSupportActionBar().getCustomView().findViewById(R.id.tv_actionbar_title);

        ButterKnife.bind(this);

        riwayatData = (RiwayatPenyakitModel) getIntent().getSerializableExtra("riwayat");
        tv_actionbar_title.setText("Riwayat Penyakit - " + String.valueOf(riwayatData.getId()));

        backFinish = (int) getIntent().getIntExtra("finish", 0); //data from after edit riwayat

        userToken = SharedPrefManager.getInstance(this).getAccessToken();
        setDataRiwayat();
    }

    @Override
    public void onBackPressed() {
        if(backFinish == 1){
            Intent intent = new Intent(RiwayatPenyakitDetailActivity.this, RiwayatPenyakitActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        finish();
    }

    @OnClick(R.id.ib_actionbar_close)
    public void close(){
        onBackPressed();
    }

    @OnClick(R.id.ib_delete_data)
    public void deleteData() {
        SweetAlertDialog wDialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
        wDialog.setTitleText("Apakah anda yakin untuk menghapus data ini?");
        wDialog.setContentText("Data riwayat penyakit id " + riwayatData.getId());
        wDialog.setConfirmText("Ya");
        wDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sDialog) {
                sDialog.dismissWithAnimation();

                Call<DataResponse> calls = RetrofitClient
                        .getInstance()
                        .getApi()
                        .delRiwayat("Bearer " + userToken, riwayatData.getId());

                calls.enqueue(new Callback<DataResponse>() {
                    @Override
                    public void onResponse(Call<DataResponse> call, Response<DataResponse> response) {
                        DataResponse resp = response.body();

                        if(response.isSuccessful()){
                            Toast.makeText(RiwayatPenyakitDetailActivity.this, resp.getMessage(), Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(RiwayatPenyakitDetailActivity.this, RiwayatPenyakitActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);

                            RiwayatPenyakitDetailActivity.this.finish();
                        }
                    }
                    @Override
                    public void onFailure(Call<DataResponse> call, Throwable t) {
                        Toast.makeText(RiwayatPenyakitDetailActivity.this, "Gagal", Toast.LENGTH_SHORT).show();
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

    @OnClick(R.id.tl_detail_riwayat)
    public void editData(){
        Intent intent = new Intent(RiwayatPenyakitDetailActivity.this, RiwayatPenyakitEditActivity.class);
        intent.putExtra("data", riwayatData);
        startActivity(intent);
    }

    private void setDataRiwayat() {
        tvId.setText(String.valueOf(riwayatData.getId()));
        tvPenyakit.setText(String.valueOf(riwayatData.getPenyakitId()));
        tvNecktag.setText(riwayatData.getNecktag());
        tvTgl.setText(riwayatData.getTglSakit());
        tvObat.setText(riwayatData.getObat());
        tvLamaSakit.setText(String.valueOf(riwayatData.getLamaSakit()));
        tvKeterangan.setText(riwayatData.getKeterangan());
        tvCreatedAt.setText(riwayatData.getCreated_at());
        tvUpdatedAt.setText(riwayatData.getUpdated_at());
    }
}
