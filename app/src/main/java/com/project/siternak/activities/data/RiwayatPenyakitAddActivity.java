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
import com.project.siternak.activities.option.PenyakitOptionActivity;
import com.project.siternak.activities.option.TernakRiwayatOptionActivity;
import com.project.siternak.fragments.DatePickerFragment;
import com.project.siternak.models.data.PenyakitModel;
import com.project.siternak.models.data.TernakModel;
import com.project.siternak.responses.RiwayatPenyakitResponse;
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

public class RiwayatPenyakitAddActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{
    @BindView(R.id.til_riwayat_penyakit) TextInputLayout tilRiwayatPenyakit;
    @BindView(R.id.tiet_riwayat_penyakit) TextInputEditText tietRiwayatPenyakit;
    @BindView(R.id.til_riwayat_necktag) TextInputLayout tilRiwayatNecktag;
    @BindView(R.id.tiet_riwayat_necktag) TextInputEditText tietRiwayatNecktag;
    @BindView(R.id.til_riwayat_tgl) TextInputLayout tilRiwayatTgl;
    @BindView(R.id.tiet_riwayat_tgl) TextInputEditText tietRiwayatTgl;
    @BindView(R.id.til_riwayat_obat) TextInputLayout tilRiwayatObat;
    @BindView(R.id.tiet_riwayat_obat) TextInputEditText tietRiwayatObat;
    @BindView(R.id.til_riwayat_lama_sakit) TextInputLayout tilRiwayatLamaSakit;
    @BindView(R.id.tiet_riwayat_lama_sakit) TextInputEditText tietRiwayatLamaSakit;
    @BindView(R.id.til_riwayat_keterangan) TextInputLayout tilRiwayatKet;
    @BindView(R.id.tiet_riwayat_keterangan) TextInputEditText tietRiwayatKet;

    private static final int REQUEST_CODE_SETPENYAKIT = 1;
    private static final int REQUEST_CODE_SETNECKTAG = 2;

    private String userToken;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_riwayat_add);

        getSupportActionBar().hide();
        ButterKnife.bind(this);

        userToken = SharedPrefManager.getInstance(this).getAccessToken();
    }

    @OnClick(R.id.tiet_riwayat_penyakit)
    public void setPenyakit(){
        Intent intent = new Intent(RiwayatPenyakitAddActivity.this, PenyakitOptionActivity.class);
        intent.putExtra("penyakit", REQUEST_CODE_SETPENYAKIT);
        startActivityForResult(intent, REQUEST_CODE_SETPENYAKIT);
    }

    @OnClick(R.id.tiet_riwayat_necktag)
    public void setNecktag(){
        Intent intent = new Intent(RiwayatPenyakitAddActivity.this, TernakRiwayatOptionActivity.class);
        intent.putExtra("necktag", REQUEST_CODE_SETNECKTAG);
        startActivityForResult(intent, REQUEST_CODE_SETNECKTAG);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK) {
            if(requestCode == REQUEST_CODE_SETPENYAKIT){
                PenyakitModel result = (PenyakitModel) data.getSerializableExtra("penyakit");
                tietRiwayatPenyakit.setText(String.valueOf(result.getId()));
            }
            else if(requestCode == REQUEST_CODE_SETNECKTAG){
                TernakModel result = (TernakModel) data.getSerializableExtra("necktag");
                tietRiwayatNecktag.setText(String.valueOf(result.getNecktag()));}
        }
    }

    public boolean validatePenyakit(){
        String p = tilRiwayatPenyakit.getEditText().getText().toString().trim();
        if(p.isEmpty()){
            tilRiwayatPenyakit.setError("Wajib diisi");
            return false;
        }
        else {
            tilRiwayatPenyakit.setError(null);
            tilRiwayatPenyakit.setErrorEnabled(false);
            return true;
        }
    }

    public boolean validateNecktag(){
        String p = tilRiwayatNecktag.getEditText().getText().toString().trim();
        if(p.isEmpty()){
            tilRiwayatNecktag.setError("Wajib diisi");
            return false;
        }
        else {
            tilRiwayatNecktag.setError(null);
            tilRiwayatNecktag.setErrorEnabled(false);
            return true;
        }
    }

    public boolean validateTgl(){
        String p = tilRiwayatTgl.getEditText().getText().toString().trim();
        if(p.isEmpty()){
            tilRiwayatTgl.setError("Wajib diisi");
            return false;
        }
        else {
            tilRiwayatTgl.setError(null);
            tilRiwayatTgl.setErrorEnabled(false);
            return true;
        }
    }

    @OnClick(R.id.tv_submit)
    public void action_add() {
        if (!validateTgl() | !validateNecktag() | !validatePenyakit()) {
            return;
        }

        SweetAlertDialog pDialog = DialogUtils.getLoadingPopup(this);

        Integer penyakit = Integer.valueOf(tietRiwayatPenyakit.getText().toString());
        String necktag = tietRiwayatNecktag.getText().toString();
        String tgl = tietRiwayatTgl.getText().toString();
        String obat = tietRiwayatObat.getText().toString();
        String ket = tietRiwayatKet.getText().toString();

        Integer lama;
        if(tietRiwayatLamaSakit.getText().toString().equals("")){
            lama = null;
        }else{
            lama = Integer.valueOf(tietRiwayatLamaSakit.getText().toString());
        }

        Call<RiwayatPenyakitResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .addRiwayat(penyakit, necktag, tgl, obat, lama, ket, "Bearer " + userToken);

        call.enqueue(new Callback<RiwayatPenyakitResponse>() {
            @Override
            public void onResponse(Call<RiwayatPenyakitResponse> call, Response<RiwayatPenyakitResponse> response) {
                RiwayatPenyakitResponse resp = response.body();
                pDialog.dismiss();

                if(response.isSuccessful()){
                    if(resp.getStatus().equals("error")){
                        Toast.makeText(RiwayatPenyakitAddActivity.this, resp.getErrors().toString(), Toast.LENGTH_LONG).show();
                    }
                    else {
                        Toast.makeText(RiwayatPenyakitAddActivity.this, "Data berhasil dibuat: id " + resp.getRiwayats().getId(), Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(RiwayatPenyakitAddActivity.this, RiwayatPenyakitActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);

                        RiwayatPenyakitAddActivity.this.finish();
                    }
                }
                else{
                    SweetAlertDialog swal = new SweetAlertDialog(RiwayatPenyakitAddActivity.this, SweetAlertDialog.ERROR_TYPE);
                    swal.setTitleText("Error");
                    swal.setContentText(response.message());
                    swal.show();
                }
            }

            @Override
            public void onFailure(Call<RiwayatPenyakitResponse> call, Throwable t) {
                pDialog.dismiss();
                SweetAlertDialog swal = new SweetAlertDialog(RiwayatPenyakitAddActivity.this, SweetAlertDialog.ERROR_TYPE);
                swal.setTitleText("Error");
                swal.setContentText(t.getMessage());
                swal.show();
            }
        });
    }

    @OnClick(R.id.tiet_riwayat_tgl)
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
        tietRiwayatTgl.setText(dateStr);
    }
}
