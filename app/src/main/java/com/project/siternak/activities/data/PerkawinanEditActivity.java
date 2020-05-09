package com.project.siternak.activities.data;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.project.siternak.R;
import com.project.siternak.activities.option.TernakPerkawinanOptionActivity;
import com.project.siternak.fragments.DatePickerFragment;
import com.project.siternak.models.data.PerkawinanModel;
import com.project.siternak.models.data.TernakModel;
import com.project.siternak.responses.PerkawinanResponse;
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

public class PerkawinanEditActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{
    @BindView(R.id.til_perkawinan_id) TextInputLayout tilPerkawinanId;
    @BindView(R.id.tiet_perkawinan_id) TextInputEditText tietPerkawinanId;
    @BindView(R.id.til_perkawinan_tgl)
    TextInputLayout tilPerkawinanTgl;
    @BindView(R.id.tiet_perkawinan_tgl)
    TextInputEditText tietPerkawinanTgl;
    @BindView(R.id.til_perkawinan_necktag) TextInputLayout tilPerkawinanNecktag;
    @BindView(R.id.tiet_perkawinan_necktag) TextInputEditText tietPerkawinanNecktag;
    @BindView(R.id.til_perkawinan_necktag_psg) TextInputLayout tilPerkawinanNecktagPsg;
    @BindView(R.id.tiet_perkawinan_necktag_psg) TextInputEditText tietPerkawinanNecktagPsg;

    private static final int REQUEST_CODE_SETNECKTAG = 1;
    private static final int REQUEST_CODE_SETNECKTAG_PSG = 2;

