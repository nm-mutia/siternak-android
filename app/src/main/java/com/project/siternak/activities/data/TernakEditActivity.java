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
import com.project.siternak.R;
import com.project.siternak.activities.option.KematianOptionActivity;
import com.project.siternak.activities.option.TernakOptionActivity;
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
import com.project.siternak.utils.SharedPrefManager;

import java.io.Serializable;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TernakEditActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener{
    @BindView(R.id.tiet_ternak_necktag) TextInputEditText tietNecktag;
    @BindView(R.id.tiet_ternak_pemilik) TextInputEditText tietPemilik;
    @BindView(R.id.til_ternak_peternakan) TextInputLayout tilPeternakan;
    @BindView(R.id.tiet_ternak_peternakan) TextInputEditText tietPeternakan;
    @BindView(R.id.til_ternak_ras) TextInputLayout tilRas;
    @BindView(R.id.tiet_ternak_ras) TextInputEditText tietRas;
    @BindView(R.id.tiet_ternak_kematian) TextInputEditText tietKematian;
    @BindView(R.id.til_ternak_tgl_lahir) TextInputLayout tilTglLahir;
    @BindView(R.id.tiet_ternak_tgl_lahir) TextInputEditText tietTglLahir;
    @BindView(R.id.tiet_ternak_bobot_lahir) TextInputEditText tietBobotLahir;
    @BindView(R.id.tiet_ternak_pukul_lahir) TextInputEditText tietPukulLahir;
    @BindView(R.id.tiet_ternak_lama_dikandungan) TextInputEditText tietLamaDikandungan;
    @BindView(R.id.tiet_ternak_lama_laktasi) TextInputEditText tietLamaLaktasi;
    @BindView(R.id.tiet_ternak_tgl_lepas_sapih) TextInputEditText tietTglLepasSapih;
    @BindView(R.id.til_ternak_blood) TextInputLayout tilBlood;
    @BindView(R.id.tiet_ternak_blood) TextInputEditText tietBlood;
    @BindView(R.id.tiet_ternak_ayah) TextInputEditText tietAyah;
    @BindView(R.id.tiet_ternak_ibu) TextInputEditText tietIbu;
    @BindView(R.id.tiet_ternak_bobot_tubuh) TextInputEditText tietBobotTubuh;
    @BindView(R.id.tiet_ternak_panjang_tubuh) TextInputEditText tietPanjangTubuh;
    @BindView(R.id.tiet_ternak_tinggi_tubuh) TextInputEditText tietTinggiTubuh;
    @BindView(R.id.tiet_ternak_cacat_fisik) TextInputEditText tietCacatFisik;
    @BindView(R.id.tiet_ternak_ciri_lain) TextInputEditText tietCiriLain;
    @BindView(R.id.s_ternak_jk) Spinner sJk;
    @BindView(R.id.s_ternak_status_ada) Spinner sStatusAda;

    private static final int REQUEST_CODE_SETPEMILIK = 1;
    private static final int REQUEST_CODE_SETPETERNAKAN = 2;
    private static final int REQUEST_CODE_SETRAS = 3;
    private static final int REQUEST_CODE_SETKEMATIAN = 4;
    private static final int REQUEST_CODE_SETAYAH = 5;
    private static final int REQUEST_CODE_SETIBU = 6;

    private TernakModel ternakData;
    private String userToken;
    private int clickedTgl;
    public final static int backFinish = 1;

    private Integer pemilik, kematian;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_ternak_edit);

        getSupportActionBar().hide();
        ButterKnife.bind(this);

        ternakData = (TernakModel) getIntent().getSerializableExtra("data");
        userToken = SharedPrefManager.getInstance(this).getAccessToken();

        setData();
    }

    private void setData() {
        ArrayAdapter<CharSequence> jkAdapter = ArrayAdapter.createFromResource(this, R.array.jenis_kelamin, android.R.layout.simple_spinner_item);
        jkAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sJk.setAdapter(jkAdapter);
        sJk.setSelection(jkAdapter.getPosition(ternakData.getJenisKelamin()));

        ArrayAdapter<CharSequence> statusAdapter = ArrayAdapter.createFromResource(this, R.array.status_ada, android.R.layout.simple_spinner_item);
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sStatusAda.setAdapter(statusAdapter);

        if(ternakData.getStatusAda()){
            sStatusAda.setSelection(jkAdapter.getPosition("Ada"));
        }else{
            sStatusAda.setSelection(jkAdapter.getPosition("Tidak Ada"));
        }

        tietNecktag.setText(ternakData.getNecktag());
        tietPemilik.setText(String.valueOf(ternakData.getPemilikId()));
        tietPeternakan.setText(String.valueOf(ternakData.getPeternakanId()));
        tietRas.setText(String.valueOf(ternakData.getRasId()));
        tietKematian.setText(String.valueOf(ternakData.getKematianId()));
        tietTglLahir.setText(ternakData.getTglLahir());
        tietBobotLahir.setText(ternakData.getBobotLahir());
        tietPukulLahir.setText(ternakData.getPukulLahir());
        tietLamaDikandungan.setText(ternakData.getLamaDiKandungan());
        tietLamaLaktasi.setText(ternakData.getLamaLaktasi());
        tietTglLepasSapih.setText(ternakData.getTglLepasSapih());
        tietBlood.setText(ternakData.getBlood());
        tietAyah.setText(ternakData.getNecktag_ayah());
        tietIbu.setText(ternakData.getNecktag_ibu());
        tietBobotTubuh.setText(ternakData.getBobotTubuh());
        tietPanjangTubuh.setText(ternakData.getPanjangTubuh());
        tietTinggiTubuh.setText(ternakData.getTinggiTubuh());
        tietCacatFisik.setText(ternakData.getCacatFisik());
        tietCiriLain.setText(ternakData.getCiriLain());
    }

    @OnClick(R.id.tiet_ternak_pemilik)
    public void setIdPemilik(){
        Intent intent = new Intent(TernakEditActivity.this, PemilikOptionActivity.class);
        startActivityForResult(intent, REQUEST_CODE_SETPEMILIK);
    }

    @OnClick(R.id.tiet_ternak_peternakan)
    public void setIdPeternakan(){
        Intent intent = new Intent(TernakEditActivity.this, PeternakanOptionActivity.class);
        startActivityForResult(intent, REQUEST_CODE_SETPETERNAKAN);
    }

    @OnClick(R.id.tiet_ternak_ras)
    public void setIdRas(){
        Intent intent = new Intent(TernakEditActivity.this, RasOptionActivity.class);
        startActivityForResult(intent, REQUEST_CODE_SETRAS);
    }

    @OnClick(R.id.tiet_ternak_kematian)
    public void setIdKematian(){
        Intent intent = new Intent(TernakEditActivity.this, KematianOptionActivity.class);
        startActivityForResult(intent, REQUEST_CODE_SETKEMATIAN);
    }

    @OnClick(R.id.tiet_ternak_ayah)
    public void setAyah(){
        Intent intent = new Intent(TernakEditActivity.this, TernakOptionActivity.class);
        intent.putExtra("parent", REQUEST_CODE_SETAYAH);
        startActivityForResult(intent, REQUEST_CODE_SETAYAH);
    }

    @OnClick(R.id.tiet_ternak_ibu)
    public void setIbu(){
        Intent intent = new Intent(TernakEditActivity.this, TernakOptionActivity.class);
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
            tietTglLahir.setText(dateStr);
        }
        else if(clickedTgl == 2){
            tietTglLepasSapih.setText(dateStr);
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
        tietPukulLahir.setText(timeStr);
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
                TernakModel result = (TernakModel) data.getSerializableExtra("parent");
                tietAyah.setText(result.getNecktag());
            }
            else if(requestCode == REQUEST_CODE_SETIBU){
                TernakModel result = (TernakModel) data.getSerializableExtra("parent");
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
            Toast.makeText(TernakEditActivity.this, "Jenis Kelamin - wajib diisi", Toast.LENGTH_LONG).show();
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
            Toast.makeText(TernakEditActivity.this, "Status Ada - wajib diisi", Toast.LENGTH_LONG).show();
            return false;
        }
        else {
            return true;
        }
    }

    public void checkIfNull(){
        if(tietPemilik.getText().toString().equals("") || tietPemilik.getText().toString().equals("null")){
            pemilik = null;
        }else{
            pemilik = Integer.valueOf(tietPemilik.getText().toString());
        }

        if(tietKematian.getText().toString().equals("") || tietKematian.getText().toString().equals("null")){
            kematian = null;
        }else{
            kematian = Integer.valueOf(tietKematian.getText().toString());
        }
    }

    @OnClick(R.id.tv_submit)
    public void action_add() {
        if (!validatePeternakan() | !validateTglLahir() | !validateRas() | !validateJk() | !validateBlood() | !validateStatus()) {
            Toast.makeText(TernakEditActivity.this, "Tolong isi data!", Toast.LENGTH_LONG).show();
            return;
        }

        SweetAlertDialog pDialog = DialogUtils.getLoadingPopup(this);
        checkIfNull();

        String necktag = tietNecktag.getText().toString();
        Integer peternakan = Integer.valueOf(tietPeternakan.getText().toString());
        Integer ras = Integer.valueOf(tietRas.getText().toString());
        String jk = sJk.getSelectedItem().toString();
        String tglLahir = tietTglLahir.getText().toString();
        String bobotLahir = tietBobotLahir.getText().toString();
        String pukulLahir = tietPukulLahir.getText().toString();
        String lamaDiKandungan = tietLamaDikandungan.getText().toString();
        String lamaLaktasi = tietLamaLaktasi.getText().toString();
        String tglLepasSapih = tietTglLepasSapih.getText().toString();
        String blood = tietBlood.getText().toString().toUpperCase();
        String ayah = tietAyah.getText().toString();
        String ibu = tietIbu.getText().toString();
        String bobotTubuh = tietBobotTubuh.getText().toString();
        String panjangTubuh = tietPanjangTubuh.getText().toString();
        String tinggiTubuh = tietTinggiTubuh.getText().toString();
        String cacatFisik = tietCacatFisik.getText().toString();
        String ciriLain = tietCiriLain.getText().toString();
        boolean statusAda;

        if(sStatusAda.getSelectedItem().toString().equals("Ada")){
            statusAda = true;
        }else{
            statusAda = false;
        }

        Call<TernakResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .editTernak(necktag, pemilik, peternakan, ras, kematian,
                        jk, tglLahir, bobotLahir, pukulLahir, lamaDiKandungan, lamaLaktasi,
                        tglLepasSapih, blood, ayah, ibu, bobotTubuh, panjangTubuh,tinggiTubuh,
                        cacatFisik, ciriLain, statusAda, "Bearer " + userToken);

        call.enqueue((new Callback<TernakResponse>() {
            @Override
            public void onResponse(Call<TernakResponse> call, Response<TernakResponse> response) {
                if(response.isSuccessful()){
                    TernakResponse resp = response.body();
                    pDialog.dismiss();

                    if(resp.getStatus().equals("error")){
                        Toast.makeText(TernakEditActivity.this, resp.getErrors().toString(), Toast.LENGTH_LONG).show();
                    }
                    else {
                        TernakModel datas = new TernakModel(necktag, pemilik, peternakan, ras, kematian,
                                jk, tglLahir, bobotLahir, pukulLahir, lamaDiKandungan, lamaLaktasi, tglLepasSapih,
                                blood, ayah, ibu, bobotTubuh, panjangTubuh, tinggiTubuh, cacatFisik, ciriLain, statusAda,
                                resp.getTernaks().getCreated_at(), resp.getTernaks().getUpdated_at(), resp.getTernaks().getDeleted_at());
                        Toast.makeText(TernakEditActivity.this, "Data berhasil diubah: id " + resp.getTernaks().getNecktag(), Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(TernakEditActivity.this, TernakDetailActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("ternak", (Serializable) datas);
                        intent.putExtra("finish", backFinish);
                        startActivity(intent);

                        TernakEditActivity.this.finish();
                    }
                }
                else{
                    SweetAlertDialog swal = new SweetAlertDialog(TernakEditActivity.this, SweetAlertDialog.ERROR_TYPE);
                    swal.setTitleText("Error");
                    swal.setContentText(response.message());
                    swal.show();
                }
            }

            @Override
            public void onFailure(Call<TernakResponse> call, Throwable t) {
                pDialog.dismiss();
                SweetAlertDialog swal = new SweetAlertDialog(TernakEditActivity.this, SweetAlertDialog.ERROR_TYPE);
                swal.setTitleText("Error");
                swal.setContentText(t.getMessage());
                swal.show();
            }
        }));
    }

}
