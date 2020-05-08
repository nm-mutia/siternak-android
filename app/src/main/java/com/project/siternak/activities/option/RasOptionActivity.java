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
import com.project.siternak.adapter.RasOptionAdapter;
import com.project.siternak.models.data.RasModel;
import com.project.siternak.responses.RasGetResponse;
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

public class RasOptionActivity extends AppCompatActivity {
    @BindView(R.id.rv) RecyclerView rv;
    @BindView(R.id.sv) SearchView sv;

    private RasOptionAdapter rasAdapter;
    private String userToken;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_option);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.actionbar_cross);
        TextView tv_actionbar_title=getSupportActionBar().getCustomView().findViewById(R.id.tv_actionbar_title);
        tv_actionbar_title.setText("ID Ras");

        ButterKnife.bind(this);

        userToken = SharedPrefManager.getInstance(this).getAccessToken();
        setData();
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                rasAdapter.getFilter().filter(newText);
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

    private void setData() {
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);

        SweetAlertDialog loadingDialog = DialogUtils.getLoadingPopup(this);

        Call<RasGetResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .getRas("Bearer " + this.userToken);

        call.enqueue(new Callback<RasGetResponse>() {
            @Override
            public void onResponse(Call<RasGetResponse> call, Response<RasGetResponse> response) {
                loadingDialog.cancel();
                RasGetResponse resp = response.body();

                List<RasModel> datas = resp.getRas();
                rasAdapter = new RasOptionAdapter(RasOptionActivity.this, datas);
                rv.setAdapter(rasAdapter);
            }

            @Override
            public void onFailure(Call<RasGetResponse> call, Throwable t) {
                loadingDialog.cancel();
            }
        });
    }
}
