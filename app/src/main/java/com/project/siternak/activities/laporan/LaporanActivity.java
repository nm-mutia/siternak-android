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

import com.appeaser.sublimepickerlibrary.datepicker.SelectedDate;
import com.appeaser.sublimepickerlibrary.helpers.SublimeOptions;
import com.appeaser.sublimepickerlibrary.recurrencepicker.SublimeRecurrencePicker;
import com.project.siternak.R;
import com.project.siternak.fragments.DateRangePickerFragment;
import com.project.siternak.models.data.PerkawinanModel;
import com.project.siternak.models.data.RiwayatPenyakitModel;
import com.project.siternak.models.data.TernakMatiModel;
import com.project.siternak.models.data.TernakModel;
import com.project.siternak.responses.LaporanResponse;
import com.project.siternak.rest.RetrofitClient;
import com.project.siternak.utils.DialogUtils;
import com.project.siternak.utils.SharedPrefManager;

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
    @BindView(R.id.tl_laporan) TableLayout tlLaporan;
    @BindView(R.id.spinner_laporan) Spinner spinner;

    private String mDateStart, mDateEnd, userToken;
    private Unbinder unbinder;
    private TableRow.LayoutParams tableRowParams = new TableRow.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_laporan);
        getSupportActionBar().hide();

        ButterKnife.bind(this);

        unbinder = ButterKnife.bind(this);
        userToken = SharedPrefManager.getInstance(this).getAccessToken();

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
                    List<TernakModel> lahir = resp.getLahir();
                    List<TernakMatiModel> mati = resp.getMati();
                    List<PerkawinanModel> kawin = resp.getKawin();
                    List<RiwayatPenyakitModel> sakit = resp.getSakit();
                    List<TernakModel> ada = resp.getAda();

                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            String data = adapterView.getItemAtPosition(i).toString();
                            tlLaporan.removeAllViewsInLayout();
                            switch (data){
                                case "LAHIR":
                                    setLaporanTernak(lahir);
                                    break;
                                case "MATI":
                                    break;
                                case "KAWIN":
                                    break;
                                case "SAKIT":
                                    break;
                                case "TOTAL ADA":
                                    setLaporanTernak(ada);
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

    //untuk laporan lahir dan total ada
    private void setLaporanTernak(List<TernakModel> data){
        tableRowParams.setMargins(40,2, 40, 2);

        TableRow tbrow0 = new TableRow(this);
        tbrow0.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        tbrow0.setLayoutParams(tableRowParams);

        TextView tv0 = new TextView(this);
        tv0.setText(" No. ");
        tv0.setTextColor(Color.WHITE);
        tbrow0.addView(tv0);
        TextView tv1 = new TextView(this);
        tv1.setText(" Necktag ");
        tv1.setTextColor(Color.WHITE);
        tbrow0.addView(tv1);
        TextView tv2 = new TextView(this);
        tv2.setText(" ID\nPemilik ");
        tv2.setTextColor(Color.WHITE);
        tbrow0.addView(tv2);
        TextView tv3 = new TextView(this);
        tv3.setText(" ID\nPeternakan ");
        tv3.setTextColor(Color.WHITE);
        tbrow0.addView(tv3);
        TextView tv4 = new TextView(this);
        tv4.setText(" ID\nRas ");
        tv4.setTextColor(Color.WHITE);
        tbrow0.addView(tv4);
        TextView tv5 = new TextView(this);
        tv5.setText(" ID\nKematian ");
        tv5.setTextColor(Color.WHITE);
        tbrow0.addView(tv5);
        TextView tv6 = new TextView(this);
        tv6.setText(" Jenis\nKelamin ");
        tv6.setTextColor(Color.WHITE);
        tbrow0.addView(tv6);
        TextView tv7 = new TextView(this);
        tv7.setText(" Tanggal\nLahir ");
        tv7.setTextColor(Color.WHITE);
        tbrow0.addView(tv7);
        TextView tv8 = new TextView(this);
        tv8.setText(" Bobot\nLahir ");
        tv8.setTextColor(Color.WHITE);
        tbrow0.addView(tv8);
        TextView tv9 = new TextView(this);
        tv9.setText(" Pukul\nLahir ");
        tv9.setTextColor(Color.WHITE);
        tbrow0.addView(tv9);
        TextView tv10 = new TextView(this);
        tv10.setText(" Lama di\nKandungan ");
        tv10.setTextColor(Color.WHITE);
        tbrow0.addView(tv10);
        TextView tv11 = new TextView(this);
        tv11.setText(" Lama\nLaktasi ");
        tv11.setTextColor(Color.WHITE);
        tbrow0.addView(tv11);
        TextView tv12 = new TextView(this);
        tv12.setText(" Tanggal\nLepas\nSapih ");
        tv12.setTextColor(Color.WHITE);
        tbrow0.addView(tv12);
        TextView tv13 = new TextView(this);
        tv13.setText(" Blood ");
        tv13.setTextColor(Color.WHITE);
        tbrow0.addView(tv13);
        TextView tv14 = new TextView(this);
        tv14.setText(" Ayah ");
        tv14.setTextColor(Color.WHITE);
        tbrow0.addView(tv14);
        TextView tv15 = new TextView(this);
        tv15.setText(" Ibu ");
        tv15.setTextColor(Color.WHITE);
        tbrow0.addView(tv15);
        TextView tv16 = new TextView(this);
        tv16.setText(" Bobot\nTubuh ");
        tv16.setTextColor(Color.WHITE);
        tbrow0.addView(tv16);
        TextView tv17 = new TextView(this);
        tv17.setText(" Panjang\nTubuh ");
        tv17.setTextColor(Color.WHITE);
        tbrow0.addView(tv17);
        TextView tv18 = new TextView(this);
        tv18.setText(" Tinggi\nTubuh ");
        tv18.setTextColor(Color.WHITE);
        tbrow0.addView(tv18);
        TextView tv19 = new TextView(this);
        tv19.setText(" Cacat\nFisik ");
        tv19.setTextColor(Color.WHITE);
        tbrow0.addView(tv19);
        TextView tv20 = new TextView(this);
        tv20.setText(" Ciri\nLain ");
        tv20.setTextColor(Color.WHITE);
        tbrow0.addView(tv20);
        TextView tv21 = new TextView(this);
        tv21.setText(" Status\nAda ");
        tv21.setTextColor(Color.WHITE);
        tbrow0.addView(tv21);
        TextView tv22 = new TextView(this);
        tv22.setText(" Created\nAt ");
        tv22.setTextColor(Color.WHITE);
        tbrow0.addView(tv22);
        TextView tv23 = new TextView(this);
        tv23.setText(" Updated\nAt ");
        tv23.setTextColor(Color.WHITE);
        tbrow0.addView(tv23);
        TextView tv24 = new TextView(this);
        tv24.setText(" Deleted\nAt ");
        tv24.setTextColor(Color.WHITE);
        tbrow0.addView(tv24);

        tlLaporan.addView(tbrow0);

        if(data != null){
            for(int i = 1; i < data.size(); i++){
                TableRow tbrow = new TableRow(this);
                TextView tvi1 = new TextView(this);
                tvi1.setText("" + i);
                tvi1.setTextColor(Color.WHITE);
                tvi1.setGravity(Gravity.CENTER);
                tbrow.addView(tvi1);
                TextView tvi2 = new TextView(this);
                tvi2.setText(data.get(i).getNecktag());
                tvi2.setTextColor(Color.WHITE);
                tvi2.setGravity(Gravity.CENTER);
                tbrow.addView(tvi2);
                TextView tvi3 = new TextView(this);
                tvi3.setText(String.valueOf(data.get(i).getPemilikId()));
                tvi3.setTextColor(Color.WHITE);
                tvi3.setGravity(Gravity.CENTER);
                tbrow.addView(tvi3);
                TextView tvi4 = new TextView(this);
                tvi4.setText(String.valueOf(data.get(i).getPeternakanId()));
                tvi4.setTextColor(Color.WHITE);
                tvi4.setGravity(Gravity.CENTER);
                tbrow.addView(tvi4);
                TextView tvi5 = new TextView(this);
                tvi5.setText(String.valueOf(data.get(i).getRasId()));
                tvi5.setTextColor(Color.WHITE);
                tvi5.setGravity(Gravity.CENTER);
                tbrow.addView(tvi5);
                TextView tvi6 = new TextView(this);
                tvi6.setText(String.valueOf(data.get(i).getKematianId()));
                tvi6.setTextColor(Color.WHITE);
                tvi6.setGravity(Gravity.CENTER);
                tbrow.addView(tvi6);
                TextView tvi7 = new TextView(this);
                tvi7.setText(data.get(i).getJenisKelamin());
                tvi7.setTextColor(Color.WHITE);
                tvi7.setGravity(Gravity.CENTER);
                tbrow.addView(tvi7);
                TextView tvi8 = new TextView(this);
                tvi8.setText(data.get(i).getTglLahir());
                tvi8.setTextColor(Color.WHITE);
                tvi8.setGravity(Gravity.CENTER);
                tbrow.addView(tvi8);
                TextView tvi9 = new TextView(this);
                tvi9.setText(data.get(i).getBobotLahir());
                tvi9.setTextColor(Color.WHITE);
                tvi9.setGravity(Gravity.CENTER);
                tbrow.addView(tvi9);
                TextView tvi10 = new TextView(this);
                tvi10.setText(data.get(i).getPukulLahir());
                tvi10.setTextColor(Color.WHITE);
                tvi10.setGravity(Gravity.CENTER);
                tbrow.addView(tvi10);
                TextView tvi11 = new TextView(this);
                tvi11.setText(data.get(i).getLamaDiKandungan());
                tvi11.setTextColor(Color.WHITE);
                tvi11.setGravity(Gravity.CENTER);
                tbrow.addView(tvi11);
                TextView tvi12 = new TextView(this);
                tvi12.setText(data.get(i).getLamaLaktasi());
                tvi12.setTextColor(Color.WHITE);
                tvi12.setGravity(Gravity.CENTER);
                tbrow.addView(tvi12);
                TextView tvi13 = new TextView(this);
                tvi13.setText(data.get(i).getTglLepasSapih());
                tvi13.setTextColor(Color.WHITE);
                tvi13.setGravity(Gravity.CENTER);
                tbrow.addView(tvi13);
                TextView tvi14 = new TextView(this);
                tvi14.setText(data.get(i).getBlood());
                tvi14.setTextColor(Color.WHITE);
                tvi14.setGravity(Gravity.CENTER);
                tbrow.addView(tvi14);
                TextView tvi15 = new TextView(this);
                tvi15.setText(data.get(i).getNecktag_ayah());
                tvi15.setTextColor(Color.WHITE);
                tvi15.setGravity(Gravity.CENTER);
                tbrow.addView(tvi15);
                TextView tvi16 = new TextView(this);
                tvi16.setText(data.get(i).getNecktag_ibu());
                tvi16.setTextColor(Color.WHITE);
                tvi16.setGravity(Gravity.CENTER);
                tbrow.addView(tvi16);
                TextView tvi17 = new TextView(this);
                tvi17.setText(data.get(i).getBobotTubuh());
                tvi17.setTextColor(Color.WHITE);
                tvi17.setGravity(Gravity.CENTER);
                tbrow.addView(tvi17);
                TextView tvi18 = new TextView(this);
                tvi18.setText(data.get(i).getPanjangTubuh());
                tvi18.setTextColor(Color.WHITE);
                tvi18.setGravity(Gravity.CENTER);
                tbrow.addView(tvi18);
                TextView tvi19 = new TextView(this);
                tvi19.setText(data.get(i).getTinggiTubuh());
                tvi19.setTextColor(Color.WHITE);
                tvi19.setGravity(Gravity.CENTER);
                tbrow.addView(tvi19);
                TextView tvi20 = new TextView(this);
                tvi20.setText(data.get(i).getCacatFisik());
                tvi20.setTextColor(Color.WHITE);
                tvi20.setGravity(Gravity.CENTER);
                tbrow.addView(tvi20);
                TextView tvi21 = new TextView(this);
                tvi21.setText(data.get(i).getCiriLain());
                tvi21.setTextColor(Color.WHITE);
                tvi21.setGravity(Gravity.CENTER);
                tbrow.addView(tvi21);
                TextView tvi22 = new TextView(this);
                tvi22.setText(data.get(i).getStatusAda().toString());
                tvi22.setTextColor(Color.WHITE);
                tvi22.setGravity(Gravity.CENTER);
                tbrow.addView(tvi22);
                TextView tvi23 = new TextView(this);
                tvi23.setText(data.get(i).getCreated_at());
                tvi23.setTextColor(Color.WHITE);
                tvi23.setGravity(Gravity.CENTER);
                tbrow.addView(tvi23);
                TextView tvi24 = new TextView(this);
                tvi24.setText(data.get(i).getUpdated_at());
                tvi24.setTextColor(Color.WHITE);
                tvi24.setGravity(Gravity.CENTER);
                tbrow.addView(tvi24);
                TextView tvi25 = new TextView(this);
                tvi25.setText(data.get(i).getDeleted_at());
                tvi25.setTextColor(Color.WHITE);
                tvi25.setGravity(Gravity.CENTER);
                tbrow.addView(tvi25);

                tbrow.setLayoutParams(tableRowParams);
                tlLaporan.addView(tbrow);
            }
        }
        else {
            TableRow tbrow = new TableRow(this);
            TextView tvi1 = new TextView(this);
            tvi1.setText("Tidak ada data");
            tvi1.setTextColor(Color.WHITE);
            tvi1.setGravity(Gravity.CENTER);
            tbrow.addView(tvi1);

            tbrow.setLayoutParams(tableRowParams);
            tlLaporan.addView(tbrow);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
