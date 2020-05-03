package com.project.siternak.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

    private Unbinder unbinder;
    private UserModel mUser;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        unbinder = ButterKnife.bind(this, view);

        ButterKnife.bind(getActivity());

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mUser = SharedPrefManager.getInstance(getActivity()).getUser();
        tvFullname.setText(mUser.getName()+ ", " + mUser.getRole());
    }


//    private void setMemberDetail(){
//        if(Authenticated.getInstance().getMember() == null || !Authenticated.getInstance().isValidCacheMember()){
//            SweetAlertDialog pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
//            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
//            pDialog.setTitleText("Loading");
//            pDialog.setCancelable(false);
//            pDialog.show();
//
//            GetMemberByIDInteractor interactor = new GetMemberByIDInteractor(mUser)
//                    .setmCallback(new OnInteractListener<Member>() {
//                        @Override
//                        public void onSuccess(Member member) {
//                            Log.w("Home Fragment", "onSuccess");
//                            mMember = member;
//                            Authenticated.getInstance().setMember(member);
//                            if (member.getFirstname() == null) tvFullname.setText(mUser.getUsername());
//                            else tvFullname.setText(member.getFullname());
//                            pDialog.dismiss();
//                        }
//
//                        @Override
//                        public void onFailure(SLException e) {
//                            pDialog.dismiss();
//                            SwalUtils.showError(getActivity(), e);
//                        }
//                    });
//            interactor.execute();
//        }
//    }

    @OnClick(R.id.ll_data)
    public void moveToData(){
//        if (!verified()) return;

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
//        if (!verified()) return;
//
//        Intent intent=new Intent(getActivity(), CreditDashboardActivity.class);
//        startActivity(intent);
    }

    @OnClick(R.id.ll_grafik)
    public void moveToGrafik(){
//        if (!verified()) return;
//
//        Intent intent=new Intent(getActivity(), ElearningDashboardActivity.class);
//        startActivity(intent);
    }

    @OnClick(R.id.ll_laporan)
    public void moveToLaporan(){
//        if (!verified()) return;
//
//        Intent intent=new Intent(getActivity(), ElearningDashboardActivity.class);
//        startActivity(intent);
    }

    @OnClick(R.id.ll_peternak)
    public void moveToPeternak(){
//        if (!verified()) return; //only admin
//
//        Intent intent=new Intent(getActivity(), ElearningDashboardActivity.class);
//        startActivity(intent);
    }

//    private Boolean verified(){
//        Boolean res = true;
//
//        if (mMember == null || mMember.getCenterVerificationStatus() == null || mMember.getCenterVerificationStatus() != 2){
//            res = false;
//            SwalUtils.showUnverifError(getActivity());
//        }
//
//        return res;
//    }
}
