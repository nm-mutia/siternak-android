package com.project.siternak.activities.perkawinan;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.project.siternak.R;
import com.project.siternak.activities.option.TernakParentOptionActivity;
import com.project.siternak.models.data.TernakModel;
import com.project.siternak.responses.MatchResponse;
import com.project.siternak.rest.RetrofitClient;
import com.project.siternak.utils.DialogUtils;
import com.project.siternak.utils.SharedPrefManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MatchActivity extends AppCompatActivity {
    @BindView(R.id.til_match_jantan) TextInputLayout tilMatchJantan;
    @BindView(R.id.tiet_match_jantan) TextInputEditText tietMatchJantan;
    @BindView(R.id.til_match_betina) TextInputLayout tilMatchBetina;
    @BindView(R.id.tiet_match_betina) TextInputEditText tietMatchBetina;

    private String userToken;

    private static final int REQUEST_CODE_JANTAN = 1;
    private static final int REQUEST_CODE_BETINA = 2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);

        getSupportActionBar().hide();
        ButterKnife.bind(this);

        userToken = SharedPrefManager.getInstance(this).getAccessToken();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @OnClick(R.id.tiet_match_jantan)
    public void setJantan(){
        Intent intent = new Intent(MatchActivity.this, TernakParentOptionActivity.class);
        intent.putExtra("match", REQUEST_CODE_JANTAN);
        startActivityForResult(intent, REQUEST_CODE_JANTAN);
    }

    @OnClick(R.id.tiet_match_betina)
    public void setBetina(){
        Intent intent = new Intent(MatchActivity.this, TernakParentOptionActivity.class);
        intent.putExtra("match", REQUEST_CODE_BETINA);
        startActivityForResult(intent, REQUEST_CODE_BETINA);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK) {
            if(requestCode == REQUEST_CODE_JANTAN){
                TernakModel result = (TernakModel) data.getSerializableExtra("necktag");
                tietMatchJantan.setText(String.valueOf(result.getNecktag()));
            }
            else if(requestCode == REQUEST_CODE_BETINA){
                TernakModel result = (TernakModel) data.getSerializableExtra("necktag");
                tietMatchBetina.setText(String.valueOf(result.getNecktag()));
            }
        }
    }

    public boolean validateJantan(){
        String p = tilMatchJantan.getEditText().getText().toString().trim();
        if(p.isEmpty()){
            tilMatchJantan.setError("Pilih ternak");
            return false;
        }
        else {
            tilMatchJantan.setError(null);
            tilMatchJantan.setErrorEnabled(false);
            return true;
        }
    }

    public boolean validateBetina(){
        String p = tilMatchBetina.getEditText().getText().toString().trim();
        if(p.isEmpty()){
            tilMatchBetina.setError("Pilih ternak");
            return false;
        }
        else {
            tilMatchBetina.setError(null);
            tilMatchBetina.setErrorEnabled(false);
            return true;
        }
    }

    @OnClick(R.id.b_match)
    public void checkMatch() {
        if (!validateJantan() | !validateBetina()) {
            return;
        }

        SweetAlertDialog pDialog = DialogUtils.getLoadingPopup(this);

        String jantan = tilMatchJantan.getEditText().getText().toString();
        String betina = tilMatchBetina.getEditText().getText().toString();

        Call<MatchResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .getMatchResult("Bearer " + userToken, jantan, betina);

        call.enqueue(new Callback<MatchResponse>() {
            @Override
            public void onResponse(Call<MatchResponse> call, Response<MatchResponse> response) {
                MatchResponse resp = response.body();
                pDialog.dismiss();

                if(response.isSuccessful()){
                    if(resp.getResult().equals("gagal")){
                        SweetAlertDialog swal = new SweetAlertDialog(MatchActivity.this, SweetAlertDialog.ERROR_TYPE);
                        swal.setTitleText("Tidak Boleh");
                        swal.setContentText(resp.getMessage());
                        swal.show();
                    }
                    else {
                        SweetAlertDialog swal = new SweetAlertDialog(MatchActivity.this, SweetAlertDialog.SUCCESS_TYPE);
                        swal.setTitleText("Boleh");
                        swal.setContentText(resp.getMessage());
                        swal.show();
                    }
                }
                else{
                    SweetAlertDialog swal = new SweetAlertDialog(MatchActivity.this, SweetAlertDialog.ERROR_TYPE);
                    swal.setTitleText("Error");
                    swal.setContentText(response.message());
                    swal.show();
                }
            }

            @Override
            public void onFailure(Call<MatchResponse> call, Throwable t) {
                pDialog.dismiss();
                SweetAlertDialog swal = new SweetAlertDialog(MatchActivity.this, SweetAlertDialog.ERROR_TYPE);
                swal.setTitleText("Error");
                swal.setContentText(t.getMessage());
                swal.show();
            }
        });
    }
}
