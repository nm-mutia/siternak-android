package com.project.siternak.activities.data;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.project.siternak.R;
import com.project.siternak.models.data.TernakModel;
import com.project.siternak.responses.DataResponse;
import com.project.siternak.responses.TernakResponse;
import com.project.siternak.rest.RetrofitClient;
import com.project.siternak.utils.DialogUtils;
import com.project.siternak.utils.SharedPrefManager;

import java.io.Serializable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TernakDetailActivity extends AppCompatActivity {
    @BindView(R.id.tv_necktag) TextView tvNecktag;
    @BindView(R.id.tv_ternak_pemilik) TextView tvPemilik;
    @BindView(R.id.tv_ternak_peternakan) TextView tvPeternakan;
    @BindView(R.id.tv_ternak_ras) TextView tvRas;
    @BindView(R.id.tv_ternak_kematian) TextView tvKematian;
    @BindView(R.id.tv_ternak_jk) TextView tvJk;
    @BindView(R.id.tv_ternak_tgl_lahir) TextView tvTglLahir;
    @BindView(R.id.tv_ternak_bobot_lahir) TextView tvBobotLahir;
    @BindView(R.id.tv_ternak_pukul_lahir) TextView tvPukulLahir;
    @BindView(R.id.tv_ternak_lama_dikandungan) TextView tvLamaDiKandungan;
    @BindView(R.id.tv_ternak_lama_laktasi) TextView tvLamaLaktasi;
    @BindView(R.id.tv_ternak_tgl_lepas_sapih) TextView tvTglLepasSapih;
    @BindView(R.id.tv_ternak_blood) TextView tvBlood;
    @BindView(R.id.tv_ternak_necktag_ayah) TextView tvAyah;
    @BindView(R.id.tv_ternak_necktag_ibu) TextView tvIbu;
    @BindView(R.id.tv_ternak_bobot_tubuh) TextView tvBobotTubuh;
    @BindView(R.id.tv_ternak_panjang_tubuh) TextView tvPanjangTubuh;
    @BindView(R.id.tv_ternak_tinggi_tubuh) TextView tvTinggiTubuh;
    @BindView(R.id.tv_ternak_cacat_fisik) TextView tvCacatFisik;
    @BindView(R.id.tv_ternak_ciri_lain) TextView tvCiriLain;
    @BindView(R.id.tv_ternak_status_ada) TextView tvStatusAda;
    @BindView(R.id.tv_created_at) TextView tvCreatedAt;
    @BindView(R.id.tv_updated_at) TextView tvUpdatedAt;
    @BindView(R.id.tv_deleted_at) TextView tvDeletedAt;
    @BindView(R.id.tl_detail_ternak) TableLayout tlDetailTernak;

    private TernakModel ternakData;
    private String userToken;
    private int backFinish;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_ternak);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.actionbar_primary_arrow);

        TextView tv_actionbar_title = getSupportActionBar().getCustomView().findViewById(R.id.tv_actionbar_title);

        ButterKnife.bind(this);

        ternakData = (TernakModel) getIntent().getSerializableExtra("ternak");
        tv_actionbar_title.setText("Ternak - " + ternakData.getNecktag());

        backFinish = (int) getIntent().getIntExtra("finish", 0); //data from after edit ternak

        userToken = SharedPrefManager.getInstance(this).getAccessToken();
        setDataTernak();
    }

    @Override
    public void onBackPressed() {
        if(backFinish == 1){
            Intent intent = new Intent(TernakDetailActivity.this, TernakActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        finish();
    }

    @OnClick(R.id.ib_actionbar_close)
    public void close(){
        onBackPressed();
    }

    @OnClick(R.id.ib_delete_data)
    public void deleteData() {
        SweetAlertDialog wDialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
        wDialog.setTitleText("Apakah anda yakin untuk menghapus data ini?");
        wDialog.setContentText("Data ternak id " + ternakData.getNecktag());
        wDialog.setConfirmText("Ya");
        wDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sDialog) {
                sDialog.dismissWithAnimation();

                Call<DataResponse> calld = RetrofitClient
                        .getInstance()
                        .getApi()
                        .delTernak("Bearer " + userToken, ternakData.getNecktag());

                calld.enqueue(new Callback<DataResponse>() {
                    @Override
                    public void onResponse(Call<DataResponse> call, Response<DataResponse> response) {
                        DataResponse resp = response.body();

                        if(response.isSuccessful()){
                            if(resp.getStatus().equals("error")){
//                                Toast.makeText(TernakDetailActivity.this, resp.getMessage(), Toast.LENGTH_LONG).show();
                                DialogUtils.swalFailed(TernakDetailActivity.this, resp.getMessage());
                            }
                            else{
                                Toast.makeText(TernakDetailActivity.this, resp.getMessage(), Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(TernakDetailActivity.this, TernakActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);

                                TernakDetailActivity.this.finish();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<DataResponse> call, Throwable t) {
                        Toast.makeText(TernakDetailActivity.this, "Gagal", Toast.LENGTH_SHORT).show();
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

    @OnClick(R.id.tl_detail_ternak)
    public void editData(){
        Intent intent = new Intent(TernakDetailActivity.this, TernakEditActivity.class);
        intent.putExtra("data", (Serializable) ternakData);
        startActivity(intent);
    }

    private void setDataTernak() {
        tvNecktag.setText(String.valueOf(ternakData.getNecktag()));
        tvPemilik.setText(String.valueOf(ternakData.getPemilikId()));
        tvPeternakan.setText(String.valueOf(ternakData.getPeternakanId()));
        tvRas.setText(String.valueOf(ternakData.getRasId()));
        tvKematian.setText(String.valueOf(ternakData.getKematianId()));
        tvJk.setText(ternakData.getJenisKelamin());
        tvTglLahir.setText(ternakData.getTglLahir());
        tvBobotLahir.setText(ternakData.getBobotLahir());
        tvPukulLahir.setText(ternakData.getPukulLahir());
        tvLamaDiKandungan.setText(ternakData.getLamaDiKandungan());
        tvLamaLaktasi.setText(ternakData.getLamaLaktasi());
        tvTglLepasSapih.setText(ternakData.getTglLepasSapih());
        tvBlood.setText(ternakData.getBlood());
        tvAyah.setText(ternakData.getNecktag_ayah());
        tvIbu.setText(ternakData.getNecktag_ibu());
        tvBobotTubuh.setText(ternakData.getBobotTubuh());
        tvPanjangTubuh.setText(ternakData.getPanjangTubuh());
        tvTinggiTubuh.setText(ternakData.getTinggiTubuh());
        tvCacatFisik.setText(ternakData.getCacatFisik());
        tvCiriLain.setText(ternakData.getCiriLain());
        if(ternakData.getStatusAda()){
            tvStatusAda.setText("Ada");
        }else{
            tvStatusAda.setText("Tidak Ada");
        }
        tvCreatedAt.setText(ternakData.getCreated_at());
        tvUpdatedAt.setText(ternakData.getUpdated_at());
        tvDeletedAt.setText(ternakData.getDeleted_at());
    }
}
