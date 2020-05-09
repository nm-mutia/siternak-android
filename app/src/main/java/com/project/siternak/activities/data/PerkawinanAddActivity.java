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
import com.project.siternak.models.data.TernakModel;
import com.project.siternak.responses.PerkawinanResponse;
import com.project.siternak.rest.RetrofitClient;
import com.project.siternak.utils.DialogUtils;
import com.project.siternak.utils.SharedPrefManager;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PerkawinanAddActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{
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

    private String userToken;
    private TernakModel n1, n2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_perkawinan_add);

        getSupportActionBar().hide();
        ButterKnife.bind(this);

        userToken = SharedPrefManager.getInstance(this).getAccessToken();
    }

    @OnClick(R.id.tiet_perkawinan_necktag)
    public void setNecktag(){
        Intent intent = new Intent(PerkawinanAddActivity.this, TernakPerkawinanOptionActivity.class);
        intent.putExtra("kawin", REQUEST_CODE_SETNECKTAG);
        startActivityForResult(intent, REQUEST_CODE_SETNECKTAG);
    }

    @OnClick(R.id.tiet_perkawinan_necktag_psg)
    public void setNecktagPsg(){
        if(tietPerkawinanNecktag.getText().toString().length()==0){
            Toast.makeText(getApplicationContext(), "Anda belum memilih Necktag",Toast.LENGTH_LONG).show();
        }
        else{
            Intent intent = new Intent(PerkawinanAddActivity.this, TernakPerkawinanOptionActivity.class);
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
        if(p.isEmpty() || n1 == null){
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
        if(k.isEmpty() || n2 == null){
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

        String tgl = tilPerkawinanTgl.getEditText().getText().toString();
        String necktag = tilPerkawinanNecktag.getEditText().getText().toString();
        String necktag_psg = tilPerkawinanNecktagPsg.getEditText().getText().toString();

        Call<PerkawinanResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .addPerkawinan(necktag, necktag_psg, tgl, "Bearer " + userToken);

        call.enqueue(new Callback<PerkawinanResponse>() {
            @Override
            public void onResponse(Call<PerkawinanResponse> call, Response<PerkawinanResponse> response) {
                PerkawinanResponse resp = response.body();
                pDialog.dismiss();

                if(response.isSuccessful()){
                    if(resp.getStatus().equals("error")){
                        Toast.makeText(PerkawinanAddActivity.this, resp.getErrors().toString(), Toast.LENGTH_LONG).show();
                    }
                    else {
                        Toast.makeText(PerkawinanAddActivity.this, "Data berhasil dibuat: id " + resp.getPerkawinans().getId(), Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(PerkawinanAddActivity.this, PerkawinanActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);

                        PerkawinanAddActivity.this.finish();
                    }
                }
                else{
                    SweetAlertDialog swal = new SweetAlertDialog(PerkawinanAddActivity.this, SweetAlertDialog.ERROR_TYPE);
                    swal.setTitleText("Error");
                    swal.setContentText(response.message());
                    swal.show();
                }
            }

            @Override
            public void onFailure(Call<PerkawinanResponse> call, Throwable t) {
                pDialog.dismiss();
                SweetAlertDialog swal = new SweetAlertDialog(PerkawinanAddActivity.this, SweetAlertDialog.ERROR_TYPE);
                swal.setTitleText("Error");
                swal.setContentText(t.getMessage());
                swal.show();
            }
        });
    }

    @OnClick(R.id.tiet_perkawinan_tgl)
    public void setDate(){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DialogFragment datePicker = new DatePickerFragment(year, month, day);
        datePicker.show(getSupportFragmentManager(), "date picker");
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        String dateStr = i+"-"+(i1+1)+"-"+i2;
        tietPerkawinanTgl.setText(dateStr);
    }
}
