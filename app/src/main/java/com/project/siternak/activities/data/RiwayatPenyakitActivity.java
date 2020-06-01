package com.project.siternak.activities.data;

import android.content.Intent;
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
import com.project.siternak.adapter.RiwayatPenyakitAdapter;
import com.project.siternak.models.data.RiwayatPenyakitModel;
import com.project.siternak.responses.RiwayatPenyakitGetResponse;
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

public class RiwayatPenyakitActivity extends AppCompatActivity {
    @BindView(R.id.rv)
    RecyclerView rv_riwayat;
    @BindView(R.id.tv_nodata)
    TextView tv_nodata;

    private RiwayatPenyakitAdapter riwayatAdapter;
    private ArrayList<RiwayatPenyakitModel> riwayatArrayList;

    private String userToken;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_list);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.actionbar_primary_arrow);

        TextView tv_actionbar_title = getSupportActionBar().getCustomView().findViewById(R.id.tv_actionbar_title);
        tv_actionbar_title.setText("Riwayat Penyakit");

        ButterKnife.bind(this);

        userToken = SharedPrefManager.getInstance(this).getAccessToken();
        rv_riwayat.setLayoutManager(new LinearLayoutManager(this));
        setDataRiwayat();
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
        Intent intent = new Intent(RiwayatPenyakitActivity.this, RiwayatPenyakitAddActivity.class);
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
                riwayatAdapter.getFilter().filter(newText);
                return false;
            }
        });

        return true;
    }

    private void setDataRiwayat() {
        SweetAlertDialog pDialog = DialogUtils.getLoadingPopup(this);

        Call<RiwayatPenyakitGetResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .getRiwayat("Bearer " + this.userToken);

        call.enqueue(new Callback<RiwayatPenyakitGetResponse>() {
            @Override
            public void onResponse(Call<RiwayatPenyakitGetResponse> call, Response<RiwayatPenyakitGetResponse> response) {
                RiwayatPenyakitGetResponse resp = response.body();
                pDialog.cancel();

                if(response.isSuccessful()) {
                    List<RiwayatPenyakitModel> riwayats = resp.getRiwayats();
                    riwayatArrayList = (ArrayList<RiwayatPenyakitModel>)riwayats;
                    riwayatAdapter = new RiwayatPenyakitAdapter(RiwayatPenyakitActivity.this, riwayatArrayList);

                    if (riwayatAdapter.getItemCount() == 0) {
                        tv_nodata.setVisibility(View.VISIBLE);
                        tv_nodata.setText("Tidak ada data riwayat");
                    } else {
                        tv_nodata.setVisibility(View.GONE);
                        rv_riwayat.setAdapter(riwayatAdapter);
                        rv_riwayat.setNestedScrollingEnabled(false);
                        riwayatAdapter.notifyDataSetChanged();
                    }
                }
                else {
                    tv_nodata.setVisibility(View.VISIBLE);
                    tv_nodata.setText("Code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<RiwayatPenyakitGetResponse> call, Throwable t) {
                pDialog.cancel();
                tv_nodata.setText(t.getMessage());
                tv_nodata.setVisibility(View.VISIBLE);
            }
        });
    }
}
