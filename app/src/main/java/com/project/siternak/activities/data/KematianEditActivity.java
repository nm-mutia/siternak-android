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
import com.project.siternak.models.data.KematianModel;
import com.project.siternak.responses.KematianResponse;
import com.project.siternak.rest.RetrofitClient;
import com.project.siternak.utils.SharedPrefManager;

import java.io.Serializable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KematianEditActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener{
    @BindView(R.id.til_kematian_id) TextInputLayout tilKematianId;
    @BindView(R.id.tiet_kematian_id) TextInputEditText tietKematianId;
    @BindView(R.id.til_kematian_tgl) TextInputLayout tilKematianTgl;
    @BindView(R.id.tiet_kematian_tgl) TextInputEditText tietKematianTgl;
    @BindView(R.id.til_kematian_waktu) TextInputLayout tilKematianWaktu;
    @BindView(R.id.tiet_kematian_waktu) TextInputEditText tietKematianWaktu;
    @BindView(R.id.til_kematian_penyebab) TextInputLayout tilKematianPenyebab;
    @BindView(R.id.tiet_kematian_penyebab) TextInputEditText tietKematianPenyebab;
    @BindView(R.id.til_kematian_kondisi) TextInputLayout tilKematianKondisi;
    @BindView(R.id.tiet_kematian_kondisi) TextInputEditText tietKematianKondisi;

    private KematianModel kematianData;
    private String userToken;
    public final static int backFinish = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_kematian_edit);

        getSupportActionBar().hide();
        ButterKnife.bind(this);

        kematianData = (KematianModel) getIntent().getSerializableExtra("data");
        userToken = SharedPrefManager.getInstance(this).getAccessToken();

        setData();
    }

    private void setData() {
        tietKematianId.setText(String.valueOf(kematianData.getId()));
        tietKematianTgl.setText(kematianData.getTgl_kematian());
        tietKematianWaktu.setText(kematianData.getWaktu_kematian());
        tietKematianPenyebab.setText(kematianData.getPenyebab());
        tietKematianKondisi.setText(kematianData.getKondisi());
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

        Integer id = Integer.valueOf(tietKematianId.getText().toString());
        String tgl = tietKematianTgl.getText().toString();
        String waktu = tietKematianWaktu.getText().toString();
        String penyebab = tietKematianPenyebab.getText().toString();
        String kondisi = tietKematianKondisi.getText().toString();

        Call<KematianResponse> calle = RetrofitClient
                .getInstance()
                .getApi()
                .editKematian(id, tgl, waktu, penyebab, kondisi, "Bearer " + userToken);

        calle.enqueue(new Callback<KematianResponse>() {
            @Override
            public void onResponse(Call<KematianResponse> call, Response<KematianResponse> response) {
                KematianResponse resp = response.body();
                pDialog.dismiss();

                if(response.isSuccessful()){
                    if(resp.getStatus().equals("error")){
                        Toast.makeText(KematianEditActivity.this, resp.getErrors().toString(), Toast.LENGTH_LONG).show();
                    }
                    else {
                        KematianModel datas = new KematianModel(id, tgl, waktu, penyebab, kondisi, resp.getKematians().getCreated_at(), resp.getKematians().getUpdated_at());
                        Toast.makeText(KematianEditActivity.this, "Data berhasil diubah: id " + resp.getKematians().getId(), Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(KematianEditActivity.this, KematianDetailActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("kematian", (Serializable) datas);
                        intent.putExtra("finish", backFinish);
                        startActivity(intent);

                        KematianEditActivity.this.finish();
                    }
                }
                else{
                    SweetAlertDialog swal = new SweetAlertDialog(KematianEditActivity.this, SweetAlertDialog.ERROR_TYPE);
                    swal.setTitleText("Error");
                    swal.setContentText(response.message());
                    swal.show();
                }
            }

            @Override
            public void onFailure(Call<KematianResponse> call, Throwable t) {
                pDialog.dismiss();
                SweetAlertDialog swal = new SweetAlertDialog(KematianEditActivity.this, SweetAlertDialog.ERROR_TYPE);
                swal.setTitleText("Error");
                swal.setContentText(t.getMessage());
                swal.show();
            }
        });
    }

    @OnClick(R.id.tiet_kematian_tgl)
    public void setDate(){
        String[] getDate = kematianData.getTgl_kematian().split("-");

        DialogFragment datePicker = new DatePickerFragment(Integer.valueOf(getDate[0]), Integer.valueOf(getDate[1])-1, Integer.valueOf(getDate[2]));
        datePicker.show(getSupportFragmentManager(), "date picker");
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        String dateStr = i+"-"+(i1+1)+"-"+i2;
        tietKematianTgl.setText(dateStr);
    }

    @OnClick(R.id.tiet_kematian_waktu)
    public void setTime(){
        String[] getTime = kematianData.getWaktu_kematian().split(":");

        DialogFragment timePicker = new TimePickerFrament(Integer.valueOf(getTime[0]), Integer.valueOf(getTime[1]));
        timePicker.show(getSupportFragmentManager(), "time picker");
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {
        String timeStr = i+":"+i1+":00";
        tietKematianWaktu.setText(timeStr);
    }
}
