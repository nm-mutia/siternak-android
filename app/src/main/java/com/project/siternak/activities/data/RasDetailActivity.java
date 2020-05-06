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
import com.project.siternak.models.data.RasModel;
import com.project.siternak.responses.DataDeleteResponse;
import com.project.siternak.responses.RasResponse;
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

public class RasDetailActivity extends AppCompatActivity {
    @BindView(R.id.tv_id) TextView tvId;
    @BindView(R.id.tv_jenis_ras) TextView tvJenisRas;
    @BindView(R.id.tv_ket_ras) TextView tvKetRas;
    @BindView(R.id.tv_created_at) TextView tvCreatedAt;
    @BindView(R.id.tv_updated_at) TextView tvUpdatedAt;
    @BindView(R.id.tl_detail_ras) TableLayout tlDetailRas;

    private RasModel rasData;
    private String userToken;
    private int backFinish;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_ras);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.actionbar_primary_arrow);

        TextView tv_actionbar_title = getSupportActionBar().getCustomView().findViewById(R.id.tv_actionbar_title);

        ButterKnife.bind(this);

        rasData = (RasModel) getIntent().getSerializableExtra("ras");
        tv_actionbar_title.setText("Ras - " + String.valueOf(rasData.getId()));

        backFinish = (int) getIntent().getIntExtra("finish", 0); //data from after edit ras

        userToken = SharedPrefManager.getInstance(this).getAccessToken();
        setDataRas();
    }

    @Override
    public void onBackPressed() {
        if(backFinish == 1){
            Intent intent = new Intent(RasDetailActivity.this, RasActivity.class);
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
    public void deleteData() {
        SweetAlertDialog wDialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
        wDialog.setTitleText("Apakah anda yakin untuk menghapus data ini?");
        wDialog.setContentText("Data ras id " + rasData.getId());
        wDialog.setConfirmText("Ya");
        wDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sDialog) {
                sDialog.dismissWithAnimation();

                Call<DataDeleteResponse> calld = RetrofitClient
                        .getInstance()
                        .getApi()
                        .delRas("Bearer " + userToken, rasData.getId());

                calld.enqueue(new Callback<DataDeleteResponse>() {
                    @Override
                    public void onResponse(Call<DataDeleteResponse> call, Response<DataDeleteResponse> response) {
                        DataDeleteResponse resp = response.body();

                        if(response.isSuccessful()){
                            Toast.makeText(RasDetailActivity.this, resp.getMessage(), Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(RasDetailActivity.this, RasActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);

                            RasDetailActivity.this.finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<DataDeleteResponse> call, Throwable t) {
                        Toast.makeText(RasDetailActivity.this, "Gagal", Toast.LENGTH_SHORT).show();
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

    @OnClick(R.id.tl_detail_ras)
    public void editData(){
        Intent intent = new Intent(RasDetailActivity.this, RasEditActivity.class);
        intent.putExtra("data", (Serializable) rasData);
        startActivity(intent);
    }

    private void setDataRas() {
        SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Mohon Tunggu");
        pDialog.setCancelable(false);
        pDialog.show();

        Call<RasResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .getRasDetail("Bearer " + this.userToken, rasData.getId());

        call.enqueue(new Callback<RasResponse>() {
            @Override
            public void onResponse(Call<RasResponse> call, Response<RasResponse> response) {
                RasResponse resp = response.body();
                pDialog.cancel();

                if(response.isSuccessful()){
                    RasModel data = resp.getRas();

                    tvId.setText(String.valueOf(data.getId()));
                    tvJenisRas.setText(data.getJenisRas());
                    tvKetRas.setText(data.getKetRas());
                    tvCreatedAt.setText(data.getCreated_at());
                    tvUpdatedAt.setText(data.getUpdated_at());
                }
                else {
                    Toast.makeText(RasDetailActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RasResponse> call, Throwable t) {
                pDialog.cancel();
                Toast.makeText(RasDetailActivity.this, "Gagal", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
