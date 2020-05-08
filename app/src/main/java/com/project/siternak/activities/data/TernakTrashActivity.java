package com.project.siternak.activities.data;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.project.siternak.R;
import com.project.siternak.adapter.TernakTrashAdapter;
import com.project.siternak.models.data.TernakModel;
import com.project.siternak.responses.DataResponse;
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

public class TernakTrashActivity extends AppCompatActivity {
    @BindView(R.id.b_restore_all) Button bRestoreAll;
    @BindView(R.id.b_delete_all) Button bDeleteAll;
    @BindView(R.id.rv) RecyclerView rv_ternak;
    @BindView(R.id.tv_nodata) TextView tv_nodata;
    @BindView(R.id.ib_add_data) ImageButton ibAddData;

    private TernakTrashAdapter ternakAdapter;
    private ArrayList<TernakModel> ternakArrayList;
    private String userToken;
    private int backFinish;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_list_ternak);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.actionbar_primary_arrow);

        TextView tv_actionbar_title = getSupportActionBar().getCustomView().findViewById(R.id.tv_actionbar_title);
        tv_actionbar_title.setText("Tong Sampah");

        ButterKnife.bind(this);

        bRestoreAll.setVisibility(View.VISIBLE);
        bDeleteAll.setVisibility(View.VISIBLE);
        ibAddData.setVisibility(View.GONE);

        backFinish = (int) getIntent().getIntExtra("finish", 0); //data from after detail

        userToken = SharedPrefManager.getInstance(this).getAccessToken();
        rv_ternak.setLayoutManager(new LinearLayoutManager(this));
        setDataTernak();
    }

    @Override
    public void onBackPressed() {
        if(backFinish == 1){
            Intent intent = new Intent(TernakTrashActivity.this, TernakActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        finish();
    }

    @OnClick(R.id.ib_actionbar_close)
    public void close(){
        onBackPressed();
    }

    @OnClick(R.id.b_restore_all)
    public void restore() {
        if (ternakAdapter.getItemCount() != 0) {
            SweetAlertDialog wDialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
            wDialog.setTitleText("Apakah anda yakin ingin mengembalikan semua data?");
            wDialog.setContentText("Semua data ternak akan dikembalikan dari tong sampah");
            wDialog.setConfirmText("Ya");
            wDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sDialog) {
                    sDialog.dismissWithAnimation();

                    Call<DataResponse> callr = RetrofitClient
                            .getInstance()
                            .getApi()
                            .restoreTernakAll("Bearer " + userToken);

                    callr.enqueue(new Callback<DataResponse>() {
                        @Override
                        public void onResponse(Call<DataResponse> call, Response<DataResponse> response) {
                            DataResponse resp = response.body();

                            if (response.isSuccessful()) {
                                Toast.makeText(TernakTrashActivity.this, resp.getMessage(), Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(TernakTrashActivity.this, TernakActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);

                                TernakTrashActivity.this.finish();
                            }
                        }

                        @Override
                        public void onFailure(Call<DataResponse> call, Throwable t) {
                            Toast.makeText(TernakTrashActivity.this, "Gagal", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
            wDialog.setCancelButton("Batal", new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sDialog) {
                    sDialog.dismissWithAnimation();
                }
            });
            wDialog.show();
        }
        else {
            Toast.makeText(TernakTrashActivity.this, "Tidak ada data ternak pada tong sampah", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.b_delete_all)
    public void delete(){
        if (ternakAdapter.getItemCount() != 0) {
            SweetAlertDialog wDialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
            wDialog.setTitleText("Apakah anda yakin ingin menghapus permanen semua data?");
            wDialog.setContentText("Semua data ternak akan dihapus permanen!");
            wDialog.setConfirmText("Ya");
            wDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sDialog) {
                    sDialog.dismissWithAnimation();

                    Call<DataResponse> calld = RetrofitClient
                            .getInstance()
                            .getApi()
                            .fdelTernakAll("Bearer " + userToken);

                    calld.enqueue(new Callback<DataResponse>() {
                        @Override
                        public void onResponse(Call<DataResponse> call, Response<DataResponse> response) {
                            DataResponse resp = response.body();

                            if (response.isSuccessful()) {
                                Toast.makeText(TernakTrashActivity.this, resp.getMessage(), Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(TernakTrashActivity.this, TernakActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);

                                TernakTrashActivity.this.finish();
                            }
                        }

                        @Override
                        public void onFailure(Call<DataResponse> call, Throwable t) {
                            Toast.makeText(TernakTrashActivity.this, "Gagal", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
            wDialog.setCancelButton("Batal", new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sDialog) {
                    sDialog.dismissWithAnimation();
                }
            });
            wDialog.show();
        }
        else {
            Toast.makeText(TernakTrashActivity.this, "Tidak ada data ternak pada tong sampah", Toast.LENGTH_SHORT).show();
        }
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
                ternakAdapter.getFilter().filter(newText);
                return false;
            }
        });

        return true;
    }

    private void setDataTernak() {
        SweetAlertDialog pDialog = DialogUtils.getLoadingPopup(this);

        Call<TernakGetResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .getTernakTrash("Bearer " + this.userToken);

        call.enqueue(new Callback<TernakGetResponse>() {
            @Override
            public void onResponse(Call<TernakGetResponse> call, Response<TernakGetResponse> response) {
                TernakGetResponse resp = response.body();
                pDialog.cancel();

                if(response.isSuccessful()) {
                    List<TernakModel> ternaks = resp.getTernaks();
                    ternakArrayList = (ArrayList<TernakModel>)ternaks;
                    ternakAdapter = new TernakTrashAdapter(TernakTrashActivity.this, ternakArrayList);

                    if (ternakAdapter.getItemCount() == 0) {
                        tv_nodata.setVisibility(View.VISIBLE);
                        tv_nodata.setText("Tidak ada data ternak pada tong sampah");
                    } else {
                        tv_nodata.setVisibility(View.GONE);
                        rv_ternak.setAdapter(ternakAdapter);
                        ternakAdapter.notifyDataSetChanged();
                    }
                }
                else {
                    tv_nodata.setVisibility(View.VISIBLE);
                    tv_nodata.setText("Code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<TernakGetResponse> call, Throwable t) {
                pDialog.cancel();
                tv_nodata.setText(t.getMessage());
                tv_nodata.setVisibility(View.VISIBLE);
            }
        });
    }
}
