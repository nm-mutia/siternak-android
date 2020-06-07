package com.project.siternak.activities.data;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.project.siternak.R;
import com.project.siternak.models.data.PemilikModel;
import com.project.siternak.responses.PemilikResponse;
import com.project.siternak.rest.RetrofitClient;
import com.project.siternak.utils.DialogUtils;
import com.project.siternak.utils.NetworkManager;
import com.project.siternak.utils.SharedPrefManager;

import java.io.Serializable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PemilikEditActivity extends AppCompatActivity {
    @BindView(R.id.tiet_pemilik_id) TextInputEditText tietPemilikId;
    @BindView(R.id.til_pemilik_ktp) TextInputLayout tilPemilikKtp;
    @BindView(R.id.tiet_pemilik_ktp) TextInputEditText tietPemilikKtp;
    @BindView(R.id.til_pemilik_nama) TextInputLayout tilPemilikNama;
    @BindView(R.id.tiet_pemilik_nama) TextInputEditText tietPemilikNama;

    private PemilikModel pemilikData;
    private String userToken;
    public final static int backFinish = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_pemilik_edit);

        getSupportActionBar().hide();
        ButterKnife.bind(this);

        pemilikData = (PemilikModel) getIntent().getSerializableExtra("data");
        userToken = SharedPrefManager.getInstance(this).getAccessToken();

        setData();
    }

    private void setData() {
        tietPemilikId.setText(String.valueOf(pemilikData.getId()));
        tietPemilikKtp.setText(pemilikData.getKtp());
        tietPemilikNama.setText(pemilikData.getNama_pemilik());
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

        Integer id = Integer.valueOf(tietPemilikId.getText().toString());
        String ktp = tilPemilikKtp.getEditText().getText().toString();
        String nama = tilPemilikNama.getEditText().getText().toString();

        if(NetworkManager.isNetworkAvailable(PemilikEditActivity.this)){
            Call<PemilikResponse> call = RetrofitClient
                    .getInstance()
                    .getApi()
                    .editPemilik(id, ktp, nama, "Bearer " + userToken);

            call.enqueue(new Callback<PemilikResponse>() {
                @Override
                public void onResponse(Call<PemilikResponse> call, Response<PemilikResponse> response) {
                    PemilikResponse resp = response.body();
                    pDialog.dismiss();

                    if(response.isSuccessful()){
                        if(resp.getStatus().equals("error")){
                            Toast.makeText(PemilikEditActivity.this, resp.getErrors().toString(), Toast.LENGTH_LONG).show();
                        }
                        else {
                            PemilikModel datas = new PemilikModel(id, ktp, nama, resp.getPemiliks().getCreated_at(), resp.getPemiliks().getUpdated_at());
                            Toast.makeText(PemilikEditActivity.this, "Data berhasil diubah: id " + resp.getPemiliks().getId(), Toast.LENGTH_LONG).show();

                            Intent intent = new Intent(PemilikEditActivity.this, PemilikDetailActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("pemilik", (Serializable) datas);
                            intent.putExtra("finish", backFinish);
                            startActivity(intent);

                            PemilikEditActivity.this.finish();
                        }
                    }
                    else{
                        SweetAlertDialog swal = new SweetAlertDialog(PemilikEditActivity.this, SweetAlertDialog.ERROR_TYPE);
                        swal.setTitleText("Error");
                        swal.setContentText(response.message());
                        swal.show();
                    }
                }

                @Override
                public void onFailure(Call<PemilikResponse> call, Throwable t) {
                    pDialog.dismiss();
                    SweetAlertDialog swal = new SweetAlertDialog(PemilikEditActivity.this, SweetAlertDialog.ERROR_TYPE);
                    swal.setTitleText("Error");
                    swal.setContentText(t.getMessage());
                    swal.show();
                }
            });
        }
        else{
            pDialog.dismiss();

            FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
            DatabaseReference mReference = mDatabase.getReference("datas");

            PemilikModel datas = new PemilikModel(id, ktp, nama);
            mReference.child("editData").child("pemilik").child(id.toString()).setValue(datas);

            Toast.makeText(this, "Disimpan", Toast.LENGTH_SHORT).show();

            PemilikEditActivity.this.finish();
        }
    }
}
