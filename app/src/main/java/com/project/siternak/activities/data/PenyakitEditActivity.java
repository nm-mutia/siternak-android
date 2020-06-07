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
import com.project.siternak.models.data.PenyakitModel;
import com.project.siternak.responses.PenyakitResponse;
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

public class PenyakitEditActivity extends AppCompatActivity {
    @BindView(R.id.tiet_penyakit_id) TextInputEditText tietPenyakitId;
    @BindView(R.id.til_penyakit_nama) TextInputLayout tilPenyakitNama;
    @BindView(R.id.tiet_penyakit_nama) TextInputEditText tietPenyakitNama;
    @BindView(R.id.til_penyakit_ket) TextInputLayout tilPenyakitKet;
    @BindView(R.id.tiet_penyakit_ket) TextInputEditText tietPenyakitKet;

    private PenyakitModel penyakitData;
    private String userToken;
    public final static int backFinish = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_penyakit_edit);

        getSupportActionBar().hide();
        ButterKnife.bind(this);

        penyakitData = (PenyakitModel) getIntent().getSerializableExtra("data");
        userToken = SharedPrefManager.getInstance(this).getAccessToken();

        setData();
    }

    private void setData() {
        tietPenyakitId.setText(String.valueOf(penyakitData.getId()));
        tietPenyakitNama.setText(penyakitData.getNamaPenyakit());
        tietPenyakitKet.setText(penyakitData.getKetPenyakit());
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

        SweetAlertDialog pDialog = DialogUtils.getLoadingPopup(this);

        Integer id = Integer.valueOf(tietPenyakitId.getText().toString());
        String nama = tilPenyakitNama.getEditText().getText().toString();
        String ket = tilPenyakitKet.getEditText().getText().toString();

        if(NetworkManager.isNetworkAvailable(PenyakitEditActivity.this)){
            Call<PenyakitResponse> call = RetrofitClient
                    .getInstance()
                    .getApi()
                    .editPenyakit(id, nama, ket, "Bearer " + userToken);

            call.enqueue(new Callback<PenyakitResponse>() {
                @Override
                public void onResponse(Call<PenyakitResponse> call, Response<PenyakitResponse> response) {
                    PenyakitResponse resp = response.body();
                    pDialog.dismiss();

                    if(response.isSuccessful()){
                        if(resp.getStatus().equals("error")){
                            Toast.makeText(PenyakitEditActivity.this, resp.getErrors().toString(), Toast.LENGTH_LONG).show();
                        }
                        else {
                            PenyakitModel datas = new PenyakitModel(id, nama, ket, resp.getPenyakits().getCreated_at(), resp.getPenyakits().getUpdated_at());
                            Toast.makeText(PenyakitEditActivity.this, "Data berhasil diubah: id " + resp.getPenyakits().getId(), Toast.LENGTH_LONG).show();

                            Intent intent = new Intent(PenyakitEditActivity.this, PenyakitDetailActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("penyakit", (Serializable) datas);
                            intent.putExtra("finish", backFinish);
                            startActivity(intent);

                            PenyakitEditActivity.this.finish();
                        }
                    }
                    else{
                        SweetAlertDialog swal = new SweetAlertDialog(PenyakitEditActivity.this, SweetAlertDialog.ERROR_TYPE);
                        swal.setTitleText("Error");
                        swal.setContentText(response.message());
                        swal.show();
                    }
                }

                @Override
                public void onFailure(Call<PenyakitResponse> call, Throwable t) {
                    pDialog.dismiss();
                    SweetAlertDialog swal = new SweetAlertDialog(PenyakitEditActivity.this, SweetAlertDialog.ERROR_TYPE);
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

            PenyakitModel datas = new PenyakitModel(id, nama, ket);
            mReference.child("editData").child("penyakit").child(id.toString()).setValue(datas);

            Toast.makeText(this, "Disimpan", Toast.LENGTH_SHORT).show();

            PenyakitEditActivity.this.finish();
        }
    }
}
