package com.project.siternak.activities.peternak;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.project.siternak.R;
import com.project.siternak.adapter.PeternakAdapter;
import com.project.siternak.models.peternak.PeternakModel;
import com.project.siternak.responses.PeternakGetResponse;
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

public class PeternakActivity extends AppCompatActivity {
    @BindView(R.id.rv) RecyclerView rvPeternak;
    @BindView(R.id.sv) SearchView sv;
    @BindView(R.id.tv_nodata) TextView tv_nodata;

    private PeternakAdapter peternakAdapter;
    private ArrayList<PeternakModel> peternakArrayList;
    private String userToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_peternak);
        getSupportActionBar().hide();

        ButterKnife.bind(this);

        userToken = SharedPrefManager.getInstance(this).getAccessToken();
        rvPeternak.setLayoutManager(new LinearLayoutManager(this));
        setDataPeternak();

        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                peternakAdapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @OnClick(R.id.ib_add_data)
    public void addData(){
        Intent intent = new Intent(PeternakActivity.this, PeternakAddActivity.class);
        startActivity(intent);
    }

    private void setDataPeternak() {
        SweetAlertDialog pDialog = DialogUtils.getLoadingPopup(this);

        Call<PeternakGetResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .getPeternak("Bearer " + this.userToken);

        call.enqueue(new Callback<PeternakGetResponse>() {
            @Override
            public void onResponse(Call<PeternakGetResponse> call, Response<PeternakGetResponse> response) {
                PeternakGetResponse resp = response.body();
                pDialog.cancel();

                if(response.isSuccessful()) {
                    List<PeternakModel> peternaks = resp.getPeternaks();
                    peternakArrayList = (ArrayList<PeternakModel>)peternaks;
                    peternakAdapter = new PeternakAdapter(PeternakActivity.this, peternakArrayList);

                    if (peternakAdapter.getItemCount() == 0) {
                        tv_nodata.setVisibility(View.VISIBLE);
                        tv_nodata.setText("Tidak ada data peternak");
                    } else {
                        tv_nodata.setVisibility(View.GONE);
                        rvPeternak.setAdapter(peternakAdapter);
                        rvPeternak.setNestedScrollingEnabled(false);
                        peternakAdapter.notifyDataSetChanged();
                    }
                }
                else {
                    tv_nodata.setVisibility(View.VISIBLE);
                    tv_nodata.setText("Code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<PeternakGetResponse> call, Throwable t) {
                pDialog.cancel();
                tv_nodata.setText(t.getMessage());
                tv_nodata.setVisibility(View.VISIBLE);
            }
        });
    }
}
