package com.project.siternak.activities.data;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.project.siternak.R;
import com.project.siternak.fragments.DatePickerFragment;
import com.project.siternak.fragments.TimePickerFrament;
import com.project.siternak.responses.KematianResponse;
import com.project.siternak.rest.RetrofitClient;
import com.project.siternak.utils.SharedPrefManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TambahKematianActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    @BindView(R.id.til_kematian_tgl) TextInputLayout tilKematianTgl;
    @BindView(R.id.tiet_kematian_tgl) TextInputEditText tietKematianTgl;
    @BindView(R.id.til_kematian_waktu) TextInputLayout tilKematianWaktu;
    @BindView(R.id.tiet_kematian_waktu) TextInputEditText tietKematianWaktu;
    @BindView(R.id.til_kematian_penyebab) TextInputLayout tilKematianPenyebab;
    @BindView(R.id.til_kematian_kondisi) TextInputLayout tilKematianKondisi;

    private String userToken;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_data_kematian);

        getSupportActionBar().hide();
        ButterKnife.bind(this);

        userToken = SharedPrefManager.getInstance(this).getAccessToken();
    }

    public boolean validateTgl(){
        String p = tilKematianTgl.getEditText().getText().toString().trim();
        if(p.isEmpty()){
            tilKematianTgl.setError("Wajib diisi");
            return false;
        }
        else {
            tilKematianTgl.setError(null);
            tilKematianTgl.setErrorEnabled(false);
            return true;
        }
    }

    public boolean validateWaktu(){
        String p = tilKematianWaktu.getEditText().getText().toString().trim();
        if(p.isEmpty()){
            tilKematianWaktu.setError("Wajib diisi");
            return false;
        }
        else {
            tilKematianWaktu.setError(null);
            tilKematianWaktu.setErrorEnabled(false);
            return true;
        }
    }

    public boolean validatePenyebab(){
        String p = tilKematianPenyebab.getEditText().getText().toString().trim();
        if(p.isEmpty()){
            tilKematianPenyebab.setError("Wajib diisi");
            return false;
        }
        else {
            tilKematianPenyebab.setError(null);
            tilKematianPenyebab.setErrorEnabled(false);
            return true;
        }
    }

    public boolean validateKondisi(){
        String k = tilKematianKondisi.getEditText().getText().toString().trim();
        if(k.isEmpty()){
            tilKematianKondisi.setError("Wajib diisi");
            return false;
        }
        else {
            tilKematianKondisi.setError(null);
            tilKematianKondisi.setErrorEnabled(false);
            return true;
        }
    }

    @OnClick(R.id.tv_submit)
    public void action_add(){
        if(!validateTgl() | !validateWaktu() | !validatePenyebab() | !validateKondisi()){
            return;
        }

        SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Mohon Tunggu");
        pDialog.setCancelable(false);
        pDialog.show();

        String tgl = tilKematianTgl.getEditText().getText().toString();
        String waktu = tilKematianWaktu.getEditText().getText().toString();
        String penyebab = tilKematianPenyebab.getEditText().getText().toString();
        String kondisi = tilKematianKondisi.getEditText().getText().toString();

        Call<KematianResponse> calld = RetrofitClient
                .getInstance()
                .getApi()
                .addKematian(tgl, waktu, penyebab, kondisi, "Bearer " + userToken);

        calld.enqueue(new Callback<KematianResponse>() {
            @Override
            public void onResponse(Call<KematianResponse> call, Response<KematianResponse> response) {
                KematianResponse resp = response.body();

                if(response.isSuccessful()){
                    pDialog.dismiss();
                    Toast.makeText(TambahKematianActivity.this, "Data berhasil dibuat, id " + resp.getKematians().getId(), Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(TambahKematianActivity.this, KematianActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                    TambahKematianActivity.this.finish();
                }
                else{
                    pDialog.dismiss();
                    SweetAlertDialog swal = new SweetAlertDialog(TambahKematianActivity.this, SweetAlertDialog.ERROR_TYPE);
                    swal.setTitleText("Error");
                    swal.setContentText(response.message());
                    swal.show();
                }
            }

            @Override
            public void onFailure(Call<KematianResponse> call, Throwable t) {
                pDialog.dismiss();
                SweetAlertDialog swal = new SweetAlertDialog(TambahKematianActivity.this, SweetAlertDialog.ERROR_TYPE);
                swal.setTitleText("Error");
                swal.setContentText(t.getMessage());
                swal.show();
            }
        });
    }

    @OnClick(R.id.tiet_kematian_tgl)
    public void setDate(){
        DialogFragment datePicker = new DatePickerFragment();
        datePicker.show(getSupportFragmentManager(), "date picker");
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        String dateStr = i+"-"+i1+"-"+i2;
        tietKematianTgl.setText(dateStr);
    }

    @OnClick(R.id.tiet_kematian_waktu)
    public void setTime(){
        DialogFragment timePicker = new TimePickerFrament();
        timePicker.show(getSupportFragmentManager(), "time picker");
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {
        String timeStr = i+":"+i1+":00";
        tietKematianWaktu.setText(timeStr);
    }
}
