package com.project.siternak.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.project.siternak.R;
import com.project.siternak.activities.data.DataActivity;
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        unbinder = ButterKnife.bind(this, view);
        
        ButterKnife.bind(getActivity());
        mUser = SharedPrefManager.getInstance(getActivity()).getUser();

        if(mUser.getRole() != null && mUser.getRole().equals("admin")){
            llPeternak.setVisibility(View.VISIBLE);
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvFullname.setText(mUser.getName()+ ", " + mUser.getRole());
    }

    @OnClick(R.id.ll_data)
    public void moveToData(){
        Intent intent=new Intent(getActivity(), DataActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.ll_barcode)
    public void moveToBarcode(){
//        Intent intent=new Intent(getActivity(), SubmissionDashboardActivity.class);
//        startActivity(intent);
    }

    @OnClick(R.id.ll_perkawinan)
    public void moveToMatch(){
//        Intent intent=new Intent(getActivity(), CreditDashboardActivity.class);
//        startActivity(intent);
    }

    @OnClick(R.id.ll_grafik)
    public void moveToGrafik(){
//        Intent intent=new Intent(getActivity(), ElearningDashboardActivity.class);
//        startActivity(intent);
    }

    @OnClick(R.id.ll_laporan)
    public void moveToLaporan(){
//        Intent intent=new Intent(getActivity(), ElearningDashboardActivity.class);
//        startActivity(intent);
    }

    @OnClick(R.id.ll_peternak)
    public void moveToPeternak(){
//        Intent intent=new Intent(getActivity(), ElearningDashboardActivity.class);
//        startActivity(intent);
    }

}
