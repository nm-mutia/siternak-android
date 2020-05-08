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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.project.siternak.R;
import com.project.siternak.activities.option.KematianOptionActivity;
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
import com.project.siternak.utils.SharedPrefManager;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TernakAddActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener{
    @BindView(R.id.til_ternak_pemilik) TextInputLayout tilPemilik;
    @BindView(R.id.tiet_ternak_pemilik) TextInputEditText tietPemilik;
    @BindView(R.id.til_ternak_peternakan) TextInputLayout tilPeternakan;
    @BindView(R.id.tiet_ternak_peternakan) TextInputEditText tietPeternakan;
    @BindView(R.id.til_ternak_ras) TextInputLayout tilRas;
    @BindView(R.id.tiet_ternak_ras) TextInputEditText tietRas;
    @BindView(R.id.til_ternak_kematian) TextInputLayout tilKematian;
    @BindView(R.id.tiet_ternak_kematian) TextInputEditText tietKematian;
//    @BindView(R.id.til_ternak_jk) TextInputLayout tilJk;
    @BindView(R.id.til_ternak_tgl_lahir) TextInputLayout tilTglLahir;
    @BindView(R.id.til_ternak_bobot_lahir) TextInputLayout tilBobotLahir;
    @BindView(R.id.til_ternak_pukul_lahir) TextInputLayout tilPukulLahir;
    @BindView(R.id.til_ternak_lama_dikandungan) TextInputLayout tilLamaDikandungan;
    @BindView(R.id.til_ternak_lama_laktasi) TextInputLayout tilLamaLaktasi;
    @BindView(R.id.til_ternak_tgl_lepas_sapih) TextInputLayout tilTglLepasSapih;
    @BindView(R.id.til_ternak_blood) TextInputLayout tilBlood;
    @BindView(R.id.til_ternak_ayah) TextInputLayout tilAyah;
    @BindView(R.id.til_ternak_ibu) TextInputLayout tilIbu;
    @BindView(R.id.til_ternak_bobot_tubuh) TextInputLayout tilBobotTubuh;
    @BindView(R.id.til_ternak_panjang_tubuh) TextInputLayout tilPanjangTubuh;
    @BindView(R.id.til_ternak_tinggi_tubuh) TextInputLayout tilTinggiTubuh;
    @BindView(R.id.til_ternak_cacat_fisik) TextInputLayout tilCacatFisik;
    @BindView(R.id.til_ternak_ciri_lain) TextInputLayout tilCiriLain;
//    @BindView(R.id.til_ternak_status_ada) TextInputLayout tilStatusAda;
    @BindView(R.id.s_ternak_jk) Spinner sJk;
    @BindView(R.id.s_ternak_status_ada) Spinner sStatusAda;

    private static final int REQUEST_CODE_SETPEMILIK = 1;
    private static final int REQUEST_CODE_SETPETERNAKAN = 2;
    private static final int REQUEST_CODE_SETRAS = 3;
    private static final int REQUEST_CODE_SETKEMATIAN = 4;
    private static final int REQUEST_CODE_SETAYAH = 5;
    private static final int REQUEST_CODE_SETIBU = 6;

//    private PemilikModel idPemilik;
//    private PeternakanModel idPeternakan;
//    private RasModel idRas;
//    private KematianModel idKematian;
//    private TernakModel idAyah, idIbu;

    private String userToken;
    private int clickedTgl;

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
//        sJk.setOnItemSelectedListener();

        ArrayAdapter<CharSequence> statusAdapter = ArrayAdapter.createFromResource(this, R.array.status_ada, android.R.layout.simple_spinner_item);
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sStatusAda.setAdapter(statusAdapter);
//        sStatusAda.setOnItemSelectedListener(new );
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
        }
    }
}
