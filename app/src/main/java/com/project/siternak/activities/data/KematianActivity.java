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
import com.project.siternak.adapter.data.KematianAdapter;
import com.project.siternak.rest.RetrofitClient;
import com.project.siternak.responses.KematianGetResponse;
import com.project.siternak.models.data.KematianModel;
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

public class KematianActivity extends AppCompatActivity {
    @BindView(R.id.rv) RecyclerView rv_kematian;
    @BindView(R.id.tv_nodata) TextView tv_nodata;

    private KematianAdapter kematianAdapter;
    private ArrayList<KematianModel> kematianArrayList;

    private String userToken;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_list);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.actionbar_primary_arrow);

        TextView tv_actionbar_title = getSupportActionBar().getCustomView().findViewById(R.id.tv_actionbar_title);
        tv_actionbar_title.setText("Kematian");

        ButterKnife.bind(this);

        userToken = SharedPrefManager.getInstance(this).getAccessToken();
        rv_kematian.setLayoutManager(new LinearLayoutManager(this));
        setDataKematian();
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
        Intent intent = new Intent(KematianActivity.this, KematianAddActivity.class);
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
                kematianAdapter.getFilter().filter(newText);
                return false;
            }
        });

        return true;
    }

    private void setDataKematian() {
        SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Mohon Tunggu");
        pDialog.setCancelable(false);
        pDialog.show();

        Call<KematianGetResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .getKematian("Bearer " + this.userToken);

        call.enqueue(new Callback<KematianGetResponse>() {
            @Override
            public void onResponse(Call<KematianGetResponse> call, Response<KematianGetResponse> response) {
                KematianGetResponse resp = response.body();
                pDialog.cancel();

                if(response.isSuccessful()) {
                    List<KematianModel> kematians = resp.getKematians();
                    kematianArrayList = (ArrayList<KematianModel>)kematians;
                    kematianAdapter = new KematianAdapter(KematianActivity.this, kematianArrayList);

                    if (kematianAdapter.getItemCount() == 0) {
                        tv_nodata.setVisibility(View.VISIBLE);
                        tv_nodata.setText("Tidak ada data kematian");
                    } else {
                        tv_nodata.setVisibility(View.GONE);
                        rv_kematian.setAdapter(kematianAdapter);
                        kematianAdapter.notifyDataSetChanged();
                    }
                }
                else {
                    tv_nodata.setVisibility(View.VISIBLE);
                    tv_nodata.setText("Code: " + response.code());
                }

            }
            @Override
            public void onFailure(Call<KematianGetResponse> call, Throwable t) {
                pDialog.cancel();
                tv_nodata.setText(t.getMessage());
                tv_nodata.setVisibility(View.VISIBLE);
            }
        });
    }
}
