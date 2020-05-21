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
import com.project.siternak.responses.PeternakResponse;
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

public class PeternakAddActivity extends AppCompatActivity {
    @BindView(R.id.til_peternak_peternakan) TextInputLayout tilPeternakan;
    @BindView(R.id.tiet_peternak_peternakan) TextInputEditText tietPeternakan;
    @BindView(R.id.til_peternak_nama) TextInputLayout tilNama;
    @BindView(R.id.tiet_peternak_nama) TextInputEditText tietNama;
    @BindView(R.id.til_peternak_username) TextInputLayout tilUsername;
    @BindView(R.id.tiet_peternak_username) TextInputEditText tietUsername;
    @BindView(R.id.til_peternak_email) TextInputLayout tilEmail;
    @BindView(R.id.tiet_peternak_email) TextInputEditText tietEmail;

    private static final int REQUEST_CODE_SETPETERNAKAN = 1;
    private String userToken;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peternak_add);

        getSupportActionBar().hide();
        ButterKnife.bind(this);

        userToken = SharedPrefManager.getInstance(this).getAccessToken();
    }

    @OnClick(R.id.tiet_peternak_peternakan)
    public void setPeternakan(){
        Intent intent = new Intent(PeternakAddActivity.this, PeternakanOptionActivity.class);
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

    public boolean validateUsername(){
        String p = tilUsername.getEditText().getText().toString().trim();
        if(p.isEmpty()){
            tilUsername.setError("Wajib diisi");
            return false;
        }
        else {
            tilUsername.setError(null);
            tilUsername.setErrorEnabled(false);
            return true;
        }
    }

    public boolean validateEmail(){
        String p = tilEmail.getEditText().getText().toString().trim();
        if(p.isEmpty()){
            tilEmail.setError("Wajib diisi");
            return false;
        }
        else {
            tilEmail.setError(null);
            tilEmail.setErrorEnabled(false);
            return true;
        }
    }

    @OnClick(R.id.tv_submit)
    public void action_add() {
        if (!validatePeternakan() | !validateNama() | !validateUsername() | !validateEmail()) {
            return;
        }

        SweetAlertDialog pDialog = DialogUtils.getLoadingPopup(this);

        Integer peternakan = Integer.valueOf(tietPeternakan.getText().toString());
        String nama = tietNama.getText().toString();
        String username = tietUsername.getText().toString();
        String email = tietEmail.getText().toString();

        Call<PeternakResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .addPeternak(peternakan, nama, username, email, "Bearer " + userToken);

        call.enqueue(new Callback<PeternakResponse>() {
            @Override
            public void onResponse(Call<PeternakResponse> call, Response<PeternakResponse> response) {
                PeternakResponse resp = response.body();
                pDialog.dismiss();

                if(response.isSuccessful()){
                    if(resp.getStatus().equals("error")){
                        Toast.makeText(PeternakAddActivity.this, resp.getErrors().toString(), Toast.LENGTH_LONG).show();
                    }
                    else{
                        Toast.makeText(PeternakAddActivity.this, "Data berhasil dibuat: id " + resp.getPeternaks().getId(), Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(PeternakAddActivity.this, PeternakActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);

                        PeternakAddActivity.this.finish();
                    }
                }
                else{
                    SweetAlertDialog swal = new SweetAlertDialog(PeternakAddActivity.this, SweetAlertDialog.ERROR_TYPE);
                    swal.setTitleText("Error");
                    swal.setContentText(response.message());
                    swal.show();
                }
            }

            @Override
            public void onFailure(Call<PeternakResponse> call, Throwable t) {
                pDialog.dismiss();
                SweetAlertDialog swal = new SweetAlertDialog(PeternakAddActivity.this, SweetAlertDialog.ERROR_TYPE);
                swal.setTitleText("Error");
                swal.setContentText(t.getMessage());
                swal.show();
            }
        });
    }
}
