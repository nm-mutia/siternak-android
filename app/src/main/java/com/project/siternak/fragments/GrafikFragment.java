package com.project.siternak.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.project.siternak.R;
import com.project.siternak.responses.GrafikResponse;
import com.project.siternak.rest.RetrofitClient;
import com.project.siternak.utils.DialogUtils;
import com.project.siternak.utils.SharedPrefManager;

import java.util.ArrayList;
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

    private Unbinder unbinder;
    private String userToken;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_grafik, container, false);
        unbinder = ButterKnife.bind(this, view);

        ButterKnife.bind(getActivity());

        userToken = SharedPrefManager.getInstance(getActivity()).getAccessToken();
        getGrafik(getArguments().getString("grafik"));

        return view;
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

                    float groupSpace = 0.04f;
                    float barSpace = 0.02f;
                    float barWidth = 0.3f;

                    if(grafik.equals("umur")){
                        List<String> umLabel = resp.getUmur().getLabel();
                        List<String> umData = resp.getUmur().getData();
                        List<String> umJantan = resp.getUmur().getJantan();
                        List<String> umBetina = resp.getUmur().getBetina();

                        List<BarEntry> umurData = new ArrayList<>();
                        List<BarEntry> umurJantan = new ArrayList<>();
                        List<BarEntry> umurBetina = new ArrayList<>();

                        for(int i = 0; i < umLabel.size(); i++) {
                            umurData.add(new BarEntry(i, Integer.valueOf(umData.get(i))));
                            umurJantan.add(new BarEntry(i, Integer.valueOf(umJantan.get(i))));
                            umurBetina.add(new BarEntry(i, Integer.valueOf(umBetina.get(i))));
                        }

                        BarDataSet set1 = new BarDataSet(umurData, "Jumlah Ternak");
                        set1.setColor(Color.parseColor("#B2DFDB"));
                        BarDataSet set2 = new BarDataSet(umurJantan, "Jantan");
                        set2.setColor(Color.parseColor("#36A7C9"));
                        BarDataSet set3 = new BarDataSet(umurBetina, "Betina");
                        set3.setColor(Color.parseColor("#F8B195"));

                        BarData data = new BarData();
                        data.addDataSet(set1);
                        data.addDataSet(set2);
                        data.addDataSet(set3);

                        XAxis xAxis = barChart1.getXAxis();
                        xAxis.setValueFormatter(new IndexAxisValueFormatter(umLabel));
                        xAxis.setCenterAxisLabels(true);
//                        xAxis.setSpaceMin(data.getBarWidth() / 2f);
//                        xAxis.setSpaceMax(data.getBarWidth() / 2f);

                        barChart1.setVisibility(View.VISIBLE);
//                        barChart.setDrawBarShadow(false);
//                        barChart.setDrawValueAboveBar(true);
//                        barChart.setDrawGridBackground(false);

                        barChart1.setData(data);
                        barChart1.setFitBars(true);
                        barChart1.setVisibleXRangeMaximum(6);
                        barChart1.getBarData().setBarWidth(barWidth);
                        barChart1.groupBars(0f, groupSpace, barSpace);

                        Description desc = new Description();
                        desc.setText("Grafik jumlah ternak berdasarkan umur");
                        barChart1.setDescription(desc);
                        barChart1.invalidate();
                    }

                    else if(grafik.equals("ras")){
                        List<String> rasLabel = resp.getRas().getLabel();
                        List<String> rasData = resp.getRas().getData();
                        List<String> rasJantan = resp.getRas().getJantan();
                        List<String> rasBetina = resp.getRas().getBetina();

                        List<BarEntry> rasDataBar = new ArrayList<>();
                        List<BarEntry> rasJantanBar = new ArrayList<>();
                        List<BarEntry> rasBetinaBar = new ArrayList<>();

                        for(int i = 0; i < rasLabel.size(); i++) {
                            rasDataBar.add(new BarEntry(i, Integer.valueOf(rasData.get(i))));

                            if(rasJantan.get(i) == null){
                                rasJantanBar.add(new BarEntry(i, 0f));
                            }
                            else {
                                rasJantanBar.add(new BarEntry(i, Integer.valueOf(rasJantan.get(i))));
                            }

                            if(rasBetina.get(i) == null){
                                rasBetinaBar.add(new BarEntry(i, 0f));
                            }
                            else {
                                rasBetinaBar.add(new BarEntry(i, Integer.valueOf(rasBetina.get(i))));
                            }
                        }

                        BarDataSet set1 = new BarDataSet(rasDataBar, "Jumlah Ternak");
                        set1.setColor(Color.parseColor("#B2DFDB"));
                        BarDataSet set2 = new BarDataSet(rasJantanBar, "Jantan");
                        set2.setColor(Color.parseColor("#36A7C9"));
                        BarDataSet set3 = new BarDataSet(rasBetinaBar, "Betina");
                        set3.setColor(Color.parseColor("#F8B195"));

                        BarData dataRas = new BarData();
                        dataRas.addDataSet(set1);
                        dataRas.addDataSet(set2);
                        dataRas.addDataSet(set3);

                        XAxis xAxis = barChart2.getXAxis();
                        xAxis.setValueFormatter(new IndexAxisValueFormatter(rasLabel));
                        xAxis.setCenterAxisLabels(true);

                        barChart2.setVisibility(View.VISIBLE);

                        barChart2.setData(dataRas);
                        barChart2.setFitBars(true);
                        barChart2.setVisibleXRangeMaximum(6);
                        barChart2.getBarData().setBarWidth(barWidth);
                        barChart2.groupBars(0f, groupSpace, barSpace);

                        Description desc = new Description();
                        desc.setText("Grafik jumlah ternak berdasarkan ras");
                        barChart2.setDescription(desc);
                        barChart2.invalidate();
                    }

                    else if(grafik.equals("lahir")){

                    }

                    else if(grafik.equals("mati")){

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
}
