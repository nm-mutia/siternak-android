package com.project.siternak.activities.auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.project.siternak.R;
import com.project.siternak.activities.home.MainActivity;
import com.project.siternak.responses.LoginResponse;
import com.project.siternak.responses.UserDetailsResponse;
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

public class LoginActivity extends AppCompatActivity {
    @BindView(R.id.et_email)
    EditText editTextEmail;
    @BindView(R.id.et_passsword)
    EditText editTextPassword;

    private FirebaseAuth mAuth;
    public static final String TAG = "LoginActivityTAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        ButterKnife.bind(this);

        mAuth = FirebaseAuth.getInstance();
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


    @OnClick(R.id.b_login)
    public void moveToAfterLogin(View view) {
        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();

        if (!validated(email, password)) return;

        SweetAlertDialog pDialog = DialogUtils.getLoadingPopup(this);

        Call<LoginResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .userLogin(email, password);

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                LoginResponse resp = response.body();

                if (response.isSuccessful()) {
                    checkUserFirebase(email, password);
                    SharedPrefManager.getInstance(LoginActivity.this).saveAccessToken(resp.getData().getToken());
                    storeUser(pDialog);

//                    pDialog.dismiss();
                }
                else{
                    pDialog.dismiss();
                    DialogUtils.swalFailed(LoginActivity.this, response.message());
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                pDialog.dismiss();
                DialogUtils.swalFailed(LoginActivity.this, t.getMessage());
            }
        });
    }


    public void moveToDashboard() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public void moveToVerifyEmail() {
        Intent intent = new Intent(LoginActivity.this, VerifyActivity.class);
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

    private void storeUser(SweetAlertDialog pDialog){
        Call<UserDetailsResponse> calls = RetrofitClient
                .getInstance()
                .getApi()
                .userDetails("Bearer " + SharedPrefManager.getInstance(LoginActivity.this).getAccessToken());

        calls.enqueue(new Callback<UserDetailsResponse>() {
            @Override
            public void onResponse(Call<UserDetailsResponse> call, Response<UserDetailsResponse> response) {
                UserDetailsResponse resp = response.body();

                if(response.isSuccessful()){
                    pDialog.dismiss();
                    SharedPrefManager.getInstance(LoginActivity.this).saveUser(resp.getData());
                    Toast.makeText(LoginActivity.this, "Selamat datang, " + resp.getData().getName(), Toast.LENGTH_LONG).show();
                    moveToDashboard();

//                    if(resp.getData().getEmail_verified_at() == null){
//                        moveToVerifyEmail();
//                    }
//                    else{
//                        moveToDashboard();
//                    }
                }
            }

            @Override
            public void onFailure(Call<UserDetailsResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void checkUserFirebase(String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    //create email and password to firebase
                    Log.d(TAG, "createUserWithEmail:success");
                }
                else {
                    //login only
                    loginFirebase(email, password);
                }
            }
        });
    }

    private void loginFirebase(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Log.d(TAG, "signInWithEmail:success");
                }
            }
        });
    }
}

