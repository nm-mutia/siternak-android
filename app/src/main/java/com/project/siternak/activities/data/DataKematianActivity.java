package com.project.siternak.activities.data;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.project.siternak.R;
import com.project.siternak.adapter.data.DataKematianAdapter;
import com.project.siternak.models.KematianModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DataKematianActivity extends AppCompatActivity {
    @BindView(R.id.rv) RecyclerView rv_kematian;
    @BindView(R.id.tv_nodata) TextView tv_nodata;
    private DataKematianAdapter kematianAdapter;
    private ArrayList<KematianModel> kematianArrayList;

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
        LinearLayoutManager llmClinic= new LinearLayoutManager(this);
        rv_kematian.setLayoutManager(llmClinic);
        rv_kematian.setHasFixedSize(true);

        kematianArrayList = new ArrayList<>();
        kematianArrayList.add(new KematianModel(1,"2020-12-12", "12:12:12", "halo", "halo",
                "2020-12-12 12:12:12", "2020-12-12 12:12:12"));
        kematianAdapter = new DataKematianAdapter(kematianArrayList,this);

        if(kematianAdapter.getItemCount()==0) {
            tv_nodata.setVisibility(View.VISIBLE);
        }
        else {
            rv_kematian.setAdapter(kematianAdapter);
            tv_nodata.setVisibility(View.GONE);
        }
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
