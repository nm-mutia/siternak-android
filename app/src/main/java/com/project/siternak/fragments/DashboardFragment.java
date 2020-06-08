package com.project.siternak.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.project.siternak.R;
import com.project.siternak.activities.barcode.BarcodeActivity;
import com.project.siternak.activities.data.DataActivity;
import com.project.siternak.activities.grafik.GrafikActivity;
import com.project.siternak.activities.laporan.LaporanActivity;
import com.project.siternak.activities.perkawinan.MatchActivity;
import com.project.siternak.activities.peternak.PeternakActivity;
import com.project.siternak.models.auth.UserModel;
import com.project.siternak.utils.SharedPrefManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class DashboardFragment extends Fragment {
    @BindView(R.id.tv_fullname) TextView tvFullname;
    @BindView(R.id.ll_peternak) LinearLayout llPeternak;

    private Unbinder unbinder;
    private UserModel mUser;
    private Context context;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        unbinder = ButterKnife.bind(this, view);
        
        ButterKnife.bind((Activity) context);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mUser = SharedPrefManager.getInstance(context).getUser();
        tvFullname.setText(mUser.getName()+ ", " + mUser.getRole());

        if(mUser.getRole() != null && mUser.getRole().equals("admin")){
            llPeternak.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.ll_data)
    public void moveToData(){
        Intent intent = new Intent(getActivity(), DataActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.ll_barcode)
    public void moveToBarcode(){
        Intent intent = new Intent(getActivity(), BarcodeActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.ll_perkawinan)
    public void moveToMatch(){
        Intent intent = new Intent(getActivity(), MatchActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.ll_grafik)
    public void moveToGrafik(){
        Intent intent = new Intent(getActivity(), GrafikActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.ll_laporan)
    public void moveToLaporan(){
        Intent intent = new Intent(getActivity(), LaporanActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.ll_peternak)
    public void moveToPeternak(){
        Intent intent = new Intent(getActivity(), PeternakActivity.class);
        startActivity(intent);
    }

}
