package com.project.siternak.activities.barcode;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.project.siternak.R;
import com.project.siternak.adapter.BarcodeAdapter;
import com.project.siternak.models.data.TernakModel;
import com.project.siternak.responses.TernakGetResponse;
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

public class BarcodeActivity extends AppCompatActivity {
    @BindView(R.id.rv) RecyclerView rvBarcode;
    @BindView(R.id.tv_nodata) TextView tvNodata;

    private BarcodeAdapter barcodeAdapter;
    private ArrayList<TernakModel> arrayList;
    private String userToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_barcode);
        getSupportActionBar().hide();

        ButterKnife.bind(this);

        userToken = SharedPrefManager.getInstance(this).getAccessToken();
        setBarcode();
        setPdf();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @OnClick(R.id.btn_barcode_download)
    public void download(){

    }

    public void setBarcode(){
        SweetAlertDialog pDialog = DialogUtils.getLoadingPopup(this);

        Call<TernakGetResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .getBarcodeTernak("Bearer " + this.userToken);

        call.enqueue(new Callback<TernakGetResponse>() {
            @Override
            public void onResponse(Call<TernakGetResponse> call, Response<TernakGetResponse> response) {
                TernakGetResponse resp = response.body();
                pDialog.cancel();

                if(response.isSuccessful()) {
                    List<TernakModel> ternaks = resp.getTernaks();
                    arrayList = (ArrayList<TernakModel>)ternaks;
                    barcodeAdapter = new BarcodeAdapter(BarcodeActivity.this, arrayList);

                    if (barcodeAdapter.getItemCount() == 0) {
                        tvNodata.setVisibility(View.VISIBLE);
                        tvNodata.setText("Tidak ada data ternak");
                    } else {
                        tvNodata.setVisibility(View.GONE);
                        rvBarcode.setAdapter(barcodeAdapter);
                        rvBarcode.setNestedScrollingEnabled(false);
                        barcodeAdapter.notifyDataSetChanged();
                    }
                }
                else {
                    tvNodata.setVisibility(View.VISIBLE);
                    tvNodata.setText("Code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<TernakGetResponse> call, Throwable t) {
                pDialog.cancel();
                tvNodata.setText(t.getMessage());
                tvNodata.setVisibility(View.VISIBLE);
            }
        });
    }

    private void setPdf(){

    }
}
