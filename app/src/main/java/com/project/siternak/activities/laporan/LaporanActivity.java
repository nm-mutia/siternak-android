package com.project.siternak.activities.laporan;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appeaser.sublimepickerlibrary.datepicker.SelectedDate;
import com.appeaser.sublimepickerlibrary.helpers.SublimeOptions;
import com.appeaser.sublimepickerlibrary.recurrencepicker.SublimeRecurrencePicker;
import com.project.siternak.R;
import com.project.siternak.adapter.laporan.DataKawinAdapter;
import com.project.siternak.adapter.laporan.DataSakitAdapter;
import com.project.siternak.adapter.laporan.DataTernakAdapter;
import com.project.siternak.adapter.laporan.DataTernakMatiAdapter;
import com.project.siternak.fragments.DateRangePickerFragment;
import com.project.siternak.models.data.PerkawinanModel;
import com.project.siternak.models.data.RiwayatPenyakitModel;
import com.project.siternak.models.data.TernakMatiModel;
import com.project.siternak.models.data.TernakModel;
import com.project.siternak.responses.LaporanResponse;
import com.project.siternak.rest.RetrofitClient;
import com.project.siternak.utils.DialogUtils;
import com.project.siternak.utils.FolderExternalStorage;
import com.project.siternak.utils.SharedPrefManager;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.project.siternak.utils.FolderExternalStorage.FOLDER_NAME;

public class LaporanActivity extends AppCompatActivity {
    @BindView(R.id.tv_start_date) TextView tvStartDate;
    @BindView(R.id.tv_end_date) TextView tvEndDate;
    @BindView(R.id.spinner_laporan) Spinner spinner;
    @BindView(R.id.rv) RecyclerView rv;

    private DataTernakAdapter lahirAdapter, adaAdapter;
    private DataTernakMatiAdapter matiAdapter;
    private DataKawinAdapter kawinAdapter;
    private DataSakitAdapter sakitAdapter;
    private String mDateStart, mDateEnd, userToken;
    private Unbinder unbinder;
    private Workbook wb;
    private boolean isSpinnerinitial = true;
    private static final int PERMISSION_REQUEST_CODE = 200;

