package com.project.siternak.activities.data;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.project.siternak.R;
import com.project.siternak.responses.PemilikResponse;
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

public class PemilikAddActivity extends AppCompatActivity {
    @BindView(R.id.til_pemilik_ktp) TextInputLayout tilPemilikKtp;
    @BindView(R.id.til_pemilik_nama) TextInputLayout tilPemilikNama;

    private String userToken;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_pemilik_add);

        getSupportActionBar().hide();
        ButterKnife.bind(this);

        userToken = SharedPrefManager.getInstance(this).getAccessToken();
    }

    public boolean validateKtp(){
        String p = tilPemilikKtp.getEditText().getText().toString().trim();
        if(p.isEmpty()){
            tilPemilikKtp.setError("Wajib diisi");
            return false;
        }
        else {
            tilPemilikKtp.setError(null);
            tilPemilikKtp.setErrorEnabled(false);
            return true;
        }
    }

    public boolean validateNama(){
        String p = tilPemilikNama.getEditText().getText().toString().trim();
        if(p.isEmpty()){
            tilPemilikNama.setError("Wajib diisi");
            return false;
        }
        else {
            tilPemilikNama.setError(null);
            tilPemilikNama.setErrorEnabled(false);
            return true;
        }
    }

    @OnClick(R.id.tv_submit)
    public void action_add() {
        if (!validateKtp() | !validateNama()) {
            return;
        }

        SweetAlertDialog pDialog = DialogUtils.getLoadingPopup(this);

        String ktp = tilPemilikKtp.getEditText().getText().toString();
        String nama = tilPemilikNama.getEditText().getText().toString();

        Call<PemilikResponse> callp = RetrofitClient
                .getInstance()
                .getApi()
                .addPemilik(ktp, nama, "Bearer " + userToken);

        callp.enqueue(new Callback<PemilikResponse>() {
            @Override
            public void onResponse(Call<PemilikResponse> call, Response<PemilikResponse> response) {
                PemilikResponse resp = response.body();
                pDialog.dismiss();

                if(response.isSuccessful()){
                    if(resp.getStatus().equals("error")){
                        Toast.makeText(PemilikAddActivity.this, resp.getErrors().toString(), Toast.LENGTH_LONG).show();
                    }
                    else{
                        Toast.makeText(PemilikAddActivity.this, "Data berhasil dibuat: id " + resp.getPemiliks().getId(), Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(PemilikAddActivity.this, PemilikActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);

                        PemilikAddActivity.this.finish();
                    }
                }
                else{
                    SweetAlertDialog swal = new SweetAlertDialog(PemilikAddActivity.this, SweetAlertDialog.ERROR_TYPE);
                    swal.setTitleText("Error");
                    swal.setContentText(response.message());
                    swal.show();
                }
            }

            @Override
            public void onFailure(Call<PemilikResponse> call, Throwable t) {
                pDialog.dismiss();
                SweetAlertDialog swal = new SweetAlertDialog(PemilikAddActivity.this, SweetAlertDialog.ERROR_TYPE);
                swal.setTitleText("Error");
                swal.setContentText(t.getMessage());
                swal.show();
            }
        });
    }
}
