package com.project.siternak.activities.data;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.project.siternak.R;
import com.project.siternak.adapter.data.PemilikAdapter;
import com.project.siternak.models.data.PemilikModel;
import com.project.siternak.responses.PemilikGetResponse;
import com.project.siternak.rest.RetrofitClient;
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

public class PemilikActivity extends AppCompatActivity {
    @BindView(R.id.rv) RecyclerView rv_pemilik;
    @BindView(R.id.tv_nodata) TextView tv_nodata;

    private PemilikAdapter pemilikAdapter;
    private ArrayList<PemilikModel> pemilikArrayList;

    private String userToken;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_list);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.actionbar_primary_arrow);

        TextView tv_actionbar_title = getSupportActionBar().getCustomView().findViewById(R.id.tv_actionbar_title);
        tv_actionbar_title.setText("Pemilik");

        ButterKnife.bind(this);

        userToken = SharedPrefManager.getInstance(this).getAccessToken();
        rv_pemilik.setLayoutManager(new LinearLayoutManager(this));
        setDataPemilik();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @OnClick(R.id.ib_actionbar_close)
    public void close(){
        finish();
    }

    @OnClick(R.id.ib_add_data)
    public void addData(){
        Intent intent = new Intent(PemilikActivity.this, PemilikAddActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                pemilikAdapter.getFilter().filter(newText);
                return false;
            }
        });

        return true;
    }

    private void setDataPemilik() {
        SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Mohon Tunggu");
        pDialog.setCancelable(false);
        pDialog.show();

        Call<PemilikGetResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .getPemilik("Bearer " + this.userToken);

        call.enqueue(new Callback<PemilikGetResponse>() {
            @Override
            public void onResponse(Call<PemilikGetResponse> call, Response<PemilikGetResponse> response) {
                PemilikGetResponse resp = response.body();

                if(response.isSuccessful()) {
                    List<PemilikModel> pemiliks = resp.getPemiliks();
                    pemilikArrayList = (ArrayList<PemilikModel>)pemiliks;

                    pDialog.cancel();
                    pemilikAdapter = new PemilikAdapter(PemilikActivity.this, pemilikArrayList);

                    if (pemilikAdapter.getItemCount() == 0) {
                        tv_nodata.setVisibility(View.VISIBLE);
                        tv_nodata.setText("Tidak ada data pemilik");
                    } else {
                        tv_nodata.setVisibility(View.GONE);
                        rv_pemilik.setAdapter(pemilikAdapter);
                        pemilikAdapter.notifyDataSetChanged();
                    }
                }
                else {
                    pDialog.cancel();
                    tv_nodata.setVisibility(View.VISIBLE);
                    tv_nodata.setText("Code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<PemilikGetResponse> call, Throwable t) {
                pDialog.cancel();
                tv_nodata.setText(t.getMessage());
                tv_nodata.setVisibility(View.VISIBLE);
            }
        });
    }
}
