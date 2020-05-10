package com.project.siternak.activities.home;

import android.os.Bundle;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.project.siternak.R;
import com.project.siternak.adapter.ScanResultAdapter;
import com.project.siternak.models.scan.Family;
import com.project.siternak.models.scan.Instance;
import com.project.siternak.responses.ScanResponse;
import com.project.siternak.rest.RetrofitClient;
import com.project.siternak.utils.DialogUtils;
import com.project.siternak.utils.SharedPrefManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScanResultActivity extends AppCompatActivity {
    @BindView(R.id.tl_scan_result) TableLayout tlScanResult;
    @BindView(R.id.rv_parent) RecyclerView rvParent;
    @BindView(R.id.rv_sibling) RecyclerView rvSibling;
    @BindView(R.id.rv_child) RecyclerView rvChild;
    @BindView(R.id.rv_gparent) RecyclerView rvGParent;
    @BindView(R.id.rv_gchild) RecyclerView rvGChild;
    @BindView(R.id.tv_nodata) TextView tv_nodata;

    @BindView(R.id.tv_inst_title) TextView tvTitle;
    @BindView(R.id.tv_inst_necktag) TextView tvNecktag;
    @BindView(R.id.tv_inst_jk) TextView tvJk;
    @BindView(R.id.tv_inst_ras) TextView tvRas;
    @BindView(R.id.tv_inst_tgl_lahir) TextView tvTglLahir;
    @BindView(R.id.tv_inst_blood) TextView tvBlood;
    @BindView(R.id.tv_inst_peternakan) TextView tvPeternakan;
    @BindView(R.id.tv_inst_ayah) TextView tvAyah;
    @BindView(R.id.tv_inst_ibu) TextView tvIbu;

    private ScanResultAdapter scanAdapter, adapterP, adapterS, adapterC, adapterGP, adapterGC;
    private ArrayList<Instance> instanceArrayList;

    private String userToken, necktag;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_result);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.actionbar_primary_arrow);

        TextView tv_actionbar_title = getSupportActionBar().getCustomView().findViewById(R.id.tv_actionbar_title);
        tv_actionbar_title.setText("Detail Ternak");

        ButterKnife.bind(this);

        necktag = getIntent().getStringExtra("result");
        userToken = SharedPrefManager.getInstance(this).getAccessToken();

        setDetailTernak();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @OnClick(R.id.ib_actionbar_close)
    public void close(){
        onBackPressed();
    }

    public void setDetailTernak() {
        SweetAlertDialog pDialog = DialogUtils.getLoadingPopup(this);

        Call<ScanResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .getScanResult("Bearer " + this.userToken, necktag);

        call.enqueue(new Callback<ScanResponse>() {
            @Override
            public void onResponse(Call<ScanResponse> call, Response<ScanResponse> response) {
                ScanResponse resp = response.body();
                pDialog.cancel();

                if(response.isSuccessful()) {
                    if(resp.getStatus().equals("error")){
                        tlScanResult.setVisibility(View.GONE);
                        tv_nodata.setVisibility(View.VISIBLE);
                        tv_nodata.setText(resp.getMessage());
                    }
                    else{
                        tv_nodata.setVisibility(View.GONE);
                        tlScanResult.setVisibility(View.VISIBLE);
                        Family fam = resp.getResult();

                        Instance inst = fam.getInst();
                        tvTitle.setText("Instance - " + inst.getNecktag());
                        tvNecktag.setText(inst.getNecktag());
                        tvJk.setText(inst.getJenisKelamin());
                        tvRas.setText(inst.getRas());
                        tvTglLahir.setText(inst.getTglLahir());
                        tvBlood.setText(inst.getBlood());
                        tvPeternakan.setText(inst.getPeternakan());
                        tvAyah.setText(inst.getAyah());
                        tvIbu.setText(inst.getIbu());

                        List<Instance> parent = fam.getParent();
                        rvParent.setLayoutManager(new LinearLayoutManager(ScanResultActivity.this));
                        instanceArrayList = (ArrayList<Instance>)parent;
                        scanAdapter = new ScanResultAdapter(ScanResultActivity.this, instanceArrayList, "Orang Tua");
                        if (scanAdapter.getItemCount() != 0) {
                            rvParent.setAdapter(scanAdapter);
                            scanAdapter.notifyDataSetChanged();
                        }

                        List<Instance> sibling = fam.getSibling();
                        rvSibling.setLayoutManager(new LinearLayoutManager(ScanResultActivity.this));
                        instanceArrayList = (ArrayList<Instance>)sibling;
                        scanAdapter = new ScanResultAdapter(ScanResultActivity.this, instanceArrayList, "Saudara");
                        if (scanAdapter.getItemCount() != 0) {
                            rvSibling.setAdapter(scanAdapter);
                            scanAdapter.notifyDataSetChanged();
                        }

                        List<Instance> child = fam.getChild();
                        rvChild.setLayoutManager(new LinearLayoutManager(ScanResultActivity.this));
                        instanceArrayList = (ArrayList<Instance>)child;
                        scanAdapter = new ScanResultAdapter(ScanResultActivity.this, instanceArrayList, "Anak");
                        if (scanAdapter.getItemCount() != 0) {
                            rvChild.setAdapter(scanAdapter);
                            scanAdapter.notifyDataSetChanged();
                        }

                        List<Instance> gparent = fam.getGparent();
                        rvGParent.setLayoutManager(new LinearLayoutManager(ScanResultActivity.this));
                        instanceArrayList = (ArrayList<Instance>)gparent;
                        scanAdapter = new ScanResultAdapter(ScanResultActivity.this, instanceArrayList, "Kakek - Nenek");
                        if (scanAdapter.getItemCount() != 0) {
                            rvGParent.setAdapter(scanAdapter);
                            scanAdapter.notifyDataSetChanged();
                        }

                        List<Instance> gchild = fam.getGchild();
                        rvGChild.setLayoutManager(new LinearLayoutManager(ScanResultActivity.this));
                        instanceArrayList = (ArrayList<Instance>)gchild;
                        scanAdapter = new ScanResultAdapter(ScanResultActivity.this, instanceArrayList, "Cucu");
                        if (scanAdapter.getItemCount() != 0) {
                            rvGChild.setAdapter(scanAdapter);
                            scanAdapter.notifyDataSetChanged();
                        }

                    }
                }
                else {
                    tv_nodata.setVisibility(View.VISIBLE);
                    tv_nodata.setText("Code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ScanResponse> call, Throwable t) {
                pDialog.cancel();
                tv_nodata.setText(t.getMessage());
                tv_nodata.setVisibility(View.VISIBLE);
            }
        });
    }
}
