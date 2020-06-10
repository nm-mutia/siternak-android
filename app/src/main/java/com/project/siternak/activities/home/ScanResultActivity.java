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

    private ScanResultAdapter adapterP, adapterS, adapterC, adapterGP, adapterGC;
    private ArrayList<Instance> parentArrayList, siblingArrayList, childArrayList, gpArrayList, gcArrayList;

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
        LinearLayoutManager llm = new LinearLayoutManager(this);
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
                        parentArrayList = (ArrayList<Instance>)parent;
                        rvParent.setLayoutManager(new LinearLayoutManager(ScanResultActivity.this));
                        adapterP = new ScanResultAdapter(ScanResultActivity.this, parentArrayList, "Orang Tua");
                        if (adapterP.getItemCount() != 0) {
                            rvParent.setAdapter(adapterP);
                            adapterP.notifyDataSetChanged();
                            rvParent.setNestedScrollingEnabled(false);
                        }

                        List<Instance> sibling = fam.getSibling();
                        siblingArrayList = (ArrayList<Instance>)sibling;
                        rvSibling.setLayoutManager(new LinearLayoutManager(ScanResultActivity.this));
                        adapterS = new ScanResultAdapter(ScanResultActivity.this, siblingArrayList, "Saudara");
                        if (adapterS.getItemCount() != 0) {
                            rvSibling.setAdapter(adapterS);
                            adapterS.notifyDataSetChanged();
                            rvSibling.setNestedScrollingEnabled(false);
                        }

                        List<Instance> child = fam.getChild();
                        childArrayList = (ArrayList<Instance>)child;
                        rvChild.setLayoutManager(new LinearLayoutManager(ScanResultActivity.this));
                        adapterC = new ScanResultAdapter(ScanResultActivity.this, childArrayList, "Anak");
                        if (adapterC.getItemCount() != 0) {
                            rvChild.setAdapter(adapterC);
                            adapterC.notifyDataSetChanged();
                            rvChild.setNestedScrollingEnabled(false);
                        }

                        List<Instance> gparent = fam.getGparent();
                        gpArrayList = (ArrayList<Instance>)gparent;
                        rvGParent.setLayoutManager(new LinearLayoutManager(ScanResultActivity.this));
                        adapterGP = new ScanResultAdapter(ScanResultActivity.this, gpArrayList, "Kakek - Nenek");
                        if (adapterGP.getItemCount() != 0) {
                            rvGParent.setAdapter(adapterGP);
                            adapterGP.notifyDataSetChanged();
                            rvGParent.setNestedScrollingEnabled(false);
                        }

                        List<Instance> gchild = fam.getGchild();
                        gcArrayList = (ArrayList<Instance>)gchild;
                        rvGChild.setLayoutManager(new LinearLayoutManager(ScanResultActivity.this));
                        adapterGC = new ScanResultAdapter(ScanResultActivity.this, gcArrayList, "Cucu");
                        if (adapterGC.getItemCount() != 0) {
                            rvGChild.setAdapter(adapterGC);
                            adapterGC.notifyDataSetChanged();
                            rvGChild.setNestedScrollingEnabled(false);
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
