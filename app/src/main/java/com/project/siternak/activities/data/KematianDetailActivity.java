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
import com.project.siternak.models.data.KematianModel;
import com.project.siternak.responses.KematianDeleteResponse;
import com.project.siternak.responses.KematianResponse;
import com.project.siternak.rest.RetrofitClient;
import com.project.siternak.utils.SharedPrefManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KematianDetailActivity extends AppCompatActivity {
    @BindView(R.id.tv_id) TextView tvId;
    @BindView(R.id.tv_tgl) TextView tvTgl;
    @BindView(R.id.tv_waktu) TextView tvWaktu;
    @BindView(R.id.tv_penyebab) TextView tvPenyebab;
    @BindView(R.id.tv_kondisi) TextView tvKondisi;
    @BindView(R.id.tv_created_at) TextView tvCreatedAt;
    @BindView(R.id.tv_updated_at) TextView tvUpdatedAt;
    @BindView(R.id.tl_detail_kematian) TableLayout tlDetailKematian;

    private KematianModel kematianData;
    private String userToken;
    public int backFinish;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_kematian_detail);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.actionbar_primary_arrow);

        TextView tv_actionbar_title = getSupportActionBar().getCustomView().findViewById(R.id.tv_actionbar_title);

        ButterKnife.bind(this);

        kematianData = (KematianModel) getIntent().getSerializableExtra("kematian");
        tv_actionbar_title.setText("Kematian - " + String.valueOf(kematianData.getId()));

        backFinish = (int) getIntent().getIntExtra("finish", 0); //data from after edit kematian

        userToken = SharedPrefManager.getInstance(this).getAccessToken();
        setDataKematian();
    }

    @Override
    public void onBackPressed() {
        if(backFinish == 1){
            Intent intent = new Intent(KematianDetailActivity.this, KematianActivity.class);
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
        wDialog.setContentText("Data kematian id " + kematianData.getId());
        wDialog.setConfirmText("Ya");
        wDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sDialog) {
                sDialog.dismissWithAnimation();

                Call<KematianDeleteResponse> calls = RetrofitClient
                        .getInstance()
                        .getApi()
                        .delKematian("Bearer " + userToken, kematianData.getId());

                calls.enqueue(new Callback<KematianDeleteResponse>() {
                    @Override
                    public void onResponse(Call<KematianDeleteResponse> call, Response<KematianDeleteResponse> response) {
                        KematianDeleteResponse resp = response.body();

                        if(response.isSuccessful()){
                            Toast.makeText(KematianDetailActivity.this, resp.getMessage(), Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(KematianDetailActivity.this, KematianActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);

                            KematianDetailActivity.this.finish();
                        }
                    }
                    @Override
                    public void onFailure(Call<KematianDeleteResponse> call, Throwable t) {
                        Toast.makeText(KematianDetailActivity.this, "Gagal", Toast.LENGTH_SHORT).show();
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

    @OnClick(R.id.tl_detail_kematian)
    public void editData(){
        Intent intent = new Intent(KematianDetailActivity.this, KematianEditActivity.class);
        intent.putExtra("data", kematianData);
        startActivity(intent);
    }

    private void setDataKematian() {
        SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Mohon Tunggu");
        pDialog.setCancelable(false);
        pDialog.show();

        Call<KematianResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .getKematianDetail("Bearer " + this.userToken, kematianData.getId());

        call.enqueue(new Callback<KematianResponse>() {
            @Override
            public void onResponse(Call<KematianResponse> call, Response<KematianResponse> response) {
                KematianResponse resp = response.body();

                if(response.isSuccessful()){
                    pDialog.cancel();
                    KematianModel data = resp.getKematians();

                    tvId.setText(String.valueOf(data.getId()));
                    tvTgl.setText(data.getTgl_kematian());
                    tvWaktu.setText(data.getWaktu_kematian());
                    tvPenyebab.setText(data.getPenyebab());
                    tvKondisi.setText(data.getKondisi());
                    tvCreatedAt.setText(data.getCreated_at());
                    tvUpdatedAt.setText(data.getUpdated_at());
                }
                else {
                    pDialog.cancel();
                    Toast.makeText(KematianDetailActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<KematianResponse> call, Throwable t) {
                pDialog.cancel();
                Toast.makeText(KematianDetailActivity.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }
}
