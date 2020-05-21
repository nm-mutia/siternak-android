package com.project.siternak.activities.peternak;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.project.siternak.R;
import com.project.siternak.activities.option.PeternakanOptionActivity;
import com.project.siternak.models.data.PeternakanModel;
import com.project.siternak.models.peternak.PeternakModel;
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

public class PeternakEditActivity extends AppCompatActivity {
    @BindView(R.id.til_peternak_id) TextInputLayout tilId;
    @BindView(R.id.tiet_peternak_id) TextInputEditText tietId;
    @BindView(R.id.til_peternak_peternakan) TextInputLayout tilPeternakan;
    @BindView(R.id.tiet_peternak_peternakan) TextInputEditText tietPeternakan;
    @BindView(R.id.til_peternak_nama) TextInputLayout tilNama;
    @BindView(R.id.tiet_peternak_nama) TextInputEditText tietNama;

    private static final int REQUEST_CODE_SETPETERNAKAN = 1;

    private PeternakModel peternak;
    private String userToken;
    public final static int backFinish = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peternak_edit);

        getSupportActionBar().hide();
        ButterKnife.bind(this);

        peternak = (PeternakModel) getIntent().getSerializableExtra("data");
        userToken = SharedPrefManager.getInstance(this).getAccessToken();

        setData();
    }

    private void setData() {
        tietId.setText(String.valueOf(peternak.getId()));
        tietPeternakan.setText(String.valueOf(peternak.getPeternakanId()));
        tietNama.setText(peternak.getNamaPeternak());
    }

    @OnClick(R.id.tiet_peternak_peternakan)
    public void setPeternakan(){
        Intent intent = new Intent(PeternakEditActivity.this, PeternakanOptionActivity.class);
        startActivityForResult(intent, REQUEST_CODE_SETPETERNAKAN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK) {
            if(requestCode == REQUEST_CODE_SETPETERNAKAN){
                PeternakanModel result = (PeternakanModel) data.getSerializableExtra("peternakan");
                tietPeternakan.setText(String.valueOf(result.getId()));
            }
        }
    }

    public boolean validatePeternakan(){
        String p = tilPeternakan.getEditText().getText().toString().trim();
        if(p.isEmpty()){
            tilPeternakan.setError("Wajib diisi");
            return false;
        }
        else {
            tilPeternakan.setError(null);
            tilPeternakan.setErrorEnabled(false);
            return true;
        }
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

    @OnClick(R.id.tv_submit)
    public void action_add() {
        if (!validateNama() | !validatePeternakan()) {
            return;
        }

        SweetAlertDialog pDialog = DialogUtils.getLoadingPopup(this);

        Integer id = Integer.valueOf(tietId.getText().toString());
        Integer peternakan = Integer.valueOf(tietPeternakan.getText().toString());
        String nama = tietNama.getText().toString();

        Call<PeternakResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .editPeternak(id, peternakan, nama, "Bearer " + userToken);

        call.enqueue(new Callback<PeternakResponse>() {
            @Override
            public void onResponse(Call<PeternakResponse> call, Response<PeternakResponse> response) {
                PeternakResponse resp = response.body();
                pDialog.dismiss();

                if(response.isSuccessful()){
                    if(resp.getStatus().equals("error")){
                        Toast.makeText(PeternakEditActivity.this, resp.getErrors().toString(), Toast.LENGTH_LONG).show();
                    }
                    else {
                        PeternakModel datas = new PeternakModel(id, peternakan, nama, resp.getPeternaks().getUsername(), resp.getPeternaks().getPassword(), resp.getPeternaks().getCreated_at(), resp.getPeternaks().getUpdated_at());
                        Toast.makeText(PeternakEditActivity.this, "Data berhasil diubah: id " + resp.getPeternaks().getId(), Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(PeternakEditActivity.this, PeternakDetailActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("peternak", (Serializable) datas);
                        intent.putExtra("finish", backFinish);
                        startActivity(intent);

                        PeternakEditActivity.this.finish();
                    }
                }
                else{
                    SweetAlertDialog swal = new SweetAlertDialog(PeternakEditActivity.this, SweetAlertDialog.ERROR_TYPE);
                    swal.setTitleText("Error");
                    swal.setContentText(response.message());
                    swal.show();
                }
            }

            @Override
            public void onFailure(Call<PeternakResponse> call, Throwable t) {
                pDialog.dismiss();
                SweetAlertDialog swal = new SweetAlertDialog(PeternakEditActivity.this, SweetAlertDialog.ERROR_TYPE);
                swal.setTitleText("Error");
                swal.setContentText(t.getMessage());
                swal.show();
            }
        });
    }

}
