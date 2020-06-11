package com.project.siternak.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.project.siternak.R;
import com.project.siternak.activities.auth.LoginActivity;
import com.project.siternak.models.auth.UserModel;
import com.project.siternak.utils.SharedPrefManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ProfileFragment extends Fragment {
    @BindView(R.id.tv_fullname) TextView tvFullname;
    @BindView(R.id.tv_name) TextView tvName;
    @BindView(R.id.tv_username) TextView tvUsername;
    @BindView(R.id.tv_email) TextView tvEmail;
    @BindView(R.id.tv_role) TextView tvRole;

    private Unbinder unbinder;
    private UserModel mUser;
    private String TAG = "ProfileTAG";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        unbinder = ButterKnife.bind(this,view);

        ButterKnife.bind(getActivity());

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mUser = SharedPrefManager.getInstance(getActivity()).getUser();

        tvFullname.setText(mUser.getName());
        tvName.setText(mUser.getName());
        tvUsername.setText(mUser.getUsername());
        tvEmail.setText(mUser.getEmail());
        tvRole.setText(mUser.getRole());
    }

    @OnClick(R.id.tv_logout)
    public void logout(){
        SharedPrefManager.getInstance(getActivity()).logout();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

        user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Log.d(TAG, "User account deleted.");
                }
            }
        });

        //        FirebaseAuth.getInstance().signOut();
    }
}
