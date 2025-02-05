package com.project.siternak.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.project.siternak.R;
import com.project.siternak.activities.home.ScanCaptureActivity;
import com.project.siternak.activities.home.ScanResultActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ScanFragment extends Fragment {
    @BindView(R.id.et_cari) EditText etCari;

    private Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scan, container, false);
        unbinder = ButterKnife.bind(this,view);

        ButterKnife.bind(getActivity());

        return view;
    }

    @OnClick(R.id.b_scan)
    public void btnScan(){
        scanCode();
    }

    private void scanCode(){
        IntentIntegrator integrator = IntentIntegrator.forSupportFragment(ScanFragment.this);
        integrator.setCaptureActivity(ScanCaptureActivity.class);
        integrator.setOrientationLocked(false);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Scanning");
        integrator.initiateScan();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null){
            if(result.getContents() != null){

                scanResult(result.getContents());

//                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//                builder.setMessage(result.getContents());
//                builder.setTitle("Hasil Scan");
//                builder.setPositiveButton("Scan Lagi", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        scanCode();
//                    }
//                }).setNegativeButton("finish", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//
//                    }
//                });
//                AlertDialog dialog = builder.create();
//                dialog.show();
            }
            else {
                Toast.makeText(getActivity(), "Tidak ada hasil", Toast.LENGTH_LONG).show();
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
            Toast.makeText(getActivity(), "Tidak ada hasil", Toast.LENGTH_LONG).show();
        }
    }

    @OnClick(R.id.b_cari)
    public void submitCari(){
        String necktag = etCari.getText().toString();

        if(necktag.isEmpty()){
            this.etCari.setError("Masukkan necktag");
        }
        else {
            scanResult(necktag);
        }
    }

    private void scanResult(String result){
        Intent intent = new Intent(getActivity(), ScanResultActivity.class);
        intent.putExtra("result", result);
        startActivity(intent);
    }
}
