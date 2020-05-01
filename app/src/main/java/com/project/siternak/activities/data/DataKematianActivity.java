package com.project.siternak.activities.data;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.project.siternak.R;
import com.project.siternak.adapter.data.DataKematianAdapter;
import com.project.siternak.rest.RetrofitClient;
import com.project.siternak.responses.KematianResponse;
import com.project.siternak.models.data.KematianModel;
import com.project.siternak.rest.ApiService;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DataKematianActivity extends AppCompatActivity {
    @BindView(R.id.rv) RecyclerView rv_kematian;
    @BindView(R.id.tv_nodata) TextView tv_nodata;

    private DataKematianAdapter kematianAdapter;
    private ArrayList<KematianModel> kematianArrayList;
    private ApiService apiService;

//    User mUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_kematian);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.actionbar_primary_arrow);

        TextView tv_actionbar_title=getSupportActionBar().getCustomView().findViewById(R.id.tv_actionbar_title);
        tv_actionbar_title.setText("Kematian");

//        mUser = Authenticated.getInstance().getUser();

        ButterKnife.bind(this);
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

    private void setDataKematian() {
        LinearLayoutManager manager = new LinearLayoutManager(this);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RetrofitClient.BASE_URL_API)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);
        Call<KematianResponse> callList = apiService.getKematian();

        callList.enqueue(new Callback<KematianResponse>() {
            @Override
            public void onResponse(Call<KematianResponse> call, Response<KematianResponse> response) {
                if(!response.isSuccessful()){
                    tv_nodata.setText("Code: " + response.code());
                }

                List<KematianModel> kematians = response.body().getKematians();
//                for (int i=0; i<kematians.size(); i++){
                    kematianArrayList = (ArrayList<KematianModel>) kematians;
                    kematianAdapter = new DataKematianAdapter(DataKematianActivity.this, kematianArrayList);

                    if(kematianAdapter.getItemCount()==0) {
                        tv_nodata.setVisibility(View.VISIBLE);
                        tv_nodata.setText("Tidak ada data kematian");
                    }
                    else {
                        rv_kematian.setAdapter(kematianAdapter);
                        rv_kematian.setLayoutManager(manager);
                        rv_kematian.setHasFixedSize(true);
                        kematianAdapter.notifyDataSetChanged();
                        tv_nodata.setVisibility(View.GONE);
                    }

//                }
            }
            @Override
            public void onFailure(Call<KematianResponse> call, Throwable t) {
                tv_nodata.setText(t.getMessage());
                tv_nodata.setVisibility(View.VISIBLE);

                Toast.makeText(DataKematianActivity.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        });

//        SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
//        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
//        pDialog.setTitleText("Mohon Tunggu");
//        pDialog.setCancelable(false);
//        pDialog.show();
//
//        new GetEventHistoryInteractor(mUser.getMemberID(),
//                new OnInteractListener<List<EventHistoryViewModel>>() {
//                    @Override
//                    public void onSuccess(List<EventHistoryViewModel> eventList) {
//                        pDialog.cancel();
//                        eventAdapter = new EventHistoryAdapter(eventList,EventHistoryListActivity.this);
//
//                        if(eventAdapter.getItemCount()==0) {
//                            tv_nodata.setVisibility(View.VISIBLE);
//                            tv_nodata.setText("Tidak ada data kematian");
//                        }
//                        else {
//                            rv_event.setAdapter(eventAdapter);
//                            tv_nodata.setVisibility(View.GONE);
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(SLException e) {
//                        pDialog.cancel();
//                    }
//                }
//        ).execute();

    }
}
