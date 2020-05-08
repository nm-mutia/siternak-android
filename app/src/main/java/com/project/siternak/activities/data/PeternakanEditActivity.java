package com.project.siternak.activities.data;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.project.siternak.R;
import com.project.siternak.models.data.PeternakanModel;
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

public class PeternakanEditActivity extends AppCompatActivity {
    @BindView(R.id.til_peternakan_id) TextInputLayout tilId;
    @BindView(R.id.tiet_peternakan_id) TextInputEditText tietId;
    @BindView(R.id.til_peternakan_nama) TextInputLayout tilNama;
    @BindView(R.id.tiet_peternakan_nama) TextInputEditText tietNama;
    @BindView(R.id.til_peternakan_ket) TextInputLayout tilKet;
    @BindView(R.id.tiet_peternakan_ket) TextInputEditText tietKet;

    private PeternakanModel peternakanData;
    private String userToken;
    public final static int backFinish = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_peternakan_edit);

        getSupportActionBar().hide();
        ButterKnife.bind(this);

        peternakanData = (PeternakanModel) getIntent().getSerializableExtra("data");
        userToken = SharedPrefManager.getInstance(this).getAccessToken();

        setData();
    }

    private void setData() {
        tietId.setText(String.valueOf(peternakanData.getId()));
        tietNama.setText(peternakanData.getNamaPeternakan());
        tietKet.setText(peternakanData.getKeterangan());
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

        Integer id = Integer.valueOf(tietId.getText().toString());
        String nama = tilNama.getEditText().getText().toString();
        String ket = tilKet.getEditText().getText().toString();

        Call<PeternakanResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .editPeternakan(id, nama, ket, "Bearer " + userToken);

        call.enqueue(new Callback<PeternakanResponse>() {
            @Override
            public void onResponse(Call<PeternakanResponse> call, Response<PeternakanResponse> response) {
                PeternakanResponse resp = response.body();
                pDialog.dismiss();

                if(response.isSuccessful()){
                    if(resp.getStatus().equals("error")){
                        Toast.makeText(PeternakanEditActivity.this, resp.getErrors().toString(), Toast.LENGTH_LONG).show();
                    }
                    else {
                        PeternakanModel datas = new PeternakanModel(id, nama, ket, resp.getPeternakans().getCreated_at(), resp.getPeternakans().getUpdated_at());
                        Toast.makeText(PeternakanEditActivity.this, "Data berhasil diubah: id " + resp.getPeternakans().getId(), Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(PeternakanEditActivity.this, PeternakanDetailActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("peternakan", (Serializable) datas);
                        intent.putExtra("finish", backFinish);
                        startActivity(intent);

                        PeternakanEditActivity.this.finish();
                    }
                }
                else{
                    SweetAlertDialog swal = new SweetAlertDialog(PeternakanEditActivity.this, SweetAlertDialog.ERROR_TYPE);
                    swal.setTitleText("Error");
                    swal.setContentText(response.message());
                    swal.show();
                }
            }

            @Override
            public void onFailure(Call<PeternakanResponse> call, Throwable t) {
                pDialog.dismiss();
                SweetAlertDialog swal = new SweetAlertDialog(PeternakanEditActivity.this, SweetAlertDialog.ERROR_TYPE);
                swal.setTitleText("Error");
                swal.setContentText(t.getMessage());
                swal.show();
            }
        });
    }
}
