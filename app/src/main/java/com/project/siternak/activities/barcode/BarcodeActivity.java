package com.project.siternak.activities.barcode;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.project.siternak.R;
import com.project.siternak.activities.laporan.LaporanActivity;
import com.project.siternak.adapter.BarcodeAdapter;
import com.project.siternak.models.data.TernakModel;
import com.project.siternak.responses.TernakGetResponse;
import com.project.siternak.rest.RetrofitClient;
import com.project.siternak.utils.DialogUtils;
import com.project.siternak.utils.FolderExternalStorage;
import com.project.siternak.utils.SharedPrefManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.project.siternak.utils.FolderExternalStorage.FOLDER_NAME;

public class BarcodeActivity extends AppCompatActivity {
    @BindView(R.id.rv) RecyclerView rvBarcode;
    @BindView(R.id.tv_nodata) TextView tvNodata;

    private BarcodeAdapter barcodeAdapter;
    private ArrayList<TernakModel> arrayList;
    private String userToken;
    private static final int PERMISSION_REQUEST_CODE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_barcode);
        getSupportActionBar().hide();

        ButterKnife.bind(this);

        userToken = SharedPrefManager.getInstance(this).getAccessToken();
        setBarcode();
        setPdf();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private boolean checkPermission() {
        return ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissionAndContinue() {
        if (ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, WRITE_EXTERNAL_STORAGE) && ActivityCompat.shouldShowRequestPermissionRationale(this, READ_EXTERNAL_STORAGE)) {
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
                alertBuilder.setCancelable(true);
                alertBuilder.setTitle("Izin dibutuhkan");
                alertBuilder.setMessage("Izin dibutuhkan untuk menyimpan file");
                alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(BarcodeActivity.this, new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
                    }
                });
                AlertDialog alert = alertBuilder.create();
                alert.show();
            } else {
                ActivityCompat.requestPermissions(BarcodeActivity.this, new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
            }
        } else {
            download();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (permissions.length > 0 && grantResults.length > 0) {
                boolean flag = true;
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        flag = false;
                    }
                }
                if (flag) {
                    download();
                } else {
                    finish();
                }
            } else {
                finish();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @OnClick(R.id.btn_barcode_download)
    public void setDownloadFile(){
        setPdf();

        if (!checkPermission()) {
            download();
        } else {
            if (checkPermission()) {
                requestPermissionAndContinue();
            } else {
                download();
            }
        }
    }

    private void download(){
        String filename = "SITERNAK_Barcode.pdf";

        if(FolderExternalStorage.checkFolder()){
            File dir = Environment.getExternalStoragePublicDirectory(FOLDER_NAME);
            File file = new File(dir, filename);
            FileOutputStream fileOutputStream = null;

            try {
                fileOutputStream = new FileOutputStream(file);
//                wb.write(fileOutputStream);
                Toast.makeText(getApplicationContext(), "File disimpan di " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Gagal menyimpan file", Toast.LENGTH_LONG).show();
                if(fileOutputStream != null){
                    try {
                        fileOutputStream.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }

    }

    public void setBarcode(){
        SweetAlertDialog pDialog = DialogUtils.getLoadingPopup(this);

        Call<TernakGetResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .getBarcodeTernak("Bearer " + this.userToken);

        call.enqueue(new Callback<TernakGetResponse>() {
            @Override
            public void onResponse(Call<TernakGetResponse> call, Response<TernakGetResponse> response) {
                TernakGetResponse resp = response.body();
                pDialog.cancel();

                if(response.isSuccessful()) {
                    List<TernakModel> ternaks = resp.getTernaks();
                    arrayList = (ArrayList<TernakModel>)ternaks;
                    barcodeAdapter = new BarcodeAdapter(BarcodeActivity.this, arrayList);

                    if (barcodeAdapter.getItemCount() == 0) {
                        tvNodata.setVisibility(View.VISIBLE);
                        tvNodata.setText("Tidak ada data ternak");
                    } else {
                        tvNodata.setVisibility(View.GONE);
                        rvBarcode.setAdapter(barcodeAdapter);
                        rvBarcode.setNestedScrollingEnabled(false);
                        barcodeAdapter.notifyDataSetChanged();
                    }
                }
                else {
                    tvNodata.setVisibility(View.VISIBLE);
                    tvNodata.setText("Code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<TernakGetResponse> call, Throwable t) {
                pDialog.cancel();
                tvNodata.setText(t.getMessage());
                tvNodata.setVisibility(View.VISIBLE);
            }
        });
    }

    private void setPdf(){

    }
}
