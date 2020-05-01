package com.project.siternak.activities.auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.project.siternak.R;
import com.project.siternak.activities.home.MainActivity;
import com.project.siternak.responses.LoginResponse;
import com.project.siternak.rest.RetrofitClient;
import com.project.siternak.utils.SharedPrefManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    @BindView(R.id.et_email)
    EditText editTextEmail;
    @BindView(R.id.et_passsword)
    EditText editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        ButterKnife.bind(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            moveToDashboard();
        }
    }


    @Override
    public void onBackPressed() {
        finish();
    }


    @OnClick(R.id.tv_login)
    public void moveToAfterLogin(View view) {
        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();

        if (!validated(email, password)) return;

        Call<LoginResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .userLogin(email, password);

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                LoginResponse resp = response.body();

                if (response.isSuccessful()) {
                    SharedPrefManager.getInstance(LoginActivity.this)
                            .saveAccessToken(resp.getData());

                    Toast.makeText(LoginActivity.this, resp.getStatus(), Toast.LENGTH_LONG).show();
                    moveToDashboard();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


    public void moveToDashboard() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private boolean validated(String email, String password) {
        boolean pass = true;

        if (email.isEmpty()) {
            pass = false;
            this.editTextEmail.setError("Email perlu diisi");
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            pass = false;
            this.editTextEmail.setError("Masukkan email yang valid");
        }
        if (password.isEmpty()) {
            pass = false;
            this.editTextPassword.setError("Password perlu diisi");
        }
        return pass;
    }
}

//    @OnClick(R.id.tv_login)
//    public void doLogin(View view){
//        if (!validated()) return;
//
//        String username = this.editTextUsername.getText().toString();
//        String password = this.editTextPassword.getText().toString();
//
//        SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
//        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
//        pDialog.setTitleText("Loading");
//        pDialog.setCancelable(false);
//        pDialog.show();
//
//        LoginInteractor interactor = new LoginInteractor(username, password, this, new OnInteractListener() {
//            @Override
//            public void onSuccess(Object object) {
//                pDialog.dismiss();
//                moveToHome();
//            }
//
//            @Override
//            public void onFailure(SLException e) {
//                pDialog.dismiss();
//                SweetAlertDialog swal = new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.ERROR_TYPE);
//                swal.setTitleText("Error");
//                swal.setContentText(e.getMessage());
//                swal.show();
//            }
//        });
//        interactor.execute();
//    }


