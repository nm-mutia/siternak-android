package com.project.siternak.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.project.siternak.R;
import com.project.siternak.responses.GrafikResponse;
import com.project.siternak.responses.GrafikYearResponse;
import com.project.siternak.rest.RetrofitClient;
import com.project.siternak.utils.DialogUtils;
import com.project.siternak.utils.SharedPrefManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GrafikFragment extends Fragment {
    @BindView(R.id.bar_chart1) BarChart barChart1;
    @BindView(R.id.bar_chart2) BarChart barChart2;
    @BindView(R.id.combined_chart1) CombinedChart combinedChart1;
    @BindView(R.id.combined_chart2) CombinedChart combinedChart2;
    @BindView(R.id.spinner_chart) Spinner spinner;

    private Unbinder unbinder;
    private String userToken;
    private List<Entry> dataLine;
    private List<BarEntry> dataBar, jantanBar, betinaBar;
    private List<String> label, data, jantan, betina;
    private boolean isSpinnerinitial = true;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_grafik, container, false);
        unbinder = ButterKnife.bind(this, view);

        ButterKnife.bind(getActivity());

        userToken = SharedPrefManager.getInstance(getActivity()).getAccessToken();
        String chart = getArguments().getString("grafik");
        getGrafik(chart);

        if(chart.equals("lahir") || chart.equals("mati")){
            setSpinner();
        }

        return view;
    }

    private void setSpinner(){
        ArrayList<String> years = new ArrayList<>();
        int yearNow = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = yearNow; i > yearNow - 5; i--){
            years.add(Integer.toString(i));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, years);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private void getGrafik(String grafik){
        SweetAlertDialog pDialog = DialogUtils.getLoadingPopup(getActivity());

        Call<GrafikResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .getGrafikData("Bearer " + this.userToken);

        call.enqueue(new Callback<GrafikResponse>() {
            @Override
            public void onResponse(Call<GrafikResponse> call, Response<GrafikResponse> response) {
                GrafikResponse resp = response.body();
                pDialog.cancel();

                if(response.isSuccessful()){
                    barChart1.setVisibility(View.GONE);
                    barChart2.setVisibility(View.GONE);
                    combinedChart1.setVisibility(View.GONE);
                    combinedChart2.setVisibility(View.GONE);
                    spinner.setVisibility(View.GONE);

                    dataLine = new ArrayList<>();
                    dataBar = new ArrayList<>();
                    jantanBar = new ArrayList<>();
                    betinaBar = new ArrayList<>();

                    Description desc = new Description();

                    switch(grafik){
                        case "umur":
                            label = resp.getUmur().getLabel();
                            data = resp.getUmur().getData();
                            jantan = resp.getUmur().getJantan();
                            betina = resp.getUmur().getBetina();

                            for(int i = 0; i < label.size(); i++) {
                                dataBar.add(new BarEntry(i, Integer.valueOf(data.get(i))));
                                jantanBar.add(new BarEntry(i, Integer.valueOf(jantan.get(i))));
                                betinaBar.add(new BarEntry(i, Integer.valueOf(betina.get(i))));
                            }

                            setBarChart(barChart1);

                            desc.setText("Grafik jumlah ternak berdasarkan umur");
                            barChart1.setDescription(desc);
                            barChart1.invalidate();
                            break;
                        case "ras":
                            label = resp.getRas().getLabel();
                            data = resp.getRas().getData();
                            jantan = resp.getRas().getJantan();
                            betina = resp.getRas().getBetina();

                            for(int i = 0; i < label.size(); i++) {
                                dataBar.add(new BarEntry(i, Integer.valueOf(data.get(i))));
                                jantanBar.add(new BarEntry(i, Integer.valueOf(jantan.get(i))));
                                betinaBar.add(new BarEntry(i, Integer.valueOf(betina.get(i))));
                            }

                            setBarChart(barChart2);

                            desc.setText("Grafik jumlah ternak berdasarkan ras");
                            barChart2.setDescription(desc);
                            barChart2.invalidate();
                            break;
                        case "lahir":
                            spinner.setVisibility(View.VISIBLE);

                            label = resp.getLahir().getLabel();
                            data = resp.getLahir().getData();
                            jantan = resp.getLahir().getJantan();
                            betina = resp.getLahir().getBetina();

                            for(int i = 0; i < label.size(); i++) {
                                dataLine.add(new Entry(i, Integer.valueOf(data.get(i))));
                                jantanBar.add(new BarEntry(i, Integer.valueOf(jantan.get(i))));
                                betinaBar.add(new BarEntry(i, Integer.valueOf(betina.get(i))));
                            }

                            setCombinedChart(combinedChart1);
                            desc.setText("Grafik jumlah ternak berdasarkan kelahiran");
                            combinedChart1.setDescription(desc);
                            combinedChart1.notifyDataSetChanged();
                            combinedChart1.invalidate();

                            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    if(isSpinnerinitial){
                                        isSpinnerinitial = false;
                                    }
                                    else {
                                        Integer year = Integer.valueOf(adapterView.getItemAtPosition(i).toString());
                                        changeYearLahir(year);
                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {}
                            });
                            break;
                        case "mati":
                            spinner.setVisibility(View.VISIBLE);

                            label = resp.getMati().getLabel();
                            data = resp.getMati().getData();
                            jantan = resp.getMati().getJantan();
                            betina = resp.getMati().getBetina();

                            for(int i = 0; i < label.size(); i++) {
                                dataLine.add(new Entry(i, Integer.valueOf(data.get(i))));
                                jantanBar.add(new BarEntry(i, Integer.valueOf(jantan.get(i))));
                                betinaBar.add(new BarEntry(i, Integer.valueOf(betina.get(i))));
                            }

                            setCombinedChart(combinedChart2);
                            desc.setText("Grafik jumlah ternak berdasarkan kematian");
                            combinedChart2.setDescription(desc);
                            combinedChart2.notifyDataSetChanged();
                            combinedChart2.invalidate();

                            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    if(isSpinnerinitial){
                                        isSpinnerinitial = false;
                                    }
                                    else {
                                        Integer year = Integer.valueOf(adapterView.getItemAtPosition(i).toString());
                                        changeYearMati(year);
                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {}
                            });
                            break;
                        default:
                            break;
                    }
                }
                else {
                    Toast.makeText(getActivity(), response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GrafikResponse> call, Throwable t) {
                pDialog.cancel();
                Toast.makeText(getActivity(), "Gagal", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private BarData setDataBarChart(){
        BarDataSet set1 = new BarDataSet(dataBar, "Jumlah Ternak");
        BarDataSet set2 = new BarDataSet(jantanBar, "Jantan");
        BarDataSet set3 = new BarDataSet(betinaBar, "Betina");
        set1.setColor(Color.parseColor("#B2DFDB"));
        set2.setColor(Color.parseColor("#36A7C9"));
        set3.setColor(Color.parseColor("#F8B195"));

        BarData datas = new BarData();
        datas.addDataSet(set1);
        datas.addDataSet(set2);
        datas.addDataSet(set3);

        return datas;
    }

    private void setBarChart(BarChart barChart){
        float groupSpace = 0.04f;
        float barSpace = 0.02f;
        float barWidth = 0.3f;

        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(label));
        xAxis.setGranularity(1f);
        xAxis.setCenterAxisLabels(true);
        xAxis.setAxisMinimum(0f);

        YAxis yAxis = barChart.getAxisLeft();
        yAxis.setGranularity(1f);
        yAxis.setAxisMinimum(0f);

        YAxis yrAxis = barChart.getAxisRight();
        yrAxis.setGranularity(1f);
        yrAxis.setAxisMinimum(0f);

        barChart.setVisibility(View.VISIBLE);
        barChart.setData(setDataBarChart());
        barChart.setDrawValueAboveBar(true);
        barChart.setFitBars(false);
        barChart.setVisibleXRangeMaximum(5);
        barChart.getBarData().setBarWidth(barWidth);
        barChart.groupBars(0f, groupSpace, barSpace);
        barChart.getXAxis().setAxisMaximum(setDataBarChart().getXMax() + 0.25f);
    }

    private CombinedData setDataCombinedChart(){
        LineDataSet set1 = new LineDataSet(dataLine, "Jumlah Ternak");
        BarDataSet set2 = new BarDataSet(jantanBar, "Jantan");
        BarDataSet set3 = new BarDataSet(betinaBar, "Betina");
        set1.setColor(Color.parseColor("#B2DFDB"));
        set2.setColor(Color.parseColor("#36A7C9"));
        set3.setColor(Color.parseColor("#F8B195"));

        LineData lineData = new LineData();
        BarData barData = new BarData();
        lineData.addDataSet(set1);
        barData.addDataSet(set2);
        barData.addDataSet(set3);

        CombinedData combinedData = new CombinedData();
        combinedData.setData(barData);
        combinedData.setData(lineData);

        return combinedData;
    }

    private void setCombinedChart(CombinedChart combinedChart){
        float groupSpace = 0.04f;
        float barSpace = 0.02f;
        float barWidth = 0.46f;

        XAxis xAxis = combinedChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(label));
        xAxis.setGranularity(1f);
        xAxis.setCenterAxisLabels(true);
        xAxis.setAxisMinimum(0f);

        YAxis yAxis = combinedChart.getAxisLeft();
        yAxis.setGranularity(1f);
        yAxis.setAxisMinimum(-0.1f);

        YAxis yrAxis = combinedChart.getAxisRight();
        yrAxis.setGranularity(1f);
        yrAxis.setAxisMinimum(-0.1f);

        combinedChart.setVisibility(View.VISIBLE);
        combinedChart.setData(setDataCombinedChart());
        combinedChart.getBarData().setBarWidth(barWidth);
        combinedChart.getBarData().groupBars(0f, groupSpace, barSpace);
        combinedChart.setVisibleXRangeMaximum(4);
        combinedChart.getXAxis().setAxisMinimum(setDataCombinedChart().getXMin() - 0.25f);
        combinedChart.getXAxis().setAxisMaximum(setDataCombinedChart().getXMax() + 0.5f);
    }

    private void changeYearLahir(Integer year){
        SweetAlertDialog pDialog = DialogUtils.getLoadingPopup(getActivity());

        Call<GrafikYearResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .getGrafikLahirData("Bearer " + this.userToken, year);

        call.enqueue(new Callback<GrafikYearResponse>() {
            @Override
            public void onResponse(Call<GrafikYearResponse> call, Response<GrafikYearResponse> response) {
                GrafikYearResponse resp = response.body();
                pDialog.cancel();

                if(response.isSuccessful()){
                    updateCombinedChart(combinedChart1, resp);

                    Description desc = new Description();
                    desc.setText("Grafik jumlah ternak berdasarkan kelahiran");
                    combinedChart1.setDescription(desc);
                    combinedChart1.notifyDataSetChanged();
                    combinedChart1.invalidate();
                }
            }

            @Override
            public void onFailure(Call<GrafikYearResponse> call, Throwable t) {
                pDialog.cancel();
                Toast.makeText(getActivity(), "Gagal", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void changeYearMati(Integer year){
        SweetAlertDialog pDialog = DialogUtils.getLoadingPopup(getActivity());

        Call<GrafikYearResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .getGrafikMatiData("Bearer " + this.userToken, year);

        call.enqueue(new Callback<GrafikYearResponse>() {
            @Override
            public void onResponse(Call<GrafikYearResponse> call, Response<GrafikYearResponse> response) {
                GrafikYearResponse resp = response.body();
                pDialog.cancel();

                if(response.isSuccessful()) {
                    updateCombinedChart(combinedChart2, resp);

                    Description desc = new Description();
                    desc.setText("Grafik jumlah ternak berdasarkan kematian");
                    combinedChart2.setDescription(desc);
                    combinedChart2.notifyDataSetChanged();
                    combinedChart2.invalidate();
                }
            }

            @Override
            public void onFailure(Call<GrafikYearResponse> call, Throwable t) {
                pDialog.cancel();
                Toast.makeText(getActivity(), "Gagal", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateCombinedChart(CombinedChart combinedChart, GrafikYearResponse resp){
        dataLine.clear();
        jantanBar.clear();
        betinaBar.clear();

        label = resp.getData().getLabel();
        data = resp.getData().getData();
        jantan = resp.getData().getJantan();
        betina = resp.getData().getBetina();

        for(int i = 0; i < label.size(); i++) {
            dataLine.add(new Entry(i, Integer.valueOf(data.get(i))));
            jantanBar.add(new BarEntry(i, Integer.valueOf(jantan.get(i))));
            betinaBar.add(new BarEntry(i, Integer.valueOf(betina.get(i))));
        }

        combinedChart.invalidate();
        combinedChart.clear();
        combinedChart.setData(setDataCombinedChart());
        setCombinedChart(combinedChart);
    }
}
