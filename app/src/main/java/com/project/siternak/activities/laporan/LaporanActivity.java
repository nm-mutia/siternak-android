package com.project.siternak.activities.laporan;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
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
    private boolean isSpinnerinitial = true;

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

    @OnClick(R.id.btn_laporan_download)
    public void download(){
        setExcel();
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
        Workbook wb = new HSSFWorkbook();
        Cell cell = null;
        CellStyle cellStyle = wb.createCellStyle();
        cellStyle.setFillBackgroundColor(HSSFColor.LIGHT_BLUE.index);

        Sheet sheetLahir = null;
        sheetLahir = wb.createSheet("Nama seet");

        Row row = sheetLahir.createRow(0);

        cell = row.createCell(0);
        cell.setCellValue("kolom1");
        cell.setCellStyle(cellStyle);

        cell = row.createCell(1);
        cell.setCellValue("kolom2");
        cell.setCellStyle(cellStyle);

        sheetLahir.setColumnWidth(0, (10*200));
        sheetLahir.setColumnWidth(1, (10*200));

        File file = new File(getExternalFilesDir(null), "SITERNAK_LAPORAN.xls");
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(file);
            wb.write(fileOutputStream);
            Toast.makeText(getApplicationContext(), "OK", Toast.LENGTH_LONG).show();
        } catch (java.io.IOException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "NOT OK", Toast.LENGTH_LONG).show();
            try {
                fileOutputStream.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

}
