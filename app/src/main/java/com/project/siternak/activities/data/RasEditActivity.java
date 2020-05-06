package com.project.siternak.activities.data;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.project.siternak.R;
import com.project.siternak.models.data.RasModel;
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

public class RasEditActivity extends AppCompatActivity {
    @BindView(R.id.tiet_ras_id) TextInputEditText tietRasId;
    @BindView(R.id.til_ras_jenis) TextInputLayout tilRasJenis;
    @BindView(R.id.tiet_ras_jenis) TextInputEditText tietRasJenis;
    @BindView(R.id.til_ras_ket) TextInputLayout tilRasKet;
    @BindView(R.id.tiet_ras_ket) TextInputEditText tietRasKet;

    private RasModel rasData;
    private String userToken;
    public final static int backFinish = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_ras_edit);

        getSupportActionBar().hide();
        ButterKnife.bind(this);

        rasData = (RasModel) getIntent().getSerializableExtra("data");
        userToken = SharedPrefManager.getInstance(this).getAccessToken();

        setData();
    }

    private void setData() {
        tietRasId.setText(String.valueOf(rasData.getId()));
        tietRasJenis.setText(rasData.getJenisRas());
        tietRasKet.setText(rasData.getKetRas());
    }

    public boolean validateJenis(){
        String p = tilRasJenis.getEditText().getText().toString().trim();
        if(p.isEmpty()){
            tilRasJenis.setError("Wajib diisi");
            return false;
        }
        else {
            tilRasJenis.setError(null);
            tilRasJenis.setErrorEnabled(false);
            return true;
        }
    }

    public boolean validateKet(){
        String p = tilRasKet.getEditText().getText().toString().trim();
        if(p.isEmpty()){
            tilRasKet.setError("Wajib diisi");
            return false;
        }
        else {
            tilRasKet.setError(null);
            tilRasKet.setErrorEnabled(false);
            return true;
        }
    }

    @OnClick(R.id.tv_submit)
    public void action_add() {
        if (!validateJenis() | !validateKet()) {
            return;
        }

        SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Mohon Tunggu");
        pDialog.setCancelable(false);
        pDialog.show();

        Integer id = Integer.valueOf(tietRasId.getText().toString());
        String jenis = tilRasJenis.getEditText().getText().toString();
        String ket = tilRasKet.getEditText().getText().toString();

        Call<RasResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .editRas(id, jenis, ket, "Bearer " + userToken);

        call.enqueue(new Callback<RasResponse>() {
            @Override
            public void onResponse(Call<RasResponse> call, Response<RasResponse> response) {
                RasResponse resp = response.body();
                pDialog.dismiss();

                if(response.isSuccessful()){
                    if(resp.getStatus().equals("error")){
                        Toast.makeText(RasEditActivity.this, resp.getErrors().toString(), Toast.LENGTH_LONG).show();
                    }
                    else {
                        RasModel datas = new RasModel(id, jenis, ket, resp.getRas().getCreated_at(), resp.getRas().getUpdated_at());
                        Toast.makeText(RasEditActivity.this, "Data berhasil diubah: id " + resp.getRas().getId(), Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(RasEditActivity.this, RasDetailActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("ras", (Serializable) datas);
                        intent.putExtra("finish", backFinish);
                        startActivity(intent);

                        RasEditActivity.this.finish();
                    }
                }
                else{
                    SweetAlertDialog swal = new SweetAlertDialog(RasEditActivity.this, SweetAlertDialog.ERROR_TYPE);
                    swal.setTitleText("Error");
                    swal.setContentText(response.message());
                    swal.show();
                }
            }

            @Override
            public void onFailure(Call<RasResponse> call, Throwable t) {
                pDialog.dismiss();
                SweetAlertDialog swal = new SweetAlertDialog(RasEditActivity.this, SweetAlertDialog.ERROR_TYPE);
                swal.setTitleText("Error");
                swal.setContentText(t.getMessage());
                swal.show();
            }
        });
    }
}
