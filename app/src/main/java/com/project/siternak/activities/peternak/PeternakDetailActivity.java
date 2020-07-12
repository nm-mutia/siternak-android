package com.project.siternak.activities.peternak;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.project.siternak.R;
import com.project.siternak.models.peternak.PeternakModel;
import com.project.siternak.responses.DataResponse;
import com.project.siternak.responses.PeternakResponse;
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

public class PeternakDetailActivity extends AppCompatActivity {
    @BindView(R.id.tv_id) TextView tvId;
    @BindView(R.id.tv_nama_peternak) TextView tvNama;
    @BindView(R.id.tv_peternakan_id) TextView tvIdPeternakan;
    @BindView(R.id.tv_reg_admin) TextView tvRegAdm;
    @BindView(R.id.tv_username) TextView tvUsername;
    @BindView(R.id.tv_email) TextView tvEmail;
    @BindView(R.id.tv_password) TextView tvPass;
    @BindView(R.id.tv_created_at) TextView tvCreatedAt;
    @BindView(R.id.tv_updated_at) TextView tvUpdatedAt;
    @BindView(R.id.tl_detail_peternak) TableLayout tlDetailPeternak;

    private PeternakModel peternak;
    private String userToken;
    private int backFinish;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peternak_detail);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.actionbar_primary_arrow);

        TextView tv_actionbar_title = getSupportActionBar().getCustomView().findViewById(R.id.tv_actionbar_title);

        ButterKnife.bind(this);

        peternak = (PeternakModel) getIntent().getSerializableExtra("peternak");
        tv_actionbar_title.setText("Peternak - " + String.valueOf(peternak.getId()));

        backFinish = (int) getIntent().getIntExtra("finish", 0); //data from after edit peternak

        userToken = SharedPrefManager.getInstance(this).getAccessToken();
        setDataPeternak();
    }

    @Override
    public void onBackPressed() {
        if(backFinish == 1){
            Intent intent = new Intent(PeternakDetailActivity.this, PeternakActivity.class);
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
        wDialog.setContentText("Data peternak id " + peternak.getId());
        wDialog.setConfirmText("Ya");
        wDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sDialog) {
                sDialog.dismissWithAnimation();

                Call<DataResponse> calld = RetrofitClient
                        .getInstance()
                        .getApi()
                        .delPeternak("Bearer " + userToken, peternak.getId());

                calld.enqueue(new Callback<DataResponse>() {
                    @Override
                    public void onResponse(Call<DataResponse> call, Response<DataResponse> response) {
                        DataResponse resp = response.body();

                        if(response.isSuccessful()){
                            Toast.makeText(PeternakDetailActivity.this, resp.getMessage(), Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(PeternakDetailActivity.this, PeternakActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);

                            PeternakDetailActivity.this.finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<DataResponse> call, Throwable t) {
                        Toast.makeText(PeternakDetailActivity.this, "Gagal", Toast.LENGTH_SHORT).show();
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

    @OnClick(R.id.tl_detail_peternak)
    public void editData(){
        Intent intent = new Intent(PeternakDetailActivity.this, PeternakEditActivity.class);
        intent.putExtra("data", (Serializable) peternak);
        startActivity(intent);
    }

    private void setDataPeternak() {
        tvId.setText(String.valueOf(peternak.getId()));
        tvNama.setText(peternak.getNamaPeternak());
        tvIdPeternakan.setText(String.valueOf(peternak.getPeternakanId()));
        tvRegAdm.setText(String.valueOf(peternak.getRegAdmin()));
        tvUsername.setText(peternak.getUsername());
        tvEmail.setText(peternak.getEmail());
        tvPass.setText(peternak.getPassword());
        tvCreatedAt.setText(peternak.getCreated_at());
        tvUpdatedAt.setText(peternak.getUpdated_at());
    }
}
