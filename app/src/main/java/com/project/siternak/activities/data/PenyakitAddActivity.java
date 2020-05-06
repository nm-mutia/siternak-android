package com.project.siternak.activities.data;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.project.siternak.R;
import com.project.siternak.responses.PenyakitResponse;
import com.project.siternak.rest.RetrofitClient;
import com.project.siternak.utils.SharedPrefManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PenyakitAddActivity extends AppCompatActivity {
    @BindView(R.id.til_penyakit_nama) TextInputLayout tilPenyakitNama;
    @BindView(R.id.til_penyakit_ket) TextInputLayout tilPenyakitKet;

    private String userToken;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_penyakit_add);

        getSupportActionBar().hide();
        ButterKnife.bind(this);

        userToken = SharedPrefManager.getInstance(this).getAccessToken();
    }

    public boolean validateNama(){
        String p = tilPenyakitNama.getEditText().getText().toString().trim();
        if(p.isEmpty()){
            tilPenyakitNama.setError("Wajib diisi");
            return false;
        }
        else {
            tilPenyakitNama.setError(null);
            tilPenyakitNama.setErrorEnabled(false);
            return true;
        }
    }

    public boolean validateKet(){
        String p = tilPenyakitKet.getEditText().getText().toString().trim();
        if(p.isEmpty()){
            tilPenyakitKet.setError("Wajib diisi");
            return false;
        }
        else {
            tilPenyakitKet.setError(null);
            tilPenyakitKet.setErrorEnabled(false);
            return true;
        }
    }

    @OnClick(R.id.tv_submit)
    public void action_add() {
        if (!validateNama() | !validateKet()) {
            return;
        }

        SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Mohon Tunggu");
        pDialog.setCancelable(false);
        pDialog.show();

        String nama = tilPenyakitNama.getEditText().getText().toString();
        String ket = tilPenyakitKet.getEditText().getText().toString();

        Call<PenyakitResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .addPenyakit(nama, ket, "Bearer " + userToken);

        call.enqueue(new Callback<PenyakitResponse>() {
            @Override
            public void onResponse(Call<PenyakitResponse> call, Response<PenyakitResponse> response) {
                PenyakitResponse resp = response.body();
                pDialog.dismiss();

                if(response.isSuccessful()){
                    if(resp.getStatus().equals("error")){
                        Toast.makeText(PenyakitAddActivity.this, resp.getErrors().toString(), Toast.LENGTH_LONG).show();
                    }
                    else{
                        Toast.makeText(PenyakitAddActivity.this, "Data berhasil dibuat: id " + resp.getPenyakits().getId(), Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(PenyakitAddActivity.this, PenyakitActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);

                        PenyakitAddActivity.this.finish();
                    }
                }
                else{
                    SweetAlertDialog swal = new SweetAlertDialog(PenyakitAddActivity.this, SweetAlertDialog.ERROR_TYPE);
                    swal.setTitleText("Error");
                    swal.setContentText(response.message());
                    swal.show();
                }
            }

            @Override
            public void onFailure(Call<PenyakitResponse> call, Throwable t) {
                pDialog.dismiss();
                SweetAlertDialog swal = new SweetAlertDialog(PenyakitAddActivity.this, SweetAlertDialog.ERROR_TYPE);
                swal.setTitleText("Error");
                swal.setContentText(t.getMessage());
                swal.show();
            }
        });
    }
}
