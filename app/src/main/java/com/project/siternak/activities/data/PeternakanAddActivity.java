package com.project.siternak.activities.data;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.project.siternak.R;
import com.project.siternak.models.data.PeternakanModel;
import com.project.siternak.responses.PeternakanResponse;
import com.project.siternak.rest.RetrofitClient;
import com.project.siternak.utils.DialogUtils;
import com.project.siternak.utils.NetworkManager;
import com.project.siternak.utils.SharedPrefManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PeternakanAddActivity extends AppCompatActivity {
    @BindView(R.id.til_peternakan_nama) TextInputLayout tilNama;
    @BindView(R.id.til_peternakan_ket) TextInputLayout tilKet;

    private String userToken;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_peternakan_add);

        getSupportActionBar().hide();
        ButterKnife.bind(this);

        userToken = SharedPrefManager.getInstance(this).getAccessToken();
    }

    public boolean validateNama(){
        String p = tilNama.getEditText().getText().toString().trim();
        if(p.isEmpty()){
            tilNama.setError("Wajib diisi");
            return false;
        }
        else {
            tilNama.setError(null);
            tilNama.setErrorEnabled(false);
            return true;
        }
    }

    public boolean validateKet(){
        String p = tilKet.getEditText().getText().toString().trim();
        if(p.isEmpty()){
            tilKet.setError("Wajib diisi");
            return false;
        }
        else {
            tilKet.setError(null);
            tilKet.setErrorEnabled(false);
            return true;
        }
    }

    @OnClick(R.id.tv_submit)
    public void action_add() {
        if (!validateNama() | !validateKet()) {
            return;
        }

        SweetAlertDialog pDialog = DialogUtils.getLoadingPopup(this);

        String nama = tilNama.getEditText().getText().toString();
        String ket = tilKet.getEditText().getText().toString();

        if(NetworkManager.isNetworkAvailable(PeternakanAddActivity.this)){
            Call<PeternakanResponse> call = RetrofitClient
                    .getInstance()
                    .getApi()
                    .addPeternakan(nama, ket, "Bearer " + userToken);

            call.enqueue(new Callback<PeternakanResponse>() {
                @Override
                public void onResponse(Call<PeternakanResponse> call, Response<PeternakanResponse> response) {
                    PeternakanResponse resp = response.body();
                    pDialog.dismiss();

                    if(response.isSuccessful()){
                        if(resp.getStatus().equals("error")){
                            Toast.makeText(PeternakanAddActivity.this, resp.getErrors().toString(), Toast.LENGTH_LONG).show();
                        }
                        else{
                            Toast.makeText(PeternakanAddActivity.this, "Data berhasil dibuat: id " + resp.getPeternakans().getId(), Toast.LENGTH_LONG).show();

                            Intent intent = new Intent(PeternakanAddActivity.this, PeternakanActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);

                            PeternakanAddActivity.this.finish();
                        }
                    }
                    else{
                        SweetAlertDialog swal = new SweetAlertDialog(PeternakanAddActivity.this, SweetAlertDialog.ERROR_TYPE);
                        swal.setTitleText("Error");
                        swal.setContentText(response.message());
                        swal.show();
                    }
                }

                @Override
                public void onFailure(Call<PeternakanResponse> call, Throwable t) {
                    pDialog.dismiss();
                    SweetAlertDialog swal = new SweetAlertDialog(PeternakanAddActivity.this, SweetAlertDialog.ERROR_TYPE);
                    swal.setTitleText("Error");
                    swal.setContentText(t.getMessage());
                    swal.show();
                }
            });
        }
        else {
            pDialog.dismiss();

            FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
            DatabaseReference mReference = mDatabase.getReference("datas");

            PeternakanModel datas = new PeternakanModel(nama, ket);
            mReference.child("addData").child("peternakan").push().setValue(datas);

            Toast.makeText(this, "Disimpan", Toast.LENGTH_SHORT).show();

            PeternakanAddActivity.this.finish();
        }
    }

}