    private List<TernakModel> lahir;
    private List<TernakMatiModel> mati;
    private List<PerkawinanModel> kawin;
    private List<RiwayatPenyakitModel> sakit;
    private List<TernakModel> ada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_laporan);
        getSupportActionBar().hide();

        ButterKnife.bind(this);

        unbinder = ButterKnife.bind(this);
        userToken = SharedPrefManager.getInstance(this).getAccessToken();

        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);

        setInitialDate();
        setSpinner();
        setLaporan();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @OnClick(R.id.btn_atur_waktu)
    public void openDateRangePicker(){
        DateRangePickerFragment dateRangePickerFragment = new DateRangePickerFragment();
        dateRangePickerFragment.setCallback(new DateRangePickerFragment.Callback() {
            @Override
            public void onCancelled() {
                Toast.makeText(LaporanActivity.this, "Dibatalkan", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDateTimeRecurrenceSet(SelectedDate selectedDate, int i, int i1, SublimeRecurrencePicker.RecurrenceOption recurrenceOption, String s) {
                @SuppressLint("SimpleDateFormat")
                SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd");
                mDateStart = dateFormat.format(selectedDate.getStartDate().getTime());
                mDateEnd = dateFormat.format(selectedDate.getEndDate().getTime());

                tvStartDate.setText(mDateStart);
                tvEndDate.setText(mDateEnd);

                isSpinnerinitial = true;
                setLaporan();
            }
        });

        SublimeOptions options = new SublimeOptions();
        options.setCanPickDateRange(true);
        options.setPickerToShow(SublimeOptions.Picker.DATE_PICKER);

        Bundle bundle = new Bundle();
        bundle.putParcelable("SUBLIME_OPTIONS", options);
        dateRangePickerFragment.setArguments(bundle);

        dateRangePickerFragment.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        dateRangePickerFragment.show(getSupportFragmentManager(), "SUBLIME_PICKER");
    }

    private boolean checkPermission() {
        return ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissionAndContinue() {
        if (ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, WRITE_EXTERNAL_STORAGE) && ActivityCompat.shouldShowRequestPermissionRationale(this, READ_EXTERNAL_STORAGE)) {
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
                alertBuilder.setCancelable(true);
                alertBuilder.setTitle("Izin dibutuhkan");
                alertBuilder.setMessage("Izin dibutuhkan untuk menyimpan file");
                alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(LaporanActivity.this, new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
                    }
                });
                AlertDialog alert = alertBuilder.create();
                alert.show();
            } else {
                ActivityCompat.requestPermissions(LaporanActivity.this, new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
            }
        } else {
            download();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (permissions.length > 0 && grantResults.length > 0) {
                boolean flag = true;
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        flag = false;
                    }
                }
                if (flag) {
                    download();
                } else {
                    finish();
                }
            } else {
                finish();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @OnClick(R.id.btn_laporan_download)
    public void setDownloadFile(){
        setExcel();

        if (!checkPermission()) {
            download();
        } else {
            if (checkPermission()) {
                requestPermissionAndContinue();
            } else {
                download();
            }
        }
    }

    private void download(){
        String filename = "SITERNAK_Laporan_"+tvStartDate.getText().toString()+"_"+tvEndDate.getText().toString()+".xls";

        if(FolderExternalStorage.checkFolder()){
            File dir = Environment.getExternalStoragePublicDirectory(FOLDER_NAME);
            File file = new File(dir, filename);
            FileOutputStream fileOutputStream = null;

            try {
                fileOutputStream = new FileOutputStream(file);
                wb.write(fileOutputStream);
                Toast.makeText(getApplicationContext(), "File disimpan di " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Gagal menyimpan file", Toast.LENGTH_LONG).show();
                if(fileOutputStream != null){
                    try {
                        fileOutputStream.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }

    }

    private void setInitialDate(){
        Calendar calendar1 = Calendar.getInstance();
        calendar1.add(Calendar.MONTH, -1);
        Date date1 = calendar1.getTime();
        SimpleDateFormat format1 = new SimpleDateFormat("YYYY-MM-dd");
        String dateOutput1 = format1.format(date1);
        tvStartDate.setText(dateOutput1);

        Calendar calendar2 = Calendar.getInstance();
        calendar2.add(Calendar.MONTH, 0);
        Date date2 = calendar2.getTime();
        SimpleDateFormat format2 = new SimpleDateFormat("YYYY-MM-dd");
        String dateOutput2 = format2.format(date2);
        tvEndDate.setText(dateOutput2);
    }

    private void setSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.laporan, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private void setLaporan(){
        SweetAlertDialog pDialog = DialogUtils.getLoadingPopup(this);

        Call<LaporanResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .getLaporan("Bearer " + this.userToken, tvStartDate.getText().toString(), tvEndDate.getText().toString());

        call.enqueue(new Callback<LaporanResponse>() {
            @Override
            public void onResponse(Call<LaporanResponse> call, Response<LaporanResponse> response) {
                LaporanResponse resp = response.body();
                pDialog.cancel();

                if(response.isSuccessful()){
                    lahir = resp.getLahir();
                    mati = resp.getMati();
                    kawin = resp.getKawin();
                    sakit = resp.getSakit();
                    ada = resp.getAda();

                    if(isSpinnerinitial){
                        isSpinnerinitial = false;

                        //lahir
                        spinner.setSelection(0);
                        lahirAdapter = new DataTernakAdapter((ArrayList<TernakModel>)lahir);
                        rv.setAdapter(lahirAdapter);
                        lahirAdapter.notifyDataSetChanged();
                    }

                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            String data = adapterView.getItemAtPosition(i).toString();
                            switch (data){
                                case "LAHIR":
                                    lahirAdapter = new DataTernakAdapter((ArrayList<TernakModel>)lahir);
                                    rv.setAdapter(lahirAdapter);
                                    lahirAdapter.notifyDataSetChanged();
                                    break;
                                case "MATI":
                                    matiAdapter = new DataTernakMatiAdapter((ArrayList<TernakMatiModel>)mati);
                                    rv.setAdapter(matiAdapter);
                                    matiAdapter.notifyDataSetChanged();
                                    break;
                                case "KAWIN":
                                    kawinAdapter = new DataKawinAdapter((ArrayList<PerkawinanModel>)kawin);
                                    rv.setAdapter(kawinAdapter);
                                    kawinAdapter.notifyDataSetChanged();
                                    break;
                                case "SAKIT":
                                    sakitAdapter = new DataSakitAdapter((ArrayList<RiwayatPenyakitModel>)sakit);
                                    rv.setAdapter(sakitAdapter);
                                    sakitAdapter.notifyDataSetChanged();
                                    break;
                                case "TOTAL ADA":
                                    adaAdapter = new DataTernakAdapter((ArrayList<TernakModel>)ada);
                                    rv.setAdapter(adaAdapter);
                                    adaAdapter.notifyDataSetChanged();
                                    break;
                                default:
                                    break;
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {}
                    });
                }
                else {
                    Toast.makeText(LaporanActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LaporanResponse> call, Throwable t) {
                pDialog.cancel();
                Toast.makeText(LaporanActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setExcel(){
        wb = new HSSFWorkbook();

        Cell cell = null;
        CellStyle cellStyle = wb.createCellStyle();
        cellStyle.setFillBackgroundColor(HSSFColor.LIGHT_BLUE.index);

        Sheet sheetLahir = null;
        sheetLahir = wb.createSheet("TernakLahir");
        Sheet sheetMati = null;
        sheetMati = wb.createSheet("TernakMati");
        Sheet sheetKawin = null;
        sheetKawin = wb.createSheet("TernakKawin");
        Sheet sheetSakit = null;
        sheetSakit = wb.createSheet("TernakSakit");
        Sheet sheetAda = null;
        sheetAda = wb.createSheet("TernakAda");

        //lahir
        Row row = sheetLahir.createRow(0);
        cell = row.createCell(0);
        cell.setCellValue("Necktag");
        cell.setCellStyle(cellStyle);
        cell = row.createCell(1);
        cell.setCellValue("ID Pemilik");
        cell.setCellStyle(cellStyle);
        cell = row.createCell(2);
        cell.setCellValue("ID Peternakan");
        cell.setCellStyle(cellStyle);
        cell = row.createCell(3);
        cell.setCellValue("ID Ras");
        cell.setCellStyle(cellStyle);
        cell = row.createCell(4);
        cell.setCellValue("ID Kematian");
        cell.setCellStyle(cellStyle);
        cell = row.createCell(5);
        cell.setCellValue("Jenis Kelamin");
        cell.setCellStyle(cellStyle);
        cell = row.createCell(6);
        cell.setCellValue("Tanggal Lahir");
        cell.setCellStyle(cellStyle);
        cell = row.createCell(7);
        cell.setCellValue("Bobot Lahir");
        cell.setCellStyle(cellStyle);
        cell = row.createCell(8);
        cell.setCellValue("Pukul Lahir");
        cell.setCellStyle(cellStyle);
        cell = row.createCell(9);
        cell.setCellValue("Lama diKandungan");
        cell.setCellStyle(cellStyle);
        cell = row.createCell(10);
        cell.setCellValue("Lama Laktasi");
        cell.setCellStyle(cellStyle);
        cell = row.createCell(11);
        cell.setCellValue("Tanggal Lepas Sapih");
        cell.setCellStyle(cellStyle);
        cell = row.createCell(12);
        cell.setCellValue("Blood");
        cell.setCellStyle(cellStyle);
        cell = row.createCell(13);
        cell.setCellValue("Necktag Ayah");
        cell.setCellStyle(cellStyle);
        cell = row.createCell(14);
        cell.setCellValue("Necktag Ibu");
        cell.setCellStyle(cellStyle);
        cell = row.createCell(15);
        cell.setCellValue("Bobot Tubuh");
        cell.setCellStyle(cellStyle);
        cell = row.createCell(16);
        cell.setCellValue("Panjang Tubuh");
        cell.setCellStyle(cellStyle);
        cell = row.createCell(17);
        cell.setCellValue("Tinggi Tubuh");
        cell.setCellStyle(cellStyle);
        cell = row.createCell(18);
        cell.setCellValue("Cacat Fisik");
        cell.setCellStyle(cellStyle);
        cell = row.createCell(19);
        cell.setCellValue("Ciri Lain");
        cell.setCellStyle(cellStyle);
        cell = row.createCell(20);
        cell.setCellValue("Status Ada");
        cell.setCellStyle(cellStyle);
        cell = row.createCell(21);
        cell.setCellValue("Created At");
        cell.setCellStyle(cellStyle);
        cell = row.createCell(22);
        cell.setCellValue("Update At");
        cell.setCellStyle(cellStyle);
        cell = row.createCell(23);
        cell.setCellValue("Deleted At");
        cell.setCellStyle(cellStyle);

        for(int i = 0; i < lahir.size(); i++){
            Row rowx = sheetLahir.createRow(i+1);
            TernakModel data = lahir.get(i);

            cell = rowx.createCell(0);
            cell.setCellValue(data.getNecktag());
            cell.setCellStyle(cellStyle);
            cell = rowx.createCell(1);
            if(data.getPemilikId() == null){
                cell.setCellValue("null");
            } else{
                cell.setCellValue(data.getPemilikId().toString());
            }
            cell.setCellStyle(cellStyle);
            cell = rowx.createCell(2);
            if(data.getPeternakanId() == null){
                cell.setCellValue("null");
            } else{
                cell.setCellValue(data.getPeternakanId().toString());
            }
            cell.setCellStyle(cellStyle);
            cell = rowx.createCell(3);
            if(data.getRasId() == null){
                cell.setCellValue("null");
            } else{
                cell.setCellValue(data.getRasId().toString());
            }
            cell.setCellStyle(cellStyle);
            cell = rowx.createCell(4);
            if(data.getKematianId() == null){
                cell.setCellValue("null");
            } else{
                cell.setCellValue(data.getKematianId().toString());
            }
            cell.setCellStyle(cellStyle);
            cell = rowx.createCell(5);
            cell.setCellValue(data.getJenisKelamin());
            cell.setCellStyle(cellStyle);
            cell = rowx.createCell(6);
            cell.setCellValue(data.getTglLahir());
            cell.setCellStyle(cellStyle);
            cell = rowx.createCell(7);
            cell.setCellValue(data.getBobotLahir());
            cell.setCellStyle(cellStyle);
            cell = rowx.createCell(8);
            cell.setCellValue(data.getPukulLahir());
            cell.setCellStyle(cellStyle);
            cell = rowx.createCell(9);
            cell.setCellValue(data.getLamaDiKandungan());
            cell.setCellStyle(cellStyle);
            cell = rowx.createCell(10);
            cell.setCellValue(data.getLamaLaktasi());
            cell.setCellStyle(cellStyle);
            cell = rowx.createCell(11);
            cell.setCellValue(data.getTglLepasSapih());
            cell.setCellStyle(cellStyle);
            cell = rowx.createCell(12);
            cell.setCellValue(data.getBlood());
            cell.setCellStyle(cellStyle);
            cell = rowx.createCell(13);
            cell.setCellValue(data.getNecktag_ayah());
            cell.setCellStyle(cellStyle);
            cell = rowx.createCell(14);
            cell.setCellValue(data.getNecktag_ibu());
            cell.setCellStyle(cellStyle);
            cell = rowx.createCell(15);
            cell.setCellValue(data.getBobotTubuh());
            cell.setCellStyle(cellStyle);
            cell = rowx.createCell(16);
            cell.setCellValue(data.getPanjangTubuh());
            cell.setCellStyle(cellStyle);
            cell = rowx.createCell(17);
            cell.setCellValue(data.getTinggiTubuh());
            cell.setCellStyle(cellStyle);
            cell = rowx.createCell(18);
            cell.setCellValue(data.getCacatFisik());
            cell.setCellStyle(cellStyle);
            cell = rowx.createCell(19);
            cell.setCellValue(data.getCiriLain());
            cell.setCellStyle(cellStyle);
            cell = rowx.createCell(20);
            cell.setCellValue(data.getStatusAda());
            cell.setCellStyle(cellStyle);
            cell = rowx.createCell(21);
            cell.setCellValue(data.getCreated_at());
            cell.setCellStyle(cellStyle);
            cell = rowx.createCell(22);
            cell.setCellValue(data.getUpdated_at());
            cell.setCellStyle(cellStyle);
            cell = rowx.createCell(23);
            cell.setCellValue(data.getDeleted_at());
            cell.setCellStyle(cellStyle);

            sheetLahir.setColumnWidth(i, (10*200));
        }

        //mati
        Row row1 = sheetMati.createRow(0);
        cell = row1.createCell(0);
        cell.setCellValue("Necktag");
        cell.setCellStyle(cellStyle);
        cell = row1.createCell(1);
        cell.setCellValue("ID Kematian");
        cell.setCellStyle(cellStyle);
        cell = row1.createCell(2);
        cell.setCellValue("Tanggal Kematian");
        cell.setCellStyle(cellStyle);
        cell = row1.createCell(3);
        cell.setCellValue("Waktu Kematian");
        cell.setCellStyle(cellStyle);
        cell = row1.createCell(4);
        cell.setCellValue("Penyebab Kematian");
        cell.setCellStyle(cellStyle);
        cell = row1.createCell(5);
        cell.setCellValue("Kondisi Kematian");
        cell.setCellStyle(cellStyle);
        cell = row1.createCell(6);
        cell.setCellValue("ID Pemilik");
        cell.setCellStyle(cellStyle);
        cell = row1.createCell(7);
        cell.setCellValue("ID Peternakan");
        cell.setCellStyle(cellStyle);
        cell = row1.createCell(8);
        cell.setCellValue("ID Ras");
        cell.setCellStyle(cellStyle);
        cell = row1.createCell(9);
        cell.setCellValue("Jenis Kelamin");
        cell.setCellStyle(cellStyle);
        cell = row1.createCell(10);
        cell.setCellValue("Tanggal Lahir");
        cell.setCellStyle(cellStyle);
        cell = row1.createCell(11);
        cell.setCellValue("Bobot Lahir");
        cell.setCellStyle(cellStyle);
        cell = row1.createCell(12);
        cell.setCellValue("Pukul Lahir");
        cell.setCellStyle(cellStyle);
        cell = row1.createCell(13);
        cell.setCellValue("Lama diKandungan");
        cell.setCellStyle(cellStyle);
        cell = row1.createCell(14);
        cell.setCellValue("Lama Laktasi");
        cell.setCellStyle(cellStyle);
        cell = row1.createCell(15);
        cell.setCellValue("Tanggal Lepas Sapih");
        cell.setCellStyle(cellStyle);
        cell = row1.createCell(16);
        cell.setCellValue("Blood");
        cell.setCellStyle(cellStyle);
        cell = row1.createCell(17);
        cell.setCellValue("Necktag Ayah");
        cell.setCellStyle(cellStyle);
        cell = row1.createCell(18);
        cell.setCellValue("Necktag Ibu");
        cell.setCellStyle(cellStyle);
        cell = row1.createCell(19);
        cell.setCellValue("Bobot Tubuh");
        cell.setCellStyle(cellStyle);
        cell = row1.createCell(20);
        cell.setCellValue("Panjang Tubuh");
        cell.setCellStyle(cellStyle);
        cell = row1.createCell(21);
        cell.setCellValue("Tinggi Tubuh");
        cell.setCellStyle(cellStyle);
        cell = row1.createCell(22);
        cell.setCellValue("Cacat Fisik");
        cell.setCellStyle(cellStyle);
        cell = row1.createCell(23);
        cell.setCellValue("Ciri Lain");
        cell.setCellStyle(cellStyle);
        cell = row1.createCell(24);
        cell.setCellValue("Status Ada");
        cell.setCellStyle(cellStyle);
        cell = row1.createCell(25);
        cell.setCellValue("Created At");
        cell.setCellStyle(cellStyle);
        cell = row1.createCell(26);
        cell.setCellValue("Update At");
        cell.setCellStyle(cellStyle);
        cell = row1.createCell(27);
        cell.setCellValue("Deleted At");
        cell.setCellStyle(cellStyle);

        for(int i = 0; i < mati.size(); i++){
            Row rowx = sheetMati.createRow(i+1);
            TernakMatiModel data = mati.get(i);

            cell = rowx.createCell(0);
            cell.setCellValue(data.getNecktag());
            cell.setCellStyle(cellStyle);
            cell = rowx.createCell(1);
            if(data.getKematianId() == null){
                cell.setCellValue("null");
            } else{
                cell.setCellValue(data.getKematianId().toString());
            }
            cell.setCellStyle(cellStyle);
            cell = rowx.createCell(2);
            cell.setCellValue(data.getTglKematian());
            cell.setCellStyle(cellStyle);
            cell = rowx.createCell(3);
            cell.setCellValue(data.getWaktuKematian());
            cell.setCellStyle(cellStyle);
            cell = rowx.createCell(4);
            cell.setCellValue(data.getPenyebab());
            cell.setCellStyle(cellStyle);
            cell = rowx.createCell(5);
            cell.setCellValue(data.getKondisi());
            cell.setCellStyle(cellStyle);
            cell = rowx.createCell(6);
            if(data.getPemilikId() == null){
                cell.setCellValue("null");
            } else{
                cell.setCellValue(data.getPemilikId().toString());
            }
            cell.setCellStyle(cellStyle);
            cell = rowx.createCell(7);
            if(data.getPeternakanId() == null){
                cell.setCellValue("null");
            } else{
                cell.setCellValue(data.getPeternakanId().toString());
            }
            cell.setCellStyle(cellStyle);
            cell = rowx.createCell(8);
            if(data.getRasId() == null){
                cell.setCellValue("null");
            } else{
                cell.setCellValue(data.getRasId().toString());
            }
            cell.setCellStyle(cellStyle);
            cell = rowx.createCell(9);
            cell.setCellValue(data.getJenisKelamin());
            cell.setCellStyle(cellStyle);
            cell = rowx.createCell(10);
            cell.setCellValue(data.getTglLahir());
            cell.setCellStyle(cellStyle);
            cell = rowx.createCell(11);
            cell.setCellValue(data.getBobotLahir());
            cell.setCellStyle(cellStyle);
            cell = rowx.createCell(12);
            cell.setCellValue(data.getPukulLahir());
            cell.setCellStyle(cellStyle);
            cell = rowx.createCell(13);
            cell.setCellValue(data.getLamaDiKandungan());
            cell.setCellStyle(cellStyle);
            cell = rowx.createCell(14);
            cell.setCellValue(data.getLamaLaktasi());
            cell.setCellStyle(cellStyle);
            cell = rowx.createCell(15);
            cell.setCellValue(data.getTglLepasSapih());
            cell.setCellStyle(cellStyle);
            cell = rowx.createCell(16);
            cell.setCellValue(data.getBlood());
            cell.setCellStyle(cellStyle);
            cell = rowx.createCell(17);
            cell.setCellValue(data.getNecktag_ayah());
            cell.setCellStyle(cellStyle);
            cell = rowx.createCell(18);
            cell.setCellValue(data.getNecktag_ibu());
            cell.setCellStyle(cellStyle);
            cell = rowx.createCell(19);
            cell.setCellValue(data.getBobotTubuh());
            cell.setCellStyle(cellStyle);
            cell = rowx.createCell(20);
            cell.setCellValue(data.getPanjangTubuh());
            cell.setCellStyle(cellStyle);
            cell = rowx.createCell(21);
            cell.setCellValue(data.getTinggiTubuh());
            cell.setCellStyle(cellStyle);
            cell = rowx.createCell(22);
            cell.setCellValue(data.getCacatFisik());
            cell.setCellStyle(cellStyle);
            cell = rowx.createCell(23);
            cell.setCellValue(data.getCiriLain());
            cell.setCellStyle(cellStyle);
            cell = rowx.createCell(24);
            cell.setCellValue(data.getStatusAda());
            cell.setCellStyle(cellStyle);
            cell = rowx.createCell(25);
            cell.setCellValue(data.getCreated_at());
            cell.setCellStyle(cellStyle);
            cell = rowx.createCell(26);
            cell.setCellValue(data.getUpdated_at());
            cell.setCellStyle(cellStyle);
            cell = rowx.createCell(27);
            cell.setCellValue(data.getDeleted_at());
            cell.setCellStyle(cellStyle);

            sheetMati.setColumnWidth(i, (10*200));
        }

        //kawin
        Row row2 = sheetKawin.createRow(0);
        cell = row2.createCell(0);
        cell.setCellValue("ID");
        cell.setCellStyle(cellStyle);
        cell = row2.createCell(1);
        cell.setCellValue("Necktag");
        cell.setCellStyle(cellStyle);
        cell = row2.createCell(2);
        cell.setCellValue("Necktag Pasangan");
        cell.setCellStyle(cellStyle);
        cell = row2.createCell(3);
        cell.setCellValue("Tanggal");
        cell.setCellStyle(cellStyle);
        cell = row2.createCell(4);
        cell.setCellValue("Created At");
        cell.setCellStyle(cellStyle);
        cell = row2.createCell(5);
        cell.setCellValue("Update At");
        cell.setCellStyle(cellStyle);

        for(int i = 0; i < kawin.size(); i++){
            Row rowx = sheetKawin.createRow(i+1);
            PerkawinanModel data = kawin.get(i);

            cell = rowx.createCell(0);
            cell.setCellValue(data.getId());
            cell.setCellStyle(cellStyle);
            cell = rowx.createCell(1);
            cell.setCellValue(data.getNecktag());
            cell.setCellStyle(cellStyle);
            cell = rowx.createCell(2);
            cell.setCellValue(data.getNecktag_psg());
            cell.setCellStyle(cellStyle);
            cell = rowx.createCell(3);
            cell.setCellValue(data.getTgl());
            cell.setCellStyle(cellStyle);
            cell = rowx.createCell(4);
            cell.setCellValue(data.getCreated_at());
            cell.setCellStyle(cellStyle);
            cell = rowx.createCell(5);
            cell.setCellValue(data.getUpdated_at());
            cell.setCellStyle(cellStyle);

            sheetKawin.setColumnWidth(i, (10*200));
        }

        //sakit
        Row row3 = sheetSakit.createRow(0);
        cell = row3.createCell(0);
        cell.setCellValue("ID");
        cell.setCellStyle(cellStyle);
        cell = row3.createCell(1);
        cell.setCellValue("ID Penyakit");
        cell.setCellStyle(cellStyle);
        cell = row3.createCell(2);
        cell.setCellValue("Necktag");
        cell.setCellStyle(cellStyle);
        cell = row3.createCell(3);
        cell.setCellValue("Tanggal Sakit");
        cell.setCellStyle(cellStyle);
        cell = row3.createCell(4);
        cell.setCellValue("Obat");
        cell.setCellStyle(cellStyle);
        cell = row3.createCell(5);
        cell.setCellValue("Lama Sakit");
        cell.setCellStyle(cellStyle);
        cell = row3.createCell(6);
        cell.setCellValue("Keterangan");
        cell.setCellStyle(cellStyle);
        cell = row3.createCell(7);
        cell.setCellValue("Created At");
        cell.setCellStyle(cellStyle);
        cell = row3.createCell(8);
        cell.setCellValue("Update At");
        cell.setCellStyle(cellStyle);

        for(int i = 0; i < sakit.size(); i++){
            Row rowx = sheetSakit.createRow(i+1);
            RiwayatPenyakitModel data = sakit.get(i);

            cell = rowx.createCell(0);
            cell.setCellValue(data.getId());
            cell.setCellStyle(cellStyle);
            cell = rowx.createCell(1);
            cell.setCellValue(data.getPenyakitId());
            cell.setCellStyle(cellStyle);
            cell = rowx.createCell(2);
            cell.setCellValue(data.getNecktag());
            cell.setCellStyle(cellStyle);
            cell = rowx.createCell(3);
            cell.setCellValue(data.getTglSakit());
            cell.setCellStyle(cellStyle);
            cell = rowx.createCell(4);
            cell.setCellValue(data.getObat());
            cell.setCellStyle(cellStyle);
            cell = rowx.createCell(5);
            if(data.getLamaSakit() == null){
                cell.setCellValue("null");
            } else{
                cell.setCellValue(data.getLamaSakit());
            }
            cell.setCellStyle(cellStyle);
            cell = rowx.createCell(6);
            cell.setCellValue(data.getKeterangan());
            cell.setCellStyle(cellStyle);
            cell = rowx.createCell(7);
            cell.setCellValue(data.getCreated_at());
            cell.setCellStyle(cellStyle);
            cell = rowx.createCell(8);
            cell.setCellValue(data.getUpdated_at());
            cell.setCellStyle(cellStyle);

            sheetSakit.setColumnWidth(i, (10*200));
        }

        //ada
        Row row4 = sheetAda.createRow(0);
        cell = row4.createCell(0);
        cell.setCellValue("Necktag");
        cell.setCellStyle(cellStyle);
        cell = row4.createCell(1);
        cell.setCellValue("ID Pemilik");
        cell.setCellStyle(cellStyle);
        cell = row4.createCell(2);
        cell.setCellValue("ID Peternakan");
        cell.setCellStyle(cellStyle);
        cell = row4.createCell(3);
        cell.setCellValue("ID Ras");
        cell.setCellStyle(cellStyle);
        cell = row4.createCell(4);
        cell.setCellValue("ID Kematian");
        cell.setCellStyle(cellStyle);
        cell = row4.createCell(5);
        cell.setCellValue("Jenis Kelamin");
        cell.setCellStyle(cellStyle);
        cell = row4.createCell(6);
        cell.setCellValue("Tanggal Lahir");
        cell.setCellStyle(cellStyle);
        cell = row4.createCell(7);
        cell.setCellValue("Bobot Lahir");
        cell.setCellStyle(cellStyle);
        cell = row4.createCell(8);
        cell.setCellValue("Pukul Lahir");
        cell.setCellStyle(cellStyle);
        cell = row4.createCell(9);
        cell.setCellValue("Lama diKandungan");
        cell.setCellStyle(cellStyle);
        cell = row4.createCell(10);
        cell.setCellValue("Lama Laktasi");
        cell.setCellStyle(cellStyle);
        cell = row4.createCell(11);
        cell.setCellValue("Tanggal Lepas Sapih");
        cell.setCellStyle(cellStyle);
        cell = row4.createCell(12);
        cell.setCellValue("Blood");
        cell.setCellStyle(cellStyle);
        cell = row4.createCell(13);
        cell.setCellValue("Necktag Ayah");
        cell.setCellStyle(cellStyle);
        cell = row4.createCell(14);
        cell.setCellValue("Necktag Ibu");
        cell.setCellStyle(cellStyle);
        cell = row4.createCell(15);
        cell.setCellValue("Bobot Tubuh");
        cell.setCellStyle(cellStyle);
        cell = row4.createCell(16);
        cell.setCellValue("Panjang Tubuh");
        cell.setCellStyle(cellStyle);
        cell = row4.createCell(17);
        cell.setCellValue("Tinggi Tubuh");
        cell.setCellStyle(cellStyle);
        cell = row4.createCell(18);
        cell.setCellValue("Cacat Fisik");
        cell.setCellStyle(cellStyle);
        cell = row4.createCell(19);
        cell.setCellValue("Ciri Lain");
        cell.setCellStyle(cellStyle);
        cell = row4.createCell(20);
        cell.setCellValue("Status Ada");
        cell.setCellStyle(cellStyle);
        cell = row4.createCell(21);
        cell.setCellValue("Created At");
        cell.setCellStyle(cellStyle);
        cell = row4.createCell(22);
        cell.setCellValue("Update At");
        cell.setCellStyle(cellStyle);
        cell = row4.createCell(23);
        cell.setCellValue("Deleted At");
        cell.setCellStyle(cellStyle);

        for(int i = 0; i < ada.size(); i++){
            Row rowx = sheetAda.createRow(i+1);
            TernakModel data = ada.get(i);

            cell = rowx.createCell(0);
            cell.setCellValue(data.getNecktag());
            cell.setCellStyle(cellStyle);
            cell = rowx.createCell(1);
            if(data.getPemilikId() == null){
                cell.setCellValue("null");
            } else{
                cell.setCellValue(data.getPemilikId().toString());
            }
            cell.setCellStyle(cellStyle);
            cell = rowx.createCell(2);
            if(data.getPeternakanId() == null){
                cell.setCellValue("null");
            } else{
                cell.setCellValue(data.getPeternakanId().toString());
            }
            cell.setCellStyle(cellStyle);
            cell = rowx.createCell(3);
            if(data.getRasId() == null){
                cell.setCellValue("null");
            } else{
                cell.setCellValue(data.getRasId().toString());
            }
            cell.setCellStyle(cellStyle);
            cell = rowx.createCell(4);
            if(data.getKematianId() == null){
                cell.setCellValue("null");
            } else{
                cell.setCellValue(data.getKematianId().toString());
            }
            cell.setCellStyle(cellStyle);
            cell = rowx.createCell(5);
            cell.setCellValue(data.getJenisKelamin());
            cell.setCellStyle(cellStyle);
            cell = rowx.createCell(6);
            cell.setCellValue(data.getTglLahir());
            cell.setCellStyle(cellStyle);
            cell = rowx.createCell(7);
            cell.setCellValue(data.getBobotLahir());
            cell.setCellStyle(cellStyle);
            cell = rowx.createCell(8);
            cell.setCellValue(data.getPukulLahir());
            cell.setCellStyle(cellStyle);
            cell = rowx.createCell(9);
            cell.setCellValue(data.getLamaDiKandungan());
            cell.setCellStyle(cellStyle);
            cell = rowx.createCell(10);
            cell.setCellValue(data.getLamaLaktasi());
            cell.setCellStyle(cellStyle);
            cell = rowx.createCell(11);
            cell.setCellValue(data.getTglLepasSapih());
            cell.setCellStyle(cellStyle);
            cell = rowx.createCell(12);
            cell.setCellValue(data.getBlood());
            cell.setCellStyle(cellStyle);
            cell = rowx.createCell(13);
            cell.setCellValue(data.getNecktag_ayah());
            cell.setCellStyle(cellStyle);
            cell = rowx.createCell(14);
            cell.setCellValue(data.getNecktag_ibu());
            cell.setCellStyle(cellStyle);
            cell = rowx.createCell(15);
            cell.setCellValue(data.getBobotTubuh());
            cell.setCellStyle(cellStyle);
            cell = rowx.createCell(16);
            cell.setCellValue(data.getPanjangTubuh());
            cell.setCellStyle(cellStyle);
            cell = rowx.createCell(17);
            cell.setCellValue(data.getTinggiTubuh());
            cell.setCellStyle(cellStyle);
            cell = rowx.createCell(18);
            cell.setCellValue(data.getCacatFisik());
            cell.setCellStyle(cellStyle);
            cell = rowx.createCell(19);
            cell.setCellValue(data.getCiriLain());
            cell.setCellStyle(cellStyle);
            cell = rowx.createCell(20);
            cell.setCellValue(data.getStatusAda());
            cell.setCellStyle(cellStyle);
            cell = rowx.createCell(21);
            cell.setCellValue(data.getCreated_at());
            cell.setCellStyle(cellStyle);
            cell = rowx.createCell(22);
            cell.setCellValue(data.getUpdated_at());
            cell.setCellStyle(cellStyle);
            cell = rowx.createCell(23);
            cell.setCellValue(data.getDeleted_at());
            cell.setCellStyle(cellStyle);

            sheetAda.setColumnWidth(i, (10*200));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

}
