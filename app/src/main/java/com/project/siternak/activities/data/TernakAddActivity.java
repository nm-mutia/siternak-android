package com.project.siternak.activities.data;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.project.siternak.R;
import com.project.siternak.activities.option.KematianOptionActivity;
import com.project.siternak.activities.option.TernakParentOptionActivity;
import com.project.siternak.activities.option.PemilikOptionActivity;
import com.project.siternak.activities.option.PeternakanOptionActivity;
import com.project.siternak.activities.option.RasOptionActivity;
import com.project.siternak.fragments.DatePickerFragment;
import com.project.siternak.fragments.TimePickerFrament;
import com.project.siternak.models.data.KematianModel;
import com.project.siternak.models.data.PemilikModel;
import com.project.siternak.models.data.PeternakanModel;
import com.project.siternak.models.data.RasModel;
import com.project.siternak.models.data.TernakModel;
import com.project.siternak.responses.TernakResponse;
import com.project.siternak.rest.RetrofitClient;
import com.project.siternak.utils.DialogUtils;
import com.project.siternak.utils.NetworkManager;
import com.project.siternak.utils.SharedPrefManager;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TernakAddActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener{
    @BindView(R.id.til_ternak_pemilik) TextInputLayout tilPemilik;
    @BindView(R.id.tiet_ternak_pemilik) TextInputEditText tietPemilik;
    @BindView(R.id.til_ternak_peternakan) TextInputLayout tilPeternakan;
    @BindView(R.id.tiet_ternak_peternakan) TextInputEditText tietPeternakan;
    @BindView(R.id.til_ternak_ras) TextInputLayout tilRas;
    @BindView(R.id.tiet_ternak_ras) TextInputEditText tietRas;
    @BindView(R.id.til_ternak_kematian) TextInputLayout tilKematian;
    @BindView(R.id.tiet_ternak_kematian) TextInputEditText tietKematian;
    @BindView(R.id.til_ternak_tgl_lahir) TextInputLayout tilTglLahir;
    @BindView(R.id.til_ternak_bobot_lahir) TextInputLayout tilBobotLahir;
    @BindView(R.id.til_ternak_pukul_lahir) TextInputLayout tilPukulLahir;
    @BindView(R.id.til_ternak_lama_dikandungan) TextInputLayout tilLamaDikandungan;
    @BindView(R.id.til_ternak_lama_laktasi) TextInputLayout tilLamaLaktasi;
    @BindView(R.id.til_ternak_tgl_lepas_sapih) TextInputLayout tilTglLepasSapih;
    @BindView(R.id.til_ternak_blood) TextInputLayout tilBlood;
    @BindView(R.id.til_ternak_ayah) TextInputLayout tilAyah;
    @BindView(R.id.tiet_ternak_ayah) TextInputEditText tietAyah;
    @BindView(R.id.til_ternak_ibu) TextInputLayout tilIbu;
    @BindView(R.id.tiet_ternak_ibu) TextInputEditText tietIbu;
    @BindView(R.id.til_ternak_bobot_tubuh) TextInputLayout tilBobotTubuh;
    @BindView(R.id.til_ternak_panjang_tubuh) TextInputLayout tilPanjangTubuh;
    @BindView(R.id.til_ternak_tinggi_tubuh) TextInputLayout tilTinggiTubuh;
    @BindView(R.id.til_ternak_cacat_fisik) TextInputLayout tilCacatFisik;
    @BindView(R.id.til_ternak_ciri_lain) TextInputLayout tilCiriLain;
    @BindView(R.id.s_ternak_jk) Spinner sJk;
    @BindView(R.id.s_ternak_status_ada) Spinner sStatusAda;

    private static final int REQUEST_CODE_SETPEMILIK = 1;
    private static final int REQUEST_CODE_SETPETERNAKAN = 2;
    private static final int REQUEST_CODE_SETRAS = 3;
    private static final int REQUEST_CODE_SETKEMATIAN = 4;
    private static final int REQUEST_CODE_SETAYAH = 5;
    private static final int REQUEST_CODE_SETIBU = 6;

    private String userToken;
    private int clickedTgl;

    private Integer pemilik, ras, kematian;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_ternak_add);

        getSupportActionBar().hide();
        ButterKnife.bind(this);

        userToken = SharedPrefManager.getInstance(this).getAccessToken();
        setSpinner();
    }

    private void setSpinner() {
        ArrayAdapter<CharSequence> jkAdapter = ArrayAdapter.createFromResource(this, R.array.jenis_kelamin, android.R.layout.simple_spinner_item);
        jkAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sJk.setAdapter(jkAdapter);

        ArrayAdapter<CharSequence> statusAdapter = ArrayAdapter.createFromResource(this, R.array.status_ada, android.R.layout.simple_spinner_item);
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sStatusAda.setAdapter(statusAdapter);
    }

    @OnClick(R.id.tiet_ternak_pemilik)
    public void setIdPemilik(){
        Intent intent = new Intent(TernakAddActivity.this, PemilikOptionActivity.class);
        startActivityForResult(intent, REQUEST_CODE_SETPEMILIK);
    }

    @OnClick(R.id.tiet_ternak_peternakan)
    public void setIdPeternakan(){
        Intent intent = new Intent(TernakAddActivity.this, PeternakanOptionActivity.class);
        startActivityForResult(intent, REQUEST_CODE_SETPETERNAKAN);
    }

    @OnClick(R.id.tiet_ternak_ras)
    public void setIdRas(){
        Intent intent = new Intent(TernakAddActivity.this, RasOptionActivity.class);
        startActivityForResult(intent, REQUEST_CODE_SETRAS);
    }

    @OnClick(R.id.tiet_ternak_kematian)
    public void setIdKematian(){
        Intent intent = new Intent(TernakAddActivity.this, KematianOptionActivity.class);
        startActivityForResult(intent, REQUEST_CODE_SETKEMATIAN);
    }

    @OnClick(R.id.tiet_ternak_ayah)
    public void setAyah(){
        Intent intent = new Intent(TernakAddActivity.this, TernakParentOptionActivity.class);
        intent.putExtra("parent", REQUEST_CODE_SETAYAH);
        startActivityForResult(intent, REQUEST_CODE_SETAYAH);
    }

    @OnClick(R.id.tiet_ternak_ibu)
    public void setIbu(){
        Intent intent = new Intent(TernakAddActivity.this, TernakParentOptionActivity.class);
        intent.putExtra("parent", REQUEST_CODE_SETIBU);
        startActivityForResult(intent, REQUEST_CODE_SETIBU);
    }


    @OnClick(R.id.tiet_ternak_tgl_lahir)
    public void setDate1(){
        clickedTgl = 1;
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DialogFragment datePicker = new DatePickerFragment(year, month, day);
        datePicker.show(getSupportFragmentManager(), "date picker");
    }

    @OnClick(R.id.tiet_ternak_tgl_lepas_sapih)
    public void setDate2(){
        clickedTgl = 2;
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
        if(clickedTgl == 1){
            tilTglLahir.getEditText().setText(dateStr);
        }
        else if(clickedTgl == 2){
            tilTglLepasSapih.getEditText().setText(dateStr);
        }
    }

    @OnClick(R.id.tiet_ternak_pukul_lahir)
    public void setTime(){
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        DialogFragment timePicker = new TimePickerFrament(hour, minute);
        timePicker.show(getSupportFragmentManager(), "time picker");
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {
        String timeStr = i+":"+i1+":00";
        tilPukulLahir.getEditText().setText(timeStr);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK) {
            if(requestCode == REQUEST_CODE_SETPEMILIK){
                PemilikModel result = (PemilikModel) data.getSerializableExtra("pemilik");
                tietPemilik.setText(String.valueOf(result.getId()));
            }
            else if(requestCode == REQUEST_CODE_SETPETERNAKAN){
                PeternakanModel result = (PeternakanModel) data.getSerializableExtra("peternakan");
                tietPeternakan.setText(String.valueOf(result.getId()));
            }
            else if(requestCode == REQUEST_CODE_SETRAS){
                RasModel result = (RasModel) data.getSerializableExtra("ras");
                tietRas.setText(String.valueOf(result.getId()));
            }
            else if(requestCode == REQUEST_CODE_SETKEMATIAN){
                KematianModel result = (KematianModel) data.getSerializableExtra("kematian");
                tietKematian.setText(String.valueOf(result.getId()));
            }
            else if(requestCode == REQUEST_CODE_SETAYAH){
                TernakModel result = (TernakModel) data.getSerializableExtra("necktag");
                tietAyah.setText(result.getNecktag());
            }
            else if(requestCode == REQUEST_CODE_SETIBU){
                TernakModel result = (TernakModel) data.getSerializableExtra("necktag");
                tietIbu.setText(result.getNecktag());
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

    public boolean validateRas(){
        String p = tilRas.getEditText().getText().toString().trim();
        if(p.isEmpty()){
            tilRas.setError("Wajib diisi");
            return false;
        }
        else {
            tilRas.setError(null);
            tilRas.setErrorEnabled(false);
            return true;
        }
    }

    public boolean validateTglLahir(){
        String p = tilTglLahir.getEditText().getText().toString().trim();
        if(p.isEmpty()){
            tilTglLahir.setError("Wajib diisi");
            return false;
        }
        else {
            tilTglLahir.setError(null);
            tilTglLahir.setErrorEnabled(false);
            return true;
        }
    }

    public boolean validateJk(){
        String p = sJk.getSelectedItem().toString().trim();
        if(p.isEmpty()){
            Toast.makeText(TernakAddActivity.this, "Jenis Kelamin - wajib diisi", Toast.LENGTH_LONG).show();
            return false;
        }
        else {
            return true;
        }
    }

    public boolean validateBlood(){
        String p = tilBlood.getEditText().getText().toString().trim();
        if(p.isEmpty()){
            tilBlood.setError("Wajib diisi");
            return false;
        }
        else {
            tilBlood.setError(null);
            tilBlood.setErrorEnabled(false);
            return true;
        }
    }

    public boolean validateStatus(){
        String p = sStatusAda.getSelectedItem().toString().trim();
        if(p.isEmpty()){
            Toast.makeText(TernakAddActivity.this, "Status Ada - wajib diisi", Toast.LENGTH_LONG).show();
            return false;
        }
        else {
            return true;
        }
    }

    public void checkIfNull(){
        if(tilPemilik.getEditText().getText().toString().equals("")){
            pemilik = null;
        }else{
            pemilik = Integer.valueOf(tilPemilik.getEditText().getText().toString());
        }

        if(tilRas.getEditText().getText().toString().equals("")){
            ras = null;
        }else{
            ras = Integer.valueOf(tilRas.getEditText().getText().toString());
        }

        if(tilKematian.getEditText().getText().toString().equals("")){
            kematian = null;
        }else{
            kematian = Integer.valueOf(tilKematian.getEditText().getText().toString());
        }
    }

    @OnClick(R.id.tv_submit)
    public void action_add() {
        if (!validatePeternakan() | !validateTglLahir() | !validateRas() | !validateJk() | !validateBlood() | !validateStatus()) {
            Toast.makeText(TernakAddActivity.this, "Tolong isi data!", Toast.LENGTH_LONG).show();
            return;
        }

        SweetAlertDialog pDialog = DialogUtils.getLoadingPopup(this);

        checkIfNull();

        Integer peternakan = Integer.valueOf(tilPeternakan.getEditText().getText().toString());
        String jk = sJk.getSelectedItem().toString().trim();
        String tglLahir = tilTglLahir.getEditText().getText().toString().trim();
        String bobotLahir = tilBobotLahir.getEditText().getText().toString().trim();
        String pukulLahir = tilPukulLahir.getEditText().getText().toString().trim();
        String lamaDiKandungan = tilLamaDikandungan.getEditText().getText().toString().trim();
        String lamaLaktasi = tilLamaLaktasi.getEditText().getText().toString().trim();
        String tglLepasSapih = tilTglLepasSapih.getEditText().getText().toString().trim();
        String blood = tilBlood.getEditText().getText().toString().toUpperCase().trim();
        String ayah = tilAyah.getEditText().getText().toString().trim();
        String ibu = tilIbu.getEditText().getText().toString().trim();
        String bobotTubuh = tilBobotTubuh.getEditText().getText().toString().trim();
        String panjangTubuh = tilPanjangTubuh.getEditText().getText().toString().trim();
        String tinggiTubuh = tilTinggiTubuh.getEditText().getText().toString().trim();
        String cacatFisik = tilCacatFisik.getEditText().getText().toString().trim();
        String ciriLain = tilCiriLain.getEditText().getText().toString().trim();
        boolean statusAda;

        if(sStatusAda.getSelectedItem().toString().equals("Ada")){
            statusAda = true;
        }else{
            statusAda = false;
        }

        if(NetworkManager.isNetworkAvailable(TernakAddActivity.this)){
            Call<TernakResponse> call = RetrofitClient
                    .getInstance()
                    .getApi()
                    .addTernak(pemilik, peternakan, ras, kematian,
                            jk, tglLahir, bobotLahir, pukulLahir, lamaDiKandungan, lamaLaktasi,
                            tglLepasSapih, blood, ayah, ibu, bobotTubuh, panjangTubuh,tinggiTubuh,
                            cacatFisik, ciriLain, statusAda, "Bearer " + userToken);

            call.enqueue(new Callback<TernakResponse>() {
                @Override
                public void onResponse(Call<TernakResponse> call, Response<TernakResponse> response) {
                    TernakResponse resp = response.body();
                    pDialog.dismiss();

                    if(response.isSuccessful()){
                        if(resp.getStatus().equals("error")){
                            Toast.makeText(TernakAddActivity.this, resp.getErrors().toString(), Toast.LENGTH_LONG).show();
                        }
                        else{
                            Toast.makeText(TernakAddActivity.this, "Data berhasil dibuat: necktag " + resp.getTernaks().getNecktag(), Toast.LENGTH_LONG).show();

                            Intent intent = new Intent(TernakAddActivity.this, TernakActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);

                            TernakAddActivity.this.finish();
                        }
                    }
                    else{
                        SweetAlertDialog swal = new SweetAlertDialog(TernakAddActivity.this, SweetAlertDialog.ERROR_TYPE);
                        swal.setTitleText("Error");
                        swal.setContentText(response.message());
                        swal.show();
                    }
                }

                @Override
                public void onFailure(Call<TernakResponse> call, Throwable t) {
                    pDialog.dismiss();
                    SweetAlertDialog swal = new SweetAlertDialog(TernakAddActivity.this, SweetAlertDialog.ERROR_TYPE);
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

            TernakModel datas = new TernakModel(pemilik, peternakan, ras, kematian,
                    jk, tglLahir, bobotLahir, pukulLahir, lamaDiKandungan, lamaLaktasi,
                    tglLepasSapih, blood, ayah, ibu, bobotTubuh, panjangTubuh,tinggiTubuh,
                    cacatFisik, ciriLain, statusAda);
            mReference.child("addData").child("ternak").push().setValue(datas);

            Toast.makeText(this, "Disimpan", Toast.LENGTH_SHORT).show();

            TernakAddActivity.this.finish();
        }
    }
}
