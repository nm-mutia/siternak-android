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
import com.project.siternak.models.data.PeternakanModel;
import com.project.siternak.responses.DataResponse;
import com.project.siternak.responses.PeternakanResponse;
import com.project.siternak.rest.RetrofitClient;
import com.project.siternak.utils.DialogUtils;
import com.project.siternak.utils.SharedPrefManager;

import java.io.Serializable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PeternakanDetailActivity extends AppCompatActivity {
    @BindView(R.id.tv_id) TextView tvId;
    @BindView(R.id.tv_nama_peternakan) TextView tvNama;
    @BindView(R.id.tv_ket) TextView tvKet;
    @BindView(R.id.tv_created_at) TextView tvCreatedAt;
    @BindView(R.id.tv_updated_at) TextView tvUpdatedAt;
    @BindView(R.id.tl_detail_peternakan) TableLayout tlDetailPeternakan;

    private PeternakanModel peternakanData;
    private String userToken;
    private int backFinish;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_peternakan);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.actionbar_primary_arrow);

        TextView tv_actionbar_title = getSupportActionBar().getCustomView().findViewById(R.id.tv_actionbar_title);

        ButterKnife.bind(this);

        peternakanData = (PeternakanModel) getIntent().getSerializableExtra("peternakan");
        tv_actionbar_title.setText("Peternakan - " + String.valueOf(peternakanData.getId()));

        backFinish = (int) getIntent().getIntExtra("finish", 0); //data from after edit peternakan

        userToken = SharedPrefManager.getInstance(this).getAccessToken();
        setDataPeternakan();
    }

    @Override
    public void onBackPressed() {
        if(backFinish == 1){
            Intent intent = new Intent(PeternakanDetailActivity.this, PeternakanActivity.class);
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
        wDialog.setContentText("Data peternakan id " + peternakanData.getId());
        wDialog.setConfirmText("Ya");
        wDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sDialog) {
                sDialog.dismissWithAnimation();

                Call<DataResponse> calld = RetrofitClient
                        .getInstance()
                        .getApi()
                        .delPeternakan("Bearer " + userToken, peternakanData.getId());

                calld.enqueue(new Callback<DataResponse>() {
                    @Override
                    public void onResponse(Call<DataResponse> call, Response<DataResponse> response) {
                        DataResponse resp = response.body();

                        if(response.isSuccessful()){
                            Toast.makeText(PeternakanDetailActivity.this, resp.getMessage(), Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(PeternakanDetailActivity.this, PeternakanActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);

                            PeternakanDetailActivity.this.finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<DataResponse> call, Throwable t) {
                        Toast.makeText(PeternakanDetailActivity.this, "Gagal", Toast.LENGTH_SHORT).show();
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

    @OnClick(R.id.tl_detail_peternakan)
    public void editData(){
        Intent intent = new Intent(PeternakanDetailActivity.this, PeternakanEditActivity.class);
        intent.putExtra("data", (Serializable) peternakanData);
        startActivity(intent);
    }

    private void setDataPeternakan() {
        tvId.setText(String.valueOf(peternakanData.getId()));
        tvNama.setText(peternakanData.getNamaPeternakan());
        tvKet.setText(peternakanData.getKeterangan());
        tvCreatedAt.setText(peternakanData.getCreated_at());
        tvUpdatedAt.setText(peternakanData.getUpdated_at());
    }
}
