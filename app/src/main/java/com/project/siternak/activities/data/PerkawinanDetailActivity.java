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
import com.project.siternak.models.data.PerkawinanModel;
import com.project.siternak.responses.DataResponse;
import com.project.siternak.responses.PerkawinanResponse;
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

public class PerkawinanDetailActivity extends AppCompatActivity {
    @BindView(R.id.tv_id) TextView tvId;
    @BindView(R.id.tv_necktag) TextView tvNecktag;
    @BindView(R.id.tv_necktag_psg) TextView tvNecktagPsg;
    @BindView(R.id.tv_tgl) TextView tvTgl;
    @BindView(R.id.tv_created_at) TextView tvCreatedAt;
    @BindView(R.id.tv_updated_at) TextView tvUpdatedAt;
    @BindView(R.id.tl_detail_perkawinan) TableLayout tlDetailPerkawinan;

    private PerkawinanModel perkawinanData;
    private String userToken;
    private int backFinish;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_perkawinan);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.actionbar_primary_arrow);

        TextView tv_actionbar_title = getSupportActionBar().getCustomView().findViewById(R.id.tv_actionbar_title);

        ButterKnife.bind(this);

        perkawinanData = (PerkawinanModel) getIntent().getSerializableExtra("perkawinan");
        tv_actionbar_title.setText("Perkawinan - " + String.valueOf(perkawinanData.getId()));

        backFinish = (int) getIntent().getIntExtra("finish", 0); //data from after edit perkawinan

        userToken = SharedPrefManager.getInstance(this).getAccessToken();
        setDataPerkawinan();
    }

    @Override
    public void onBackPressed() {
        if(backFinish == 1){
            Intent intent = new Intent(PerkawinanDetailActivity.this, PerkawinanActivity.class);
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
    public void deleteData(){
        SweetAlertDialog wDialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
        wDialog.setTitleText("Apakah anda yakin untuk menghapus data ini?");
        wDialog.setContentText("Data perkawinan id " + perkawinanData.getId());
        wDialog.setConfirmText("Ya");
        wDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sDialog) {
                sDialog.dismissWithAnimation();

                Call<DataResponse> calls = RetrofitClient
                        .getInstance()
                        .getApi()
                        .delPerkawinan("Bearer " + userToken, perkawinanData.getId());

                calls.enqueue(new Callback<DataResponse>() {
                    @Override
                    public void onResponse(Call<DataResponse> call, Response<DataResponse> response) {
                        DataResponse resp = response.body();

                        if(response.isSuccessful()){
                            Toast.makeText(PerkawinanDetailActivity.this, resp.getMessage(), Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(PerkawinanDetailActivity.this, PerkawinanActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);

                            PerkawinanDetailActivity.this.finish();
                        }
                    }
                    @Override
                    public void onFailure(Call<DataResponse> call, Throwable t) {
                        Toast.makeText(PerkawinanDetailActivity.this, "Gagal", Toast.LENGTH_SHORT).show();
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

    @OnClick(R.id.tl_detail_perkawinan)
    public void editData(){
        Intent intent = new Intent(PerkawinanDetailActivity.this, PerkawinanEditActivity.class);
        intent.putExtra("data", perkawinanData);
        startActivity(intent);
    }

    private void setDataPerkawinan() {
        tvId.setText(String.valueOf(perkawinanData.getId()));
        tvNecktag.setText(perkawinanData.getNecktag());
        tvNecktagPsg.setText(perkawinanData.getNecktag_psg());
        tvTgl.setText(perkawinanData.getTgl());
        tvCreatedAt.setText(perkawinanData.getCreated_at());
        tvUpdatedAt.setText(perkawinanData.getUpdated_at());
    }
}
