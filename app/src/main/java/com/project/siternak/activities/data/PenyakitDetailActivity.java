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
import com.project.siternak.models.data.PenyakitModel;
import com.project.siternak.responses.DataResponse;
import com.project.siternak.responses.PenyakitResponse;
import com.project.siternak.rest.RetrofitClient;
import com.project.siternak.utils.SharedPrefManager;

import java.io.Serializable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PenyakitDetailActivity extends AppCompatActivity {
    @BindView(R.id.tv_id) TextView tvId;
    @BindView(R.id.tv_nama_penyakit) TextView tvNamaPenyakit;
    @BindView(R.id.tv_ket_penyakit) TextView tvKetPenyakit;
    @BindView(R.id.tv_created_at) TextView tvCreatedAt;
    @BindView(R.id.tv_updated_at) TextView tvUpdatedAt;
    @BindView(R.id.tl_detail_penyakit) TableLayout tlDetailPenyakit;

    private PenyakitModel penyakitData;
    private String userToken;
    private int backFinish;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_penyakit);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.actionbar_primary_arrow);

        TextView tv_actionbar_title = getSupportActionBar().getCustomView().findViewById(R.id.tv_actionbar_title);

        ButterKnife.bind(this);

        penyakitData = (PenyakitModel) getIntent().getSerializableExtra("penyakit");
        tv_actionbar_title.setText("Penyakit - " + String.valueOf(penyakitData.getId()));

        backFinish = (int) getIntent().getIntExtra("finish", 0); //data from after edit penyakit

        userToken = SharedPrefManager.getInstance(this).getAccessToken();
        setDataPenyakit();
    }

    @Override
    public void onBackPressed() {
        if(backFinish == 1){
            Intent intent = new Intent(PenyakitDetailActivity.this, PenyakitActivity.class);
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
        wDialog.setContentText("Data penyakit id " + penyakitData.getId());
        wDialog.setConfirmText("Ya");
        wDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sDialog) {
                sDialog.dismissWithAnimation();

                Call<DataResponse> calld = RetrofitClient
                        .getInstance()
                        .getApi()
                        .delPenyakit("Bearer " + userToken, penyakitData.getId());

                calld.enqueue(new Callback<DataResponse>() {
                    @Override
                    public void onResponse(Call<DataResponse> call, Response<DataResponse> response) {
                        DataResponse resp = response.body();

                        if(response.isSuccessful()){
                            Toast.makeText(PenyakitDetailActivity.this, resp.getMessage(), Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(PenyakitDetailActivity.this, PenyakitActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);

                            PenyakitDetailActivity.this.finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<DataResponse> call, Throwable t) {
                        Toast.makeText(PenyakitDetailActivity.this, "Gagal", Toast.LENGTH_SHORT).show();
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

    @OnClick(R.id.tl_detail_penyakit)
    public void editData(){
        Intent intent = new Intent(PenyakitDetailActivity.this, PenyakitEditActivity.class);
        intent.putExtra("data", (Serializable) penyakitData);
        startActivity(intent);
    }

    private void setDataPenyakit() {
        SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Mohon Tunggu");
        pDialog.setCancelable(false);
        pDialog.show();

        Call<PenyakitResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .getPenyakitDetail("Bearer " + this.userToken, penyakitData.getId());

        call.enqueue(new Callback<PenyakitResponse>() {
            @Override
            public void onResponse(Call<PenyakitResponse> call, Response<PenyakitResponse> response) {
                PenyakitResponse resp = response.body();
                pDialog.cancel();

                if(response.isSuccessful()){
                    PenyakitModel data = resp.getPenyakits();

                    tvId.setText(String.valueOf(data.getId()));
                    tvNamaPenyakit.setText(data.getNamaPenyakit());
                    tvKetPenyakit.setText(data.getKetPenyakit());
                    tvCreatedAt.setText(data.getCreated_at());
                    tvUpdatedAt.setText(data.getUpdated_at());
                }
                else {
                    Toast.makeText(PenyakitDetailActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PenyakitResponse> call, Throwable t) {
                pDialog.cancel();
                Toast.makeText(PenyakitDetailActivity.this, "Gagal", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