    private PerkawinanModel perkawinanData;
    private String userToken;
    public final static int backFinish = 1;
    private TernakModel n1, n2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_perkawinan_edit);

        getSupportActionBar().hide();
        ButterKnife.bind(this);

        perkawinanData = (PerkawinanModel) getIntent().getSerializableExtra("data");
        userToken = SharedPrefManager.getInstance(this).getAccessToken();

        setData();
    }

    private void setData() {
        tietPerkawinanId.setText(String.valueOf(perkawinanData.getId()));
        tietPerkawinanNecktag.setText(perkawinanData.getNecktag());
        tietPerkawinanNecktagPsg.setText(perkawinanData.getNecktag_psg());
        tietPerkawinanTgl.setText(perkawinanData.getTgl());
    }

    @OnClick(R.id.tiet_perkawinan_necktag)
    public void setNecktag(){
        Intent intent = new Intent(PerkawinanEditActivity.this, TernakPerkawinanOptionActivity.class);
        intent.putExtra("kawin", REQUEST_CODE_SETNECKTAG);
        startActivityForResult(intent, REQUEST_CODE_SETNECKTAG);
    }

    @OnClick(R.id.tiet_perkawinan_necktag_psg)
    public void setNecktagPsg(){
        if(tietPerkawinanNecktag.getText().toString().length()==0){
            Toast.makeText(getApplicationContext(), "Anda belum memilih Necktag",Toast.LENGTH_LONG).show();
        }
        else{
            Intent intent = new Intent(PerkawinanEditActivity.this, TernakPerkawinanOptionActivity.class);
            intent.putExtra("kawin", REQUEST_CODE_SETNECKTAG_PSG);
            intent.putExtra("necktag", n1);
            startActivityForResult(intent, REQUEST_CODE_SETNECKTAG_PSG);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK) {
            if(requestCode == REQUEST_CODE_SETNECKTAG){
                TernakModel result = (TernakModel) data.getSerializableExtra("necktag");
                tietPerkawinanNecktag.setText(String.valueOf(result.getNecktag()));
                tietPerkawinanNecktagPsg.setText(null);
                n1 = new TernakModel(result.getNecktag(), result.getRasId(), result.getJenisKelamin(), result.getTglLahir(), result.getBlood(), result.getNecktag_ayah(), result.getNecktag_ibu(), result.getStatusAda());
                n2 = null;
            }
            else if(requestCode == REQUEST_CODE_SETNECKTAG_PSG){
                TernakModel result = (TernakModel) data.getSerializableExtra("necktag");
                tietPerkawinanNecktagPsg.setText(String.valueOf(result.getNecktag()));
                n2 = new TernakModel(result.getNecktag(), result.getRasId(), result.getJenisKelamin(), result.getTglLahir(), result.getBlood(), result.getNecktag_ayah(), result.getNecktag_ibu(), result.getStatusAda());
            }
        }
    }

    public boolean validateTgl(){
        String p = tilPerkawinanTgl.getEditText().getText().toString().trim();
        if(p.isEmpty()){
            tilPerkawinanTgl.setError("Wajib diisi");
            return false;
        }
        else {
            tilPerkawinanTgl.setError(null);
            tilPerkawinanTgl.setErrorEnabled(false);
            return true;
        }
    }

    public boolean validateNecktag(){
        String p = tilPerkawinanNecktag.getEditText().getText().toString().trim();
        if(p.isEmpty() && n1 == null){
            tilPerkawinanNecktag.setError("Wajib diisi");
            return false;
        }
        else {
            tilPerkawinanNecktag.setError(null);
            tilPerkawinanNecktag.setErrorEnabled(false);
            return true;
        }
    }

    public boolean validateNecktagPsg(){
        String k = tilPerkawinanNecktagPsg.getEditText().getText().toString().trim();
        if(k.isEmpty() && n2 == null){
            tilPerkawinanNecktagPsg.setError("Wajib diisi");
            return false;
        }
        else {
            tilPerkawinanNecktagPsg.setError(null);
            tilPerkawinanNecktagPsg.setErrorEnabled(false);
            return true;
        }
    }

    @OnClick(R.id.tv_submit)
    public void action_add() {
        if (!validateTgl() | !validateNecktag() | !validateNecktagPsg()) {
            return;
        }

        SweetAlertDialog pDialog = DialogUtils.getLoadingPopup(this);

        Integer id = Integer.valueOf(tietPerkawinanId.getText().toString());
        String tgl = tietPerkawinanTgl.getText().toString();
        String necktag = tietPerkawinanNecktag.getText().toString();
        String necktag_psg = tietPerkawinanNecktagPsg.getText().toString();

        Call<PerkawinanResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .editPerkawinan(id, necktag, necktag_psg, tgl, "Bearer " + userToken);

        call.enqueue(new Callback<PerkawinanResponse>() {
            @Override
            public void onResponse(Call<PerkawinanResponse> call, Response<PerkawinanResponse> response) {
                PerkawinanResponse resp = response.body();
                pDialog.dismiss();

                if(response.isSuccessful()){
                    if(resp.getStatus().equals("error")){
                        Toast.makeText(PerkawinanEditActivity.this, resp.getErrors().toString(), Toast.LENGTH_LONG).show();
                    }
                    else {
                        PerkawinanModel datas = new PerkawinanModel(id, necktag, necktag_psg, tgl, resp.getPerkawinans().getCreated_at(), resp.getPerkawinans().getUpdated_at());
                        Toast.makeText(PerkawinanEditActivity.this, "Data berhasil diubah: id " + resp.getPerkawinans().getId(), Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(PerkawinanEditActivity.this, PerkawinanDetailActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("perkawinan", (Serializable) datas);
                        intent.putExtra("finish", backFinish);
                        startActivity(intent);

                        PerkawinanEditActivity.this.finish();
                    }
                }
                else{
                    SweetAlertDialog swal = new SweetAlertDialog(PerkawinanEditActivity.this, SweetAlertDialog.ERROR_TYPE);
                    swal.setTitleText("Error");
                    swal.setContentText(response.message());
                    swal.show();
                }
            }

            @Override
            public void onFailure(Call<PerkawinanResponse> call, Throwable t) {
                pDialog.dismiss();
                SweetAlertDialog swal = new SweetAlertDialog(PerkawinanEditActivity.this, SweetAlertDialog.ERROR_TYPE);
                swal.setTitleText("Error");
                swal.setContentText(t.getMessage());
                swal.show();
            }
        });
    }

    @OnClick(R.id.tiet_perkawinan_tgl)
    public void setDate(){
        String[] getDate = perkawinanData.getTgl().split("-");

        DialogFragment datePicker = new DatePickerFragment(Integer.valueOf(getDate[0]), Integer.valueOf(getDate[1])-1, Integer.valueOf(getDate[2]));
        datePicker.show(getSupportFragmentManager(), "date picker");
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        String dateStr = i+"-"+(i1+1)+"-"+i2;
        tietPerkawinanTgl.setText(dateStr);
    }

}
