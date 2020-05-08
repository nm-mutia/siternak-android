package com.project.siternak.activities.option;

import android.app.Activity;
import android.os.Bundle;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.project.siternak.R;
import com.project.siternak.adapter.PemilikOptionAdapter;
import com.project.siternak.models.data.PemilikModel;
import com.project.siternak.responses.PemilikGetResponse;
import com.project.siternak.rest.RetrofitClient;
import com.project.siternak.utils.DialogUtils;
import com.project.siternak.utils.SharedPrefManager;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PemilikOptionActivity extends AppCompatActivity {
    @BindView(R.id.rv) RecyclerView rv;
    @BindView(R.id.sv) SearchView sv;

    private PemilikOptionAdapter pemilikAdapter;
    private String userToken;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_option);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.actionbar_cross);
        TextView tv_actionbar_title=getSupportActionBar().getCustomView().findViewById(R.id.tv_actionbar_title);
        tv_actionbar_title.setText("ID Pemilik");

        ButterKnife.bind(this);

        userToken = SharedPrefManager.getInstance(this).getAccessToken();
        setPemilikData();
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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
    }

    @Override
    public void onBackPressed() {
        setResult(Activity.RESULT_CANCELED);
        finish();
    }

    @OnClick(R.id.ib_actionbar_close)
    public void close(){
        this.onBackPressed();
    }

    private void setPemilikData() {
        LinearLayoutManager llmPemilik = new LinearLayoutManager(this);
        rv.setLayoutManager(llmPemilik);
        rv.setHasFixedSize(true);

        SweetAlertDialog loadingDialog = DialogUtils.getLoadingPopup(this);

        Call<PemilikGetResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .getPemilik("Bearer " + this.userToken);

        call.enqueue(new Callback<PemilikGetResponse>() {
            @Override
            public void onResponse(Call<PemilikGetResponse> call, Response<PemilikGetResponse> response) {
                loadingDialog.cancel();
                PemilikGetResponse resp = response.body();

                List<PemilikModel> datas = resp.getPemiliks();
                pemilikAdapter = new PemilikOptionAdapter(PemilikOptionActivity.this, datas);
                rv.setAdapter(pemilikAdapter);
            }

            @Override
            public void onFailure(Call<PemilikGetResponse> call, Throwable t) {
                loadingDialog.cancel();
            }
        });
    }
}
